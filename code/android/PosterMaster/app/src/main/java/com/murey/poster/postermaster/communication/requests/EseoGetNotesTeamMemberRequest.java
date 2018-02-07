package com.murey.poster.postermaster.communication.requests;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.murey.poster.postermaster.communication.message.WebMessage;
import com.murey.poster.postermaster.communication.message.WebMessageFactory;
import com.murey.poster.postermaster.communication.requests.protocole.ProtocolVocabulary;
import com.murey.poster.postermaster.model.database.tables.StudentMarks;
import com.murey.poster.postermaster.model.database.tables.Users;
import com.murey.poster.postermaster.utils.LogUtils;

import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class EseoGetNotesTeamMemberRequest extends EseoBaseRequest {

    public static final String REQUEST_GET_NOTES_TEAM_MEMBER ="REQUEST_GET_NOTES_TEAM_MEMBER";

    public static boolean isRequestRunning = false;

    private int projectId;

    public EseoGetNotesTeamMemberRequest(String requestId, boolean isShowLoadingDialog, Context context, Users appUser, int projectId) {
        super(requestId, isShowLoadingDialog, context);
        this.appUsers = appUser;
        this.projectId = projectId;
    }

    @Override
    protected void onSuccess(int responseCode, WebMessage webMessage) {
        try{
            if (webMessage.getResult().equals(ProtocolVocabulary.JSON_RESULT_VALUE_OK)) {

                // Manage save in databases
                StudentMarks.updateStudentMarksReceivedFromEseo(context,webMessage.getNotes());
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
        Call call = ((EseoService)interfaceObject).getNotesOfTeamMember(
                ProtocolVocabulary.PROCESS_LIST_MEMBER_PROJECT_NOTES,
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
