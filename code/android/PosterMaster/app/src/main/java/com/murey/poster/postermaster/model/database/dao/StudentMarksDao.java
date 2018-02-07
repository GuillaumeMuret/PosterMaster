package com.murey.poster.postermaster.model.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.murey.poster.postermaster.model.database.DatabaseVocabulary;
import com.murey.poster.postermaster.model.database.tables.Projects;
import com.murey.poster.postermaster.model.database.tables.StudentMarks;

import java.util.List;

@Dao
public interface StudentMarksDao {

    @Query("SELECT * FROM "+ DatabaseVocabulary.TABLE_NAME_STUDENT_MARKS)
    List<StudentMarks> getAll();

    @Query("SELECT COUNT(*) from " + DatabaseVocabulary.TABLE_NAME_STUDENT_MARKS)
    int countStudentMarks();

    @Query("SELECT * from " + DatabaseVocabulary.TABLE_NAME_STUDENT_MARKS +
            " where " + DatabaseVocabulary.COLUMN_NAME_STUDENT_ID + " LIKE  :idStudent")
    StudentMarks getStudentMarkById(int idStudent);

    @Query("SELECT * from " + DatabaseVocabulary.TABLE_NAME_STUDENT_MARKS +
            " where " + DatabaseVocabulary.COLUMN_NAME_NEED_TO_SYNC + " = :needToSync")
    List<StudentMarks> getUnsyncStudentMarks(boolean needToSync);

    @Update
    void update(StudentMarks studentMarks);

    @Insert
    void insertAll(StudentMarks... studentMarks);

    @Delete
    void delete(StudentMarks studentMarks);

    @Delete
    void deleteAll(List<StudentMarks> studentMarks);
}
