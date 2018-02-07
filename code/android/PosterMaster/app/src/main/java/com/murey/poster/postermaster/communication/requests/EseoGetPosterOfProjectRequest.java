package com.murey.poster.postermaster.communication.requests;

import android.content.Context;

import com.google.gson.Gson;
import com.murey.poster.postermaster.communication.message.WebMessage;
import com.murey.poster.postermaster.communication.requests.protocole.ProtocolVocabulary;
import com.murey.poster.postermaster.model.database.tables.Posters;
import com.murey.poster.postermaster.model.database.tables.Users;
import com.murey.poster.postermaster.utils.LogUtils;

import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class EseoGetPosterOfProjectRequest extends EseoBaseRequest {

    public static final String REQUEST_GET_POSTER_OF_PROJECT ="REQUEST_GET_POSTER_OF_PROJECT";

    public static boolean isRequestRunning = false;

    private int projectId;

    public EseoGetPosterOfProjectRequest(String requestId, boolean isShowLoadingDialog, Context context, Users appUser, int projectId) {
        super(requestId, isShowLoadingDialog, context);
        this.appUsers = appUser;
        this.projectId = projectId;
    }

    @Override
    protected void onSuccess(int responseCode, WebMessage webMessage) {
        try{
            Posters.updatePosterReceivedFromEseo(context, projectId, webMessage.getRawBody());
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
        Call call = ((EseoService)interfaceObject).getPosterOfProject(
                ProtocolVocabulary.PROCESS_GET_POSTER_PROJECT,
                appUsers.getUsername(),
                this.projectId,
                ProtocolVocabulary.QUERY_KEY_STYLE_VALUE_FLB64,
                appUsers.getToken()
        );
        LogUtils.d(LogUtils.DEBUG_TAG,"Call = "+call.request().url());
        return call;
    }

    @Override
    protected Converter.Factory getConverterFactory() {
        return ScalarsConverterFactory.create();
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
