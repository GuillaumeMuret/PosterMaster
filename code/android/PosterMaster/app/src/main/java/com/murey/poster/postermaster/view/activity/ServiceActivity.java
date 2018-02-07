package com.murey.poster.postermaster.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.murey.poster.postermaster.utils.LogUtils;
import com.murey.poster.postermaster.utils.ServiceUtils;

public class ServiceActivity extends AbstractActivity {

    private static final int REQUEST_CODE_ACTIVITY_SPLASH               =300;
    private static final int REQUEST_CODE_ACTIVITY_LOGIN                =301;
    private static final int REQUEST_CODE_ACTIVITY_PROJECT_LIST         =302;
    private static final int REQUEST_CODE_ACTIVITY_MY_ACCOUNT           =303;
    private static final int REQUEST_CODE_ACTIVITY_PROJECT_DETAILS      =304;
    private static final int REQUEST_CODE_ACTIVITY_JURIES_LIST          =305;
    private static final int REQUEST_CODE_ACTIVITY_OPEN_HOUSE           =306;

    public static final int DISPLAY_ACTIVITY_LOGIN                      = 1;
    public static final int DISPLAY_ACTIVITY_PROJECT_LIST               = 2;
    public static final int DISPLAY_ACTIVITY_MY_ACCOUNT                 = 3;
    public static final int DISPLAY_ACTIVITY_PROJECT_DETAILS            = 4;
    public static final int DISPLAY_ACTIVITY_JURIES_LIST                = 5;
    public static final int DISPLAY_ACTIVITY_OPEN_HOUSE                 = 6;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceUtils.startAppService(this);
        if(savedInstanceState==null) {
            Intent i = new Intent(this, SplashActivity.class);
            startActivityForResult(i, REQUEST_CODE_ACTIVITY_SPLASH);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtils.d(LogUtils.DEBUG_TAG,"ResultCode ==> "+requestCode);
        switch(requestCode){
            case REQUEST_CODE_ACTIVITY_SPLASH:
                manageEndActivity(requestCode, resultCode, data);
                break;

            case REQUEST_CODE_ACTIVITY_LOGIN:
                manageEndActivity(requestCode, resultCode, data);
                break;

            case REQUEST_CODE_ACTIVITY_PROJECT_LIST:
                manageEndActivity(requestCode, resultCode, data);
                break;

            case REQUEST_CODE_ACTIVITY_MY_ACCOUNT:
                manageEndActivity(requestCode, resultCode, data);
                break;

            case REQUEST_CODE_ACTIVITY_PROJECT_DETAILS:
                manageEndActivity(requestCode, resultCode, data);
                break;

            case REQUEST_CODE_ACTIVITY_JURIES_LIST:
                manageEndActivity(requestCode, resultCode, data);
                break;

            case REQUEST_CODE_ACTIVITY_OPEN_HOUSE:
                manageEndActivity(requestCode, resultCode, data);
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void manageEndActivity(int requestCode, int resultCode, Intent data){
        if(resultCode==RESULT_OK){
            LogUtils.d(LogUtils.DEBUG_TAG,"resultCode == RESULT_OK");
            finish();
        }else{
            LogUtils.d(LogUtils.DEBUG_TAG,"resultCode == " + resultCode);
            displayScreen(resultCode, resultCode, data);
        }
    }

    private void displayScreen(int requestCode, int resultCode, Intent data){
        switch(resultCode){
            case DISPLAY_ACTIVITY_LOGIN:
                startActivityWithParams(LoginActivity.class,REQUEST_CODE_ACTIVITY_LOGIN,
                        requestCode, resultCode, data);
                break;

            case DISPLAY_ACTIVITY_PROJECT_LIST:
                startActivityWithParams(ProjectListActivity.class, REQUEST_CODE_ACTIVITY_PROJECT_LIST,
                        requestCode, resultCode, data);
                break;

            case DISPLAY_ACTIVITY_MY_ACCOUNT:
                startActivityWithParams(MyAccountActivity.class, REQUEST_CODE_ACTIVITY_MY_ACCOUNT,
                        requestCode, resultCode, data);
                break;

            case DISPLAY_ACTIVITY_PROJECT_DETAILS:
                startActivityWithParams(ProjectDetailsActivity.class, REQUEST_CODE_ACTIVITY_PROJECT_DETAILS,
                        requestCode, resultCode, data);
                break;

            case DISPLAY_ACTIVITY_JURIES_LIST:
                startActivityWithParams(JuriesListActivity.class, REQUEST_CODE_ACTIVITY_JURIES_LIST,
                        requestCode, resultCode, data);
                break;

            case DISPLAY_ACTIVITY_OPEN_HOUSE:
                startActivityWithParams(OpenHouseActivity.class, REQUEST_CODE_ACTIVITY_OPEN_HOUSE,
                        requestCode, resultCode, data);
                break;

            default:
                finish();
                break;
        }
    }

    private void startActivityWithParams(Class myClass, int toRequestCode, int fromRequestCode, int resultCode, Intent data){
        Bundle extras = data.getExtras();
        Intent intent = new Intent(this, myClass);
        if (extras != null) {
            for (String key : extras.keySet()) {
                Parcelable value = (Parcelable) extras.get(key);
                intent.putExtra(key, value);
                LogUtils.d(LogUtils.DEBUG_TAG,"SERVICE EXTRA KEY ==>" + key + " VALUE ==> " + extras.get(key));
            }
        }
        setResult(resultCode, intent);
        LogUtils.d(LogUtils.DEBUG_TAG,"Start activity = " + myClass);
        startActivityForResult(intent,toRequestCode);
    }
}
