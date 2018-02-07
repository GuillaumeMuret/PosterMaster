package com.murey.poster.postermaster.model.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.murey.poster.postermaster.model.database.DatabaseVocabulary;
import com.murey.poster.postermaster.model.database.tables.JuryMembers;

import java.util.List;

@Dao
public interface JuryMembersDao {

    @Query("SELECT * FROM "+ DatabaseVocabulary.TABLE_NAME_JURY_MEMBERS)
    List<JuryMembers> getAll();

    @Query("SELECT COUNT(*) from " + DatabaseVocabulary.TABLE_NAME_JURY_MEMBERS)
    int countJuryMembers();

    @Query("SELECT " + DatabaseVocabulary.COLUMN_NAME_JURY_ID + " FROM "+ DatabaseVocabulary.TABLE_NAME_JURY_MEMBERS +
            " where " + DatabaseVocabulary.COLUMN_NAME_MEMBER_ID + " LIKE  :idMember")
    List<Integer> getMyJuriesIdList(int idMember);

    @Query("SELECT * FROM "+ DatabaseVocabulary.TABLE_NAME_JURY_MEMBERS +
            " where " + DatabaseVocabulary.COLUMN_NAME_JURY_ID + " LIKE  :idJury")
    List<JuryMembers> getJuryMembersFromJuryId(int idJury);

    @Insert
    void insertAll(JuryMembers... juryMembers);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllOrReplace(JuryMembers... juryMembers);

    @Update
    void update(JuryMembers juryMembers);

    @Delete
    void delete(JuryMembers juryMembers);

    @Delete
    void deleteAll(List<JuryMembers> juryMembers);
}
