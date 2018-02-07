package com.murey.poster.postermaster.communication.requests;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.murey.poster.postermaster.communication.message.WebMessage;
import com.murey.poster.postermaster.communication.requests.base.EseoRetrofitBaseOperation;
import com.murey.poster.postermaster.communication.requests.base.EseoRetrofitRequestObserver;
import com.murey.poster.postermaster.communication.requests.base.EseoRetrofitResponse;
import com.murey.poster.postermaster.communication.requests.protocole.ProtocolVocabulary;
import com.murey.poster.postermaster.model.DataStore;
import com.murey.poster.postermaster.model.database.AppDatabase;
import com.murey.poster.postermaster.model.database.tables.Posters;
import com.murey.poster.postermaster.model.database.tables.Users;
import com.murey.poster.postermaster.service.AppService;
import com.murey.poster.postermaster.utils.FileUtils;
import com.murey.poster.postermaster.utils.LogUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public abstract class EseoBaseRequest extends EseoRetrofitBaseOperation {

    boolean isReasking = false;
    protected Users appUsers;

    public EseoBaseRequest(String requestId, boolean isShowLoadingDialog, Context activity) {
        super(requestId, isShowLoadingDialog, activity);
    }

    public void startRequest() {
        LogUtils.d(LogUtils.DEBUG_TAG,"startTheRequest");
        Map<String, String> newMap = new HashMap<>();

        if(getHeaders() != null)
            newMap.putAll(getHeaders());

        Object interfaceObject= initRequest(getBaseUrl(), getServiceInterface(), newMap);
        call = createCall(interfaceObject);

        call.enqueue(this);
    }

    private boolean requestNeedToken(){
        if(requestId.equals(EseoAuthenticateUserRequest.REQUEST_AUTHENTICATE_USER)){
            return false;
        }else{
            return true;
        }
    }

    @Override
    protected Map<String, String> getHeaders() {
        return null;
    }

    @Override
    public void onResponse(Call call, Response response) {
        if(dialog != null) dialog.dismiss();
        LogUtils.d(LogUtils.DEBUG_TAG,"URL            ==> "+call.request().url());
        LogUtils.d(LogUtils.DEBUG_TAG,"Response Code  ==> "+response.code());
        LogUtils.d(LogUtils.DEBUG_TAG,"Body           ==> "+bodyToString(call));
        LogUtils.d(LogUtils.DEBUG_TAG,"Body STRING    ==> "+response.body().toString());

        EseoRetrofitResponse eseoRetrofitResponse;

        if(response.isSuccessful()) {
            if(!requestId.equals(EseoGetPosterOfProjectRequest.REQUEST_GET_POSTER_OF_PROJECT)){
                if(((WebMessage) response.body()).getResult().equals(ProtocolVocabulary.JSON_KEY_RESULT_VALUE_OK)){
                    onSuccess(response.code(),(WebMessage) response.body());
                    eseoRetrofitResponse = new EseoRetrofitResponse(
                            this,
                            response.code(),
                            response.message(),
                            (WebMessage)response.body()
                    );
                    for (EseoRetrofitRequestObserver requestObserver : requestObservers) {
                        requestObserver.handleRequestFinished(requestId, eseoRetrofitResponse);
                    }
                }else if(requestNeedToken() && !isReasking && appUsers != null){
                    startAuthenticateUserRequest(appUsers.getUsername(), appUsers.getPassword());
                }else{
                    eseoRetrofitResponse = new EseoRetrofitResponse(
                            this,
                            EseoRetrofitResponse.FAILURE_CODE,
                            response.message(),
                            (WebMessage) response.body()
                    );
                    for (EseoRetrofitRequestObserver requestObserver : requestObservers) {
                        requestObserver.handleRequestFinished(requestId, eseoRetrofitResponse);
                    }
                }
            }else{
                // Manage poster response
                try{
                    Gson gson = new Gson();
                    WebMessage posterWebMessage = gson.fromJson(response.body().toString(), WebMessage.class);
                    if(posterWebMessage.getResult().equals(ProtocolVocabulary.JSON_RESULT_VALUE_KO)){
                        if(!isReasking && appUsers != null){
                            startAuthenticateUserRequest(appUsers.getUsername(), appUsers.getPassword());
                        }else{
                            posterWebMessage = new WebMessage();
                            posterWebMessage.setRawBody(response.body().toString());
                            posterWebMessage.setResult(ProtocolVocabulary.JSON_KEY_RESULT_VALUE_KO);
                            eseoRetrofitResponse = new EseoRetrofitResponse(
                                    this,
                                    EseoRetrofitResponse.FAILURE_CODE,
                                    response.message(),
                                    posterWebMessage
                            );
                            for (EseoRetrofitRequestObserver requestObserver : requestObservers) {
                                requestObserver.handleRequestFinished(requestId, eseoRetrofitResponse);
                            }
                        }
                    }
                }catch(JsonSyntaxException e){
                    LogUtils.e(LogUtils.DEBUG_TAG, "Malformed json");
                    WebMessage posterWebMessage = new WebMessage();
                    LogUtils.d(LogUtils.DEBUG_TAG, "poster response " + response.body().toString());
                    posterWebMessage.setRawBody(response.body().toString());
                    posterWebMessage.setResult(ProtocolVocabulary.JSON_KEY_RESULT_VALUE_OK);
                    onSuccess(response.code(),posterWebMessage);
                    eseoRetrofitResponse = new EseoRetrofitResponse(
                            this,
                            response.code(),
                            response.message(),
                            posterWebMessage
                    );
                    for (EseoRetrofitRequestObserver requestObserver : requestObservers) {
                        requestObserver.handleRequestFinished(requestId, eseoRetrofitResponse);
                    }
                }
            }
        }else {
            LogUtils.d(LogUtils.DEBUG_TAG,
                    "BAD REQUEST ==> "+response.code()+
                            " message ==> "+response.message());
            if(response.errorBody()!=null){
                try {
                    String str = response.errorBody().string();
                    LogUtils.d(LogUtils.DEBUG_TAG, "ERROR BODY ==> \n" + str);
                    WebMessage webErrorMessage = null;
                    webErrorMessage = new Gson().fromJson(str, WebMessage.class);

                    if (webErrorMessage != null){
                        eseoRetrofitResponse = new EseoRetrofitResponse(
                                this,
                                EseoRetrofitResponse.FAILURE_CODE,
                                response.message(),
                                webErrorMessage
                        );
                    } else {
                        eseoRetrofitResponse = new EseoRetrofitResponse(
                                this,
                                EseoRetrofitResponse.FAILURE_CODE,
                                response.message()
                        );
                    }

                } catch (Exception e) {
                    LogUtils.e(LogUtils.DEBUG_TAG,"Exception error body ==> "+e.toString());
                    e.printStackTrace();
                    eseoRetrofitResponse = new EseoRetrofitResponse(
                            this,
                            EseoRetrofitResponse.FAILURE_CODE,
                            response.message()
                    );
                }
            }else{
                LogUtils.d(LogUtils.DEBUG_TAG, "NO  Error BODY !");
                eseoRetrofitResponse = new EseoRetrofitResponse(
                        this,
                        EseoRetrofitResponse.FAILURE_CODE,
                        response.message()
                );
            }
            for (EseoRetrofitRequestObserver requestObserver : requestObservers) {
                requestObserver.handleRequestFinished(requestId, eseoRetrofitResponse);
            }
        }
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        if(dialog != null){
            if(dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        EseoRetrofitResponse eseoRetrofitResponse = new EseoRetrofitResponse(this,t);
        for(EseoRetrofitRequestObserver requestObserver : requestObservers) {
            requestObserver.handleRequestFinished(requestId, eseoRetrofitResponse);
        }
        LogUtils.e(LogUtils.DEBUG_TAG,"onFailure",t);
    }


    private void startAuthenticateUserRequest(String username, String password){
        isReasking = true;
        EseoAuthenticateUserRequest request = new EseoAuthenticateUserRequest(
                EseoAuthenticateUserRequest.REQUEST_AUTHENTICATE_USER,
                false,
                context,
                username,
                password
        );

        request.addRequestObserver(EseoRequestHandler);
        request.startRequest();
    }

    EseoRetrofitRequestObserver EseoRequestHandler = new EseoRetrofitRequestObserver() {
        @Override
        public void handleRequestFinished(Object requestId, EseoRetrofitResponse eseoRetrofitResponse) {
            if(eseoRetrofitResponse.getCode() == EseoRetrofitResponse.FAILURE_CODE){
                onFailureReceived(eseoRetrofitResponse);
            }else if (eseoRetrofitResponse.getWebMessage() != null && eseoRetrofitResponse.getWebMessage().getResult().equals(ProtocolVocabulary.JSON_KEY_RESULT_VALUE_KO)) {
                onFailureReceived(eseoRetrofitResponse);
            }else{
                switch (requestId.toString()) {

                    case EseoAuthenticateUserRequest.REQUEST_AUTHENTICATE_USER:
                        onLoginSucceed(eseoRetrofitResponse);
                        break;

                }
            }
        }

        @Override
        public void requestCanceled(Object requestId, Throwable error) {
            onRequestCanceled();
        }
    };

    ////////////////////////////////
    //                            //
    // METHOD WHEN ERROR RECEIVED //
    //                            //
    ////////////////////////////////

    protected void onFailureReceived(EseoRetrofitResponse eseoRetrofitResponse){
        LogUtils.d(LogUtils.DEBUG_TAG,"Failure received");
        for (EseoRetrofitRequestObserver requestObserver : requestObservers) {
            requestObserver.handleRequestFinished(requestId, eseoRetrofitResponse);
        }
    }

    protected void onRequestCanceled(){
        LogUtils.d(LogUtils.DEBUG_TAG,"Request canceled");
    }

    //////////////////////////////////
    //                              //
    // METHOD WHEN MESSAGE RECEIVED //
    //                              //
    //////////////////////////////////

    protected void onLoginSucceed(EseoRetrofitResponse eseoRetrofitResponse){
        if(appUsers != null){
            appUsers.setToken(eseoRetrofitResponse.getWebMessage().getToken());
        }
        startRequest();
    }
}
