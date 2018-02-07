package com.murey.poster.postermaster.utils;

import android.util.Log;

import com.murey.poster.postermaster.BuildConfig;

public class LogUtils {

    public static final String DEBUG_TAG = "DEBUG_TAG";

    public static void d(String tag, String message){
        if(BuildConfig.DEBUG){
            Log.d(tag,message);
        }
    }

    public static void e(String tag, String message){
        if(BuildConfig.DEBUG) {
            Log.e(tag, message);
        }
    }

    public static void e(String tag, String message, Throwable throwable){
        if(BuildConfig.DEBUG) {
            Log.e(tag, message, throwable);
        }
    }
}

