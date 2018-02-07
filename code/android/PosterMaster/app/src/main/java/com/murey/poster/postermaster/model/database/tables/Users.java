package com.murey.poster.postermaster.model.database.tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.murey.poster.postermaster.communication.message.data.Student;
import com.murey.poster.postermaster.model.database.AppDatabase;
import com.murey.poster.postermaster.model.database.DatabaseVocabulary;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = DatabaseVocabulary.TABLE_NAME_USERS)
public class Users implements Parcelable {

    public static final int APP_USER_ID = 0;
    public static final int APP_NON_USER_ID = 1;

    public static final int MODE_VISITOR = 1;
    public static final int MODE_NON_VISITOR = 0;

    public static final int MANDATORY_DATA_RECEIVED = 1;
    public static final int NO_MANDATORY_DATA_RECEIVED = 0;

    public static final Parcelable.Creator<Users> CREATOR = new Parcelable.Creator<Users>() {

        public Users createFromParcel(Parcel source) {
            return new Users(source);
        }


        public Users[] newArray(int size) {
            return new Users[size];
        }
    };

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_USER_ID)
    private int idUser;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_USER_APP_ID)
    private int idAppUser;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_USER_SERVER_ESEO_ID)
    private int idServerEseoUser;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_USER_NAME)
    private String username;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_USER_SALT)
    private int salt;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_PASSWORD)
    private String password;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_FORENAME)
    private String forename;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_SURNAME)
    private String surname;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_EMAIL)
    private String email;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_PATH_PHOTO)
    private String pathPhoto;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_TOKEN)
    private String token;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_MODE_VISITOR)
    private int modeVisitor;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_ALL_MANDATORY_DATA_RECEIVED)
    private int mandatoryDataReceived;

    public Users(int idAppUser, int idServerEseoUser, String username, String password, String token) {
        this.idAppUser = idAppUser;
        this.idServerEseoUser = idServerEseoUser;
        this.username = username;
        this.salt = 0;
        this.password = password;
        this.forename = "";
        this.surname = "";
        this.email = "";
        this.pathPhoto = "";
        this.token = token;
    }

    @Ignore
    public Users(int idAppUser, int idServerEseoUser, String forename, String surname) {
        this.idAppUser = idAppUser;
        this.idServerEseoUser = idServerEseoUser;
        this.username = "";
        this.salt = 0;
        this.password = "";
        this.forename = forename;
        this.surname = surname;
        this.email = "";
        this.pathPhoto = "";
        this.token = "";
        this.modeVisitor = MODE_NON_VISITOR;
        this.mandatoryDataReceived = NO_MANDATORY_DATA_RECEIVED;
    }

    @Ignore
    public Users(Parcel in) {
        this.idAppUser = in.readInt();
        this.idServerEseoUser = in.readInt();
        this.username = in.readString();
        this.salt = in.readInt();
        this.password = in.readString();
        this.forename = in.readString();
        this.surname = in.readString();
        this.email = in.readString();
        this.pathPhoto = in.readString();
        this.token = in.readString();
        this.modeVisitor = in.readInt();
        this.mandatoryDataReceived = in.readInt();
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdAppUser() {
        return idAppUser;
    }

    public void setIdAppUser(int idAppUser) {
        this.idAppUser = idAppUser;
    }

    public int getIdServerEseoUser() {
        return idServerEseoUser;
    }

    public void setIdServerEseoUser(int idServerEseoUser) {
        this.idServerEseoUser = idServerEseoUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getSalt() {
        return salt;
    }

    public void setSalt(int salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPathPhoto() {
        return pathPhoto;
    }

    public void setPathPhoto(String pathPhoto) {
        this.pathPhoto = pathPhoto;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isModeVisitor() {
        return modeVisitor==MODE_VISITOR;
    }

    public void setModeVisitor(boolean modeVisitor) {
        this.modeVisitor = modeVisitor?MODE_VISITOR:MODE_NON_VISITOR;
    }

    public int getModeVisitor() {
        return modeVisitor;
    }

    public void setModeVisitor(int modeVisitor) {
        this.modeVisitor = modeVisitor;
    }

    public int getMandatoryDataReceived() {
        return mandatoryDataReceived;
    }

    public boolean isMandatoryDataReceived() {
        return mandatoryDataReceived==MANDATORY_DATA_RECEIVED;
    }

    public void setMandatoryDataReceived(int mandatoryDataReceived) {
        this.mandatoryDataReceived = mandatoryDataReceived;
    }

    public void setMandatoryDataReceived(boolean mandatoryDataReceived) {
        this.mandatoryDataReceived = mandatoryDataReceived?MANDATORY_DATA_RECEIVED:NO_MANDATORY_DATA_RECEIVED;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.idAppUser);
        dest.writeInt(this.idServerEseoUser);
        dest.writeString(this.username);
        dest.writeInt(this.salt);
        dest.writeString(this.password);
        dest.writeString(this.forename);
        dest.writeString(this.surname);
        dest.writeString(this.email);
        dest.writeString(this.pathPhoto);
        dest.writeString(this.token);
        dest.writeInt(this.modeVisitor);
        dest.writeInt(this.mandatoryDataReceived);
    }

    public static void updateStudentsProjectReceivedFromEseo(Context context, Student... serverStudents){
        List<Users> usersList = AppDatabase.getInstance(context).usersDao().getAll();
        ArrayList<Integer> listUsersIdInDatabase = new ArrayList<>();

        // Update project
        for(int j=0;j<usersList.size();j++){
            listUsersIdInDatabase.add(usersList.get(j).getIdServerEseoUser());
        }
        for(int i=0;i<serverStudents.length;i++){
            if(listUsersIdInDatabase.contains(serverStudents[i].getUserServerEseoId())){
                usersList.get(listUsersIdInDatabase.indexOf(serverStudents[i].getUserServerEseoId()))
                        .setSurname(serverStudents[i].getFirstName());
                usersList.get(listUsersIdInDatabase.indexOf(serverStudents[i].getUserServerEseoId()))
                        .setForename(serverStudents[i].getLastName());

                AppDatabase.getInstance(context).usersDao().updateUsers(usersList.get(
                        listUsersIdInDatabase.indexOf(serverStudents[i].getUserServerEseoId())));
            } else {
                Users users = new Users(
                        Users.APP_NON_USER_ID,
                        serverStudents[i].getUserServerEseoId(),
                        serverStudents[i].getLastName(),
                        serverStudents[i].getFirstName()
                );
                AppDatabase.getInstance(context).usersDao().insertAll(users);
            }
        }
    }
}
