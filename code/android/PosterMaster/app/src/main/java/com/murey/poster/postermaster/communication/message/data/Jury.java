package com.murey.poster.postermaster.communication.message.data;

import com.google.gson.annotations.SerializedName;
import com.murey.poster.postermaster.communication.requests.protocole.ProtocolVocabulary;

public class Jury {

    @SerializedName(ProtocolVocabulary.JSON_KEY_JURY_ID)
    private int idJury;

    @SerializedName(ProtocolVocabulary.JSON_KEY_DATE)
    private String date;

    @SerializedName(ProtocolVocabulary.JSON_KEY_INFO)
    private Information info;

    public Jury(int idJury, String date, Information info) {
        this.idJury = idJury;
        this.date = date;
        this.info = info;
    }

    public int getIdJury() {
        return idJury;
    }

    public void setIdJury(int idJury) {
        this.idJury = idJury;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Information getInfo() {
        return info;
    }

    public void setInfo(Information info) {
        this.info = info;
    }
}
