package com.zhj.bluetooth.sdkdemo.ui;

import android.Manifest;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;


import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zhj.bluetooth.sdkdemo.MyAppcation;
import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.base.BaseAdapter;
import com.zhj.bluetooth.sdkdemo.util.CommonDialog;
import com.zhj.bluetooth.sdkdemo.util.CommonUtil;
import com.zhj.bluetooth.sdkdemo.util.DialogHelperNew;
import com.zhj.bluetooth.sdkdemo.util.IntentUtil;
import com.zhj.bluetooth.sdkdemo.util.PermissionUtil;
import com.zhj.bluetooth.sdkdemo.util.RecyclerRefreshLayout;
import com.zhj.bluetooth.zhjbluetoothsdk.bean.BLEDevice;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.BluetoothLe;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.OnLeConnectListener;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.OnLeScanListener;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.exception.ConnBleException;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.exception.ScanBleException;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.scanner.ScanRecord;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.scanner.ScanResult;
import com.zhj.bluetooth.zhjbluetoothsdk.util.LogUtil;
import com.zhj.bluetooth.zhjbluetoothsdk.util.SPHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

public class ScanDeviceReadyActivity extends BaseActivity implements BaseAdapter.OnItemClickListener, RecyclerRefreshLayout.SuperRefreshLayoutListener {

    protected final static int ACCESS_FINE_LOCATION_REQUEST_CODE = 100;

    @BindView(R.id.refresh_recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mRefreshLayout)
    RecyclerRefreshLayout mRefreshLayout;
    private ScanDeviceAdapter mAdapter;

    private BluetoothLe mBluetoothLe;

    @Override
    protected int getContentView() {
        return R.layout.activity_scan_device;
    }

    private int i1, i2, i3, i4;

    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.main_pairing));
        mBluetoothLe = BluetoothLe.getDefault();
        mRefreshLayout.setSuperRefreshLayoutListener(this);
        mRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.CYAN);
        mRefreshLayout.setCanLoadMore(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBluetoothLe.setOnConnectListener(TAG, new OnLeConnectListener() {
            @Override
            public void onDeviceConnecting() {
            }

            @Override
            public void onDeviceConnected() {
            }

            @Override
            public void onDeviceDisconnected() {
                isConnecting = false;
                MyAppcation.getInstance().setConnected(false);
                connectDevice = null;
                if (mAdapter != null) {
                    mAdapter.connecting(-1);
                    showList.clear();
                    mAdapter.setData(showList);
                }
                scan();
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt bluetoothGatt) {
                //不需要显示配对码调用
                isConnecting = false;
                MyAppcation.getInstance().setConnected(true);
                SPHelper.saveBLEDevice(ScanDeviceReadyActivity.this, connectDevice);
                finish();
                //显示配对码
//                Random random = new Random();
//                i1 = random.nextInt(10);
//                i2 = random.nextInt(10);
//                i3 = random.nextInt(10);
//                i4 = random.nextInt(10);
//                BleSdkWrapper.setPairingcode(i1, i2, i3, i4, new OnLeWriteCharacteristicListener() {
//                    @Override
//                    public void onSuccess(HandlerBleDataResult  handlerBleDataResult) {
//                        LogUtil.d("发送配对码");
//                        showPairingDialog();
//                    }
//
//                    @Override
//                    public void onFailed(WriteBleException e) {
//                        LogUtil.d("发送配对码失败");
//                    }
//                } );
            }

            @Override
            public void onDeviceConnectFail(ConnBleException e) {
                isConnecting = false;
                MyAppcation.getInstance().setConnected(false);
                connectDevice = null;
                if (mAdapter != null) {
                    mAdapter.connecting(-1);
                    showList.clear();
                    mAdapter.setData(showList);
                }
                scan();
            }
        });
        if (!mBluetoothLe.isBluetoothOpen()) {
            DialogHelperNew.showRemindDialog(this, getResources().getString(R.string.permisson_location_title),
                    getResources().getString(R.string.permisson_location_tips), getResources().getString(R.string.permisson_location_open), view -> {
                        mBluetoothLe.enableBluetooth(this);
                    }, view -> ScanDeviceReadyActivity.this.finish());
        }
        if (!CommonUtil.isOPen(this)) {
            DialogHelperNew.showRemindDialog(this, getResources().getString(R.string.permisson_location_title),
                    getResources().getString(R.string.permisson_location_tips), getResources().getString(R.string.permisson_location_open), view -> {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, 1000);
                    }, view -> ScanDeviceReadyActivity.this.finish());
        } else {
            getScanDevice();
        }
    }


    private Dialog showPairingDialog() {
        Dialog dialog = new Dialog(this, R.style.center_dialog);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_pairing, null);
        EditText et1 = view.findViewById(R.id.et1);
        EditText et2 = view.findViewById(R.id.et2);
        EditText et3 = view.findViewById(R.id.et3);
        EditText et4 = view.findViewById(R.id.et4);
        et1.setFocusable(true);
        et1.setFocusableInTouchMode(true);
        et1.requestFocus();
        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 1) {
                    et2.requestFocus();
                }
            }
        });
        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 1) {
                    et3.requestFocus();
                }
            }
        });
        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 1) {
                    et4.requestFocus();
                }
            }
        });

        view.findViewById(R.id.tvCanle).setOnClickListener(v -> {
            dialog.dismiss();
            //退出输入  解绑
        });
        view.findViewById(R.id.tvSure).setOnClickListener(v -> {
            if (TextUtils.isEmpty(et1.getText().toString()) || TextUtils.isEmpty(et2.getText().toString()) ||
                    TextUtils.isEmpty(et3.getText().toString()) || TextUtils.isEmpty(et4.getText().toString())) {
                showToast(getResources().getString(R.string.scan_device_info_put_complete_code));
            } else {
                if (Integer.parseInt(et1.getText().toString()) == i1 &&
                        Integer.parseInt(et2.getText().toString()) == i2 &&
                        Integer.parseInt(et3.getText().toString()) == i3 &&
                        Integer.parseInt(et4.getText().toString()) == i4) {
                    dialog.dismiss();
                } else {
                    showToast(getResources().getString(R.string.scan_device_info_put_error));
                    et1.setText("");
                    et2.setText("");
                    et3.setText("");
                    et4.setText("");
                }
            }
        });
        dialog.setContentView(view);
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = this.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8);
        dialogWindow.setAttributes(lp);
        dialog.show();
        return dialog;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (!CommonUtil.isOPen(this)) {
                ScanDeviceReadyActivity.this.finish();
            } else {
                getScanDevice();
            }
        } else if (requestCode == ACCESS_FINE_LOCATION_REQUEST_CODE) {
            if (!checkSelfPermission(permissionsLocation)) {
                requestPermissions(ACCESS_FINE_LOCATION_REQUEST_CODE, permissionsLocation);
            } else {
                scan();
            }
        }
    }

    private void scan() {
        mBluetoothLe.setScanPeriod(5000)
                .setReportDelay(0)
//                .setScanWithDeviceName("DfuTarg")
//                .setScanWithDeviceAddress("E6:78:9C:CE:7B:80")
                .startScan(this, new OnLeScanListener() {
                    @Override
                    public void onScanResult(BluetoothDevice bluetoothDevice, int rssi, ScanRecord scanRecord) {
                        BLEDevice device = new BLEDevice();
                        device.mDeviceAddress = bluetoothDevice.getAddress();
                        if (ActivityCompat.checkSelfPermission(ScanDeviceReadyActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                        }
                        device.mDeviceName = bluetoothDevice.getName();
                        device.mRssi = rssi;
                        device.setmBytes(scanRecord.getBytes());
                        device.setParcelId(scanRecord.getParcelId());
                        LogUtil.d("device.mDeviceAddress:"+device.mDeviceAddress);
                        mRefreshLayout.onComplete();
                        if(!showList.contains(device)){
                            showList.add(device);
                            Collections.sort(showList);
                            if(mAdapter == null){
                                mAdapter = new ScanDeviceAdapter(ScanDeviceReadyActivity.this,showList);
                                mRecyclerView.setAdapter(mAdapter);
                            }else{
                                mAdapter.setData(showList);
                            }
                            mAdapter.setOnItemClickListener(ScanDeviceReadyActivity.this);
                        }
                    }

                    @Override
                    public void onBatchScanResults(List<ScanResult> results) {
                        Log.i(TAG, "扫描到设备：" + results.toString());
                    }

                    @Override
                    public void onScanCompleted() {
                        mBluetoothLe.stopScan();
                        mRefreshLayout.onComplete();
                    }

                    @Override
                    public void onScanFailed(ScanBleException e) {
                        Log.e(TAG, "扫描错误：" + e.toString());
                        onScanCompleted();
                        mBluetoothLe.stopScan();
                    }
                });
    }

    private void getScanDevice(){
        mRefreshLayout.post(() -> {
            mRefreshLayout.setRefreshing(true);
            showList.clear();
            if(mAdapter != null){
                mAdapter.setData(showList);
            }
            if(checkSelfPermission(permissionsLocation)){
                scan();
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                    requestPermissions(ACCESS_FINE_LOCATION_REQUEST_CODE,permissionsLocation_12);
                }else{
                    requestPermissions(ACCESS_FINE_LOCATION_REQUEST_CODE,permissionsLocation);
                }

            }
        });
    }
    private String[] permissionsLocation_12 = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.BLUETOOTH_SCAN,Manifest.permission.BLUETOOTH_CONNECT};
    private String[] permissionsLocation = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    private Dialog mDialog;
    @Override
    public void requestPermissionsFail(int requestCode) {
        super.requestPermissionsFail(requestCode);
        if (requestCode == ACCESS_FINE_LOCATION_REQUEST_CODE) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsLocation[0])) {
                mDialog = DialogHelperNew.showRemindDialog(this, getResources().getString(R.string.permisson_location_title),
                        getResources().getString(R.string.permisson_location_tips), getResources().getString(R.string.permisson_location_open), view -> {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent,ACCESS_FINE_LOCATION_REQUEST_CODE);
                        }, view -> {
                            mDialog.dismiss();
                            ScanDeviceReadyActivity.this.finish();
                        });
            }else{
                ScanDeviceReadyActivity.this.finish();
            }
        }
    }
    @Override
    public void requestPermissionsSuccess(int requestCode) {
        if (requestCode == ACCESS_FINE_LOCATION_REQUEST_CODE) {
            scan();
        }
    }


    private List<BLEDevice> showList = new ArrayList<>();

    @Override
    protected void onPause() {
        super.onPause();
        mRefreshLayout.onComplete();
    }

    private boolean isConnecting = false;
    private BLEDevice connectDevice;
    @Override
    public void onItemClick(View view, int position) {
        //链接设备
        if (!BluetoothLe.getDefault().isBluetoothOpen()){
            showToast(getResources().getString(R.string.scan_device_binding));
        }else{
            if(!isConnecting){
                mAdapter.connecting(position);
                connectDevice = showList.get(position);
                isConnecting = true;
                mBluetoothLe.stopScan();
                mBluetoothLe.startConnect(showList.get(position).mDeviceAddress);

            }
        }
    }
    @Override
    public void onRefreshing() {
        if(isConnecting || mBluetoothLe.getScanning()){
//            showToast(getResources().getString(R.string.connect_device_str));
            mRefreshLayout.onComplete();
        }else{
            isOpenBle();
        }
    }
    private void isOpenBle(){
        if (!BluetoothLe.getDefault().isBluetoothOpen()){
            mRefreshLayout.onComplete();
            CommonDialog commonDialog=new CommonDialog.Builder(this)
                    .isVertical(false).setTitle(R.string.scan_device_blu_not_open)
                    .setLeftButton(R.string.cancel, (dialog, which) -> ScanDeviceReadyActivity.this.finish())
                    .setMessage(R.string.scan_device_open_set)
                    .setRightButton(R.string.scan_device_set, (dialog, which) -> BluetoothLe.getDefault().enableBluetooth(this))
                    .create();
            commonDialog.show();
        }else{
            getScanDevice();
        }
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onBackPressed() {
        if(isConnecting){
            showToast(getResources().getString(R.string.scan_device_binding));
        }else{
            mBluetoothLe.stopScan();
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBluetoothLe.stopScan();
        //根据TAG注销监听，避免内存泄露
        mBluetoothLe.destroy(TAG);
    }
}
