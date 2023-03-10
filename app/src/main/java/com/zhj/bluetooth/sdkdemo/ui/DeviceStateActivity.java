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
                //  0x00????????? 0x01????????? 0x02: ???????????? 0x03: ???????????? 0x04????????? 0x05???????????????
                //  0x06??????????????? 0x07????????? 0x08????????? 0x09????????? 0x0A????????????
                //0x0B??????????????? 0x0C: ???????????? 0x0D: ??????
                tvLanguage.setText(getResources().getString(R.string.device_state_language)+deviceState.language);
                //0x00:?????? 0x01:??????
                tvUnit.setText(getResources().getString(R.string.device_state_unit)+deviceState.unit);
                //0x00???24 ????????? 0x01???12 ?????????
                tvTimeFormat.setText(getResources().getString(R.string.device_state_time_system)+deviceState.timeFormat);
                //0x00:?????? 0x01:??????
                tvUpHander.setText(getResources().getString(R.string.device_state_handle_up)+deviceState.upHander);
                //0x00:?????? 0x01:??????
                tvMusic.setText(getResources().getString(R.string.device_state_music_control)+deviceState.isMusic);
                //0x00:?????? 0x01:??????
                tvNotice.setText(getResources().getString(R.string.device_state_messagr_swith)+deviceState.isNotice);
                //0x01:?????? 0x02:?????? ???????????????
                tvHandHabits.setText(getResources().getString(R.string.device_state_hand_hibits)+deviceState.handHabits);
                tvHandHabits.setText("???????????????"+deviceState.tempUnit);
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        } );
        rightText.setText("??????");
        rightText.setOnClickListener(view -> {
            //??????????????????
            deviceState.screenLight = 80;
            deviceState.tempUnit = deviceState.tempUnit == 0 ? 1: 0;
            BleSdkWrapper.setDeviceState(deviceState, new OnLeWriteCharacteristicListener() {
                @Override
                public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                    showToast("????????????");
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
