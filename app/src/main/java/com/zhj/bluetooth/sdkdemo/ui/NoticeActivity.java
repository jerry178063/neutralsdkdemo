package com.zhj.bluetooth.sdkdemo.ui;



import android.bluetooth.BluetoothGattCharacteristic;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.views.ItemToggleLayout;
import com.zhj.bluetooth.zhjbluetoothsdk.bean.AppNotice;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleSdkWrapper;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.HandlerBleDataResult;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.exception.WriteBleException;

import butterknife.BindView;

public class NoticeActivity extends BaseActivity {

    @BindView(R.id.itMessage)
    ItemToggleLayout itMessage;
    @BindView(R.id.itCall)
    ItemToggleLayout itCall;
    @BindView(R.id.itQQ)
    ItemToggleLayout itQQ;
    @BindView(R.id.itFacebook)
    ItemToggleLayout itFacebook;
    @BindView(R.id.itWeChat)
    ItemToggleLayout itWeChat;
    @BindView(R.id.itLinked)
    ItemToggleLayout itLinked;
    @BindView(R.id.itSkype)
    ItemToggleLayout itSkype;
    @BindView(R.id.itInstagram)
    ItemToggleLayout itInstagram;
    @BindView(R.id.itTwitter)
    ItemToggleLayout itTwitter;
    @BindView(R.id.itLine)
    ItemToggleLayout itLine;
    @BindView(R.id.itWhatsApp)
    ItemToggleLayout itWhatsApp;
    @BindView(R.id.itVK)
    ItemToggleLayout itVK;
    @BindView(R.id.itMessenger)
    ItemToggleLayout itMessenger;

    @Override
    protected int getContentView() {
        return R.layout.activity_notice;
    }

    @Override
    protected void initView() {
        super.initView();
        titleName.setText("消息通知开关");
        BleSdkWrapper.getNotice(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                AppNotice appNotice = (AppNotice) handlerBleDataResult.data;
                setNoticeState(appNotice);
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        } );

    }

    private void setNoticeState(AppNotice appNotice) {
        itMessage.setOpen(appNotice.sms);
        itQQ.setOpen(appNotice.qq);
        itFacebook.setOpen(appNotice.facebook);
        itInstagram.setOpen(appNotice.instagram);
        itLine.setOpen(appNotice.line);
        itLinked.setOpen(appNotice.linked);
        itSkype.setOpen(appNotice.skype);
        itTwitter.setOpen(appNotice.twitter);
        itVK.setOpen(appNotice.vk);
        itWeChat.setOpen(appNotice.wechat);
        itWhatsApp.setOpen(appNotice.whatsApp);
        itMessenger.setOpen(appNotice.messager);
        itMessage.setOnToggleListener((layout, isSwitchOn) -> {
            appNotice.sms = isSwitchOn;
            BleSdkWrapper.setNotice(appNotice, null);
        });
        itCall.setOpen(appNotice.incoming);
        itCall.setOnToggleListener((layout, isSwitchOn) -> {
            appNotice.incoming = isSwitchOn;
            BleSdkWrapper.setNotice(appNotice, null);
        });
        itWhatsApp.setOnToggleListener((layout, isSwitchOn) -> {
            appNotice.whatsApp = isSwitchOn;
            BleSdkWrapper.setNotice(appNotice, null);
        });
        itInstagram.setOnToggleListener((layout, isSwitchOn) -> {
            appNotice.instagram=isSwitchOn;
            BleSdkWrapper.setNotice(appNotice, null);
        });
        itQQ.setOnToggleListener((layout, isSwitchOn) -> {
            appNotice.qq=isSwitchOn;
            BleSdkWrapper.setNotice(appNotice, null);
        });
        itFacebook.setOnToggleListener((layout, isSwitchOn) -> {
            appNotice.facebook=isSwitchOn;
            BleSdkWrapper.setNotice(appNotice, null);
        });
        itLine.setOnToggleListener((layout, isSwitchOn) -> {
            appNotice.line=isSwitchOn;
            BleSdkWrapper.setNotice(appNotice, null);
        });
        itLinked.setOnToggleListener((layout, isSwitchOn) -> {
            appNotice.linked=isSwitchOn;
            BleSdkWrapper.setNotice(appNotice, null);
        });
        itSkype.setOnToggleListener((layout, isSwitchOn) -> {
            appNotice.skype=isSwitchOn;
            BleSdkWrapper.setNotice(appNotice, null);
        });
        itTwitter.setOnToggleListener((layout, isSwitchOn) -> {
            appNotice.twitter=isSwitchOn;
            BleSdkWrapper.setNotice(appNotice, null);
        });
        itVK.setOnToggleListener((layout, isSwitchOn) -> {
            appNotice.vk=isSwitchOn;
            BleSdkWrapper.setNotice(appNotice, null);
        });
        itWeChat.setOnToggleListener((layout, isSwitchOn) -> {
            appNotice.wechat=isSwitchOn;
            BleSdkWrapper.setNotice(appNotice, null);
        });
        itMessenger.setOnToggleListener((layout, isSwitchOn) -> {
            appNotice.messager=isSwitchOn;
            BleSdkWrapper.setNotice(appNotice, null);
        });
    }
}
