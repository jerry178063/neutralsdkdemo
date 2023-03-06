package com.zhj.bluetooth.sdkdemo.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.sevice.AssistService;
import com.zhj.bluetooth.sdkdemo.sevice.PhoneReceiver;
import com.zhj.bluetooth.sdkdemo.util.DialogHelperNew;
import com.zhj.bluetooth.sdkdemo.util.PhoneUtil;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.CmdHelper;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.DeviceCallbackWrapper;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.BluetoothLe;

public class TelephoAvtivity extends BaseActivity {

    private static final int REQUEST_NOTICE_PERMISSION_CODE = 0x12;
    private Dialog mDialog;
    @Override
    protected int getContentView() {
        return R.layout.activity_telepho;
    }

    @Override
    protected void initView() {
        super.initView();

        //启动服务
        Intent assistIntent = new Intent(this, AssistService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(assistIntent);
        } else {
            startService(assistIntent);
        }

//        startActivityForResult(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"), REQUEST_NOTICE_PERMISSION_CODE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(1100,new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN});
            }
        }else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(1100, new String[]{Manifest.permission.CALL_PHONE});
            }
        }
        // 如果CALL_PHONE权限没有被赋予
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ANSWER_PHONE_CALLS)!= PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.MODIFY_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS)!= PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.MEDIA_CONTENT_CONTROL)!= PackageManager.PERMISSION_GRANTED) {

            // 请求权限
            // permissions请求的权限
            // requestCode:应用程序特定的请求代码以匹配报告给OnRequestPermissionsResultCallback#onRequestPermissionsResult(int, String[], int[])}
            // 也就是下面回调的OnRequestPermissionResult()方法
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.ANSWER_PHONE_CALLS
                    ,Manifest.permission.MODIFY_PHONE_STATE,Manifest.permission.PROCESS_OUTGOING_CALLS,Manifest.permission.MEDIA_CONTENT_CONTROL},10010);
            Log.d("FF4534", "request_permission():正在申请权限！");
        }else {
            Log.d("FF4534", "request_permission():已经拥有权限！");
        }
        BluetoothLe.getDefault().addDeviceCallback(deviceCallback);

    }
    DeviceCallbackWrapper deviceCallback = new DeviceCallbackWrapper(){
        @SuppressLint("MissingPermission")
        @Override
        public void answerRingingCall() {
            BluetoothLe.getDefault().writeDataToCharacteristic(CmdHelper.answerRingingCallToDevice());
            TelecomManager tm = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tm = (TelecomManager) TelephoAvtivity.this.getSystemService(Context.TELECOM_SERVICE);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tm.acceptRingingCall();
            }
        }

        @Override
        public void endRingingCall() {
            BluetoothLe.getDefault().writeDataToCharacteristic(CmdHelper.endRingingCallToDevice());
            PhoneUtil.endCall(TelephoAvtivity.this);
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_NOTICE_PERMISSION_CODE){
//            requestNotice();
        }
    }

    private void requestNotice(){
        if(!isNotificationEnabled()) {
            mDialog = DialogHelperNew.showRemindDialog(this, getResources().getString(R.string.permisson_notication_title),
                    getResources().getString(R.string.permisson_notication_tips), getResources().getString(R.string.permisson_location_open), view -> {
                        startActivityForResult(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"), REQUEST_NOTICE_PERMISSION_CODE);
                        mDialog.dismiss();
                    }, view -> {
                        mDialog.dismiss();
                        TelephoAvtivity.this.finish();
                    });
        }else{
            Log.d("FF332","监听手环广播...");
            Intent assistIntent = new Intent(this, PhoneReceiver.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(assistIntent);
            } else {
                startService(assistIntent);
            }
        }
    }

    private boolean isNotificationEnabled() {
        ContentResolver contentResolver = getContentResolver();
        String enabledListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        if (!TextUtils.isEmpty(enabledListeners)) {
            return enabledListeners.contains(PhoneReceiver.class.getName());
        } else {
            return false;
        }
    }
}
