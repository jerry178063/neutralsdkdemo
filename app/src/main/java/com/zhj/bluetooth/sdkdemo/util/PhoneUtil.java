package com.zhj.bluetooth.sdkdemo.util;

import static android.content.Context.TELEPHONY_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;

import androidx.core.app.ActivityCompat;

import com.zhj.bluetooth.sdkdemo.MyAppcation;

import java.lang.reflect.Method;

/**
 */
public class PhoneUtil {

    public static String TAG = PhoneUtil.class.getSimpleName();

//    public static ITelephony getITelephony(TelephonyManager telephony) throws Exception {
//        @SuppressLint("SoonBlockedPrivateApi")
//        Method getITelephonyMethod = telephony.getClass().getDeclaredMethod("getITelephony");
//        getITelephonyMethod.setAccessible(true);//私有化函数也能使用
//        return (ITelephony)getITelephonyMethod.invoke(telephony);
//    }
    /**
     * 挂断电话
     *
     * @param context
     */
    public static void endCall(Context context) {
        TelecomManager tm = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tm = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
        }
        if (tm != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                tm.endCall();
            }else{
                //判断是否已经赋予权限

            }
        }else{

        }
    }

    private static Object getTelephonyObject(Context context) {
        Object telephonyObject = null;
        try {
            // 初始化iTelephony
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            // Will be used to invoke hidden methods with reflection
            // Get the current object implementing ITelephony interface
            Class telManager = telephonyManager.getClass();
            @SuppressLint("SoonBlockedPrivateApi") Method getITelephony = telManager.getDeclaredMethod("getITelephony");
            getITelephony.setAccessible(true);
            telephonyObject = getITelephony.invoke(telephonyManager);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return telephonyObject;
    }


    /**
     * 通过反射调用的方法，接听电话，该方法只在android 2.3之前的系统上有效。
     * Neither user 11164 nor current process has android.permission.MODIFY_PHONE_STATE.
     * @param context
     */
    public static void answerRingingCallWithReflect(Context context) {
        try {
            Object telephonyObject = getTelephonyObject(context);
            if (null != telephonyObject) {
                Class telephonyClass = telephonyObject.getClass();
                Method endCallMethod = telephonyClass.getMethod("answerRingingCall");
                endCallMethod.setAccessible(true);
                endCallMethod.invoke(telephonyObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 伪造一个有线耳机插入，并按接听键的广播，让系统开始接听电话。
     * 发送Intent.ACTION_HEADSET_PLUG广播时奔溃
     * java.lang.SecurityException: Permission Denial: not allowed to send broadcast android.intent.action.HEADSET_PLUG from pid=8281, uid=11164
     * @param context
     */
    public static void answerRingingCallWithBroadcast(Context context) {
        AudioManager localAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        //判断是否插上了耳机
        boolean isWiredHeadsetOn = localAudioManager.isWiredHeadsetOn();
        if (!isWiredHeadsetOn) {
//            Intent headsetPluggedIntent = new Intent(Intent.ACTION_HEADSET_PLUG);
//            headsetPluggedIntent.putExtra("state", 1);
//            headsetPluggedIntent.putExtra("microphone", 0);
//            headsetPluggedIntent.putExtra("name", "");
//            context.sendBroadcast(headsetPluggedIntent);
//
//            Intent meidaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
//            KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
//            meidaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
//            context.sendOrderedBroadcast(meidaButtonIntent, null);
//
//            Intent headsetUnpluggedIntent = new Intent(Intent.ACTION_HEADSET_PLUG);
//            headsetUnpluggedIntent.putExtra("state", 0);
//            headsetUnpluggedIntent.putExtra("microphone", 0);
//            headsetUnpluggedIntent.putExtra("name", "");
//            context.sendBroadcast(headsetUnpluggedIntent);

            // 2.3以上执行以下代码实现自动接听
            Intent mintent = new Intent(Intent.ACTION_MEDIA_BUTTON);

            //按下音量
            KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK);
            mintent.putExtra("android.intent.extra.KEY_EVENT", keyEvent);
            // 通话权限 允许程序拨打电话， 替换系统的拨号器界面
            MyAppcation.getInstance().sendOrderedBroadcast(mintent,"android.permission.CALL_PRIVILEGED");

            mintent = new Intent(Intent.ACTION_MEDIA_BUTTON);
            keyEvent = new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_HEADSETHOOK);
            mintent.putExtra("android.intent.extra.KEY_EVENT", keyEvent);
            MyAppcation.getInstance().sendOrderedBroadcast(mintent,"android.permission.CALL_PRIVILEGED");
        } else {
//            DebugLog.d("伪造一个有线耳机插入-----2=" + isWiredHeadsetOn);
            Intent meidaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
            KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
            meidaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
            context.sendOrderedBroadcast(meidaButtonIntent, null);
        }
    }
    public static void answerRingingCallWithBroadcast(Context context, TelephonyManager telmanager){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        //判断是否插上了耳机
        if (! audioManager.isWiredHeadsetOn()) {
            //4.1以上系统限制了部分权限， 使用三星4.1版本测试提示警告：Permission Denial: not allowed to send broadcast android.intent.action.HEADSET_PLUG from pid=1324, uid=10017
//这里需要注意一点，发送广播时加了权限“android.permission.CALL_PRIVLEGED”，则接受该广播时也需要增加该权限。但是4.1以上版本貌似这个权限只能系统应用才可以得到。测试的时候，自定义的接收器无法接受到此广播，后来去掉了这个权限，设为NULL便可以监听到了。
            Log.d("FF4533","没有耳机");
            if(Build.VERSION.SDK_INT >=15 ){
                Log.d("FF4533",">=15");
                Intent meidaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
                KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
                meidaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT,keyEvent);
                context.sendOrderedBroadcast(meidaButtonIntent, "android.permission.CALL_PRIVILEGED");
            }else{
// 以下适用于Android2.3及2.3以上的版本上 ，但测试发现4.1系统上不管用。
                Log.d("FF4533","<15");
                Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);
                localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                localIntent1.putExtra("state", 1);
                localIntent1.putExtra("microphone", 1);
                localIntent1.putExtra("name", "Headset");
                context.sendOrderedBroadcast(localIntent1,  "android.permission.CALL_PRIVILEGED");

                Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);
                KeyEvent localKeyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN,   KeyEvent.KEYCODE_HEADSETHOOK);
                localIntent2.putExtra(Intent.EXTRA_KEY_EVENT,   localKeyEvent1);
                context.sendOrderedBroadcast(localIntent2,  "android.permission.CALL_PRIVILEGED");

                Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
                KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP,  KeyEvent.KEYCODE_HEADSETHOOK);
                localIntent3.putExtra(Intent.EXTRA_KEY_EVENT,  localKeyEvent2);
                context.sendOrderedBroadcast(localIntent3,   "android.permission.CALL_PRIVILEGED");

                Intent localIntent4 = new Intent(Intent.ACTION_HEADSET_PLUG);
                localIntent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                localIntent4.putExtra("state", 0);
                localIntent4.putExtra("microphone", 1);
                localIntent4.putExtra("name", "Headset");
                context.sendOrderedBroadcast(localIntent4, "android.permission.CALL_PRIVILEGED");
            }

        } else {
            Log.d("FF4533","插上了耳机");
            Intent meidaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
            KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
            meidaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT,keyEvent);
            context.sendOrderedBroadcast(meidaButtonIntent, null);
        }
    }

    /**
     * 接听电话
     *
     * @param context
     */
    public static void answerRingingCall(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {  //2.3或2.3以上系统
            answerRingingCallWithBroadcast(context,telephonyManager);
//            answerRingingCallWithBroadcast(context);
            Log.d("FF4533","接听电话> 2.3");
        } else {
            Log.d("FF4533","接听电话< 2.3");
            answerRingingCallWithReflect(context);
        }
    }


    /**
     * 拨电话
     *
     * @param context
     * @param phoneNumber
     */
    public static void dialPhone(Context context, String phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            try {
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                context.startActivity(callIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}