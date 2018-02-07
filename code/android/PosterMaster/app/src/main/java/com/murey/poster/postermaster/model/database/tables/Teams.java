package com.murey.poster.postermaster.model.database.tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;

import com.murey.poster.postermaster.communication.message.data.Student;
import com.murey.poster.postermaster.model.database.AppDatabase;
import com.murey.poster.postermaster.model.database.DatabaseVocabulary;

import java.util.ArrayList;
import java.util.List;

@Entity(
        tableName = DatabaseVocabulary.TABLE_NAME_TEAMS,
        primaryKeys =
                {
                        DatabaseVocabulary.COLUMN_NAME_PROJECT_ID,
                        DatabaseVocabulary.COLUMN_NAME_MEMBER_ID
                }
)
public class Teams {

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_PROJECT_ID)
    private int idProject;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_MEMBER_ID)
    private int idMember;

    public Teams(int idProject, int idMember) {
        this.idProject = idProject;
        this.idMember = idMember;
    }

    public int getIdProject() {
        return idProject;
    }

    public void setIdProject(int idProject) {
        this.idProject = idProject;
    }

    public int getIdMember() {
        return idMember;
    }

    public void setIdMember(int idMember) {
        this.idMember = idMember;
    }

    public static void updateTeamProjectReceivedFromEseo(Context context, int projectId, int studentId){
        AppDatabase.getInstance(context).teamsDao().insertAllOrReplace(new Teams(projectId,studentId));
    }
}
