package com.zhj.bluetooth.sdkdemo.ui;

import android.bluetooth.BluetoothGattCharacteristic;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.ui.adapter.AlarmListAdapter;
import com.zhj.bluetooth.zhjbluetoothsdk.bean.Alarm;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleSdkWrapper;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.HandlerBleDataResult;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.exception.WriteBleException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AlarmActivity extends BaseActivity {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private AlarmListAdapter mAdapter;

    private List<Alarm> alarmList;
    @Override
    protected int getContentView() {
        return R.layout.activity_alarm;
    }

    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.alarm_info_title));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //获取手环中设置的闹钟
        BleSdkWrapper.getAlarmList(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if(handlerBleDataResult.isComplete){
                    alarmList = (List<Alarm>) handlerBleDataResult.data;
                    mAdapter = new AlarmListAdapter(AlarmActivity.this,alarmList);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        } );
        //设置新的闹钟   修改alarmList
//        BleSdkWrapper.setAlarm(alarmList, new OnLeWriteCharacteristicListener() {
//            @Override
//            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
//
//            }
//
//            @Override
//            public void onFailed(WriteBleException e) {
//
//            }
//        });
    }
}
