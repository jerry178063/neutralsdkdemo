package com.zhj.bluetooth.sdkdemo.ui;

import android.Manifest;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Environment;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.zhj.bluetooth.sdkdemo.MyAppcation;
import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.base.BaseAdapter;
import com.zhj.bluetooth.sdkdemo.ui.adapter.RecommendItemAdapter;
import com.zhj.bluetooth.sdkdemo.util.IntentUtil;
import com.zhj.bluetooth.zhjbluetoothsdk.bean.DialCenterDetail;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleCallbackWrapper;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleSdkWrapper;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.HandlerBleDataResult;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.exception.WriteBleException;
import com.zhj.bluetooth.zhjbluetoothsdk.net.NetConnect;
import com.zhj.bluetooth.zhjbluetoothsdk.util.Constants;
import com.zhj.bluetooth.zhjbluetoothsdk.util.FileUtil;
import com.zhj.bluetooth.zhjbluetoothsdk.util.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class DialCenterActivity extends BaseActivity implements BaseAdapter.OnItemClickListener {

    private String[] pers = new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
    private final static int WRITE_EXTERNAL_STORAGE_REQUEST_CODE=200;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tvProgress)
    TextView tvProgress;
    private List<DialCenterDetail> dicalDataBean;
    @Override
    protected int getContentView() {
        return R.layout.activity_dial_center;
    }

    @Override
    protected void initView() {
        super.initView();
        titleName.setText("设置表盘");
        ((SimpleItemAnimator) Objects.requireNonNull(mRecyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        if (!checkSelfPermission(pers)){
            requestPermissions(WRITE_EXTERNAL_STORAGE_REQUEST_CODE,pers);
        }
//        BleSdkWrapper.getDialInfo(new OnLeWriteCharacteristicListener() {
//            @Override
//            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
//                DialInfoList dialInfoList = (DialInfoList) handlerBleDataResult.data;
//                LogUtil.d("获取表盘："+dialInfoList.getCurrentDial().getDialId());
//            }
//
//            @Override
//            public void onFailed(WriteBleException e) {
//
//            }
//        });
        NetConnect.getDialCenterByModel(this, "T22Q19N7", new BleCallbackWrapper() {
            @Override
            public void complete(int resultCode, Object o) {
                if(resultCode == Constants.BLE_RESULT_CODE.SUCCESS){
                    runOnUiThread(() -> {
                        dicalDataBean = (List<DialCenterDetail>) o;
                        RecommendItemAdapter itemAdapter = new RecommendItemAdapter(DialCenterActivity.this,dicalDataBean,240,280);
                        mRecyclerView.setAdapter(itemAdapter);
                        itemAdapter.setOnItemClickListener(DialCenterActivity.this);
                    });
                }else{
                    LogUtil.d("获取信息失败");
                }
            }

            @Override
            public void setSuccess() {

            }
        });

    }
    private DialCenterDetail sendDial;
    @Override
    public void onItemClick(View view, int position) {
        sendDial = dicalDataBean.get(position);
        downLoadFile(sendDial.getFileUrl());
    }

    private File dialFile;
    private String fileName;
    private  String saveFileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/SDKDemo/DialCenter/";
    private void downLoadFile(String fileUrl) {
        File file = new File(saveFileName);
        if(file.exists()){
            FileUtil.deleteDir(file);
        }
        FileUtil.createOrExistsDir(saveFileName);
        fileName =  System.currentTimeMillis() + ".bin";
        dialFile = new File(saveFileName + fileName);
        if(dialFile.exists()) FileUtil.deleteFile(dialFile);
        new Thread() {
            @Override
            public void run() {
                try {
                    downloadUpdateFile(fileUrl, dialFile);
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
            if (httpConnection.getResponseCode() == 404){
                throw new Exception("fail!");
            }
            is = httpConnection.getInputStream();
            fos = new FileOutputStream(saveFile, false);
            byte buffer[] = new byte[2048];
            int readSize;
            while ((readSize = is.read(buffer)) > 0) {
                fos.write(buffer, 0, readSize);
                totalSize += readSize;
                if ((downloadCount == 0) || (int) (totalSize * 100 / updateTotalSize) - 1 > downloadCount) {
                    downloadCount += 1;
                    if(totalSize >= updateTotalSize){
                        sendDialToDevice();
                    }
                }
            }
        }catch (Exception e){
            LogUtil.d(e.toString());
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
            if(is != null){
                is.close();
            }
            if(fos != null){
                fos.close();
            }
        }
        return totalSize;
    }
    private void sendDialToDevice() {
        BleSdkWrapper.sendDailCenter(sendDial.getCustomId(), dialFile, new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                int progress = (int) handlerBleDataResult.data;
                tvProgress.setText("发送进度："+progress);
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {
                LogUtil.d("升级失败："+e.toString());
            }
        });
    }
}
