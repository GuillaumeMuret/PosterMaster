package com.murey.poster.postermaster.communication.message.data;

import com.google.gson.annotations.SerializedName;
import com.murey.poster.postermaster.communication.requests.protocole.ProtocolVocabulary;

public class Note {

    @SerializedName(ProtocolVocabulary.JSON_KEY_USER_ID)
    private int userId;

    @SerializedName(ProtocolVocabulary.JSON_KEY_FORENAME)
    private String lastName;

    @SerializedName(ProtocolVocabulary.JSON_KEY_SURNAME)
    private String firstName;

    @SerializedName(ProtocolVocabulary.JSON_KEY_MY_NOTE)
    private String myNote;

    @SerializedName(ProtocolVocabulary.JSON_KEY_AVERAGE_NOTE)
    private String averageNote;

    public Note(int userId, String lastName, String firstName, String myNote, String averageNote) {
        this.userId = userId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.myNote = myNote;
        this.averageNote = averageNote;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Float getMyNote() {
        return Float.valueOf(myNote);
    }

    public void setMyNote(String myNote) {
        this.myNote = myNote;
    }

    public Float getAverageNote() {
        return averageNote != null
                ? Float.valueOf(averageNote)
                : -1f;
    }

    public void setAverageNote(String averageNote) {
        this.averageNote = averageNote;
    }
}