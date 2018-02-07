package com.murey.poster.postermaster.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RootScreen implements Parcelable{

    public static final Parcelable.Creator<RootScreen> CREATOR = new Parcelable.Creator<RootScreen>() {

        public RootScreen createFromParcel(Parcel source) {
            return new RootScreen(source);
        }


        public RootScreen[] newArray(int size) {
            return new RootScreen[size];
        }
    };
    
    private int rootScreen;

    public RootScreen(int rootScreen) {
        this.rootScreen = rootScreen;
    }

    public RootScreen(Parcel in) {
        this.rootScreen = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.rootScreen);
    }

    public int getRootScreen() {
        return rootScreen;
    }
}
