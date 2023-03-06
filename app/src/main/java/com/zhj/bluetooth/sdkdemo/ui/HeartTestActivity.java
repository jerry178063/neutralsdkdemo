package com.zhj.bluetooth.sdkdemo.ui;

import android.bluetooth.BluetoothGattCharacteristic;
import android.widget.Button;
import android.widget.TextView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.util.CustomToggleButton;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleSdkWrapper;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.HandlerBleDataResult;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.exception.WriteBleException;
import com.zhj.bluetooth.zhjbluetoothsdk.util.LogUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class    HeartTestActivity extends BaseActivity {

    @BindView(R.id.mSwitch)
    CustomToggleButton mSwitch;
    @BindView(R.id.tvTag)
    TextView tvTag;

    @Override
    protected int getContentView() {
        return R.layout.activity_heart_test;
    }

    @Override
    protected void initView() {
        super.initView();
        tvTag.setText(getResources().getString(R.string.heart_test_swith));
        titleName.setText(getResources().getString(R.string.heart_test_title));

    }


    @OnClick(R.id.startHeart)
    void startHeart(){
        BleSdkWrapper.startHeartTest(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                LogUtil.d("Heart rate monitoring has started");
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
    }

    @OnClick(R.id.endHeart)
    void endHeart(){
        BleSdkWrapper.endHeartTest(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                LogUtil.d("Heart rate monitoring has ended");
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
    }
}
