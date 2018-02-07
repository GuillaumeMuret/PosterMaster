package com.murey.poster.postermaster.model.database.tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.murey.poster.postermaster.communication.message.WebMessage;
import com.murey.poster.postermaster.communication.message.data.Note;
import com.murey.poster.postermaster.communication.message.data.Project;
import com.murey.poster.postermaster.model.database.AppDatabase;
import com.murey.poster.postermaster.model.database.DatabaseVocabulary;
import com.murey.poster.postermaster.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = DatabaseVocabulary.TABLE_NAME_PROJECTS)
public class Projects implements Parcelable {

    public static final String PROJECTS_DESCRIPTION_CONFIDENTIAL = "Confidential!";

    public static final Parcelable.Creator<Projects> CREATOR = new Parcelable.Creator<Projects>() {

        public Projects createFromParcel(Parcel source) {
            return new Projects(source);
        }


        public Projects[] newArray(int size) {
            return new Projects[size];
        }
    };

    @PrimaryKey
    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_PROJECT_ID)
    private int idProject;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_TITLE)
    private String title;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_DESCRIPTION)
    private String description;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_CONFIDENTIALITY_ID)
    private int idConfidentiality;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_POSTER_ENABLE)
    private String posterEnable;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_FORENAME)
    private String supervisorForename;

    @ColumnInfo(name = DatabaseVocabulary.COLUMN_NAME_SURNAME)
    private String supervisorSurname;

    public Projects(int idProject, String title, String description, int idConfidentiality, String posterEnable, String supervisorForename, String supervisorSurname) {
        this.idProject = idProject;
        this.title = title;
        this.description = description;
        this.idConfidentiality = idConfidentiality;
        this.posterEnable = posterEnable;
        this.supervisorForename = supervisorForename;
        this.supervisorSurname = supervisorSurname;
    }

    public Projects(Parcel in) {
        this.idProject = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.idConfidentiality = in.readInt();
        this.posterEnable = in.readString();
        this.supervisorForename = in.readString();
        this.supervisorSurname = in.readString();
    }

    public int getIdProject() {
        return idProject;
    }

    public Projects setIdProject(int idProject) {
        this.idProject = idProject;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Projects setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Projects setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getIdConfidentiality() {
        return idConfidentiality;
    }

    public Projects setIdConfidentiality(int idConfidentiality) {
        this.idConfidentiality = idConfidentiality;
        return this;
    }

    public String getPosterEnable() {
        return posterEnable;
    }

    public Projects setPosterEnable(String posterEnable) {
        this.posterEnable = posterEnable;
        return this;
    }

    public boolean isPosterEnable() {
        return posterEnable.equals("true");
    }

    public Projects setPosterEnable(boolean posterEnable) {
        if(posterEnable){
            this.posterEnable = "true";
        }else{
            this.posterEnable = "false";
        }
        return this;
    }

    public String getSupervisorForename() {
        return supervisorForename;
    }

    public Projects setSupervisorForename(String supervisorForename) {
        this.supervisorForename = supervisorForename;
        return this;
    }

    public String getSupervisorSurname() {
        return supervisorSurname;
    }

    public Projects setSupervisorSurname(String supervisorSurname) {
        this.supervisorSurname = supervisorSurname;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.idProject);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeInt(this.idConfidentiality);
        dest.writeString(this.posterEnable);
        dest.writeString(this.supervisorForename);
        dest.writeString(this.supervisorSurname);
    }

    private static boolean isPosterEnable(String posterEnable){
        if(posterEnable.equals("true")){
            return true;
        }else{
            return false;
        }
    }

    public static void updateRangeProjectReceivedFromEseo(Context context, Users appUser, Project... serverProjects){
        List<Projects> projectList = AppDatabase.getInstance(context).projectsDao().getAll();
        ArrayList<Integer> listProjectIdInDatabase = new ArrayList<>();

        // Update project
        for(int j=0;j<projectList.size();j++){
            listProjectIdInDatabase.add(projectList.get(j).getIdProject());
        }
        for(int i=0;i<serverProjects.length;i++){

            JuryProjects.updateJuryProjectsFromEseo(context, JuryMembers.JURY_ID_OPEN_HOUSE,serverProjects[i].getIdProject());
            Posters.updatePosterReceivedFromEseo(context, serverProjects[i].getIdProject(), serverProjects[i].getPosterEnable());

            if(listProjectIdInDatabase.contains(serverProjects[i].getIdProject())){
                projectList.get(listProjectIdInDatabase.indexOf(serverProjects[i].getIdProject()))
                        .setIdConfidentiality(serverProjects[i].getConfidentialLevel())
                        .setPosterEnable(true)
                        .setTitle(serverProjects[i].getProjectTitle());


                if(serverProjects[i].getDescription() != null && !serverProjects[i].getDescription().equals("")){
                    projectList.get(listProjectIdInDatabase.indexOf(serverProjects[i].getIdProject()))
                            .setDescription(serverProjects[i].getDescription());
                }

                AppDatabase.getInstance(context).projectsDao().update(projectList.get(
                        listProjectIdInDatabase.indexOf(serverProjects[i].getIdProject())));
            } else {
                Projects projects = new Projects(
                        serverProjects[i].getIdProject(),
                        serverProjects[i].getProjectTitle(),
                        serverProjects[i].getDescription(),
                        serverProjects[i].getConfidentialLevel(),
                        "true",
                        "no name",
                        "no name"
                );
                AppDatabase.getInstance(context).projectsDao().insertAll(projects);
            }

            if(appUser.isModeVisitor()){
                List<Teams> studentProjectTeam = AppDatabase.getInstance(context).teamsDao().getProjectTeam(serverProjects[i].getIdProject());
                Note[] notes = new Note[studentProjectTeam.size()];
                for(int j=0;j<studentProjectTeam.size();j++){
                    notes[j] = new Note(studentProjectTeam.get(j).getIdMember(),"","","15","15");
                }
                StudentMarks.updateStudentMarksReceivedFromEseo(context,notes);
            }
        }
    }

    public static void updateProjectReceivedFromEseo(Context context, Project... serverProjects){
        List<Projects> projectList = AppDatabase.getInstance(context).projectsDao().getAll();
        ArrayList<Integer> listProjectIdInDatabase = new ArrayList<>();

        // Update project
        for(int j=0;j<projectList.size();j++){
            listProjectIdInDatabase.add(projectList.get(j).getIdProject());
        }
        for(int i=0;i<serverProjects.length;i++){
            if(listProjectIdInDatabase.contains(serverProjects[i].getProjectId())){
                projectList.get(listProjectIdInDatabase.indexOf(serverProjects[i].getProjectId()))
                        .setIdConfidentiality(serverProjects[i].getConfidentialLevel())
                        .setPosterEnable(isPosterEnable(serverProjects[i].getPosterEnable()))
                        .setTitle(serverProjects[i].getProjectTitle())
                        .setSupervisorForename(serverProjects[i].getSupervisor().getLastName())
                        .setSupervisorSurname(serverProjects[i].getSupervisor().getFirstName());

                if(serverProjects[i].getDescription() != null && !serverProjects[i].getDescription().equals("")){
                    projectList.get(listProjectIdInDatabase.indexOf(serverProjects[i].getProjectId()))
                            .setDescription(serverProjects[i].getDescription());
                }

                AppDatabase.getInstance(context).projectsDao().update(projectList.get(
                        listProjectIdInDatabase.indexOf(serverProjects[i].getProjectId())));
            } else {
                Projects projects;
                projects = new Projects(
                        serverProjects[i].getProjectId(),
                        serverProjects[i].getProjectTitle(),
                        serverProjects[i].getDescription(),
                        serverProjects[i].getConfidentialLevel(),
                        serverProjects[i].getPosterEnable(),
                        serverProjects[i].getSupervisor().getLastName(),
                        serverProjects[i].getSupervisor().getFirstName()
                );
                AppDatabase.getInstance(context).projectsDao().insertAll(projects);
            }
        }
    }
}
