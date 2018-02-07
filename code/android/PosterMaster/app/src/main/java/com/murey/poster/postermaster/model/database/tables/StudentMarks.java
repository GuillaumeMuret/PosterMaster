package com.murey.poster.postermaster.model.database.tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;

import com.murey.poster.postermaster.communication.message.data.Note;
import com.murey.poster.postermaster.model.database.AppDatabase;
import com.murey.poster.postermaster.model.database.DatabaseVocabulary;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = DatabaseVocabulary.TABLE_NAME_STUDENT_MARKS)
public class StudentMarks {

    @PrimaryKey
    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_STUDENT_ID)
    private int idStudent;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_MY_MARK)
    private float myMark;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_AVERAGE_MARK)
    private float averageMark;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_MY_MARK_TO_SYNC)
    private float myMarkToSynchronize;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_NEED_TO_SYNC)
    private boolean needToSync;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_JUSTIFICATION)
    private String justification;

    public StudentMarks(int idStudent, float myMark, float averageMark, float myMarkToSynchronize, boolean needToSync, String justification) {
        this.idStudent = idStudent;
        this.myMark = myMark;
        this.averageMark = averageMark;
        this.myMarkToSynchronize = myMarkToSynchronize;
        this.needToSync = needToSync;
        this.justification = justification;
    }

    public int getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
    }

    public float getMyMark() {
        return myMark;
    }

    public void setMyMark(float myMark) {
        this.myMark = myMark;
    }

    public float getAverageMark() {
        return averageMark;
    }

    public void setAverageMark(float averageMark) {
        this.averageMark = averageMark;
    }

    public float getMyMarkToSynchronize() {
        return myMarkToSynchronize;
    }

    public void setMyMarkToSynchronize(float myMarkToSynchronize) {
        this.myMarkToSynchronize = myMarkToSynchronize;
    }

    public boolean isNeedToSync() {
        return needToSync;
    }

    public void setNeedToSync(boolean needToSync) {
        this.needToSync = needToSync;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public static void updateStudentMarksReceivedFromEseo(Context context, Note[] serverNotes) {
        List<StudentMarks> studentMarksInDatabase = AppDatabase.getInstance(context).studentMarksDao().getAll();
        ArrayList<Integer> listStudentIdInDatabase = new ArrayList<>();

        // Update project
        for (int j = 0; j < studentMarksInDatabase.size(); j++) {
            listStudentIdInDatabase.add(studentMarksInDatabase.get(j).getIdStudent());
        }
        for (int i = 0; i < serverNotes.length; i++) {
            if (listStudentIdInDatabase.contains(serverNotes[i].getUserId())) {
                studentMarksInDatabase.get(listStudentIdInDatabase.indexOf(serverNotes[i].getUserId()))
                        .setMyMark(serverNotes[i].getMyNote());
                studentMarksInDatabase.get(listStudentIdInDatabase.indexOf(serverNotes[i].getUserId()))
                        .setAverageMark(serverNotes[i].getAverageNote());
                studentMarksInDatabase.get(listStudentIdInDatabase.indexOf(serverNotes[i].getUserId()))
                        .setNeedToSync(false);
                AppDatabase.getInstance(context).studentMarksDao().update(studentMarksInDatabase.get(listStudentIdInDatabase.indexOf(serverNotes[i].getUserId())));
            } else {
                StudentMarks studentMarks = new StudentMarks(
                        serverNotes[i].getUserId(),
                        serverNotes[i].getMyNote(),
                        serverNotes[i].getAverageNote(),
                        serverNotes[i].getMyNote(),
                        false,
                        ""
                );
                AppDatabase.getInstance(context).studentMarksDao().insertAll(studentMarks);
            }
        }
    }

}
