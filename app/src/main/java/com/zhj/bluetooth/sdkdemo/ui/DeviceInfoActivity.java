package com.zhj.bluetooth.sdkdemo.ui;


import android.bluetooth.BluetoothGattCharacteristic;
import android.widget.TextView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.zhjbluetoothsdk.bean.BLEDevice;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleSdkWrapper;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.HandlerBleDataResult;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.exception.WriteBleException;

import butterknife.BindView;

public class DeviceInfoActivity extends BaseActivity {
    @BindView(R.id.tvDeviceProduct)
    TextView tvDeviceProduct;
    @BindView(R.id.tvDeviceAddress)
    TextView tvDeviceAddress;
    @BindView(R.id.tvDeviceVersion)
    TextView tvDeviceVersion;
    @BindView(R.id.tvDevicePower)
    TextView tvDevicePower;

    @Override
    protected int getContentView() {
        return R.layout.activity_device_info;
    }

    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.device_info_title));
        //设备信息里面的设备名称和信号值再搜索设备的时候已经返回了

        BleSdkWrapper.getDeviceInfo(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                BLEDevice bleDevice = (BLEDevice) handlerBleDataResult.data;
                tvDeviceProduct.setText(getResources().getString(R.string.device_info_model) + bleDevice.mDeviceProduct);
                tvDeviceAddress.setText(getResources().getString(R.string.device_info_address) + bleDevice.mDeviceAddress);
                tvDeviceVersion.setText(getResources().getString(R.string.device_info_version) + bleDevice.mDeviceVersion);
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
        //获取设备电量
        BleSdkWrapper.getPower(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                int power = (int) handlerBleDataResult.data;
                tvDevicePower.setText(getResources().getString(R.string.device_info_power) + power);
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
