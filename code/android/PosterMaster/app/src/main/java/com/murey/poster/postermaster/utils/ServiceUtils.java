package com.murey.poster.postermaster.utils;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.murey.poster.postermaster.service.AppService;

public class ServiceUtils {

    public static boolean isServiceRunning(Context context, Class serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if(manager!=null){
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void startAppService(Context context){
        if(!isServiceRunning(context, AppService.class)){
            LogUtils.d(LogUtils.DEBUG_TAG,"START APP SERVICE");
            Intent intent = new Intent(context.getApplicationContext(), AppService.class);
            context.getApplicationContext().startService(intent);
        }else{
            LogUtils.d(LogUtils.DEBUG_TAG,"NO START APP SERVICE SINCE ALREADY STARTED");
        }
    }
}
