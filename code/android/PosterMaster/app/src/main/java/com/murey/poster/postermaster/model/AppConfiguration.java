package com.murey.poster.postermaster.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AppConfiguration {

    /**
     * Object ==> userAccount
     **/
    @SerializedName("poster_list")
    private ArrayList<String> posterList;

    public ArrayList<String> getPosterList() {
        return posterList;
    }

    public void setPosterList(ArrayList<String> posterList) {
        this.posterList = posterList;
    }
}
