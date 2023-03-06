package com.zhj.bluetooth.sdkdemo.sevice;

import android.app.Service;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;


import com.zhj.bluetooth.zhjbluetoothsdk.ble.ByteDataConvertUtil;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.CmdHelper;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.HandlerBleDataResult;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.BluetoothLe;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.exception.WriteBleException;
import com.zhj.bluetooth.zhjbluetoothsdk.util.BaseCmdUtil;

import java.util.List;

public class PhoneReceiver extends BroadcastReceiver {
    private static final String TAG = "FF4534";
    private int msgTypeMsg = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            // 如果是拨打电话
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            String outNumber = this.getResultData();// 去电号码
            Log.i(TAG, "call OUT 1:" + phoneNumber);
            Log.i(TAG, "call OUT 2:" + outNumber);

        } else if ("android.intent.action.PHONE_STATE".equals(intent.getAction())) {
            // 如果是来电
            TelephonyManager tManager = (TelephonyManager) context
                    .getSystemService(Service.TELEPHONY_SERVICE);
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            // 来电号码
            String mIncomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.i(TAG, "call IN 1:" + state);
            Log.i(TAG, "call IN 2:" + mIncomingNumber);

            switch (tManager.getCallState()) {
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d(TAG, "**********************监测到电话呼入!!!!*****");

                    BluetoothLe.getDefault().destroy(TAG);
                    BluetoothLe.getDefault().setOnWriteCharacteristicListener(TAG, new OnLeWriteCharacteristicListener() {
                        @Override
                        public void onSuccess(HandlerBleDataResult result) {
                        }

                        @Override
                        public void onSuccessCharac(BluetoothGattCharacteristic characteristic) {

                            byte[] to = new byte[20];
                            BaseCmdUtil.copy(characteristic.getValue(), to);
                            Log.d("FF4555","回复1数据demo:" + ByteDataConvertUtil.bytesToHexString(to));
                            if((to[0]&255) == 0x8A && (to[3]&255) == 0x00){
                                List<byte[]> datas = CmdHelper.setMessage2(1,mIncomingNumber  + "");
                                for (int i=0;i<datas.size();i++){
                                    Log.d("FF4555","回复2数据demo:" + ByteDataConvertUtil.bytesToHexString(to));
                                    Log.d("FF4555","发送第二针:" + ByteDataConvertUtil.bytesToHexString(datas.get(i)));
                                    BluetoothLe.getDefault().writeDataToCharacteristic(datas.get(i));
                                }
                                Log.d("FF4534","11111:");
                            }else if((to[0]&255) == 0x8A && (to[3]&255) == 0x01){
                                List<byte[]> datas2 = CmdHelper.setMessage2(2,mIncomingNumber + "");
                                for (int i=0;i<datas2.size();i++){
                                    Log.d("FF4555","回复3数据demo:" + ByteDataConvertUtil.bytesToHexString(to));
                                    Log.d("FF4555","发送第三针:" + ByteDataConvertUtil.bytesToHexString(datas2.get(i)));
                                    BluetoothLe.getDefault().writeDataToCharacteristic(datas2.get(i));
                                }
                                Log.d("FF4534","22222:" );
                            }else if((to[0]&255) == 0x8A && (to[3]&255) == 0x02){
                                Log.d("FF4555","回复4数据demo:" + ByteDataConvertUtil.bytesToHexString(to));
                                Log.d("FF4555","发送第四针:" + ByteDataConvertUtil.bytesToHexString(CmdHelper.END_MESSAGE));
                                BluetoothLe.getDefault().writeDataToCharacteristic(CmdHelper.END_MESSAGE);
                                Log.d("FF4534","33333:");
                            }else if((to[0]&255) == 0x8A && (to[3]&255) == 0x03){
                            }
                        }

                        @Override
                        public void onFailed(WriteBleException e) {
                            Log.d("FF4534","e:" + e);
                        }
                    });
                    Log.d("FF4555","发送第一针:" + ByteDataConvertUtil.bytesToHexString(CmdHelper.setMessageType(msgTypeMsg)));
                    BluetoothLe.getDefault().writeDataToCharacteristic(CmdHelper.setMessageType(msgTypeMsg));

//                    BluetoothLe.getDefault().writeDataToCharacteristic("telepho",CmdHelper.setMessageType(msgTypeMsg), new OnLeWriteCharacteristicListener() {
//
//                        @Override
//                        public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
//                            Log.d("FF4534","回复第一针00:" + handlerBleDataResult.data);
//                        }
//
//                        @Override
//                        public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
//                            Log.d("FF4534","回复第一针:" + bluetoothGattCharacteristic.getValue());
//                        }
//
//                        @Override
//                        public void onFailed(WriteBleException e) {
//
//                        }
//                    });
//                    //获取当前步数
//                    BleSdkWrapper.getRealtimeSteps(new OnLeWriteCharacteristicListener() {
//                        @Override
//                        public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
//                            Log.d("FF4534","获取当前步数00:" + handlerBleDataResult.data);
//                        }
//
//                        @Override
//                        public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
//                        }
//
//                        @Override
//                        public void onFailed(WriteBleException e) {
//
//                        }
//                    });
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d(TAG, "**********************监测到接听电话!!!!************");
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d(TAG, "**********************监测到挂断电话!!!!*******************");
                    break;
            }
        }
    }
}
