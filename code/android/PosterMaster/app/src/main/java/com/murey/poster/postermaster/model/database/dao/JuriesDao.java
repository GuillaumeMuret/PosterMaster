package com.murey.poster.postermaster.model.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.murey.poster.postermaster.model.database.DatabaseVocabulary;
import com.murey.poster.postermaster.model.database.tables.Juries;
import com.murey.poster.postermaster.model.database.tables.Projects;
import com.murey.poster.postermaster.model.database.tables.Teams;

import java.util.List;

@Dao
public interface JuriesDao {

    @Query("SELECT * FROM "+ DatabaseVocabulary.TABLE_NAME_JURIES)
    List<Juries> getAll();

    @Query("SELECT * FROM " + DatabaseVocabulary.TABLE_NAME_JURIES
            + " WHERE " + DatabaseVocabulary.COLUMN_NAME_JURY_ID + " LIKE  :juryId")
    Juries getJuryById(int juryId);

    @Query("SELECT COUNT(*) from " + DatabaseVocabulary.TABLE_NAME_JURIES)
    int countJuries();

    @Insert
    void insertAll(Juries... juries);

    @Delete
    void delete(Juries juries);

    @Update
    void update(Juries juries);

    @Delete
    void deleteAll(List<Juries> juries);
}
