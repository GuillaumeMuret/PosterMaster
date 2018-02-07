package com.murey.poster.postermaster.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.murey.poster.postermaster.communication.requests.EseoBaseRequest;
import com.murey.poster.postermaster.communication.requests.EseoAuthenticateUserRequest;
import com.murey.poster.postermaster.communication.requests.EseoGetJuryInformationRequest;
import com.murey.poster.postermaster.communication.requests.EseoGetJuryListRequest;
import com.murey.poster.postermaster.communication.requests.EseoGetMyJuryListRequest;
import com.murey.poster.postermaster.communication.requests.EseoGetMyProjectListRequest;
import com.murey.poster.postermaster.communication.requests.EseoGetNotesTeamMemberRequest;
import com.murey.poster.postermaster.communication.requests.EseoGetPosterOfProjectRequest;
import com.murey.poster.postermaster.communication.requests.EseoGetProjectInformationRequest;
import com.murey.poster.postermaster.communication.requests.EseoGetProjectListRequest;
import com.murey.poster.postermaster.communication.requests.EseoGetRangeProjectListRequest;
import com.murey.poster.postermaster.communication.requests.EseoPostNewNoteForStudentRequest;
import com.murey.poster.postermaster.communication.requests.base.EseoRetrofitRequestObserver;
import com.murey.poster.postermaster.communication.requests.base.EseoRetrofitResponse;
import com.murey.poster.postermaster.communication.requests.protocole.ProtocolVocabulary;
import com.murey.poster.postermaster.model.database.tables.Users;
import com.murey.poster.postermaster.utils.LogUtils;

import java.util.ArrayList;

public class AppService extends Service {

    // Request canceled or failed
    public static final String REQUEST_ACTIVITY_REQUEST_FAILED = "REQUEST_ACTIVITY_REQUEST_FAILED";
    public static final String REQUEST_ACTIVITY_REQUEST_CANCELED = "REQUEST_ACTIVITY_REQUEST_CANCELED";

    // Request Activity succeed
    public static final String REQUEST_ACTIVITY_AUTHENTICATE_USER_SUCCEED = "REQUEST_ACTIVITY_AUTHENTICATE_USER_SUCCEED";
    public static final String REQUEST_ACTIVITY_GET_JURY_INFORMATION_SUCCEED = "REQUEST_ACTIVITY_GET_JURY_INFORMATION_SUCCEED";
    public static final String REQUEST_ACTIVITY_GET_JURY_LIST_SUCCEED = "REQUEST_ACTIVITY_GET_JURY_LIST_SUCCEED";
    public static final String REQUEST_ACTIVITY_GET_MY_JURY_LIST_SUCCEED = "REQUEST_ACTIVITY_GET_MY_JURY_LIST_SUCCEED";
    public static final String REQUEST_ACTIVITY_GET_MY_PROJECT_LIST_SUCCEED = "REQUEST_ACTIVITY_GET_MY_PROJECT_LIST_SUCCEED";
    public static final String REQUEST_ACTIVITY_GET_NOTES_TEAM_MEMBER_SUCCEED = "REQUEST_ACTIVITY_GET_NOTES_TEAM_MEMBER_SUCCEED";
    public static final String REQUEST_ACTIVITY_GET_POSTER_OF_PROJECT_SUCCEED = "REQUEST_ACTIVITY_GET_POSTER_OF_PROJECT_SUCCEED";
    public static final String REQUEST_ACTIVITY_GET_PROJECT_INFORMATION_SUCCEED = "REQUEST_ACTIVITY_GET_PROJECT_INFORMATION_SUCCEED";
    public static final String REQUEST_ACTIVITY_GET_PROJECT_LIST_SUCCEED = "REQUEST_ACTIVITY_GET_PROJECT_LIST_SUCCEED";
    public static final String REQUEST_ACTIVITY_GET_RANGE_PROJECT_LIST_SUCCEED = "REQUEST_ACTIVITY_GET_RANGE_PROJECT_LIST_SUCCEED";
    public static final String REQUEST_ACTIVITY_POST_NEW_NOTE_FOR_STUDENT_SUCCEED = "REQUEST_ACTIVITY_POST_NEW_NOTE_FOR_STUDENT_SUCCEED";

    // Request Service succeed
    public static final String REQUEST_SERVICE_AUTHENTICATE_USER = "REQUEST_SERVICE_AUTHENTICATE_USER";
    public static final String REQUEST_SERVICE_GET_JURY_INFORMATION = "REQUEST_SERVICE_GET_JURY_INFORMATION";
    public static final String REQUEST_SERVICE_GET_JURY_LIST = "REQUEST_SERVICE_GET_JURY_LIST";
    public static final String REQUEST_SERVICE_GET_MY_JURY_LIST = "REQUEST_SERVICE_GET_MY_JURY_LIST";
    public static final String REQUEST_SERVICE_GET_MY_PROJECT_LIST = "REQUEST_SERVICE_GET_MY_PROJECT_LIST";
    public static final String REQUEST_SERVICE_GET_NOTES_TEAM_MEMBER = "REQUEST_SERVICE_GET_NOTES_TEAM_MEMBER";
    public static final String REQUEST_SERVICE_GET_POSTER_OF_PROJECT = "REQUEST_SERVICE_GET_POSTER_OF_PROJECT";
    public static final String REQUEST_SERVICE_GET_PROJECT_INFORMATION = "REQUEST_SERVICE_GET_PROJECT_INFORMATION";
    public static final String REQUEST_SERVICE_GET_PROJECT_LIST = "REQUEST_SERVICE_GET_PROJECT_LIST";
    public static final String REQUEST_SERVICE_GET_RANGE_PROJECT_LIST = "REQUEST_SERVICE_GET_RANGE_PROJECT_LIST";
    public static final String REQUEST_SERVICE_POST_NEW_NOTE_FOR_STUDENT = "REQUEST_SERVICE_POST_NEW_NOTE_FOR_STUDENT";

    private AppService.ServiceBroadcastReceiver serviceBroadcastReceiver;

    private boolean isRegistered = false;
    private boolean isReasking = false;

    private ArrayList<EseoBaseRequest> requestList;

    @Override
    public void onCreate() {
        super.onCreate();
        registerBroadcastReceiver();
        requestList = new ArrayList<>();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterBroadcastReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //////////////////////////
    // BROADCAST MANAGEMENT //
    //////////////////////////

    protected void unregisterBroadcastReceiver() {
        if (serviceBroadcastReceiver != null && isRegistered) {
            isRegistered = false;
            this.unregisterReceiver(serviceBroadcastReceiver);
        }
    }

    protected void registerBroadcastReceiver() {
        if (!isRegistered) {
            isRegistered = true;
            serviceBroadcastReceiver = new ServiceBroadcastReceiver();

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(AppService.REQUEST_SERVICE_AUTHENTICATE_USER);
            intentFilter.addAction(AppService.REQUEST_SERVICE_GET_JURY_INFORMATION);
            intentFilter.addAction(AppService.REQUEST_SERVICE_GET_JURY_LIST);
            intentFilter.addAction(AppService.REQUEST_SERVICE_GET_MY_JURY_LIST);
            intentFilter.addAction(AppService.REQUEST_SERVICE_GET_MY_PROJECT_LIST);
            intentFilter.addAction(AppService.REQUEST_SERVICE_GET_NOTES_TEAM_MEMBER);
            intentFilter.addAction(AppService.REQUEST_SERVICE_GET_POSTER_OF_PROJECT);
            intentFilter.addAction(AppService.REQUEST_SERVICE_GET_PROJECT_INFORMATION);
            intentFilter.addAction(AppService.REQUEST_SERVICE_GET_PROJECT_LIST);
            intentFilter.addAction(AppService.REQUEST_SERVICE_GET_RANGE_PROJECT_LIST);
            intentFilter.addAction(AppService.REQUEST_SERVICE_POST_NEW_NOTE_FOR_STUDENT);

            this.registerReceiver(serviceBroadcastReceiver, intentFilter);
        }
    }

    public class ServiceBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtils.d(LogUtils.DEBUG_TAG, "Broadcast Receive => " + action);
            if (intent.getAction() != null) {
                switch (intent.getAction()) {

                    case AppService.REQUEST_SERVICE_AUTHENTICATE_USER:
                        startAuthenticateUserRequest(intent.getExtras().getBundle(ProtocolVocabulary.KEY_BUNDLE));
                        break;

                    case AppService.REQUEST_SERVICE_GET_JURY_INFORMATION:
                        startGetJuryInformationRequest(intent.getExtras().getBundle(ProtocolVocabulary.KEY_BUNDLE));
                        break;

                    case AppService.REQUEST_SERVICE_GET_JURY_LIST:
                        startGetJuryListRequest(intent.getExtras().getBundle(ProtocolVocabulary.KEY_BUNDLE));
                        break;

                    case AppService.REQUEST_SERVICE_GET_MY_JURY_LIST:
                        startGetMyJuryListRequest(intent.getExtras().getBundle(ProtocolVocabulary.KEY_BUNDLE));
                        break;

                    case AppService.REQUEST_SERVICE_GET_MY_PROJECT_LIST:
                        startGetMyProjectListRequest(intent.getExtras().getBundle(ProtocolVocabulary.KEY_BUNDLE));
                        break;

                    case AppService.REQUEST_SERVICE_GET_NOTES_TEAM_MEMBER:
                        startGetNotesTeamMemberRequest(intent.getExtras().getBundle(ProtocolVocabulary.KEY_BUNDLE));
                        break;

                    case AppService.REQUEST_SERVICE_GET_POSTER_OF_PROJECT:
                        startGetPosterOfProjectRequest(intent.getExtras().getBundle(ProtocolVocabulary.KEY_BUNDLE));
                        break;

                    case AppService.REQUEST_SERVICE_GET_PROJECT_INFORMATION:
                        startGetProjectInformationRequest(intent.getExtras().getBundle(ProtocolVocabulary.KEY_BUNDLE));
                        break;

                    case AppService.REQUEST_SERVICE_GET_PROJECT_LIST:
                        startGetProjectListRequest(intent.getExtras().getBundle(ProtocolVocabulary.KEY_BUNDLE));
                        break;

                    case AppService.REQUEST_SERVICE_GET_RANGE_PROJECT_LIST:
                        startGetRangeProjectListRequest(intent.getExtras().getBundle(ProtocolVocabulary.KEY_BUNDLE));
                        break;

                    case AppService.REQUEST_SERVICE_POST_NEW_NOTE_FOR_STUDENT:
                        startPostNewNoteForStudentRequest(intent.getExtras().getBundle(ProtocolVocabulary.KEY_BUNDLE));
                        break;

                }
            }
        }
    }

    public void sendActivityRequest(String requestAction, Bundle bundle) {
        Intent intent = new Intent();
        intent.setAction(requestAction);
        intent.putExtra(ProtocolVocabulary.KEY_BUNDLE, bundle);
        this.sendBroadcast(intent);
    }


    /////////////////////////////////////////
    // REQUEST MANAGEMENT WITH ESEO SERVER //
    /////////////////////////////////////////

    private void startAuthenticateUserRequest(Bundle bundle) {
        EseoAuthenticateUserRequest request = new EseoAuthenticateUserRequest(
                EseoAuthenticateUserRequest.REQUEST_AUTHENTICATE_USER,
                false,
                this,
                bundle.getString(ProtocolVocabulary.BUNDLE_KEY_USER),
                bundle.getString(ProtocolVocabulary.BUNDLE_KEY_PASSWORD)
        );

        request.addRequestObserver(EseoRequestHandler);
        requestList.add(request);
        request.startRequest();
    }

    private void startGetJuryInformationRequest(Bundle bundle) {
        EseoGetJuryInformationRequest request = new EseoGetJuryInformationRequest(
                EseoGetJuryInformationRequest.REQUEST_GET_JURY_INFORMATION,
                false,
                this,
                (Users) bundle.getParcelable(ProtocolVocabulary.BUNDLE_KEY_USER),
                bundle.getInt(ProtocolVocabulary.BUNDLE_KEY_JURY_ID)
        );
        request.addRequestObserver(EseoRequestHandler);
        requestList.add(request);
        request.startRequest();
    }

    private void startGetJuryListRequest(Bundle bundle) {
        EseoGetJuryListRequest request = new EseoGetJuryListRequest(
                EseoGetJuryListRequest.REQUEST_GET_JURY_LIST,
                false,
                this,
                (Users) bundle.getParcelable(ProtocolVocabulary.BUNDLE_KEY_USER)
        );
        request.addRequestObserver(EseoRequestHandler);
        requestList.add(request);
        request.startRequest();
    }

    private void startGetMyJuryListRequest(Bundle bundle) {
        EseoGetMyJuryListRequest request = new EseoGetMyJuryListRequest(
                EseoGetMyJuryListRequest.REQUEST_GET_MY_JURY_LIST,
                false,
                this,
                (Users) bundle.getParcelable(ProtocolVocabulary.BUNDLE_KEY_USER)
        );
        request.addRequestObserver(EseoRequestHandler);
        requestList.add(request);
        request.startRequest();
    }

    private void startGetMyProjectListRequest(Bundle bundle) {
        EseoGetMyProjectListRequest request = new EseoGetMyProjectListRequest(
                EseoGetMyProjectListRequest.REQUEST_GET_MY_PROJECT_LIST,
                false,
                this,
                (Users) bundle.getParcelable(ProtocolVocabulary.BUNDLE_KEY_USER)
        );
        request.addRequestObserver(EseoRequestHandler);
        requestList.add(request);
        request.startRequest();
    }

    private void startGetNotesTeamMemberRequest(Bundle bundle) {
        EseoGetNotesTeamMemberRequest request = new EseoGetNotesTeamMemberRequest(
                EseoGetNotesTeamMemberRequest.REQUEST_GET_NOTES_TEAM_MEMBER,
                false,
                this,
                (Users) bundle.getParcelable(ProtocolVocabulary.BUNDLE_KEY_USER),
                bundle.getInt(ProtocolVocabulary.BUNDLE_KEY_PROJECT_ID)
        );
        request.addRequestObserver(EseoRequestHandler);
        requestList.add(request);
        request.startRequest();
    }

    private void startGetPosterOfProjectRequest(Bundle bundle) {
        EseoGetPosterOfProjectRequest request = new EseoGetPosterOfProjectRequest(
                EseoGetPosterOfProjectRequest.REQUEST_GET_POSTER_OF_PROJECT,
                false,
                this,
                (Users) bundle.getParcelable(ProtocolVocabulary.BUNDLE_KEY_USER),
                bundle.getInt(ProtocolVocabulary.BUNDLE_KEY_PROJECT_ID)
        );
        request.addRequestObserver(EseoRequestHandler);
        requestList.add(request);
        request.startRequest();
    }

    private void startGetProjectInformationRequest(Bundle bundle) {
        EseoGetProjectInformationRequest request = new EseoGetProjectInformationRequest(
                EseoGetProjectInformationRequest.REQUEST_GET_PROJECT_INFORMATION,
                false,
                this,
                (Users) bundle.getParcelable(ProtocolVocabulary.BUNDLE_KEY_USER),
                bundle.getInt(ProtocolVocabulary.BUNDLE_KEY_PROJECT_ID)
        );
        request.addRequestObserver(EseoRequestHandler);
        requestList.add(request);
        request.startRequest();
    }

    private void startGetProjectListRequest(Bundle bundle) {
        EseoGetProjectListRequest request = new EseoGetProjectListRequest(
                EseoGetProjectListRequest.REQUEST_GET_PROJECT_LIST,
                false,
                this,
                (Users) bundle.getParcelable(ProtocolVocabulary.BUNDLE_KEY_USER)
        );

        request.addRequestObserver(EseoRequestHandler);
        requestList.add(request);
        request.startRequest();
    }

    private void startGetRangeProjectListRequest(Bundle bundle) {
        EseoGetRangeProjectListRequest request = new EseoGetRangeProjectListRequest(
                EseoGetRangeProjectListRequest.REQUEST_GET_RANGE_PROJECT_LIST,
                false,
                this,
                (Users) bundle.getParcelable(ProtocolVocabulary.BUNDLE_KEY_USER),
                bundle.getInt(ProtocolVocabulary.BUNDLE_KEY_PROJECT_ID)
        );

        request.addRequestObserver(EseoRequestHandler);
        requestList.add(request);
        request.startRequest();
    }

    private void startPostNewNoteForStudentRequest(Bundle bundle) {
        EseoPostNewNoteForStudentRequest request = new EseoPostNewNoteForStudentRequest(
                EseoPostNewNoteForStudentRequest.REQUEST_POST_NEW_NOTE_FOR_STUDENT,
                false,
                this,
                (Users) bundle.getParcelable(ProtocolVocabulary.BUNDLE_KEY_USER),
                bundle.getInt(ProtocolVocabulary.BUNDLE_KEY_PROJECT_ID),
                bundle.getInt(ProtocolVocabulary.BUNDLE_KEY_STUDENT_ID),
                bundle.getFloat(ProtocolVocabulary.BUNDLE_KEY_STUDENT_NOTE)
        );

        request.addRequestObserver(EseoRequestHandler);
        requestList.add(request);
        request.startRequest();
    }

    EseoRetrofitRequestObserver EseoRequestHandler = new EseoRetrofitRequestObserver() {
        @Override
        public void handleRequestFinished(Object requestId, EseoRetrofitResponse eseoRetrofitResponse) {
            requestList.remove(eseoRetrofitResponse.getEseoBaseRequest());
            if (eseoRetrofitResponse.getCode() == EseoRetrofitResponse.FAILURE_CODE) {
                onFailureReceived(eseoRetrofitResponse);
            } else if (eseoRetrofitResponse.getWebMessage() != null && eseoRetrofitResponse.getWebMessage().getResult().equals(ProtocolVocabulary.JSON_KEY_RESULT_VALUE_KO)) {
                onFailureReceived(eseoRetrofitResponse);
            } else {
                isReasking = false;
                switch (requestId.toString()) {

                    case EseoAuthenticateUserRequest.REQUEST_AUTHENTICATE_USER:
                        onAuthenticateUserSucceed(eseoRetrofitResponse);
                        break;

                    case EseoGetJuryInformationRequest.REQUEST_GET_JURY_INFORMATION:
                        onGetJuryInformationSucceed(eseoRetrofitResponse);
                        break;

                    case EseoGetJuryListRequest.REQUEST_GET_JURY_LIST:
                        onGetJuryListSucceed(eseoRetrofitResponse);
                        break;

                    case EseoGetMyJuryListRequest.REQUEST_GET_MY_JURY_LIST:
                        onGetMyJuryListSucceed(eseoRetrofitResponse);
                        break;

                    case EseoGetMyProjectListRequest.REQUEST_GET_MY_PROJECT_LIST:
                        onGetMyProjectListSucceed(eseoRetrofitResponse);
                        break;

                    case EseoGetNotesTeamMemberRequest.REQUEST_GET_NOTES_TEAM_MEMBER:
                        onGetNotesTeamMemberSucceed(eseoRetrofitResponse);
                        break;

                    case EseoGetPosterOfProjectRequest.REQUEST_GET_POSTER_OF_PROJECT:
                        onGetPosterOfProjectSucceed(eseoRetrofitResponse);
                        break;

                    case EseoGetProjectInformationRequest.REQUEST_GET_PROJECT_INFORMATION:
                        onGetProjectInformationSucceed(eseoRetrofitResponse);
                        break;

                    case EseoGetProjectListRequest.REQUEST_GET_PROJECT_LIST:
                        onGetProjectListSucceed(eseoRetrofitResponse);
                        break;

                    case EseoGetRangeProjectListRequest.REQUEST_GET_RANGE_PROJECT_LIST:
                        onGetRangeProjectListSucceed(eseoRetrofitResponse);
                        break;

                    case EseoPostNewNoteForStudentRequest.REQUEST_POST_NEW_NOTE_FOR_STUDENT:
                        onPostNewNoteForStudentSucceed(eseoRetrofitResponse);
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

    protected void onFailureReceived(EseoRetrofitResponse eseoRetrofitResponse) {
        LogUtils.d(LogUtils.DEBUG_TAG, "Failure received");
        Bundle bundle = new Bundle();
        if (eseoRetrofitResponse.getWebMessage() != null && eseoRetrofitResponse.getWebMessage().getError() != null) {
            bundle.putString(ProtocolVocabulary.BUNDLE_KEY_MESSAGE, eseoRetrofitResponse.getWebMessage().getError());
        }
        sendActivityRequest(AppService.REQUEST_ACTIVITY_REQUEST_FAILED, bundle);
    }

    protected void onRequestCanceled() {
        LogUtils.d(LogUtils.DEBUG_TAG, "Request canceled");
        sendActivityRequest(AppService.REQUEST_ACTIVITY_REQUEST_CANCELED, new Bundle());
    }

    //////////////////////////////////
    //                              //
    // METHOD WHEN MESSAGE RECEIVED //
    //                              //
    //////////////////////////////////

    protected void onAuthenticateUserSucceed(EseoRetrofitResponse eseoRetrofitResponse) {
        sendActivityRequest(REQUEST_ACTIVITY_AUTHENTICATE_USER_SUCCEED, new Bundle());
    }

    protected void onGetJuryInformationSucceed(EseoRetrofitResponse eseoRetrofitResponse) {
        sendActivityRequest(REQUEST_ACTIVITY_GET_JURY_INFORMATION_SUCCEED, new Bundle());
    }

    protected void onGetJuryListSucceed(EseoRetrofitResponse eseoRetrofitResponse) {
        sendActivityRequest(REQUEST_ACTIVITY_GET_JURY_LIST_SUCCEED, new Bundle());
    }

    protected void onGetMyJuryListSucceed(EseoRetrofitResponse eseoRetrofitResponse) {
        sendActivityRequest(REQUEST_ACTIVITY_GET_MY_JURY_LIST_SUCCEED, new Bundle());
    }

    protected void onGetMyProjectListSucceed(EseoRetrofitResponse eseoRetrofitResponse) {
        sendActivityRequest(REQUEST_ACTIVITY_GET_MY_PROJECT_LIST_SUCCEED, new Bundle());
    }

    protected void onGetNotesTeamMemberSucceed(EseoRetrofitResponse eseoRetrofitResponse) {
        sendActivityRequest(REQUEST_ACTIVITY_GET_NOTES_TEAM_MEMBER_SUCCEED, new Bundle());
    }

    protected void onGetPosterOfProjectSucceed(EseoRetrofitResponse eseoRetrofitResponse) {
        sendActivityRequest(REQUEST_ACTIVITY_GET_POSTER_OF_PROJECT_SUCCEED, new Bundle());
    }

    protected void onGetProjectInformationSucceed(EseoRetrofitResponse eseoRetrofitResponse) {
        sendActivityRequest(REQUEST_ACTIVITY_GET_PROJECT_INFORMATION_SUCCEED, new Bundle());
    }

    protected void onGetProjectListSucceed(EseoRetrofitResponse eseoRetrofitResponse) {
        sendActivityRequest(REQUEST_ACTIVITY_GET_PROJECT_LIST_SUCCEED, new Bundle());
    }

    protected void onGetRangeProjectListSucceed(EseoRetrofitResponse eseoRetrofitResponse) {
        sendActivityRequest(REQUEST_ACTIVITY_GET_RANGE_PROJECT_LIST_SUCCEED, new Bundle());
    }

    protected void onPostNewNoteForStudentSucceed(EseoRetrofitResponse eseoRetrofitResponse) {
        sendActivityRequest(REQUEST_ACTIVITY_POST_NEW_NOTE_FOR_STUDENT_SUCCEED, new Bundle());
    }
}