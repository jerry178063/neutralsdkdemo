package com.zhj.bluetooth.sdkdemo.sevice;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.zhj.bluetooth.sdkdemo.MyAppcation;



public class ServiceWorker extends Worker {

    public ServiceWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            MyAppcation.getInstance().startForegroundService(new Intent(MyAppcation.getInstance(), IntelligentNotificationService.class));
            MyAppcation.getInstance().startForegroundService(new Intent(MyAppcation.getInstance(), AssistService.class));
            Log.d("FF332","监听手环广播.222..");
        } else {
            MyAppcation.getInstance().startService(new Intent(MyAppcation.getInstance(), IntelligentNotificationService.class));
            MyAppcation.getInstance().startService(new Intent(MyAppcation.getInstance(), AssistService.class));
            Log.d("FF332","监听手环广播.333..");
        }
        return null;
    }
}
