package com.zhj.bluetooth.sdkdemo.sevice;

import static android.app.NotificationManager.IMPORTANCE_HIGH;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;

import com.zhj.bluetooth.sdkdemo.MainActivity;
import com.zhj.bluetooth.sdkdemo.MyAppcation;
import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.CmdHelper;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.HandlerBleDataResult;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.BluetoothLe;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.bluetooth.zhjbluetoothsdk.ble.bluetooth.exception.WriteBleException;
import com.zhj.bluetooth.zhjbluetoothsdk.util.BaseCmdUtil;
import com.zhj.bluetooth.zhjbluetoothsdk.util.LogUtil;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;


/**
 * 通知提醒服务
 */

@SuppressLint("OverrideAbstract")
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class IntelligentNotificationService extends NotificationListenerService {

    // 智能提醒命令
    private String mTitle = null;
    private String mText = null;
    private byte mType;

    private boolean isSending = false;
    public Deque<SendMessage> sendMessages = new ArrayDeque<>();
    public static final int NOTICE_ID = 100;
    private List<String> listName = new ArrayList<>();
    private boolean isExist;

    private static class SingletonHolder {
        private static final IntelligentNotificationService INSTANCE = new IntelligentNotificationService();
    }

    public static IntelligentNotificationService getDefault() {
        return SingletonHolder.INSTANCE;
    }

    private Notification notification;
    @Override
    public void onCreate() {
        super.onCreate();
        PendingIntent contentIntent ;
        if(Build.VERSION.SDK_INT >= 31){
            contentIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, MainActivity.class), PendingIntent.FLAG_IMMUTABLE);
        }else{
            contentIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);
        }
        Notification.Builder builder = new Notification.Builder(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ONE_ID = "com.hearth.notification";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ONE_ID,
                    "otifition_one", IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
            builder.setChannelId(CHANNEL_ONE_ID);
        }
        if (MyAppcation.getInstance() !=  null){
            builder.setContentTitle(MyAppcation.getInstance().getResources().getString(R.string.app_notice_title));
            builder.setContentText(MyAppcation.getInstance().getResources().getString(R.string.app_notice_tips));
        }else{
            builder.setContentTitle("\"Keep Health\" is running");
            builder.setContentText("Click to learn more or stop app");
        }
        builder.setContentIntent(contentIntent);
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_launcher);

        notification = builder.build();
        startForeground(NOTICE_ID, notification);
        Log.d("FF332","启动提醒通知服务...");


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NOTICE_ID, notification);
        stopForeground(true);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Notification notification = sbn.getNotification();
        if (notification == null){
            return;
        }
        String pkgName = sbn.getPackageName();
        String tickerText = (String) notification.tickerText;
        LogUtil.d("pkgName:"+pkgName+"   tickerText:"+tickerText + "  title:");
//        if ("com.tencent.mm".equals(pkgName)) { // 微信
//            //如果微信正在通话中，把微信通话界面退到后台，微信会每隔5S发一次信息到通知栏，导致APP误以为是信息提升，发送的字符串 语音通话中,
//            // 轻击以继续    0:05，此时过滤
//            // 测试发现，系统语言改成繁体时  語音通話中，輕觸以繼續    0:03
//            // 测试发现，系统语言改成英文时  Tap to continue as voice call in progress    0:03
//            if((deviceModel == null || deviceModel.isWechat())&& appNotice.wechat ){
//                if (!TextUtils.isEmpty(tickerText)) {
//                    boolean isTalk = tickerText.startsWith("语音通话中") || tickerText.startsWith("語音通話中") || tickerText.startsWith("Tap to continue as voice call in progress");
//                    //因为微信支持很多种语言，因此大概率不能用字符串去判断，
//                    //也因为国外微信用户并不是很多，故中文繁体，和英文用字符串去判断，其他语言根据字符
//                    if (isTalk) {
//                        LogUtil.d("微信正在语音");
//                        return;
//                    }
//                }
//                sendText(notification, (byte) BleContant.MSG_TYPE_WX,"weixin");
//            }
//        } else if (("com.tencent.mobileqq".equals(pkgName) || "com.tencent.qqlite".equals(pkgName) || "com.tencent.mobileqqi".equals(pkgName))) { // QQ
//            Log.d("FF332","appNotice.qq:" + appNotice.qq + "  deviceModel.isQq():" + deviceModel.isQq());
//            if(appNotice.qq && (deviceModel == null || deviceModel.isQq())){
//                sendText(notification, (byte) BleContant.MSG_TYPE_QQ,"qq");
//            }
//        } else if ("com.facebook.katana".equals(pkgName)) { // Facebook
//            if(appNotice.facebook && (deviceModel == null || deviceModel.isFacebook())){
//                sendText(notification, (byte) BleContant.MSG_TYPE_FACEBOOK,"facebook");
//            }
//        } else if ("com.twitter.android".equals(pkgName)) { // Twitter
//            if(appNotice.twitter && (deviceModel == null || deviceModel.isTwitter())){
//                sendText(notification, (byte) BleContant.MSG_TYPE_TWITTER,"twitter");
//            }
//        } else if ("com.whatsapp".equals(pkgName)) { // Whatsapp
//            if(appNotice.whatsApp && (deviceModel == null || deviceModel.isWhatsapp())){
//                sendText(notification, (byte) MSG_TYPE_WHATSAPP,"whatsApp");
//            }
//        } else if ("com.linkedin.android".equals(pkgName) || "com.linkedin.android.injobs".equals(pkgName)) { // linkedin
//            if(appNotice.linked && (deviceModel == null || deviceModel.isLinkedin())){
//                sendText(notification, (byte) MSG_TYPE_LINKEDIN,"linked");
//            }
//        } else if ("com.instagram.android".equals(pkgName)) { // instagram
//            if(appNotice.instagram && (deviceModel == null || deviceModel.isInstagram())){
//                sendText(notification, (byte) BleContant.MSG_TYPE_INSTAGRAM,"instagram");
//            }
//        } else if (("jp.naver.line.android".equals(pkgName) || pkgName.contains("line.android"))) {  //line
//            if(appNotice.line && (deviceModel == null || deviceModel.isLine())){
//                sendText(notification, (byte) BleContant.MES_TYPE_LINE,"line");
//            }
//        } else if ("com.vkontakte.android".equals(pkgName)) { //vk
//            if(appNotice.vk && (deviceModel == null || deviceModel.isVk())){
//                sendText(notification, (byte) BleContant.MES_TYPE_VK,"vkontakte");
//            }
//        } else if (("com.skype.raider".equals(pkgName) || "com.skype.rover".equals(pkgName))) { //skype
//            if(appNotice.skype && (deviceModel == null || deviceModel.isSkype())){
//                sendText(notification, (byte) BleContant.MSG_TYPE_SKEY,"skype");
//            }
//        } else if("com.facebook.orca".equals(pkgName)){
//            if(appNotice.messager && (deviceModel == null || deviceModel.isMessenger())){
//                sendText(notification, (byte) BleContant.MSG_TYPE_MESSENGER,"messager");
//            }
//        } else if("com.viber.voip".equals(pkgName)){
//            if(appNotice.viber && (deviceModel == null || deviceModel.isMessenger())){
//                sendText(notification, (byte) BleContant.MES_TYPE_VIBER,"messager");
//            }
//        } else if("org.telegram.messenger".equals(pkgName)){
//            if(appNotice.telegram && (deviceModel == null || deviceModel.isTelegram())){
//                sendText(notification, (byte) MES_TYPE_TELEGRAM,"messager");
//            }
//        } else if(("com.google.android.gm".equals(pkgName)||
//                "com.tencent.androidqqmail".equals(pkgName)||
//                "com.netease.mail".equals(pkgName)||
//                "com.netease.mobimail".equals(pkgName)||
//                "com.asiainfo.android".equals(pkgName) ||
//                "com.yahoo.mobile.client.android.mail".equals(pkgName) ||
//                "com.microsoft.office.outlook".equals(pkgName)
//                //谷歌商店提醒
////                || "com.android.vending".equals(pkgName)
//        )){
//            if(appNotice.email && (deviceModel == null || deviceModel.isEmail())){
//                sendText(notification, (byte) BleContant.MSG_TYPE_EMAIL,"emial");
//            }
//        } else if("com.kakao.talk".equals(pkgName)){
//            if(appNotice.kakao && (deviceModel == null || deviceModel.isKakaotalk())){
//                sendText(notification, (byte) BleContant.MES_TYPE_KAKAO_TALK,"kakaotalk");
//            }
//        }else if("com.keephealth.android".equals(pkgName) || "com.runmifit.android".equals(pkgName)){

//        } else{
//            if(appNotice.otherApp && (deviceModel == null || deviceModel.isOtherApp())){
                isExist = false;
                Log.d("pkgName:","listName.size():" + listName.size());
                Log.d("pkgName:","pkgName:" + pkgName);
                Log.d("pkgName:","notification:" + notification.extras.toString());
//                sendText(notification, (byte) BleContant.MSG_TYPE_OTHER, "other");
                    if(listName.size() == 0) {
                        //首次添加第一个下载的应用
                        if(pkgName != null) {
                            listName.add(pkgName);
                            sendText(notification, (byte) 0xFE,"other");
                        }
                    }else {
                        for (int i = 0; i < listName.size();i++){
                            if(pkgName != null) {
                                //判断相等存在，改变变量值
                                if (listName.get(i).equals(pkgName)) {
                                    isExist = true;
                                }
                            }
                        }
                        //如果数组里面没有
                        if(!isExist){
                                listName.add(pkgName);
                                sendText(notification, (byte) 0xFE, "other");
                        }else {
                            //如果数组里面存在

                            if(pkgName == null){
                                sendText(notification, (byte) 0xFE, "other");
                            }else {
                                if(pkgName.equals("android")){
                                    listName.clear();
                                    sendText(notification, (byte) 0xFE, "other");
                                }else if(notification.extras.toString().contains(MyAppcation.getInstance().getResources().getString(R.string.confirmed))){
                                    sendText(notification, (byte)0xFE, "other");
                                    listName.clear();
                                }
                            }
                        }
                    }

//            }
//        }


    }


    @SuppressLint("NewApi")
    private void sendText(Notification notification, byte type,String title2) {
        //通知内容
        String title = null, text;
        try {
            text = TextUtils.isEmpty(notification.tickerText) ? null : notification.tickerText.toString();
            if (!TextUtils.isEmpty(text)) {
                assert text != null;
                if (text.contains(":")) {
                    int index = text.indexOf(":");
                    title = text.substring(0,index);
                    text = text.substring(index+1);
                    if (title.contains("]")) {
                        title = title.substring(title.indexOf("]") + 1);
                    }
                }
            }
        } catch (Exception e) {
            title = null;
            text = null;
        }
//        if (type==MSG_TYPE_WHATSAPP){
            Bundle extras=notification.extras;
            if (extras!=null) {
                for (String key : extras.keySet()) {
                    try {
                        LogUtil.d("Bundle Content"+"Key=" + key+","+extras.get(key));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                text = notification.extras.getString("android.text");
                title=notification.extras.getString("android.title");
                if (text == null ||text.contains("条新信息")){
                    return;
                }
                if (title==null){
                    return;
                }
                if (title.equals("WhatsApp")){
                    return;
                }
            }
//        }else if(type==MSG_TYPE_LINKEDIN){
//            if(text == null){
//                text = notification.extras.getString("android.text");
//            }
//        }
//        if(text == null  || TextUtils.isEmpty(text)){
//            assert notification.extras != null;
//            title = notification.extras.getString("android.title");
//            text = notification.extras.getString("android.text");
//        }
        if(notification.extras != null){
            String sTitle = notification.extras.getString(Notification.EXTRA_TITLE);
            Object sText = notification.extras.getCharSequence(Notification.EXTRA_TEXT);
            if(sText instanceof SpannableString){
                text = sText.toString();
            }else{
                text = (String) sText;
            }
            if(!TextUtils.isEmpty(sTitle)){
                title = sTitle;
            }
        }
        LogUtil.d("发送的提醒的信息,mTitle:" + title + ",mText:" + text + ",mType:" + type);
//        if (TextUtils.isEmpty(text)) {
//            return;
//        }
        if(mType == type && mTitle.equals(title) && mText.equals(text)){
            return;
        }
        mTitle = title;
        mText = text;
        mType = type;
        if (mTitle==null){
            mTitle="";
        }
//        if (mTitle.equalsIgnoreCase("QQ")) {
//            LogUtil.d("QQ内部消息，，，，不发送");
//            return;
//        }
//        if(!BluetoothLe.getDefault().getConnected()){
//            return;
//        }
//        if (!TextUtils.isEmpty(mText)) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setmType(mType);
            sendMessage.setmTitle(mTitle);
            sendMessage.setmText(mText);
            sendMessages.add(sendMessage);
            postMessage();
//        }
    }

//    @Subscribe(threadMode= ThreadMode.MAIN)
//    public void handleMessage(BaseMessage message){
//        switch (message.getType()){
//            case EVENT_TYPE_SYN_DATA_COMPLETE:
//                if(!sendMessages.isEmpty()){
//                    postMessage();
//                }
//                break;
//        }
//    }
    SendMessage sendMessage;
    public void addMessage(int type,String mTitle,String mText){
        LogUtil.d("来电了");
        sendMessage = new SendMessage();
        sendMessage.setmType(type);
        sendMessage.setmTitle(mTitle);
        sendMessage.setmText(mText);
        sendMessages.addFirst(sendMessage);
        if(sendMessages.size() == 1){
            postMessage();
        }
    }

    private void postMessage(){
//        Log.d("FF4534","发送消息...isRunningDialCenter:" + isRunningDialCenter + " isSending:" + isSending + " AppApplication.getInstance().isSysndata()" + AppApplication.getInstance().isSysndata());
//        if(!isSending && !AppApplication.getInstance().isSysndata() && !isRunningDialCenter){
            SendMessage sendMessage = sendMessages.poll();
            if(sendMessage != null){
                LogUtil.d(sendMessage.getmTitle()+","+sendMessage.getmText());
                setSmsEvt(sendMessage.getmType(),sendMessage.getmTitle(),sendMessage.getmText());
                handler.postDelayed(timeRun,0);
            }
//        }
    }

    int index = 0;
    Runnable timeRun = new Runnable() {
        @Override
        public void run() {
            index ++;
            if(index <= 30){
                handler.postDelayed(timeRun,1000);
            }else{
                index = 0;
                handler.removeCallbacks(timeRun);
                isSending = false;
                if(!sendMessages.isEmpty()){
                    SendMessage sendMessage = sendMessages.poll();
                    setSmsEvt(sendMessage.getmType(),sendMessage.getmTitle(),sendMessage.getmText());
                    handler.postDelayed(timeRun,0);
                }
            }
        }
    };
    private Handler handler = new Handler();

    private final String TAG = "NOTICE";
    private void setSmsEvt(int msgTypeMsg, String mTitle, String mText) {
        isSending = true;
        BluetoothLe.getDefault().destroy(TAG);
        BluetoothLe.getDefault().setOnWriteCharacteristicListener(TAG, new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult characteristic) {

            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
                Log.d("FF4536","mTitle:" + mTitle + "  mText:" + mText);
                byte[] to = new byte[20];
                BaseCmdUtil.copy(bluetoothGattCharacteristic.getValue(), to);
                if((to[0]&255) == 0x8A && (to[3]&255) == 0x00){
                    List<byte[]> datas = CmdHelper.setMessage2(1,mTitle);
                    for (int i=0;i<datas.size();i++){
                        BluetoothLe.getDefault().writeDataToCharacteristic(datas.get(i));
                    }
                    Log.d("FF4534","11111:" +  + sendMessages.size());
                }else if((to[0]&255) == 0x8A && (to[3]&255) == 0x01){
                    List<byte[]> datas2 = CmdHelper.setMessage2(2,mText);
                    for (int i=0;i<datas2.size();i++){
                        BluetoothLe.getDefault().writeDataToCharacteristic(datas2.get(i));
                    }
                    Log.d("FF4534","22222:" + sendMessages.size());
                }else if((to[0]&255) == 0x8A && (to[3]&255) == 0x02){
                    BluetoothLe.getDefault().writeDataToCharacteristic(CmdHelper.END_MESSAGE);
                    Log.d("FF4534","33333:" + sendMessages.size());
                }else if((to[0]&255) == 0x8A && (to[3]&255) == 0x03){
                    BluetoothLe.getDefault().destroy(TAG);
                    isSending = false;
                    index = 0;
                    handler.removeCallbacks(timeRun);
                    if(!sendMessages.isEmpty()){
                        SendMessage sendMessage = sendMessages.poll();
                        setSmsEvt(sendMessage.getmType(),sendMessage.getmTitle(),sendMessage.getmText());
                        handler.postDelayed(timeRun,0);
                        Log.d("FF4534","44444");
                    }
                }
            }

            @Override
            public void onFailed(WriteBleException e) {
                index = 0;
                handler.removeCallbacks(timeRun);
                BluetoothLe.getDefault().destroy(TAG);
                isSending = false;
                if(!sendMessages.isEmpty()){
                    SendMessage sendMessage = sendMessages.poll();
                    setSmsEvt(sendMessage.getmType(),sendMessage.getmTitle(),sendMessage.getmText());
                    handler.postDelayed(timeRun,0);
                }
                LogUtil.d("WriteBleException:"+e.toString());
            }
        });
        BluetoothLe.getDefault().writeDataToCharacteristic(CmdHelper.setMessageType(msgTypeMsg));
//        BluetoothLe.getDefault().writeDataToCharacteristic(TAG, CmdHelper.setMessageType(msgTypeMsg), new OnLeWriteCharacteristicListener() {
//            @Override
//            public void onSuccess(BluetoothGattCharacteristic characteristic) {
//                byte[] to = new byte[20];
//                BaseCmdUtil.copy(characteristic.getValue(), to);
//                if((to[0]&255) == 0x8A && (to[3]&255) == 0x00){
//                    List<byte[]> datas = CmdHelper.setMessage2(1,mTitle);
//                    for (int i=0;i<datas.size();i++){
//                        if(i == datas.size() - 1){
//                            BluetoothLe.getDefault().writeDataToCharacteristic(TAG, datas.get(i), new OnLeWriteCharacteristicListener() {
//                                @Override
//                                public void onSuccess(BluetoothGattCharacteristic characteristic) {
//                                    byte[] to = new byte[20];
//                                    BaseCmdUtil.copy(characteristic.getValue(), to);
//                                    if((to[0]&255) == 0x8A && (to[3]&255) == 0x01){
//                                        List<byte[]> datas2 = CmdHelper.setMessage2(2,mText);
//                                        for (int i=0;i<datas2.size();i++){
//                                            if(i == datas2.size() - 1){
//                                                BluetoothLe.getDefault().writeDataToCharacteristic(TAG, datas2.get(i), new OnLeWriteCharacteristicListener() {
//                                                    @Override
//                                                    public void onSuccess(BluetoothGattCharacteristic characteristic) {
//                                                        byte[] to = new byte[20];
//                                                        BaseCmdUtil.copy(characteristic.getValue(), to);
//                                                        if((to[0]&255) == 0x8A && (to[3]&255) == 0x02){
//                                                            BluetoothLe.getDefault().writeDataToCharacteristic(TAG,CmdHelper.END_MESSAGE, new OnLeWriteCharacteristicListener() {
//                                                                @Override
//                                                                public void onSuccess(BluetoothGattCharacteristic characteristic) {
//                                                                    byte[] to = new byte[20];
//                                                                    BaseCmdUtil.copy(characteristic.getValue(), to);
//                                                                    if((to[0]&255) == 0x8A && (to[3]&255) == 0x03){
//                                                                        BluetoothLe.getDefault().destroy(TAG);
//                                                                        isSending = false;
//                                                                        index = 0;
//                                                                        handler.removeCallbacks(timeRun);
//                                                                        if(!sendMessages.isEmpty()){
//                                                                            SendMessage sendMessage = sendMessages.poll();
//                                                                            setSmsEvt(sendMessage.getmType(),sendMessage.getmTitle(),sendMessage.getmText());
//                                                                            handler.postDelayed(timeRun,0);
//                                                                        }
//                                                                    }
//                                                                }
//
//                                                                @Override
//                                                                public void onFailed(WriteBleException e) {
//
//                                                                }
//                                                            });
//                                                        }
//                                                    }
//
//                                                    @Override
//                                                    public void onFailed(WriteBleException e) {
//
//                                                    }
//                                                });
//                                            }else{
//                                                BluetoothLe.getDefault().writeDataToCharacteristic(datas2.get(i));
//                                            }
//
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onFailed(WriteBleException e) {
//
//                                }
//                            });
//                        }else{
//                            BluetoothLe.getDefault().writeDataToCharacteristic(datas.get(i));
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailed(WriteBleException e) {
//                LogUtil.d("WriteBleException:"+e.toString());
//            }
//        });
    }
}
