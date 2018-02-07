package com.murey.poster.postermaster.model.database.tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.icu.text.DateIntervalFormat;
import android.os.Parcel;
import android.os.Parcelable;

import com.murey.poster.postermaster.communication.message.data.Jury;
import com.murey.poster.postermaster.communication.message.data.Project;
import com.murey.poster.postermaster.model.database.AppDatabase;
import com.murey.poster.postermaster.model.database.DatabaseVocabulary;
import com.murey.poster.postermaster.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity(tableName = DatabaseVocabulary.TABLE_NAME_JURIES)
public class Juries implements Parcelable {

    public static final int MODE_ALERT_ENABLE = 1;
    public static final int MODE_ALERT_DISABLE = 0;

    public static final Parcelable.Creator<Juries> CREATOR = new Parcelable.Creator<Juries>() {

        public Juries createFromParcel(Parcel source) {
            return new Juries(source);
        }


        public Juries[] newArray(int size) {
            return new Juries[size];
        }
    };

    @PrimaryKey
    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_JURY_ID)
    private int idJury;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_DESCRIPTION)
    private String description;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_DATE)
    private String date;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_ALERT_ENABLE)
    private int modeAlert;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_ALERT_DATE)
    private String alertDate;

    public Juries(int idJury, String description, String date, int modeAlert, String alertDate) {
        this.idJury = idJury;
        this.description = description;
        this.date = date;
        this.modeAlert = modeAlert;
        this.alertDate = alertDate;
    }

    @Ignore
    public Juries(int idJury, String description, String date) {
        this.idJury = idJury;
        this.description = description;
        this.date = date;
        this.modeAlert = MODE_ALERT_DISABLE;
        this.alertDate = DateUtils.getStringFromDate(Calendar.getInstance().getTime());
    }

    public Juries(Parcel in) {
        this.idJury = in.readInt();
        this.description = in.readString();
        this.date = in.readString();
        this.modeAlert = in.readInt();
        this.alertDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.idJury);
        dest.writeString(this.description);
        dest.writeString(this.date);
        dest.writeInt(this.modeAlert);
        dest.writeString(this.alertDate);
    }

    public int getIdJury() {
        return idJury;
    }

    public void setIdJury(int idJury) {
        this.idJury = idJury;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getModeAlert() {
        return modeAlert;
    }

    public void setModeAlert(int modeAlert) {
        this.modeAlert = modeAlert;
    }

    public boolean isModeAlert() {
        return modeAlert==MODE_ALERT_ENABLE;
    }

    public void setModeAlert(boolean modeAlert) {
        this.modeAlert = modeAlert?MODE_ALERT_ENABLE:MODE_ALERT_DISABLE;
    }

    public String getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(String alertDate) {
        this.alertDate = alertDate;
    }

    public static void updateJuriesReceivedFromEseo(Context context, Jury... serverJuries){
        List<Juries> databaseJuriesList = AppDatabase.getInstance(context).juriesDao().getAll();
        ArrayList<Integer> databaseJuriesId = new ArrayList<>();

        // Update juries
        for(int j=0;j<databaseJuriesList.size();j++){
            databaseJuriesId.add(databaseJuriesList.get(j).getIdJury());
        }
        for(int i=0;i<serverJuries.length;i++){

            if(databaseJuriesId.contains(serverJuries[i].getIdJury())){
                databaseJuriesList.get(databaseJuriesId.indexOf(serverJuries[i].getIdJury()))
                        .setDate(serverJuries[i].getDate());

                AppDatabase.getInstance(context).juriesDao().update(databaseJuriesList.get(
                        databaseJuriesId.indexOf(serverJuries[i].getIdJury())));

            } else {
                Juries juries = new Juries(
                        serverJuries[i].getIdJury(),
                        "",
                        serverJuries[i].getDate()
                );
                AppDatabase.getInstance(context).juriesDao().insertAll(juries);
            }
        }
    }
}
