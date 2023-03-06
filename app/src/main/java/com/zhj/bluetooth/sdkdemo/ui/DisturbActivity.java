package com.zhj.bluetooth.sdkdemo.ui;

import android.app.TimePickerDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.widget.TextView;


import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.util.CustomToggleButton;
import com.zhj.bluetooth.zhjbluetoothsdk.bean.SleepBean;
import com.zhj.bluetooth.zhjbluetoothsdk.bean.SleepBeans;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleSdkWrapper;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.HandlerBleDataResult;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.exception.WriteBleException;
import com.zhj.bluetooth.zhjbluetoothsdk.util.StringUtils;

import butterknife.BindView;


public class DisturbActivity extends BaseActivity {
    @BindView(R.id.mToggleButton)
    CustomToggleButton mToggleButton;

    @BindView(R.id.tvRemindStartTime)
    TextView tvRemindStartTime;
    @BindView(R.id.tvRemindEndTime)
    TextView tvRemindEndTime;
    @Override
    protected int getContentView() {
        return R.layout.activity_disturb;
    }
    private SleepBean sleepBean;
    @Override
    protected void initView() {
        super.initView();
        titleName.setText("勿扰模式");
        rightText.setText("设置");
        rightText.setOnClickListener(v -> {
            sleepBean.setSwitch(mToggleButton.getSwitchState());
            sleepBean.setStartHour(10);
            sleepBean.setStartMin(10);
            sleepBean.setEndHour(15);
            sleepBean.setEndMin(15);
            BleSdkWrapper.setDisturbSetting(sleepBean, new OnLeWriteCharacteristicListener() {
                @Override
                public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                    showToast("设置成功");
                    DisturbActivity.this.finish();
                }

                @Override
                public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

                }

                @Override
                public void onFailed(WriteBleException e) {

                }
            });
        });

        BleSdkWrapper.getDisturbSetting(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                SleepBeans sleepBeans = (SleepBeans) handlerBleDataResult.data;
                sleepBean = sleepBeans.getSleepBeans().get(0);
                mToggleButton.setSwitchState(sleepBean.isSwitch());
                tvRemindStartTime.setText(StringUtils.format("%02d",sleepBean.getStartHour())+":"+StringUtils.format("%02d",sleepBean.getStartMin()));
                tvRemindEndTime.setText(StringUtils.format("%02d",sleepBean.getEndHour())+":"+StringUtils.format("%02d",sleepBean.getEndMin()));
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
