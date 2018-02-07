package com.murey.poster.postermaster.communication.message.data;

import com.google.gson.annotations.SerializedName;
import com.murey.poster.postermaster.communication.requests.protocole.ProtocolVocabulary;

public class Information {

    @SerializedName(ProtocolVocabulary.JSON_KEY_PROJECTS)
    private Project[] projects;

    @SerializedName(ProtocolVocabulary.JSON_KEY_MEMBERS)
    private Member[] members;

    public Project[] getProjects() {
        return projects;
    }

    public void setProjects(Project[] projects) {
        this.projects = projects;
    }

    public Member[] getMembers() {
        return members;
    }

    public void setMembers(Member[] members) {
        this.members = members;
    }
}
