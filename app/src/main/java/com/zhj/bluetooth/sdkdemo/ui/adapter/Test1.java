package com.zhj.bluetooth.sdkdemo.ui.adapter;

import android.bluetooth.BluetoothGattCharacteristic;

import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleSdkWrapper;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.HandlerBleDataResult;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.exception.WriteBleException;

public class Test1 extends BaseActivity {
    @Override
    protected int getContentView() {
        return 0;
    }


    @Override
    protected void initView() {
        super.initView();
        getDeviceState();
    }


    private void initDevice(){
        getDeviceState();
    }

    private void  getDeviceState(){
        BleSdkWrapper.getDeviceState(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if(handlerBleDataResult == null)  return;
                if(!handlerBleDataResult.hasNext && handlerBleDataResult.isComplete){
                    //success  do  next
                    getDeviceInfo();
                }
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
    }
    private void  getDeviceInfo(){
        BleSdkWrapper.getDeviceInfo(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if(handlerBleDataResult == null)  return;
                if(!handlerBleDataResult.hasNext && handlerBleDataResult.isComplete){
                    //   do next
                }
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
