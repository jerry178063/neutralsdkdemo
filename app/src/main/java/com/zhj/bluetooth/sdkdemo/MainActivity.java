package com.zhj.bluetooth.sdkdemo;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.sevice.ServiceWorker;
import com.zhj.bluetooth.sdkdemo.ui.AlarmActivity;
import com.zhj.bluetooth.sdkdemo.ui.DatumLineActivity;
import com.zhj.bluetooth.sdkdemo.ui.DeviceInfoActivity;
import com.zhj.bluetooth.sdkdemo.ui.DeviceStateActivity;
import com.zhj.bluetooth.sdkdemo.ui.DialCenterActivity;
import com.zhj.bluetooth.sdkdemo.ui.DisturbActivity;
import com.zhj.bluetooth.sdkdemo.ui.DrinkWaterActivity;
import com.zhj.bluetooth.sdkdemo.ui.FirmwareUpdateActivity;
import com.zhj.bluetooth.sdkdemo.ui.FirmwareUpdateOtaActivity;
import com.zhj.bluetooth.sdkdemo.ui.GoalActivity;
import com.zhj.bluetooth.sdkdemo.ui.HeartTestActivity;
import com.zhj.bluetooth.sdkdemo.ui.HeartWarnActivity;
import com.zhj.bluetooth.sdkdemo.ui.LongSitActivity;
import com.zhj.bluetooth.sdkdemo.ui.MessageContreActivity;
import com.zhj.bluetooth.sdkdemo.ui.NoticeActivity;
import com.zhj.bluetooth.sdkdemo.ui.RateHistoryActivity;
import com.zhj.bluetooth.sdkdemo.ui.RestingCalorieActivity;
import com.zhj.bluetooth.sdkdemo.ui.ScanDeviceReadyActivity;
import com.zhj.bluetooth.sdkdemo.ui.SportActivity;
import com.zhj.bluetooth.sdkdemo.ui.SportModeActivity;
import com.zhj.bluetooth.sdkdemo.ui.StepsDataActivity;
import com.zhj.bluetooth.sdkdemo.ui.TelephoAvtivity;
import com.zhj.bluetooth.sdkdemo.ui.TempDayDataActivity;
import com.zhj.bluetooth.sdkdemo.ui.TempHistoryActivity;
import com.zhj.bluetooth.sdkdemo.ui.TempTestActivity;
import com.zhj.bluetooth.sdkdemo.ui.TempWaringActivity;
import com.zhj.bluetooth.sdkdemo.ui.Test;
import com.zhj.bluetooth.sdkdemo.ui.UserInfoActivity;
import com.zhj.bluetooth.sdkdemo.ui.WeatherActivity;
import com.zhj.bluetooth.sdkdemo.util.IntentUtil;
import com.zhj.bluetooth.sdkdemo.util.PermissionUtil;
import com.zhj.bluetooth.zhjbluetoothsdk.bean.BLEDevice;
import com.zhj.bluetooth.zhjbluetoothsdk.bean.EcgHistoryData;
import com.zhj.bluetooth.zhjbluetoothsdk.bean.WarningInfo;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleCallbackWrapper;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.BleSdkWrapper;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.CmdHelper;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.HandlerBleDataResult;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.BluetoothLe;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.DeviceCallback;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.OnLeConnectListener;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.exception.ConnBleException;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.exception.WriteBleException;
import com.zhj.bluetooth.zhjbluetoothsdk.util.Constants;
import com.zhj.bluetooth.zhjbluetoothsdk.util.LogUtil;
import com.zhj.bluetooth.zhjbluetoothsdk.util.SPHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity implements PermissionUtil.RequsetResult {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvMac)
    TextView tvMac;
    private OneTimeWorkRequest mRequest;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.main_home_page));

        BluetoothLe.getDefault().init(this, new BleCallbackWrapper() {

            @Override
            public void complete(int resultCode, Object data) {
                Log.d("fff33","resultCode:" + resultCode);
                if (resultCode == Constants.BLE_RESULT_CODE.SUCCESS) {


                    runOnUiThread(() -> checkBind());
                    BluetoothLe.getDefault().setOnConnectListener(MyListenner);
                    //重连
                    BluetoothLe.getDefault().reconnect(MainActivity.this);
                    BluetoothLe.getDefault().addDeviceCallback(new DeviceCallback() {
                        @Override
                        public void enterCamare() {
                            LogUtil.d("进入拍照");
                        }

                        @Override
                        public void camare() {
                            LogUtil.d("拍照");
                        }

                        @Override
                        public void exitCamare() {
                            LogUtil.d("退出拍照");
                        }

                        @Override
                        public void musicControl() {
                        }

                        @Override
                        public void preMusic() {
                            LogUtil.d("上一首");
                        }

                        @Override
                        public void nextMusic() {
                            LogUtil.d("下一首");
                        }

                        @Override
                        public void exitMusic() {
                            LogUtil.d("退出音乐");
                        }

                        @Override
                        public void addVol() {
                            LogUtil.d("音量加");
                        }

                        @Override
                        public void subVol() {
                            LogUtil.d("音量减");
                        }

                        @Override
                        public void findPhone() {
                            LogUtil.d("进入查找手机");
                        }

                        @Override
                        public void answerRingingCall() {
                        }

                        @Override
                        public void endRingingCall() {
                        }

                        @Override
                        public void sos() {
                        }

                        @Override
                        public void WarningInfo(WarningInfo warningInfo) {
                            LogUtil.d("获取报警信息");
                            LogUtil.d("心率报警最高值:" + warningInfo.getWarningHrMax());
                            LogUtil.d("心率报警最低值:" + warningInfo.getWarningHrMin());
                            LogUtil.d("舒张压报警最高值:" + warningInfo.getWarningFzMax());
                            LogUtil.d("舒张压报警最低值:" + warningInfo.getWarningFzMin());
                            LogUtil.d("收缩压报警最高值:" + warningInfo.getWarningSsMax());
                            LogUtil.d("收缩压报警最低值:" + warningInfo.getWarningSsMin());
                            LogUtil.d("血氧报警最低值:" + warningInfo.getWarningOxygenMin());
                            LogUtil.d("体温报警最低值:" + warningInfo.getWarningTemp());
                        }
                    });
//                    BluetoothLe.getDefault().startConnect("A4:C1:38:85:07:7B");
                } else {
                    LogUtil.d("SDK不能使用");
                }
            }

            @Override
            public void setSuccess() {
                Log.d("fff33","setSuccess");

            }
        });
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
        mRequest = new OneTimeWorkRequest.Builder(ServiceWorker.class).setConstraints(constraints)
                .build();
        WorkManager.getInstance(this).enqueue(mRequest);



    }

    private byte completeCheckCode(byte[] bytes) {
        byte sum = 0;
        for (byte b : bytes) {
            sum += b;
        }
        return (byte) (sum * 0x56 + 0x5A);
    }

    private OnLeConnectListener MyListenner = new OnLeConnectListener() {
        @Override
        public void onDeviceConnecting() {
            //正在链接
        }

        @Override
        public void onDeviceConnected() {
            //连接成功
        }

        @Override
        public void onDeviceDisconnected() {
            //连接中断
            MyAppcation.getInstance().setConnected(false);
            BluetoothLe.getDefault().reconnect(MainActivity.this);
            checkBind();
            LogUtil.d("duankailianjie");
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt bluetoothGatt) {
            //发现服务，可以进行通信
//            BluetoothLe.getDefault().enableNotification(true, Device.SERVICE_UUID,
//                    CHARACTERISTIC_UUID_WRITE);
            BluetoothLe.getDefault().setScanFaildCount(0);
            MyAppcation.getInstance().setConnected(true);
            //清除蓝牙缓存
            BluetoothLe.getDefault().clearDeviceCache();
            checkBind();
        }

        @Override
        public void onDeviceConnectFail(ConnBleException e) {
            //连接异常
            MyAppcation.getInstance().setConnected(false);
            BluetoothLe.getDefault().reconnect(MainActivity.this);
        }
    };
    private final int RESULT_CODE_BIND = 1000;

    private void checkBind() {
        if (SPHelper.getBindBLEDevice(this) != null) {
            rightText.setText(getResources().getString(R.string.main_unbind));
            if (MyAppcation.getInstance().isConnected()) {
                tvTitle.setText(getResources().getString(R.string.main_bind_device_connecr));
            } else {
                tvTitle.setText(getResources().getString(R.string.main_bind_device_unconnected));
            }
            BLEDevice bleDevice = SPHelper.getBindBLEDevice(this);
            tvMac.setText(getResources().getString(R.string.main_mac_address) + bleDevice.mDeviceAddress);
        } else {
            rightText.setText(getResources().getString(R.string.main_pairing));
            tvTitle.setText(getResources().getString(R.string.main_no_pairing));
            tvMac.setText("");
        }
        rightText.setOnClickListener(view -> {
            if (SPHelper.getBindBLEDevice(this) != null) {
                BluetoothLe.getDefault().unBind(this);
                MyAppcation.getInstance().setConnected(false);
                tvTitle.setText(getResources().getString(R.string.main_no_pairing));
                tvMac.setText("");
                rightText.setText(getResources().getString(R.string.main_pairing));
            } else {
                if (BluetoothLe.getDefault().isBluetoothOpen()) {
                    IntentUtil.goToActivityForResult(MainActivity.this, ScanDeviceReadyActivity.class, RESULT_CODE_BIND);
                } else {
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
//                        return;
                    }
                    startActivityForResult(intent, 1);
                }
            }
        });
    }

    @OnClick(R.id.btnSendPairing)
    void toSendPairing(){
//        BleSdkWrapper.sendSos("123456","qwer" , new OnLeWriteCharacteristicListener() {
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
//        List<Contacts> contacts = new ArrayList<>();
//        for (int i = 0 ; i <1;i++){
//            Contacts c = new Contacts();
//            c.setName("dwad:"+i);
//            c.setNumber("sdwad:"+i);
//            contacts.add(c);
//        }
//        BleSdkWrapper.sendContacts(contacts, new OnLeWriteCharacteristicListener() {
//            @Override
//            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
//
//            }
//
//            @Override
//            public void onFailed(WriteBleException e) {
//                LogUtil.d(e.toString());
//            }
//        });
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, Test.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
//        NetConnect.getUpdateInfo(this, "T02S90N2", "1.03", new BleCallbackWrapper() {
//            @Override
//            public void complete(int resultCode, Object o) {
//                if(resultCode == Constants.BLE_RESULT_CODE.SUCCESS){
//                    FirmwareUpdate update = (FirmwareUpdate) o;
//                    LogUtil.d(update.toString());
//                }else{
//                    LogUtil.d("获取信息失败");
//                }
//            }
//
//            @Override
//            public void setSuccess() {
//
//            }
//        });
//        NetConnect.getDialCenterInfo(this, "T04S90N7", new BleCallbackWrapper() {
//
//            @Override
//            public void complete(int resultCode, Object o) {
//                if(resultCode == Constants.BLE_RESULT_CODE.SUCCESS){
//                    DicalDataBean dicalDataBean = (DicalDataBean) o;
//                    LogUtil.d(dicalDataBean.toString());
//                }else{
//                    LogUtil.d("获取信息失败");
//                }
//            }
//
//            @Override
//            public void setSuccess() {
//
//            }
//        });
//        NetConnect.getDialCenterInfoList(this, "T04S90N7", new BleCallbackWrapper() {
//
//            @Override
//            public void complete(int resultCode, Object o) {
//                if(resultCode == Constants.BLE_RESULT_CODE.SUCCESS){
//                    DialCenterList dicalDataBean = (DialCenterList) o;
//                    LogUtil.d(dicalDataBean.toString());
//                }else{
//                    LogUtil.d("获取信息失败");
//                }
//            }
//
//            @Override
//            public void setSuccess() {
//
//            }
//        });
//        NetConnect.getDialCenterByModel(this, "T04S90N7", new BleCallbackWrapper() {
//            @Override
//            public void complete(int resultCode, Object o) {
//                if(resultCode == Constants.BLE_RESULT_CODE.SUCCESS){
//                    List<DialCenterDetail> dicalDataBean = (List<DialCenterDetail>) o;
//                    LogUtil.d(dicalDataBean.toString());
//                }else{
//                    LogUtil.d("获取信息失败");
//                }
//            }
//
//            @Override
//            public void setSuccess() {
//
//            }
//        });

//        BleSdkWrapper.getDialInfo(new OnLeWriteCharacteristicListener() {
//            @Override
//            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
//                DialInfoList dialInfoList = (DialInfoList) handlerBleDataResult.data;
//                LogUtil.d(dialInfoList.getCurrentDial().getDialId()+"");
//            }
//
//            @Override
//            public void onFailed(WriteBleException e) {
//
//            }
//        });
//        BleSdkWrapper.getMettInfo(new OnLeWriteCharacteristicListener() {
//            @Override
//            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
//                Map<Integer, Integer> map = (Map<Integer, Integer>) handlerBleDataResult.data;
//                for(Integer key : map.keySet()){
//                    LogUtil.d("key:"+key+",value:"+map.get(key));
//                }
//            }
//
//            @Override
//            public void onFailed(WriteBleException e) {
//
//            }
//        });
//        BleSdkWrapper.startTemperatureMonitoring(new OnLeWriteCharacteristicListener() {
//            @Override
//            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
//                LogUtil.d("开启成功");
//            }
//
//            @Override
//            public void onFailed(WriteBleException e) {
//
//            }
//        });
//        BleSdkWrapper.endTemperatureMonitoring(new OnLeWriteCharacteristicListener() {
//            @Override
//            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
//                LogUtil.d("结束成功");
//            }
//
//            @Override
//            public void onFailed(WriteBleException e) {
//
//            }
//        });
    }
    @OnClick(R.id.btnGetSleep)
    void togGetSleep(){


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        File outFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/000/" + System.currentTimeMillis() + ".jpg");
//        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".fileProvider", outFile);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        startActivity(intent);


        intent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivity(intent);

//        if(MyAppcation.getInstance().isConnected()){
//            IntentUtil.goToActivity(this, SleepDataActivity.class);
//        }else{
//            showToast(getResources().getString(R.string.main_device_unconnected));
//        }
    }


    @OnClick(R.id.btnGetSteps)
    void toGetSteps(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, StepsDataActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnSetHeartTest)
    void toSetHeartTest(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, HeartTestActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnReset)
    void resetDevice(){
        if(MyAppcation.getInstance().isConnected()){
            BleSdkWrapper.recoverSet(new OnLeWriteCharacteristicListener() {

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
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnSendMessage)
    void toSendMessage(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, MessageContreActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnDeviceInfo)
    void getDeviceInfo(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, DeviceInfoActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnDeviceState)
    void getDeviceState(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, DeviceStateActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnUserInfo)
    void getUserInfo(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, UserInfoActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnAlarmInfo)
    void getAlarmInfo(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, AlarmActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnLongSitInfo)
    void getLongSitInfo(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, LongSitActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnGoalInfo)
    void getGoalInfo(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, GoalActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnNoticeInfo)
    void getNoticeInfo(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, NoticeActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnGetRateHistory)
    void getRateHistory(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, RateHistoryActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnHeartWarn)
    void toHeartWarn(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, HeartWarnActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.btnSportActivity)
    void toSport(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, SportActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.tvShowSleepsResting)
    void getRestingCal(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, RestingCalorieActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.tvCurrentTemp)
    void getCurrentTemp(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, TempDayDataActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.tvHistoryTemp)
    void getHistoryTemp(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, TempHistoryActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.tvTempTest)
    void setTempTest(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, TempTestActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.tvDfu)
    void toFirmwareUpdate(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, FirmwareUpdateActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }


    @OnClick(R.id.tvDfuOta)
    void toFirmwareUpdateOta(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, FirmwareUpdateOtaActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.tvTempWaring)
    void toTempWaring(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, TempWaringActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.tvWeather)
    void toWeather(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, WeatherActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.tvSenfDialCenter)
    void toSenfDialCenter(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, DialCenterActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick(R.id.tvDrinkWater)
    void toDrinkWater(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, DrinkWaterActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }
    @OnClick(R.id.tvDisturb)
    void toDisturb(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, DisturbActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }
    @OnClick(R.id.tvSportMode)
    void toSportMode(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, SportModeActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }

    @OnClick({R.id.tvDatumLine})
    void toDatumLine(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, DatumLineActivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }
    @OnClick({R.id.tvTelepho})
    void tvTelepho(){
        if(MyAppcation.getInstance().isConnected()){
            IntentUtil.goToActivity(this, TelephoAvtivity.class);
        }else{
            showToast(getResources().getString(R.string.main_device_unconnected));
        }
    }
    @OnClick(R.id.tvEcg)
    void startEcg(){
    }
    private List<EcgHistoryData> ecgHistoryData = new ArrayList<>();
    private void synchEcg(){
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BluetoothLe.getDefault().close();
    }


}
