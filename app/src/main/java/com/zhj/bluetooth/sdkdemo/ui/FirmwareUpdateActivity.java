package com.zhj.bluetooth.sdkdemo.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.util.Device;
import com.zhj.bluetooth.sdkdemo.util.TelinkLog;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleSdkWrapper;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.HandlerBleDataResult;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.BluetoothLe;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.BluetoothUUID;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.OnLeConnectListener;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.OnLeScanListener;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.exception.ConnBleException;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.exception.ScanBleException;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.exception.WriteBleException;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.scanner.ScanRecord;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.scanner.ScanResult;
import com.zhj.bluetooth.zhjbluetoothsdk.util.LogUtil;
import com.zhj.bluetooth.zhjbluetoothsdk.util.ThreadUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import no.nordicsemi.android.dfu.DfuProgressListener;
import no.nordicsemi.android.dfu.DfuProgressListenerAdapter;
import no.nordicsemi.android.dfu.DfuServiceController;
import no.nordicsemi.android.dfu.DfuServiceInitiator;
import no.nordicsemi.android.dfu.DfuServiceListenerHelper;

public class FirmwareUpdateActivity extends BaseActivity {

    private final String saveFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SDKDemo/Device_update/";
    private String[] pers = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private final static int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 200;
    private final String downUrl = "http://47.75.143.120/file_v2/images/1645414466101.bin";
    private BluetoothDevice mDfuDevice;
    @BindView(R.id.pb_update_progress)
    ProgressBar mProgressBar;
    @BindView(R.id.progressRate)
    TextView progressRate;
    @BindView(R.id.tvTips)
    TextView tvTips;
    @BindView(R.id.tvTips2)
    TextView tvTips2;

    @Override
    protected int getContentView() {
        return R.layout.activity_firmware;
    }

    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.main_update));
        if (!checkSelfPermission(pers)) {
            requestPermissions(WRITE_EXTERNAL_STORAGE_REQUEST_CODE, pers);
        } else {
            downLoadFile(downUrl);
//            updateDfu();
        }
        DfuServiceListenerHelper.registerProgressListener(this, dfuProgressListener);
        BluetoothLe.getDefault().setOnConnectListener(TAG, new OnLeConnectListener() {
            @Override
            public void onDeviceConnecting() {
                LogUtil.d("正在链接设备");
            }

            @Override
            public void onDeviceConnected() {
                LogUtil.d("已链接设备");
            }

            @Override
            public void onDeviceDisconnected() {
                LogUtil.d("断开链接");
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt bluetoothGatt) {
                LogUtil.d("发现服务，开始空生");
                BluetoothLe.getDefault().stopScan();
                final DfuServiceInitiator starter = new DfuServiceInitiator(mDfuDevice.getAddress())
                        .setDeviceName("DfuTarg").setKeepBond(true);
                // If you want to have experimental buttonless DFU feature supported call additionally:starter.setUnsafeExperimentalButtonlessServiceInSecureDfuEnabled(true);
                starter.setZip(saveFileName + "deviceUpdate.zip");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    starter.setForeground(false);
                    starter.setDisableNotification(true);
                }
                starter.start(FirmwareUpdateActivity.this, DfuService.class);
            }

            @Override
            public void onDeviceConnectFail(ConnBleException e) {
                LogUtil.d("链接失败:" + e.toString());
                ThreadUtil.delayTask(() -> {
                    BluetoothLe.getDefault().startConnect(mDfuDevice);
                }, 2000);
            }
        });
    }

    private byte[] readFirmware(String fileName) {
        try {
            InputStream stream = new FileInputStream(fileName);
            int length = stream.available();
            byte[] firmware = new byte[length];
            stream.read(firmware);
            stream.close();
            return firmware;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void requestPermissionsSuccess(int requestCode) {
        super.requestPermissionsSuccess(requestCode);
//        downLoadFile(downUrl);
        updateDfu();
    }

    @Override
    public void requestPermissionsFail(int requestCode) {
        showToast("授权失败");
        FirmwareUpdateActivity.this.finish();
    }

    private void downLoadFile(String filrUrl) {
        File file = new File(saveFileName);
        if (!file.exists()) file.mkdirs();
        final File apkFile = new File(saveFileName + "deviceUpdate.zip");
        if (apkFile.exists()) apkFile.delete();
        new Thread() {
            @Override
            public void run() {
                try {
                    downloadUpdateFile(filrUrl, apkFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private long downloadUpdateFile(String downloadUrl, File saveFile) throws IOException {
        int downloadCount = 0;
        int currentSize = 0;
        int totalSize = 0;
        int updateTotalSize = 0;

        HttpURLConnection httpConnection = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            URL url = new URL(downloadUrl);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setConnectTimeout(10000);
            httpConnection.setReadTimeout(20000);
            updateTotalSize = httpConnection.getContentLength();
            if (httpConnection.getResponseCode() == 404) {
                mHandler.sendEmptyMessage(DOWN_FAILD);
                throw new Exception("fail!");
            }
            is = httpConnection.getInputStream();
            fos = new FileOutputStream(saveFile, false);
            byte buffer[] = new byte[2048];
            int readSize = 0;
            while ((readSize = is.read(buffer)) > 0) {
                fos.write(buffer, 0, readSize);
                totalSize += readSize;
                if ((downloadCount == 0) || (int) (totalSize * 100 / updateTotalSize) - 1 > downloadCount) {
                    downloadCount += 1;
                    Message msg = mHandler.obtainMessage();
                    msg.what = DOWN_UPDATE;
                    msg.arg1 = totalSize;
                    msg.arg2 = updateTotalSize;
                    mHandler.sendMessage(msg);
                }
            }
        } catch (Exception e) {
            mHandler.sendEmptyMessage(DOWN_FAILD);
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
            if (is != null) {
                is.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
        return totalSize;
    }

    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_FAILD = 0;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    downLoadRate(msg.arg1, msg.arg2);
                    break;
                case DOWN_FAILD:
                    showToast("升级文件下载失败，请重新尝试");
                    FirmwareUpdateActivity.this.finish();
                    break;
            }
        }
    };


    private void downLoadRate(long totalSize, long updateTotalSize) {
        if (totalSize >= updateTotalSize) {
            mProgressBar.setProgress((int) (totalSize * 100 / updateTotalSize));
            progressRate.setText((int) (totalSize * 100 / updateTotalSize) + "%");
            tvTips.setText("升级包大小：" + updateTotalSize / 1024 + "KB");
            tvTips2.setText("下载完成");
            ThreadUtil.delayTask(() -> {
                tvTips.setText("升级包大小：" + updateTotalSize / 1024 + "KB");
                updateDfu();
            }, 500);
        } else {
            mProgressBar.setProgress((int) (totalSize * 100 / updateTotalSize));
            progressRate.setText((int) (totalSize * 100 / updateTotalSize) + "%");
            tvTips.setText("升级包大小：" + updateTotalSize / 1024 + "KB");
            tvTips2.setText("升级文件下载中...");
        }
    }

    private void updateDfu() {
        mProgressBar.setProgress(0);
        progressRate.setText("0%");
        tvTips2.setText("DFU连接中...");
        BleSdkWrapper.enterUpdate(new OnLeWriteCharacteristicListener() {

            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                BluetoothLe.getDefault().enableNotification(false, BluetoothUUID.SCAN_SERVICE,
                        new UUID[]{BluetoothUUID.READ});
                BluetoothLe.getDefault().close();
                ThreadUtil.delayTask(() -> {
                    startDfu();
                }, 1000);
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
    }

    private void startDfu() {
        BluetoothLe.getDefault().setScanPeriod(10000)
//                .setScanWithDeviceName("DfuTarg")
                .startScan(FirmwareUpdateActivity.this, new OnLeScanListener() {
                    @Override
                    public void onScanResult(BluetoothDevice bluetoothDevice, int i, ScanRecord scanRecord) {
                        if (ActivityCompat.checkSelfPermission(FirmwareUpdateActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                        }
                        if (bluetoothDevice != null && bluetoothDevice.getName() != null) {
                            if (bluetoothDevice.getName().contains("DfuTarg")) {
                                mDfuDevice = bluetoothDevice;
                                LogUtil.d("搜索到设备");
                                BluetoothLe.getDefault().stopScan();
                                BluetoothLe.getDefault().startConnect(mDfuDevice.getAddress());
                            }
                        }
                    }

                    @Override
                    public void onBatchScanResults(List<ScanResult> list) {

                    }

                    @Override
                    public void onScanCompleted() {

                    }

                    @Override
                    public void onScanFailed(ScanBleException e) {

                    }
                });
    }

    private final DfuProgressListener dfuProgressListener = new DfuProgressListenerAdapter() {
        @Override
        public void onDeviceConnecting(@NonNull final String deviceAddress) { tvTips2.setText("DFU已连接"); }
        @Override
        public void onDfuProcessStarting(@NonNull final String deviceAddress) { tvTips2.setText("DFU升级中..."); }
        @Override
        public void onEnablingDfuMode(@NonNull final String deviceAddress) { }
        @Override public void onFirmwareValidating(@NonNull final String deviceAddress) { }
        @Override
        public void onDeviceDisconnecting(@NonNull final String deviceAddress) { tvTips2.setText("设备断开连接"); }
        @Override
        public void onDfuCompleted(@NonNull final String deviceAddress) {
            new Handler().postDelayed(() -> {
                // if this activity is still open and upload process was completed, cancel the notification
                final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(DfuService.NOTIFICATION_ID);
                tvTips2.setText("升级完成");
            }, 200);
        }
        @Override
        public void onDfuAborted(@NonNull final String deviceAddress) {
            // let's wait a bit until we cancel the notification. When canceled immediately it will be recreated by service again.
            new Handler().postDelayed(() -> {
                // if this activity is still open and upload process was completed, cancel the notification
                final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(DfuService.NOTIFICATION_ID);
            }, 200);
        }

        @Override
        public void onProgressChanged(@NonNull final String deviceAddress, final int percent,
                                      final float speed, final float avgSpeed,
                                      final int currentPart, final int partsTotal) {
            mProgressBar.setProgress(percent);
            progressRate.setText(percent+"%");
        }

        @Override
        public void onError(@NonNull final String deviceAddress, final int error, final int errorType, final String message) {
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BluetoothLe.getDefault().destroy(TAG);
        DfuServiceListenerHelper.unregisterProgressListener(this, dfuProgressListener);
    }
}
