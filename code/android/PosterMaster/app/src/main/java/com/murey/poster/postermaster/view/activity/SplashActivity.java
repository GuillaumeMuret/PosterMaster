package com.murey.poster.postermaster.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.murey.poster.postermaster.R;
import com.murey.poster.postermaster.model.DataStore;
import com.murey.poster.postermaster.model.constant.Constant;
import com.murey.poster.postermaster.model.database.AppDatabase;
import com.murey.poster.postermaster.model.database.tables.Users;
import com.murey.poster.postermaster.utils.DialogUtils;
import com.murey.poster.postermaster.utils.FileUtils;
import com.murey.poster.postermaster.utils.PermissionUtils;

import java.util.HashMap;

public class SplashActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set splash Layout
        setContentView(R.layout.activity_splash);

        if(PermissionUtils.areAllPermissionsGranted(this)){
            onPermissionGranted();
        }
    }

    private void launchTimerSplash(){
        Users users = AppDatabase.getInstance(this).usersDao().getAppUser();
        if(users != null && users.isMandatoryDataReceived()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finishWithResult(ServiceActivity.DISPLAY_ACTIVITY_PROJECT_LIST,mapParams);
                }
            }, Constant.TIME_SPLASH_SCREEN);
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finishWithResult(ServiceActivity.DISPLAY_ACTIVITY_LOGIN,mapParams);
                }
            }, Constant.TIME_SPLASH_SCREEN);
        }
    }

    private void onPermissionGranted(){
        launchTimerSplash();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(PermissionUtils.areAllPermissionsGranted(this)){
            onPermissionGranted();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.MY_PERMISSIONS_REQUEST_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                String permission="";
                if(permissions.length>0){
                    permission = permissions[0];
                }
                boolean reAskPermission=false;
                if(grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    reAskPermission = true;
                }
                if (reAskPermission) {
                    // user rejected the permission
                    boolean showRationale = false;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        showRationale = shouldShowRequestPermissionRationale( permission );
                    }
                    if (! showRationale) {
                        // user also CHECKED "never ask again" you can either enable some fall back,
                        // disable features of your app or open another dialog explaining again the permission and directing to
                        // the app setting

                        DialogUtils.displayDialogAlertPermission(this);
                    }else {
                        // Ooooooooh...
                        PermissionUtils.areAllPermissionsGranted(this);
                    }
                } else {
                    // SUPER !!!
                    onPermissionGranted();
                }
            }
        }
    }
}
