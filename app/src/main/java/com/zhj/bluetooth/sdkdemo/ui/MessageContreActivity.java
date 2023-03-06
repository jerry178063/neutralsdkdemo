package com.zhj.bluetooth.sdkdemo.ui;

import android.bluetooth.BluetoothGattCharacteristic;
import android.view.View;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.views.ItemToggleLayout;
import com.zhj.bluetooth.zhjbluetoothsdk.bean.AppNotice;
import com.zhj.bluetooth.zhjbluetoothsdk.bean.DeviceState;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleSdkWrapper;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.HandlerBleDataResult;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.exception.WriteBleException;

import butterknife.BindView;
import butterknife.OnClick;

public class MessageContreActivity extends BaseActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_message;
    }

    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.message_info_title));
        //发送前请打开设备状态里面的消息开关
        DeviceState deviceState = new DeviceState();
        deviceState.isNotice = 1;
//        BleSdkWrapper.setDeviceState(deviceState, new OnLeWriteCharacteristicListener() {
//            @Override
//            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
//            }
//
//            @Override
//            public void onFailed(WriteBleException e) {
//
//            }
//        } );

        BleSdkWrapper.getNotice(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                AppNotice appNotice = (AppNotice) handlerBleDataResult.data;
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        } );

    }
    //英语
    @OnClick(R.id.tvSendMessageEh)
    void sendMessageEh(){
        /**
         * CATEGORYID_INCOMING = 0x00,//来电
         * CATEGORYID_SMS = 0x01,
         * CATEGORYID_WEIXIN = 0x02
         * CATEGORYID_MQQ = 0x03,
         * CATEGORYID_FACEBOOK = 0x04,
         * CATEGORYID_SKYPE = 0x05,
         * CATEGORYID_TWITTER = 0x06,
         * CATEGORYID_WHATISAPP = 0x07,
         * CATEGORYID_LINE = 0x08 ,
         * CATEGORYID_EMAIL = 0x09 ,
         * CATEGORYID_INSTAGRAM = 0x0A ,
         * CATEGORYID_LINKEDIN = 0x0B ,
         * CATEGORYID_UNKNOW = 0xFF,  //自定义消息
         */


    }
    //法语
    @OnClick(R.id.tvSendMessageFr)
    void sendMessageFr(){

    }
    //西班牙语
    @OnClick(R.id.tvSendMessageEs)
    void sendMessageEs(){

    }
    //德语
    @OnClick(R.id.tvSendMessageDe)
    void sendMessageDe(){

    }

}
