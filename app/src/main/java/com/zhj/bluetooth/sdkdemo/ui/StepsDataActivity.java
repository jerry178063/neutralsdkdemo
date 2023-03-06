package com.zhj.bluetooth.sdkdemo.ui;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleSdkWrapper;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.HandlerBleDataResult;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.exception.WriteBleException;

import butterknife.BindView;

public class StepsDataActivity extends BaseActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_steps;
    }

    @BindView(R.id.tvSteps)
    TextView tvSteps;
    @BindView(R.id.tvCal)
    TextView tvCal;
    @BindView(R.id.tvDistance)
    TextView tvDistance;
    @BindView(R.id.tvRate)
    TextView tvRate;
    @BindView(R.id.tvFz)
    TextView tvFz;
    @BindView(R.id.tvSs)
    TextView tvSs;

    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.steps_current_title));
        //获取当前步数
        BleSdkWrapper.getCurrentStep(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                Log.d("FF4332","DATA:" + new Gson().toJson(handlerBleDataResult.data));
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {
                Log.d("FF4332","e:" + e);
            }
        });
    }


    private void getCurrentRate(){
        //获取当前心率
        BleSdkWrapper.getHeartRate(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {

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
