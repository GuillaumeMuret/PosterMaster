package com.murey.poster.postermaster.model.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.murey.poster.postermaster.model.database.DatabaseVocabulary;
import com.murey.poster.postermaster.model.database.tables.Supervisors;

import java.util.List;

@Dao
public interface SupervisorsDao {

    @Query("SELECT * FROM "+ DatabaseVocabulary.TABLE_NAME_SUPERVISORS)
    List<Supervisors> getAll();

    @Query("SELECT COUNT(*) from " + DatabaseVocabulary.TABLE_NAME_SUPERVISORS)
    int countSupervisors();

    @Insert
    void insertAll(Supervisors... supervisors);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllOrReplace(Supervisors... supervisors);

    @Delete
    void delete(Supervisors supervisors);

    @Delete
    void deleteAll(List<Supervisors> supervisors);
}
