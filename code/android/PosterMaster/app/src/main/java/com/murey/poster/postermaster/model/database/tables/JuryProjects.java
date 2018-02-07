package com.murey.poster.postermaster.model.database.tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;

import com.murey.poster.postermaster.model.database.AppDatabase;
import com.murey.poster.postermaster.model.database.DatabaseVocabulary;

@Entity(
        tableName = DatabaseVocabulary.TABLE_NAME_JURY_PROJECTS,
        primaryKeys =
                {
                        DatabaseVocabulary.COLUMN_NAME_JURY_ID,
                        DatabaseVocabulary.COLUMN_NAME_PROJECT_ID
                }
)
public class JuryProjects {

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_JURY_ID)
    private int idJury;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_PROJECT_ID)
    private int idProject;

    public JuryProjects(int idJury, int idProject) {
        this.idJury = idJury;
        this.idProject = idProject;
    }

    public int getIdJury() {
        return idJury;
    }

    public void setIdJury(int idJury) {
        this.idJury = idJury;
    }

    public int getIdProject() {
        return idProject;
    }

    public void setIdProject(int idProject) {
        this.idProject = idProject;
    }

    public static void updateJuryProjectsFromEseo(Context context, int juryId, int projectId){
        AppDatabase.getInstance(context).juryProjectsDao().insertAllOrReplace(new JuryProjects(juryId,projectId));
    }
}
