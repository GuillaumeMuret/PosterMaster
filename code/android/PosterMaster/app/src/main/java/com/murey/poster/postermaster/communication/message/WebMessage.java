package com.murey.poster.postermaster.communication.message;

import com.google.gson.annotations.SerializedName;
import com.murey.poster.postermaster.communication.message.data.Jury;
import com.murey.poster.postermaster.communication.message.data.Note;
import com.murey.poster.postermaster.communication.message.data.Project;
import com.murey.poster.postermaster.communication.requests.protocole.ProtocolVocabulary;

public class WebMessage {

    public static final String NON_VALID_USER = "Non valid user";

    @SerializedName(ProtocolVocabulary.JSON_KEY_API)
    private String api;

    @SerializedName(ProtocolVocabulary.JSON_KEY_RESULT)
    private String result;

    @SerializedName(ProtocolVocabulary.JSON_KEY_TOKEN)
    private String token;

    @SerializedName(ProtocolVocabulary.JSON_KEY_ERROR)
    private String error;

    @SerializedName(ProtocolVocabulary.JSON_KEY_PROJECTS)
    private Project[] projects;

    @SerializedName(ProtocolVocabulary.JSON_KEY_JURIES)
    private Jury[] juries;

    @SerializedName(ProtocolVocabulary.JSON_KEY_NOTES)
    private Note[] notes;

    private String rawBody;

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Project[] getProjects() {
        return projects;
    }

    public void setProjects(Project[] projects) {
        this.projects = projects;
    }

    public Jury[] getJuries() {
        return juries;
    }

    public void setJuries(Jury[] juries) {
        this.juries = juries;
    }

    public Note[] getNotes() {
        return notes;
    }

    public void setNotes(Note[] notes) {
        this.notes = notes;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRawBody() {
        return rawBody;
    }

    public void setRawBody(String rawBody) {
        this.rawBody = rawBody;
    }
}
