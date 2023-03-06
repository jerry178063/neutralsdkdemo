package com.zhj.bluetooth.sdkdemo.ui;

import android.bluetooth.BluetoothGattCharacteristic;
import android.widget.TextView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.zhjbluetoothsdk.bean.Goal;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleSdkWrapper;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.HandlerBleDataResult;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.exception.WriteBleException;

import butterknife.BindView;

public class GoalActivity extends BaseActivity {

    @BindView(R.id.tvSleepTimeState)
    TextView tvSleepTimeState;
    @BindView(R.id.tvSleepTime)
    TextView tvSleepTime;
    @BindView(R.id.tvStepsState)
    TextView tvStepsState;
    @BindView(R.id.tvSteps)
    TextView tvSteps;
    @BindView(R.id.tvCalState)
    TextView tvCalState;
    @BindView(R.id.tvCal)
    TextView tvCal;
    @BindView(R.id.tvDistanceState)
    TextView tvDistanceState;
    @BindView(R.id.tvDistance)
    TextView tvDistance;

    @Override
    protected int getContentView() {
        return R.layout.activity_goal;
    }

    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.target_info_title));
        BleSdkWrapper.getTarget(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                Goal goal = (Goal) handlerBleDataResult.data;
                tvSleepTimeState.setText(getResources().getString(R.string.target_info_sleep_swith)+goal.sleepstate);//0x01:开 0x00:关
                tvSleepTime.setText(getResources().getString(R.string.target_info_sleep_time)+goal.goalSleep);//单位小时
                tvStepsState.setText(getResources().getString(R.string.target_info_steps_swith)+goal.stepstate);//0x01:开 0x00:关
                tvSteps.setText(getResources().getString(R.string.target_info_steps)+goal.goalStep);//单位步
                tvCalState.setText(getResources().getString(R.string.target_info_cal_swith)+goal.calstate);//0x01:开 0x00:关
                tvCal.setText(getResources().getString(R.string.target_info_cal)+goal.goalCal);//单位千卡
                tvDistanceState.setText(getResources().getString(R.string.target_info_distance_swith)+goal.distancestate);//0x01:开 0x00:关
                tvDistance.setText(getResources().getString(R.string.target_info_distance)+goal.goalDistanceKm+"KM");//单位千米
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        } );
    }
}
