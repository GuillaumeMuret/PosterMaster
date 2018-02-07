package com.murey.poster.postermaster.model.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.murey.poster.postermaster.model.database.DatabaseVocabulary;
import com.murey.poster.postermaster.model.database.tables.Posters;
import com.murey.poster.postermaster.model.database.tables.Projects;
import com.murey.poster.postermaster.model.database.tables.Supervisors;
import com.murey.poster.postermaster.model.database.tables.Teams;

import java.util.List;

@Dao
public interface PostersDao {

    @Query("SELECT * FROM "+ DatabaseVocabulary.TABLE_NAME_POSTERS)
    List<Posters> getAll();

    @Query("SELECT * FROM "+ DatabaseVocabulary.TABLE_NAME_POSTERS +
            " WHERE " + DatabaseVocabulary.TABLE_NAME_POSTERS + "." + DatabaseVocabulary.COLUMN_NAME_PROJECT_ID + " =:idProject")
    Posters getPosterByProjectId(int idProject);

    @Query("SELECT COUNT(*) from " + DatabaseVocabulary.TABLE_NAME_POSTERS)
    int countPosters();

    @Update
    void update(Posters posters);

    @Insert
    void insertAll(Posters... posters);

    @Delete
    void delete(Posters posters);

    @Delete
    void deleteAll(List<Posters> posters);
}
