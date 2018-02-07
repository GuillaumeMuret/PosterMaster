package com.murey.poster.postermaster.communication.requests;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.murey.poster.postermaster.communication.message.WebMessage;
import com.murey.poster.postermaster.communication.message.WebMessageFactory;
import com.murey.poster.postermaster.communication.requests.protocole.ProtocolVocabulary;
import com.murey.poster.postermaster.model.database.tables.Projects;
import com.murey.poster.postermaster.model.database.tables.Teams;
import com.murey.poster.postermaster.model.database.tables.Users;
import com.murey.poster.postermaster.utils.LogUtils;

import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class EseoGetProjectInformationRequest extends EseoBaseRequest {

    public static final String REQUEST_GET_PROJECT_INFORMATION ="REQUEST_GET_PROJECT_INFORMATION";

    public static boolean isRequestRunning = false;

    private int projectId;

    public EseoGetProjectInformationRequest(String requestId, boolean isShowLoadingDialog, Context context, Users appUser, int projectId) {
        super(requestId, isShowLoadingDialog, context);
        this.appUsers = appUser;
        this.projectId = projectId;
    }

    @Override
    protected void onSuccess(int responseCode, WebMessage webMessage) {
        try{
            if (webMessage.getResult().equals(ProtocolVocabulary.JSON_RESULT_VALUE_OK)) {
                // TODO manage saving data
                // Manage save in databases
                Projects.updateProjectReceivedFromEseo(context,webMessage.getProjects());
                for(int i=0;i<webMessage.getProjects().length;i++){
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
        Call call = ((EseoService)interfaceObject).getProjectInformation(
                ProtocolVocabulary.PROCESS_GET_PROJECT_INFO,
                appUsers.getUsername(),
                this.projectId,
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
