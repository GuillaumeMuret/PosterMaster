package com.murey.poster.postermaster.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.murey.poster.postermaster.communication.requests.protocole.ProtocolVocabulary;
import com.murey.poster.postermaster.model.database.AppDatabase;
import com.murey.poster.postermaster.model.database.tables.Juries;
import com.murey.poster.postermaster.utils.DateUtils;
import com.murey.poster.postermaster.utils.LogUtils;
import com.murey.poster.postermaster.utils.NotificationUtils;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        LogUtils.d(LogUtils.DEBUG_TAG,"AlarmReceiver => onReceive !");
        int juryId = intent.getIntExtra(ProtocolVocabulary.BUNDLE_KEY_JURY,-1);
        LogUtils.d(LogUtils.DEBUG_TAG,"Jury id => " + juryId);
        Juries juries = AppDatabase.getInstance(context).juriesDao().getJuryById(juryId);
        NotificationUtils.getInstance(context).createNotification(
                context,
                "Rappel Jury",
                "Jury n°"+juries.getIdJury()+" le "+ DateUtils.getFriendlyFromDateString(juries.getDate(),DateUtils.FORMAT_DATE_FROM_ESEO));

    }
}
