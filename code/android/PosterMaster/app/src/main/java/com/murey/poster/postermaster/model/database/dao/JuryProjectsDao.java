package com.murey.poster.postermaster.model.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.murey.poster.postermaster.model.database.DatabaseVocabulary;
import com.murey.poster.postermaster.model.database.tables.JuryProjects;
import com.murey.poster.postermaster.model.database.tables.Teams;

import java.util.List;

@Dao
public interface JuryProjectsDao {

    @Query("SELECT * FROM "+ DatabaseVocabulary.TABLE_NAME_JURY_PROJECTS)
    List<JuryProjects> getAll();

    @Query("SELECT " + DatabaseVocabulary.COLUMN_NAME_PROJECT_ID + " FROM "+ DatabaseVocabulary.TABLE_NAME_JURY_PROJECTS)
    List<Integer> getAllProjectId();

    @Query("SELECT " + DatabaseVocabulary.COLUMN_NAME_PROJECT_ID + " FROM "+ DatabaseVocabulary.TABLE_NAME_JURY_PROJECTS +
            " where " + DatabaseVocabulary.COLUMN_NAME_JURY_ID + " LIKE  :idJury")
    List<Integer> getProjectsIdFromJuryId(int idJury);

    @Query("SELECT COUNT(*) from " + DatabaseVocabulary.TABLE_NAME_JURY_PROJECTS)
    int countJuryProjects();

    @Insert
    void insertAll(JuryProjects... juryProjects);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllOrReplace(JuryProjects... juryProjects);

    @Delete
    void delete(JuryProjects juryProjects);

    @Delete
    void deleteAll(List<JuryProjects> juryProjects);
}
