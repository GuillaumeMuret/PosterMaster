package com.murey.poster.postermaster.model.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.murey.poster.postermaster.model.database.DatabaseVocabulary;
import com.murey.poster.postermaster.model.database.tables.Projects;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ProjectsDao {

    @Query("SELECT * FROM "+ DatabaseVocabulary.TABLE_NAME_PROJECTS)
    List<Projects> getAll();

    @Query("SELECT * FROM " + DatabaseVocabulary.TABLE_NAME_PROJECTS
            + " where " + DatabaseVocabulary.COLUMN_NAME_PROJECT_ID + " LIKE  :idProject")
    Projects getProjectById(int idProject);

    @Query("SELECT COUNT(*) from " + DatabaseVocabulary.TABLE_NAME_PROJECTS)
    int countProjects();

    @Insert
    void insertAll(Projects... projects);

    @Update
    void update(Projects projects);

    @Query("DELETE FROM " + DatabaseVocabulary.TABLE_NAME_PROJECTS + " WHERE " + DatabaseVocabulary.COLUMN_NAME_PROJECT_ID + " = :idProject")
    void deleteProjectById(int idProject);

    @Delete
    void deleteAll(List<Projects> projectList);
}
