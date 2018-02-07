package com.murey.poster.postermaster.model.database.tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.murey.poster.postermaster.model.database.AppDatabase;
import com.murey.poster.postermaster.model.database.DatabaseVocabulary;
import com.murey.poster.postermaster.utils.FileUtils;
import com.murey.poster.postermaster.utils.LogUtils;

@Entity(tableName = DatabaseVocabulary.TABLE_NAME_POSTERS)
public class Posters {

    @PrimaryKey
    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_POSTER_ID)
    private int idPoster;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_PROJECT_ID)
    private int idProject;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_POSTER_URI)
    private String uriPoster;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_FILEPATH)
    private String filename;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_USER_NOTES)
    private String userNotes;

    public Posters(int idPoster, int idProject, String uriPoster, String filename, String userNotes) {
        this.idPoster = idPoster;
        this.idProject = idProject;
        this.uriPoster = uriPoster;
        this.filename = filename;
        this.userNotes = userNotes;
    }

    public int getIdPoster() {
        return idPoster;
    }

    public void setIdPoster(int idPoster) {
        this.idPoster = idPoster;
    }

    public int getIdProject() {
        return idProject;
    }

    public void setIdProject(int idProject) {
        this.idProject = idProject;
    }

    public String getUriPoster() {
        return uriPoster;
    }

    public void setUriPoster(String uriPoster) {
        this.uriPoster = uriPoster;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUserNotes() {
        return userNotes;
    }

    public void setUserNotes(String userNotes) {
        this.userNotes = userNotes;
    }

    public static void updatePosterReceivedFromEseo(Context context, int projectId, String responseBody){
        if(responseBody.startsWith("{")){
            LogUtils.d(LogUtils.DEBUG_TAG, "Response from ESEO => " + responseBody);
        }else{
            byte[] decodedString = Base64.decode(responseBody, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            String filename = String.valueOf(projectId);
            String posterUri = FileUtils.savePosterFile(context,filename,decodedByte);
            Posters poster = AppDatabase.getInstance(context).postersDao().getPosterByProjectId(projectId);
            if(poster == null){
                AppDatabase.getInstance(context).postersDao().insertAll(new Posters(
                                projectId,
                                projectId,
                                posterUri,
                                filename,
                                ""
                        )
                );
            }else{
                poster.setFilename(filename);
                poster.setUriPoster(posterUri);
                AppDatabase.getInstance(context).postersDao().update(poster);
            }
        }
    }
}
