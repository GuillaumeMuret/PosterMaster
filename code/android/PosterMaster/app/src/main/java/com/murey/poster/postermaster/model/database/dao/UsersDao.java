package com.murey.poster.postermaster.model.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.murey.poster.postermaster.model.database.DatabaseVocabulary;
import com.murey.poster.postermaster.model.database.tables.Projects;
import com.murey.poster.postermaster.model.database.tables.Users;

import java.util.List;

@Dao
public interface UsersDao {

    @Query("SELECT * FROM "+ DatabaseVocabulary.TABLE_NAME_USERS)
    List<Users> getAll();

    @Query("SELECT COUNT(*) from " + DatabaseVocabulary.TABLE_NAME_USERS)
    int countUsers();

    @Query("SELECT * FROM "+ DatabaseVocabulary.TABLE_NAME_USERS + " WHERE " +
            DatabaseVocabulary.COLUMN_NAME_USER_APP_ID + " = "+Users.APP_USER_ID)
    Users getAppUser();

    @Query("SELECT * FROM "+ DatabaseVocabulary.TABLE_NAME_USERS + " WHERE " +
            DatabaseVocabulary.COLUMN_NAME_USER_SERVER_ESEO_ID + " =:idUser")
    Users getUserByEseoId(int idUser);

    @Insert
    void insertAll(Users... users);

    @Update
    void updateUsers(Users... users);

    @Delete
    void delete(Users users);

    @Delete
    void deleteAll(List<Users> usersList);
}
