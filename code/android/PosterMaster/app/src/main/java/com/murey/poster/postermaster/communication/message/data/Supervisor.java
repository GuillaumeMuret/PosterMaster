package com.murey.poster.postermaster.communication.message.data;

import com.google.gson.annotations.SerializedName;
import com.murey.poster.postermaster.communication.requests.protocole.ProtocolVocabulary;

public class Supervisor {

    @SerializedName(ProtocolVocabulary.JSON_KEY_FORENAME)
    private String lastName;

    @SerializedName(ProtocolVocabulary.JSON_KEY_SURNAME)
    private String firstName;

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
}
