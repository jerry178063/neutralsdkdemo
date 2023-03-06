package com.zhj.bluetooth.sdkdemo.ui;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.ui.adapter.DrinkWaterAdapter;
import com.zhj.bluetooth.sdkdemo.views.ItemToggleLayout;
import com.zhj.bluetooth.zhjbluetoothsdk.bean.DrinkWaterBean;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleSdkWrapper;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.HandlerBleDataResult;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.exception.WriteBleException;
import com.zhj.bluetooth.zhjbluetoothsdk.util.LogUtil;

import butterknife.BindView;

public class DrinkWaterActivity extends BaseActivity {
    @BindView(R.id.itDrinkWater)
    ItemToggleLayout itDrinkWater;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;


    @Override
    protected int getContentView() {
        return R.layout.activity_drink_water;
    }
    private DrinkWaterBean drinkWaterBean;
    private int i = 0;
    @Override
    protected void initView() {
        super.initView();
        titleName.setText("喝水提醒");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            BleSdkWrapper.getDrinkWaterWarm(new OnLeWriteCharacteristicListener() {
                @Override
                public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                    if (handlerBleDataResult.isComplete) {
                        if (!handlerBleDataResult.hasNext) {
                            drinkWaterBean = (DrinkWaterBean) handlerBleDataResult.data;
                            itDrinkWater.setOpen(drinkWaterBean.isSwitch());
                            DrinkWaterAdapter adapter = new DrinkWaterAdapter(DrinkWaterActivity.this, drinkWaterBean.getmList());
                            mRecyclerView.setAdapter(adapter);
                            rightText.setText("设置:" + drinkWaterBean.getWaterGoal());
                        }
                    }
                }

                @Override
                public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

                }

                @Override
                public void onFailed(WriteBleException e) {
                }
            });
        rightText.setOnClickListener(view -> {
            drinkWaterBean.setWaterGoal(drinkWaterBean.getWaterGoal()+200);
            drinkWaterBean.setSwitch(itDrinkWater.isOpen());
            drinkWaterBean.getmList().get(3).setHour(11);
            drinkWaterBean.getmList().get(3).setMinuter(11);
            drinkWaterBean.getmList().get(5).setHour(13);
            drinkWaterBean.getmList().get(5).setMinuter(13);
            drinkWaterBean.getmList().get(7).setHour(15);
            drinkWaterBean.getmList().get(7).setMinuter(15);
            BleSdkWrapper.setDrinkWaterWarm(drinkWaterBean, new OnLeWriteCharacteristicListener() {
                @Override
                public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                    LogUtil.d("设置成功");
                    DrinkWaterActivity.this.finish();
                }

                @Override
                public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

                }

                @Override
                public void onFailed(WriteBleException e) {

                }
            });
        });
    }
}
