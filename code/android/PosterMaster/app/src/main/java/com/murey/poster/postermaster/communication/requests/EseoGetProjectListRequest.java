package com.murey.poster.postermaster.communication.requests;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.murey.poster.postermaster.communication.message.WebMessage;
import com.murey.poster.postermaster.communication.message.WebMessageFactory;
import com.murey.poster.postermaster.communication.requests.protocole.ProtocolVocabulary;
import com.murey.poster.postermaster.model.database.AppDatabase;
import com.murey.poster.postermaster.model.database.tables.Posters;
import com.murey.poster.postermaster.model.database.tables.Projects;
import com.murey.poster.postermaster.model.database.tables.Supervisors;
import com.murey.poster.postermaster.model.database.tables.Teams;
import com.murey.poster.postermaster.model.database.tables.Users;
import com.murey.poster.postermaster.utils.LogUtils;

import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class EseoGetProjectListRequest extends EseoBaseRequest {

    public static final String REQUEST_GET_PROJECT_LIST ="REQUEST_GET_PROJECT_LIST";

    public static boolean isRequestRunning = false;

    public EseoGetProjectListRequest(String requestId, boolean isShowLoadingDialog, Context context, Users appUser) {
        super(requestId, isShowLoadingDialog, context);
        this.appUsers = appUser;
    }

    @Override
    protected void onSuccess(int responseCode, WebMessage webMessage) {
        try{
            if (webMessage.getResult().equals(ProtocolVocabulary.JSON_RESULT_VALUE_OK)) {
                // Manage save in databases
                Projects.updateProjectReceivedFromEseo(context,webMessage.getProjects());
                for(int i=0;i<webMessage.getProjects().length;i++){
                    Posters poster = AppDatabase.getInstance(context).postersDao().getPosterByProjectId(webMessage.getProjects()[i].getProjectId());
                    if(poster == null){
                        AppDatabase.getInstance(context).postersDao().insertAll(new Posters(
                                webMessage.getProjects()[i].getProjectId(),
                                webMessage.getProjects()[i].getProjectId(),
                                "",
                                "",
                                ""
                                )
                        );
                    }
                    for(int j=0;j<webMessage.getProjects()[i].getStudents().length;j++){
                        Teams.updateTeamProjectReceivedFromEseo(context,webMessage.getProjects()[i].getProjectId(),webMessage.getProjects()[i].getStudents()[j].getUserServerEseoId());
                        Users.updateStudentsProjectReceivedFromEseo(context, webMessage.getProjects()[i].getStudents()[j]);
                    }
                }
            }
        }catch(NullPointerException npe){
            LogUtils.e(LogUtils.DEBUG_TAG,"Exception => ",npe);
        }
    }

    @Override
    protected String getBaseUrl() {
        return EseoService.BASE_URL;
    }

    @Override
    protected Class getServiceInterface() {
        return EseoService.class;
    }

    @Override
    protected Call createCall(Object interfaceObject) {
        Call call = ((EseoService)interfaceObject).getProjectList(
                ProtocolVocabulary.PROCESS_LIST_ALL_PROJECT,
                appUsers.getUsername(),
                appUsers.getToken()
        );
        LogUtils.d(LogUtils.DEBUG_TAG,"Call = "+call.request().url());
        return call;
    }

    @Override
    protected Converter.Factory getConverterFactory() {
        Gson gson = new GsonBuilder().registerTypeAdapter(WebMessage.class, new WebMessageFactory.WebMessageDeserializer()).create();
        return GsonConverterFactory.create(gson);
    }

    @Override
    public void onResponse(Call call, Response response) {
        super.onResponse(call, response);
        isRequestRunning = false;
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        super.onFailure(call, t);
        isRequestRunning = false;
    }

    @Override
    public void startRequest() {
        super.startRequest();
        isRequestRunning = true;
    }
}
