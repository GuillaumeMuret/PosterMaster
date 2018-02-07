package com.murey.poster.postermaster.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.murey.poster.postermaster.view.activity.AbstractActivity;

public class PermissionUtils {

    // The request storage permission
    public static final int MY_PERMISSIONS_REQUEST_STORAGE = 12;

    /**
     * Process called to check storage permissions
     * @param activity : the context
     * @return if user has the permission
     */
    public static boolean isStoragePermissionEnable(FragmentActivity activity){
        // Here, thisActivity is the current context
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                ) {


            // Request the permission.
            ActivityCompat.requestPermissions(activity,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                    },
                    MY_PERMISSIONS_REQUEST_STORAGE);
            return false;
        }
        return true;
    }

    public static boolean checkBluetoothPermission(AppCompatActivity activity){
        // Here, thisActivity is the current context
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH)!= PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_ADMIN)!= PackageManager.PERMISSION_GRANTED
                ) {


            // Request the permission.
            ActivityCompat.requestPermissions(activity,
                    new String[]{
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_ADMIN,
                    },
                    MY_PERMISSIONS_REQUEST_STORAGE);
            return false;
        }
        return true;
    }

    /**
     * Process called to check storage permissions
     * @param context : the context of the context
     * @return if user has the permission
     */
    public static boolean isStoragePermissionEnable(Context context){
        // Here, thisActivity is the current context
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE)!= PackageManager.PERMISSION_GRANTED
                ) {
            return false;
        }
        return true;
    }

    public static boolean areAllPermissionsGranted(AbstractActivity abstractActivity){
        LogUtils.d(LogUtils.DEBUG_TAG,"ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED => "+(ActivityCompat.checkSelfPermission(abstractActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED));
        LogUtils.d(LogUtils.DEBUG_TAG,"ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED => "+(ActivityCompat.checkSelfPermission(abstractActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED));
        LogUtils.d(LogUtils.DEBUG_TAG,"ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED => "+(ActivityCompat.checkSelfPermission(abstractActivity, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED));
        LogUtils.d(LogUtils.DEBUG_TAG,"ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED => "+(ActivityCompat.checkSelfPermission(abstractActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED));
        LogUtils.d(LogUtils.DEBUG_TAG,"ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED => "+(ActivityCompat.checkSelfPermission(abstractActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED));
        LogUtils.d(LogUtils.DEBUG_TAG,"ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED => "+(ActivityCompat.checkSelfPermission(abstractActivity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED));
        LogUtils.d(LogUtils.DEBUG_TAG,"ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED => "+(ActivityCompat.checkSelfPermission(abstractActivity, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED));
        LogUtils.d(LogUtils.DEBUG_TAG,"ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED => "+(ActivityCompat.checkSelfPermission(abstractActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED));
        LogUtils.d(LogUtils.DEBUG_TAG,"ActivityCompat.checkSelfPermission(this, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED => "+(ActivityCompat.checkSelfPermission(abstractActivity, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED));
        LogUtils.d(LogUtils.DEBUG_TAG,"ActivityCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED => "+(ActivityCompat.checkSelfPermission(abstractActivity, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED));
        LogUtils.d(LogUtils.DEBUG_TAG,"ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED => "+(ActivityCompat.checkSelfPermission(abstractActivity, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED));
        LogUtils.d(LogUtils.DEBUG_TAG,"ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED => "+(ActivityCompat.checkSelfPermission(abstractActivity, Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED));
        LogUtils.d(LogUtils.DEBUG_TAG,"ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_SYNC_SETTINGS) != PackageManager.PERMISSION_GRANTED => "+(ActivityCompat.checkSelfPermission(abstractActivity, Manifest.permission.WRITE_SYNC_SETTINGS) != PackageManager.PERMISSION_GRANTED));

        if (
                 	ActivityCompat.checkSelfPermission(abstractActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || 	ActivityCompat.checkSelfPermission(abstractActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || 	ActivityCompat.checkSelfPermission(abstractActivity, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                || 	ActivityCompat.checkSelfPermission(abstractActivity, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED
                || 	ActivityCompat.checkSelfPermission(abstractActivity, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED

                ) {
            LogUtils.e(LogUtils.DEBUG_TAG,"REQUEST PERMISSION");
            ActivityCompat.requestPermissions(
                    abstractActivity,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.INTERNET,
                            Manifest.permission.WAKE_LOCK,
                    },
                    MY_PERMISSIONS_REQUEST_STORAGE);
            return false;
        }else{
            return true;
        }
    }
}