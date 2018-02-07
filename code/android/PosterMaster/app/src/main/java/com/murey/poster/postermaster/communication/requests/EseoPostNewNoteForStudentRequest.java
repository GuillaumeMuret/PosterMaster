package com.murey.poster.postermaster.communication.requests;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.murey.poster.postermaster.communication.message.WebMessage;
import com.murey.poster.postermaster.communication.message.WebMessageFactory;
import com.murey.poster.postermaster.communication.requests.protocole.ProtocolVocabulary;
import com.murey.poster.postermaster.model.database.AppDatabase;
import com.murey.poster.postermaster.model.database.tables.Projects;
import com.murey.poster.postermaster.model.database.tables.StudentMarks;
import com.murey.poster.postermaster.model.database.tables.Teams;
import com.murey.poster.postermaster.model.database.tables.Users;
import com.murey.poster.postermaster.utils.LogUtils;

import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class EseoPostNewNoteForStudentRequest extends EseoBaseRequest {

    public static final String REQUEST_POST_NEW_NOTE_FOR_STUDENT ="REQUEST_POST_NEW_NOTE_FOR_STUDENT";

    public static boolean isRequestRunning = false;

    private int projectId;
    private int studentId;
    private float note;

    public EseoPostNewNoteForStudentRequest(String requestId, boolean isShowLoadingDialog, Context context, Users appUser, int projectId, int studentId, float note) {
        super(requestId, isShowLoadingDialog, context);
        this.appUsers = appUser;
        this.projectId = projectId;
        this.studentId = studentId;
        this.note = note;
    }

    @Override
    protected void onSuccess(int responseCode, WebMessage webMessage) {
        try{
            if (webMessage.getResult().equals(ProtocolVocabulary.JSON_RESULT_VALUE_OK)) {
                StudentMarks studentMarks = AppDatabase.getInstance(context).studentMarksDao().getStudentMarkById(studentId);
                studentMarks.setMyMark(this.note);
                studentMarks.setNeedToSync(false);
                AppDatabase.getInstance(context).studentMarksDao().update(studentMarks);
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
        Call call = ((EseoService)interfaceObject).postNewNoteForStudent(
                ProtocolVocabulary.PROCESS_GIVE_NOTE_FOR_STUDENT,
                appUsers.getUsername(),
                this.projectId,
                this.studentId,
                this.note,
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
