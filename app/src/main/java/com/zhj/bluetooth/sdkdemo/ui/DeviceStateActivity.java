package com.zhj.bluetooth.sdkdemo.ui;

import android.bluetooth.BluetoothGattCharacteristic;
import android.view.View;
import android.widget.TextView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.zhjbluetoothsdk.bean.DeviceState;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleSdkWrapper;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.HandlerBleDataResult;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.exception.WriteBleException;

import butterknife.BindView;

public class DeviceStateActivity extends BaseActivity {

    @BindView(R.id.tvScreenLight)
    TextView tvScreenLight;
    @BindView(R.id.tvScreenTime)
    TextView tvScreenTime;
    @BindView(R.id.tvScreenTheme)
    TextView tvScreenTheme;
    @BindView(R.id.tvLanguage)
    TextView tvLanguage;
    @BindView(R.id.tvUnit)
    TextView tvUnit;
    @BindView(R.id.tvTimeFormat)
    TextView tvTimeFormat;
    @BindView(R.id.tvUpHander)
    TextView tvUpHander;
    @BindView(R.id.tvMusic)
    TextView tvMusic;
    @BindView(R.id.tvNotice)
    TextView tvNotice;
    @BindView(R.id.tvHandHabits)
    TextView tvHandHabits;

    @Override
    protected int getContentView() {
        return R.layout.activity_device_state;
    }
    private DeviceState deviceState;
    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.device_state_title));
        BleSdkWrapper.getDeviceState(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                deviceState = (DeviceState) handlerBleDataResult.data;
                tvScreenLight.setText(getResources().getString(R.string.device_state_screen_brightness)+deviceState.screenLight);
                tvScreenTime.setText(getResources().getString(R.string.device_state_bright_duration)+deviceState.screenTime);
                tvScreenTheme.setText(getResources().getString(R.string.device_state_theme)+deviceState.theme);
                //  0x00：英文 0x01：中文 0x02: 俄罗斯语 0x03: 乌克兰语 0x04：法语 0x05：西班牙语
                //  0x06：葡萄牙语 0x07：德语 0x08：日本 0x09：波兰 0x0A：意大利
                //0x0B：罗马尼亚 0x0C: 繁体中文 0x0D: 韩语
                tvLanguage.setText(getResources().getString(R.string.device_state_language)+deviceState.language);
                //0x00:公制 0x01:英制
                tvUnit.setText(getResources().getString(R.string.device_state_unit)+deviceState.unit);
                //0x00：24 小时制 0x01：12 小时制
                tvTimeFormat.setText(getResources().getString(R.string.device_state_time_system)+deviceState.timeFormat);
                //0x00:关闭 0x01:开启
                tvUpHander.setText(getResources().getString(R.string.device_state_handle_up)+deviceState.upHander);
                //0x00:关闭 0x01:开启
                tvMusic.setText(getResources().getString(R.string.device_state_music_control)+deviceState.isMusic);
                //0x00:关闭 0x01:开启
                tvNotice.setText(getResources().getString(R.string.device_state_messagr_swith)+deviceState.isNotice);
                //0x01:左手 0x02:右手 其他：无效
                tvHandHabits.setText(getResources().getString(R.string.device_state_hand_hibits)+deviceState.handHabits);
                tvHandHabits.setText("体温单位："+deviceState.tempUnit);
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        } );
        rightText.setText("设置");
        rightText.setOnClickListener(view -> {
            //设置体温单位
            deviceState.screenLight = 80;
            deviceState.tempUnit = deviceState.tempUnit == 0 ? 1: 0;
            BleSdkWrapper.setDeviceState(deviceState, new OnLeWriteCharacteristicListener() {
                @Override
                public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                    showToast("设置成功");
                    DeviceStateActivity.this.finish();
                }

                @Override
                public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

                }

                @Override
                public void onFailed(WriteBleException e) {

                }
            });
        });
//
    }
}
