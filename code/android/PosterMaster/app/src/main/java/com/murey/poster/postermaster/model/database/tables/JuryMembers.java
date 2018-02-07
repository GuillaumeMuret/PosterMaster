package com.murey.poster.postermaster.model.database.tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;

import com.murey.poster.postermaster.communication.message.data.Member;
import com.murey.poster.postermaster.model.database.AppDatabase;
import com.murey.poster.postermaster.model.database.DatabaseVocabulary;
import com.murey.poster.postermaster.utils.LogUtils;

import java.net.ContentHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity(
        tableName = DatabaseVocabulary.TABLE_NAME_JURY_MEMBERS,
        primaryKeys =
                {
                        DatabaseVocabulary.COLUMN_NAME_JURY_ID,
                        DatabaseVocabulary.COLUMN_NAME_MEMBER_ID
                }
)
public class JuryMembers {

    public static final Integer JURY_ID_OPEN_HOUSE = 666666;

    public static final Integer MY_JURY_ID = 1;
    public static final Integer NOT_MY_JURY_ID = 0;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_JURY_ID)
    private int idJury;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_MEMBER_ID)
    private int idMember;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_SURNAME)
    private String surname;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_FORENAME)
    private String forename;

    public JuryMembers(int idJury, int idMember, String surname, String forename) {
        this.idJury = idJury;
        this.idMember = idMember;
        this.surname = surname;
        this.forename = forename;
    }

    public int getIdJury() {
        return idJury;
    }

    public void setIdJury(int idJury) {
        this.idJury = idJury;
    }

    public int getIdMember() {
        return idMember;
    }

    public void setIdMember(int idMember) {
        this.idMember = idMember;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public static void updateJuryMembersReceivedFromEseo(Context context, int idJury){
        AppDatabase.getInstance(context).juryMembersDao().insertAllOrReplace(new JuryMembers(idJury,JuryMembers.MY_JURY_ID,"",""));
    }

    public static void updateRangeJuryMembersReceivedFromEseo(Context context,int idJury, Member... members){
        for(int i=0;i<members.length;i++){
            int aleatoire = new Random().nextInt(9999999);
            aleatoire += 2;
            LogUtils.d(LogUtils.DEBUG_TAG,"Aleatoire = "+aleatoire);
            AppDatabase.getInstance(context).juryMembersDao().insertAllOrReplace(new JuryMembers(idJury,aleatoire,members[i].getForename(),members[i].getSurname()));
        }
    }
}
