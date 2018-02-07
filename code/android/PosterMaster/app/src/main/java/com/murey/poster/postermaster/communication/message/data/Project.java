package com.murey.poster.postermaster.communication.message.data;

import com.google.gson.annotations.SerializedName;
import com.murey.poster.postermaster.communication.requests.protocole.ProtocolVocabulary;

public class Project {

    @SerializedName(ProtocolVocabulary.JSON_KEY_PROJECT_ID)
    private int projectId;

    @SerializedName(ProtocolVocabulary.JSON_KEY_ID_PROJECT)
    private int idProject;

    @SerializedName(ProtocolVocabulary.JSON_KEY_PROJECT_TITLE)
    private String projectTitle;

    @SerializedName(ProtocolVocabulary.JSON_KEY_PROJECT_DESCRIPTION)
    private String description;

    @SerializedName(ProtocolVocabulary.JSON_KEY_PROJECT_POSTER_ENABLE)
    private String posterEnable;

    @SerializedName(ProtocolVocabulary.JSON_KEY_SUPERVISOR)
    private Supervisor supervisor;

    @SerializedName(ProtocolVocabulary.JSON_KEY_PROJECT_CONFIDENTIALITY)
    private int confidentialLevel;

    @SerializedName(ProtocolVocabulary.JSON_KEY_STUDENTS)
    private Student[] students;

    public int getIdProject() {
        return idProject;
    }

    public void setIdProject(int idProject) {
        this.idProject = idProject;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosterEnable() {
        return posterEnable;
    }

    public void setPosterEnable(String posterEnable) {
        this.posterEnable = posterEnable;
    }

    public Supervisor getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
    }

    public int getConfidentialLevel() {
        return confidentialLevel;
    }

    public void setConfidentialLevel(int confidentialLevel) {
        this.confidentialLevel = confidentialLevel;
    }

    public Student[] getStudents() {
        return students;
    }

    public void setStudents(Student[] students) {
        this.students = students;
    }
}