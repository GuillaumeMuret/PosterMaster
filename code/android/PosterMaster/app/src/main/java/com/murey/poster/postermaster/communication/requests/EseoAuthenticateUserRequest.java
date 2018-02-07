package com.murey.poster.postermaster.communication.requests;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.murey.poster.postermaster.communication.message.WebMessage;
import com.murey.poster.postermaster.communication.message.WebMessageFactory;
import com.murey.poster.postermaster.communication.requests.protocole.ProtocolVocabulary;
import com.murey.poster.postermaster.model.DataStore;
import com.murey.poster.postermaster.model.database.AppDatabase;
import com.murey.poster.postermaster.model.database.tables.Users;
import com.murey.poster.postermaster.utils.FileUtils;
import com.murey.poster.postermaster.utils.LogUtils;

import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class EseoAuthenticateUserRequest extends EseoBaseRequest {

    public static final String REQUEST_AUTHENTICATE_USER ="REQUEST_AUTHENTICATE_USER";

    public static boolean isRequestRunning = false;

    private String username;
    private String password;

    public EseoAuthenticateUserRequest(String requestId, boolean isShowLoadingDialog, Context context, String username, String password) {
        super(requestId, isShowLoadingDialog, context);
        this.username = username;
        this.password = password;
    }

    @Override
    protected void onSuccess(int responseCode, WebMessage webMessage) {
        try{
            if (webMessage.getResult().equals(ProtocolVocabulary.JSON_RESULT_VALUE_OK)) {
                Users users = AppDatabase.getInstance(context).usersDao().getAppUser();
                if(AppDatabase.getInstance(context).usersDao().getAppUser() == null){
                    users = new Users(Users.APP_USER_ID,-1,username,password,webMessage.getToken());
                    AppDatabase.getInstance(context).usersDao().insertAll(users);
                }else{
                    users.setToken(webMessage.getToken());
                    AppDatabase.getInstance(context).usersDao().updateUsers(users);
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
        Call call = ((EseoService)interfaceObject).authenticateUser(
                ProtocolVocabulary.PROCESS_AUTHENTICATE_USER,
                username,
                password
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
