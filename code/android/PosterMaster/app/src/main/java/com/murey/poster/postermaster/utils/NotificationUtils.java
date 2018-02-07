package com.murey.poster.postermaster.utils;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.murey.poster.postermaster.R;
import com.murey.poster.postermaster.view.activity.ServiceActivity;

public class NotificationUtils {

    private static NotificationUtils notificationHandler;
    private static NotificationManager notificationManager;

    public static NotificationUtils getInstance(Context context){
        if(notificationHandler == null){
            notificationHandler = new NotificationUtils();
            notificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationHandler;
    }

    public void createNotification(Context context, String notificationTitle, String notificationText){

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_app_icon)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, notification);
    }
}
