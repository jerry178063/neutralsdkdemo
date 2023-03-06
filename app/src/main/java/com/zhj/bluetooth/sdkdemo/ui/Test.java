package com.zhj.bluetooth.sdkdemo.ui;

import android.bluetooth.BluetoothGattCharacteristic;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleSdkWrapper;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.HandlerBleDataResult;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.exception.WriteBleException;
import com.zhj.bluetooth.zhjbluetoothsdk.util.LogUtil;

import java.util.Deque;

public class Test extends BaseActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_device_info;
    }


    @Override
    protected void initView() {
        super.initView();
        BleSdkWrapper.getDeviceInfo(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                getDeviceState();
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });

    }

    private void getDeviceState() {
        BleSdkWrapper.getDeviceState(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                getPower();
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
    }

    private void getPower() {
        BleSdkWrapper.getPower(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                getAlarmList();
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
    }

    private void getAlarmList() {
        BleSdkWrapper.getAlarmList(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                getLongSit();
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
    }

    private void getLongSit() {
        BleSdkWrapper.getLongSit(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                getDialInfo();
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
    }

    private void getDialInfo() {
        BleSdkWrapper.getDialInfo(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                getNotice();
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
    }

    private void getNotice() {
        BleSdkWrapper.getNotice(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                enterCamare();
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
    }

    private void enterCamare() {
        BleSdkWrapper.enterCamare(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                exitCamare();
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
    }

    private void exitCamare() {
        BleSdkWrapper.exitCamare(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                findPhone();
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
    }

    private void findPhone() {
        BleSdkWrapper.findPhone(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                answerRingingCallToDevice();
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
    }

    private void answerRingingCallToDevice() {
        BleSdkWrapper.answerRingingCallToDevice(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                endRingingCallToDevice();
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
    }

    private void endRingingCallToDevice() {
        BleSdkWrapper.endRingingCallToDevice(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                startHeartTest();
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
    }

    private void startHeartTest() {
        BleSdkWrapper.startHeartTest(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                setMessage();
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
    }

    private void setMessage() {
        BleSdkWrapper.setMessage(1, "ssssss", "sssss", new OnLeWriteCharacteristicListener() {
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

    private OnLeWriteCharacteristicListener MyListener = new OnLeWriteCharacteristicListener() {
        @Override
        public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
//            LogUtil.d("handlerBleDataResult.bluetooth_code:"+handlerBleDataResult.bluetooth_code);
//            if (handlerBleDataResult.bluetooth_code == BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_DEVICE_INFO){
//                LogUtil.d("获取设备信息");
//            }else if (handlerBleDataResult.bluetooth_code == BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_DEVICE_STATE){
//                LogUtil.d("获取设备状态");
//            }else if (handlerBleDataResult.bluetooth_code == BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_DEVICE_POWER){
//                LogUtil.d("获取设备电量");
//            }else if (handlerBleDataResult.bluetooth_code == BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_ALARM_CLOCK){
//                LogUtil.d("获取设备闹钟");
//            }else if (handlerBleDataResult.bluetooth_code == BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_SEDENTARY){
//                LogUtil.d("获取设备久坐");
//            }else if (handlerBleDataResult.bluetooth_code == BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_DIAL_INFO){
//                LogUtil.d("获取设备表盘");
//            }else if (handlerBleDataResult.bluetooth_code == BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_NOTICE){
//                LogUtil.d("获取设备消息");
//            }else if (handlerBleDataResult.bluetooth_code == BleSdkWrapper.BLUETOOTH_CODE.CODE_SEND_MESSAGE){
//                LogUtil.d("发送设备消息");
//            }
        }

        @Override
        public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

        }

        @Override
        public void onFailed(WriteBleException e) {

        }
    };

}
