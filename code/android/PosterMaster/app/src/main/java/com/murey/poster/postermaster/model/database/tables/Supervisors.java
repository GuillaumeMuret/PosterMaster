package com.murey.poster.postermaster.model.database.tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.support.annotation.NonNull;

import com.murey.poster.postermaster.communication.message.data.Supervisor;
import com.murey.poster.postermaster.model.database.AppDatabase;
import com.murey.poster.postermaster.model.database.DatabaseVocabulary;

import java.util.ArrayList;
import java.util.List;

@Entity(
        tableName = DatabaseVocabulary.TABLE_NAME_SUPERVISORS,
        primaryKeys =
                {
                        DatabaseVocabulary.COLUMN_NAME_PROJECT_ID,
                        DatabaseVocabulary.COLUMN_NAME_SUPERVISOR_ID
                }
)
public class Supervisors {

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_PROJECT_ID)
    private int idProject;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_SUPERVISOR_ID)
    private int idSupervisor;

    public Supervisors(int idProject,int idSupervisor) {
        this.idProject = idProject;
        this.idSupervisor = idSupervisor;
    }

    public int getIdProject() {
        return idProject;
    }

    public void setIdProject(int idProject) {
        this.idProject = idProject;
    }

    public int getIdSupervisor() {
        return idSupervisor;
    }

    public void setIdSupervisor(int idSupervisor) {
        this.idSupervisor = idSupervisor;
    }

    public static void updateMyProjectReceivedFromEseo(Context context, int idJury, int idMember){
        AppDatabase.getInstance(context).supervisorsDao().insertAllOrReplace(new Supervisors(idJury,idMember));
    }

}
