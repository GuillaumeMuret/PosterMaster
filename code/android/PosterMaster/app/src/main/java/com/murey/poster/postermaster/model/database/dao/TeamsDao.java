package com.murey.poster.postermaster.model.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.murey.poster.postermaster.model.database.DatabaseVocabulary;
import com.murey.poster.postermaster.model.database.tables.Projects;
import com.murey.poster.postermaster.model.database.tables.Teams;
import com.murey.poster.postermaster.model.database.tables.Users;

import java.util.List;

@Dao
public interface TeamsDao {

    @Query("SELECT * FROM "+ DatabaseVocabulary.TABLE_NAME_TEAMS)
    List<Teams> getAll();

    @Query("SELECT COUNT(*) from " + DatabaseVocabulary.TABLE_NAME_TEAMS)
    int countTeams();

    /*
    @Query("SELECT * FROM " +
            "( SELECT * FROM " + DatabaseVocabulary.TABLE_NAME_TEAMS +" , " + DatabaseVocabulary.TABLE_NAME_USERS +
            " WHERE " + DatabaseVocabulary.TABLE_NAME_TEAMS + "." + DatabaseVocabulary.COLUMN_NAME_PROJECT_ID + " =:idProject )" +
            "WHERE " + DatabaseVocabulary.TABLE_NAME_TEAMS + "." + DatabaseVocabulary.COLUMN_NAME_MEMBER_ID +
            " = " + DatabaseVocabulary.TABLE_NAME_USERS + "." + DatabaseVocabulary.COLUMN_NAME_USER_ID
    )
    */
    @Query("SELECT * FROM " + DatabaseVocabulary.TABLE_NAME_TEAMS +
            " WHERE " + DatabaseVocabulary.TABLE_NAME_TEAMS + "." +
            DatabaseVocabulary.COLUMN_NAME_PROJECT_ID + " =:idProject "
    )
    List<Teams> getProjectTeam(int idProject);

    @Query("SELECT " + DatabaseVocabulary.COLUMN_NAME_PROJECT_ID +
            " FROM " + DatabaseVocabulary.TABLE_NAME_TEAMS +
            " WHERE " + DatabaseVocabulary.TABLE_NAME_TEAMS + "." +
            DatabaseVocabulary.COLUMN_NAME_MEMBER_ID + " =:idUser "
    )
    int getUserProjectId(int idUser);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllOrReplace(Teams... teams);

    @Insert
    void insertAll(Teams... teams);

    @Delete
    void delete(Teams teams);

    @Delete
    void deleteAll(List<Teams> teams);
}
