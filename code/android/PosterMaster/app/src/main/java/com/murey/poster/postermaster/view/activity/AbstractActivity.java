package com.murey.poster.postermaster.view.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.murey.poster.postermaster.R;
import com.murey.poster.postermaster.communication.message.data.Jury;
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
import com.murey.poster.postermaster.communication.requests.base.EseoRetrofitResponse;
import com.murey.poster.postermaster.communication.requests.protocole.ProtocolVocabulary;
import com.murey.poster.postermaster.model.database.AppDatabase;
import com.murey.poster.postermaster.model.database.tables.Juries;
import com.murey.poster.postermaster.model.database.tables.JuryMembers;
import com.murey.poster.postermaster.model.database.tables.Projects;
import com.murey.poster.postermaster.model.database.tables.StudentMarks;
import com.murey.poster.postermaster.model.database.tables.Users;
import com.murey.poster.postermaster.service.AlarmReceiver;
import com.murey.poster.postermaster.service.AppService;
import com.murey.poster.postermaster.utils.DateUtils;
import com.murey.poster.postermaster.utils.DialogUtils;
import com.murey.poster.postermaster.utils.LogUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public abstract class AbstractActivity extends AppCompatActivity {

    protected boolean drawerOpen;
    protected HashMap<String, Parcelable> mapParams;

    private ServiceBroadcastReceiver serviceBroadcastReceiver;
    private boolean isRegistered = false;

    protected Users appUserAccount;

    protected MaterialDialog waitingDialog;
    protected MaterialDialog errorConnnectioDialog;

    private int counterSyncMark = 0;
    protected int counterNotesRequest = 0;

    /**
     * Process called at the creation of the context that extends this class
     *
     * @param savedInstanceState : the saved instance state
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBroadcastReceiver();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            // get model
            appUserAccount = extras.getParcelable(ProtocolVocabulary.BUNDLE_KEY_USER);

            mapParams = new HashMap<>();
            for (String key : extras.keySet()) {
                try {
                    LogUtils.d(LogUtils.DEBUG_TAG, "KEY => " + key + " VALUE => " + extras.get(key));
                    mapParams.put(key, (Parcelable) extras.get(key));
                } catch (ClassCastException e) {
                    LogUtils.e(LogUtils.DEBUG_TAG, "Exception in onCreate AbstractActivity => ", e);
                }
            }
        }

        if(appUserAccount == null){
            appUserAccount = AppDatabase.getInstance(this).usersDao().getAppUser();
        }
    }

    ///////////////////
    // ALARM MANAGER //
    ///////////////////

    private AlarmManager alarmManager;

    public void beginAlarmManager(Calendar calendar, Juries jury){
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if(alarmManager!=null){
            Intent myIntent = new Intent(this, AlarmReceiver.class);
            myIntent.putExtra(ProtocolVocabulary.BUNDLE_KEY_JURY,jury.getIdJury());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, jury.getIdJury(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            //alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
            Toast.makeText(this,"Alarme prévue pour le jury n°"+jury.getIdJury()+
                    " le "+ DateUtils.getFriendlyFromDateString(
                            DateUtils.getStringFromDate(calendar.getTime()) +
                    " à "+String.format("%s:%s",
                                    calendar.get(Calendar.HOUR)<10
                                            ?"0"+String.valueOf(calendar.get(Calendar.HOUR))
                                            :String.valueOf(calendar.get(Calendar.HOUR)),
                                    calendar.get(Calendar.MINUTE)<10
                                            ?"0"+String.valueOf(calendar.get(Calendar.MINUTE))
                                            :String.valueOf(calendar.get(Calendar.MINUTE))),
                    DateUtils.FORMAT_DATE_FROM_ESEO),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void setAlarm(Calendar calendar, Juries jury){
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if(alarmManager!=null) {
            Intent myIntent = new Intent(this, AlarmReceiver.class);
            myIntent.putExtra(ProtocolVocabulary.BUNDLE_KEY_JURY, jury.getIdJury());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, jury.getIdJury(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            //alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
            Toast.makeText(this,"Alarme prévue pour le jury n°"+jury.getIdJury()+
                            " le "+ DateUtils.getFriendlyFromDateString(
                    DateUtils.getStringFromDate(calendar.getTime()),DateUtils.FORMAT_DATE_POSTER_MASTER) +
                            " à "+String.format("%s:%s",
                            calendar.get(Calendar.HOUR_OF_DAY)<10
                                    ?"0"+String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))
                                    :String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)),
                            calendar.get(Calendar.MINUTE)<10
                                    ?"0"+String.valueOf(calendar.get(Calendar.MINUTE))
                                    :String.valueOf(calendar.get(Calendar.MINUTE))),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelAlarm(int juryId) {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if(alarmManager!=null) {
            Intent myIntent = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, juryId, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
        }
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

            // Broadcast when failed or success
            intentFilter.addAction(AppService.REQUEST_ACTIVITY_REQUEST_FAILED);
            intentFilter.addAction(AppService.REQUEST_ACTIVITY_REQUEST_CANCELED);

            // Broadcast when succeed
            intentFilter.addAction(AppService.REQUEST_ACTIVITY_AUTHENTICATE_USER_SUCCEED);
            intentFilter.addAction(AppService.REQUEST_ACTIVITY_GET_JURY_INFORMATION_SUCCEED);
            intentFilter.addAction(AppService.REQUEST_ACTIVITY_GET_JURY_LIST_SUCCEED);
            intentFilter.addAction(AppService.REQUEST_ACTIVITY_GET_MY_JURY_LIST_SUCCEED);
            intentFilter.addAction(AppService.REQUEST_ACTIVITY_GET_MY_PROJECT_LIST_SUCCEED);
            intentFilter.addAction(AppService.REQUEST_ACTIVITY_GET_NOTES_TEAM_MEMBER_SUCCEED);
            intentFilter.addAction(AppService.REQUEST_ACTIVITY_GET_POSTER_OF_PROJECT_SUCCEED);
            intentFilter.addAction(AppService.REQUEST_ACTIVITY_GET_PROJECT_INFORMATION_SUCCEED);
            intentFilter.addAction(AppService.REQUEST_ACTIVITY_GET_PROJECT_LIST_SUCCEED);
            intentFilter.addAction(AppService.REQUEST_ACTIVITY_GET_RANGE_PROJECT_LIST_SUCCEED);
            intentFilter.addAction(AppService.REQUEST_ACTIVITY_POST_NEW_NOTE_FOR_STUDENT_SUCCEED);

            this.registerReceiver(serviceBroadcastReceiver, intentFilter);
        }
    }

    public class ServiceBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtils.d(LogUtils.DEBUG_TAG, "Broadcast Receive => " + action);

            String message = null;
            if (intent.getExtras() != null && intent.getExtras().getBundle(ProtocolVocabulary.KEY_BUNDLE) != null) {
                message = intent.getExtras().getBundle(ProtocolVocabulary.KEY_BUNDLE).getString(ProtocolVocabulary.BUNDLE_KEY_MESSAGE);
            }
            if (intent.getAction() != null) {
                switch (intent.getAction()) {

                    case AppService.REQUEST_ACTIVITY_REQUEST_FAILED:
                        onRequestFailed(message);
                        break;

                    case AppService.REQUEST_ACTIVITY_REQUEST_CANCELED:
                        onRequestCanceled();
                        break;

                    case AppService.REQUEST_ACTIVITY_AUTHENTICATE_USER_SUCCEED:
                        onAuthenticateUserSucceed(message);
                        break;

                    case AppService.REQUEST_ACTIVITY_GET_JURY_INFORMATION_SUCCEED:
                        onGetJuryInformationSucceed(message);
                        break;

                    case AppService.REQUEST_ACTIVITY_GET_JURY_LIST_SUCCEED:
                        onGetJuryListSucceed(message);
                        break;

                    case AppService.REQUEST_ACTIVITY_GET_MY_JURY_LIST_SUCCEED:
                        onGetMyJuryListSucceed(message);
                        break;

                    case AppService.REQUEST_ACTIVITY_GET_MY_PROJECT_LIST_SUCCEED:
                        onGetMyProjectListSucceed(message);
                        break;

                    case AppService.REQUEST_ACTIVITY_GET_NOTES_TEAM_MEMBER_SUCCEED:
                        onGetNotesTeamMemberSucceed(message);
                        break;

                    case AppService.REQUEST_ACTIVITY_GET_POSTER_OF_PROJECT_SUCCEED:
                        onGetPosterOfProjectSucceed(message);
                        break;

                    case AppService.REQUEST_ACTIVITY_GET_PROJECT_INFORMATION_SUCCEED:
                        onGetProjectInformationSucceed(message);
                        break;

                    case AppService.REQUEST_ACTIVITY_GET_PROJECT_LIST_SUCCEED:
                        onGetProjectListSucceed(message);
                        break;

                    case AppService.REQUEST_ACTIVITY_GET_RANGE_PROJECT_LIST_SUCCEED:
                        onGetRangeProjectListSucceed(message);
                        break;

                    case AppService.REQUEST_ACTIVITY_POST_NEW_NOTE_FOR_STUDENT_SUCCEED:
                        onPostNewNoteForStudentSucceed(message);
                        break;

                }
            }
        }
    }

    private void sendServiceRequest(String requestAction, Bundle bundle) {
        Intent intent = new Intent();
        intent.putExtra(ProtocolVocabulary.KEY_BUNDLE, bundle);
        intent.setAction(requestAction);
        this.sendBroadcast(intent);
    }

    public void sendServiceRequestAuthenticateUser(String username, String password) {
        Bundle bundle = new Bundle();
        bundle.putString(ProtocolVocabulary.BUNDLE_KEY_USER, username);
        bundle.putString(ProtocolVocabulary.BUNDLE_KEY_PASSWORD, password);
        sendServiceRequest(AppService.REQUEST_SERVICE_AUTHENTICATE_USER, bundle);
    }

    public void sendServiceRequestGetJuryInformation(Users appUser, int juryId) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ProtocolVocabulary.BUNDLE_KEY_USER,appUser);
        bundle.putInt(ProtocolVocabulary.BUNDLE_KEY_JURY_ID,juryId);
        sendServiceRequest(AppService.REQUEST_SERVICE_GET_JURY_INFORMATION,bundle);
    }

    public void sendServiceRequestGetJuryList(Users appUser) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ProtocolVocabulary.BUNDLE_KEY_USER,appUser);
        sendServiceRequest(AppService.REQUEST_SERVICE_GET_JURY_LIST,bundle);
    }

    public void sendServiceRequestGetMyJuryList(Users appUser) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ProtocolVocabulary.BUNDLE_KEY_USER,appUser);
        sendServiceRequest(AppService.REQUEST_SERVICE_GET_MY_JURY_LIST,bundle);
    }

    public void sendServiceRequestGetMyProjectList(Users appUser) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ProtocolVocabulary.BUNDLE_KEY_USER,appUser);
        sendServiceRequest(AppService.REQUEST_SERVICE_GET_MY_PROJECT_LIST,bundle);
    }

    public void sendServiceRequestGetNotesTeamMember(Users appUser, int projectId) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ProtocolVocabulary.BUNDLE_KEY_USER,appUser);
        bundle.putInt(ProtocolVocabulary.BUNDLE_KEY_PROJECT_ID,projectId);
        sendServiceRequest(AppService.REQUEST_SERVICE_GET_NOTES_TEAM_MEMBER,bundle);
    }

    public void sendServiceRequestGetPosterOfProject(Users appUser, int projectId) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ProtocolVocabulary.BUNDLE_KEY_USER,appUser);
        bundle.putInt(ProtocolVocabulary.BUNDLE_KEY_PROJECT_ID,projectId);
        sendServiceRequest(AppService.REQUEST_SERVICE_GET_POSTER_OF_PROJECT,bundle);
    }

    public void sendServiceRequestGetProjectInformation(Users appUser, int projectId) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ProtocolVocabulary.BUNDLE_KEY_USER,appUser);
        bundle.putInt(ProtocolVocabulary.BUNDLE_KEY_PROJECT_ID,projectId);
        sendServiceRequest(AppService.REQUEST_SERVICE_GET_PROJECT_INFORMATION,bundle);
    }

    public void sendServiceRequestGetProjectList(Users appUser) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ProtocolVocabulary.BUNDLE_KEY_USER, appUser);
        sendServiceRequest(AppService.REQUEST_SERVICE_GET_PROJECT_LIST,bundle);
    }

    public void sendServiceRequestGetRangeProjectList(Users appUser) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ProtocolVocabulary.BUNDLE_KEY_USER,appUser);
        sendServiceRequest(AppService.REQUEST_SERVICE_GET_RANGE_PROJECT_LIST,bundle);
    }

    public void sendServiceRequestPostNewNoteForStudent(Users appUser, int projectId, int studentId, float studentNote) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ProtocolVocabulary.BUNDLE_KEY_USER,appUser);
        bundle.putInt(ProtocolVocabulary.BUNDLE_KEY_PROJECT_ID,projectId);
        bundle.putInt(ProtocolVocabulary.BUNDLE_KEY_STUDENT_ID,studentId);
        bundle.putFloat(ProtocolVocabulary.BUNDLE_KEY_STUDENT_NOTE,studentNote);
        sendServiceRequest(AppService.REQUEST_SERVICE_POST_NEW_NOTE_FOR_STUDENT,bundle);
    }

    ///////////////////////////////
    // ANDROID LIFE CYCLE METHOD //
    ///////////////////////////////

    /**
     * Process called by the user to finish the context
     */
    @Override
    public void finish() {
        super.finish();
        unregisterBroadcastReceiver();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        drawerOpen = false;
    }

    /**
     * Process called when an context will be started. This process override the super
     * class in order to set the animation of the transition
     *
     * @param intent      : the intent to change the context
     * @param requestCode : the request code of the context created
     */
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    /**
     * Process called to finish the context with a result and params
     *
     * @param resultCode : the result code of the context that will be destroyed
     */
    protected void finishWithResult(int resultCode, HashMap<String, Parcelable> mapParams) {
        Intent intent = new Intent();
        if (mapParams != null) {
            for (String key : mapParams.keySet()) {
                intent.putExtra(key, mapParams.get(key));
            }
        }
        intent.putExtra(ProtocolVocabulary.BUNDLE_KEY_USER, appUserAccount);
        setResult(resultCode, intent);
        finish();
    }

    /**
     * Process called to finish the context with a result
     *
     * @param resultCode : the result code of the context that will be destroyed
     */
    protected void finishWithResult(int resultCode) {
        Intent intent = new Intent();
        setResult(resultCode, intent);
        finish();
    }

    /**
     * Process called when the user click on the back of his device
     */
    @Override
    public void onBackPressed() {
        if (drawerOpen) {
            closeDrawer();
        } else {
            DialogUtils.displayDialogSureToQuitApp(this);
        }
    }

    protected void syncAllMarks(){
        List<StudentMarks> unsyncMarks = AppDatabase.getInstance(this).studentMarksDao().getUnsyncStudentMarks(true);
        counterSyncMark = unsyncMarks.size();

        this.waitingDialog = DialogUtils.getWaitingDialog(this, false);
        this.waitingDialog.show();

        if(counterSyncMark>0){
            for(int i =0;i<unsyncMarks.size();i++){
                sendServiceRequestPostNewNoteForStudent(
                        appUserAccount,
                        AppDatabase.getInstance(this).teamsDao().getUserProjectId(unsyncMarks.get(i).getIdStudent()),
                        unsyncMarks.get(i).getIdStudent(),
                        unsyncMarks.get(i).getMyMarkToSynchronize()
                );
            }
        }else{
            counterNotesRequest = 0;

            // In this case the user is a jury member
            List<Integer> myJuriesIdList = AppDatabase.getInstance(this).juryMembersDao().getMyJuriesIdList(JuryMembers.MY_JURY_ID);
            ArrayList<Juries> juriesArrayList = new ArrayList<>();
            ArrayList<Projects> juryProjectsArrayList = new ArrayList<>();

            if(myJuriesIdList.size()>0){
                for(int i=0;i<myJuriesIdList.size();i++){
                    juriesArrayList.add(AppDatabase.getInstance(this).juriesDao().getJuryById(myJuriesIdList.get(i)));
                }
                for (int i = 0; i < juriesArrayList.size(); i++) {
                    List<Integer> projectsIdFromJuryIdList = AppDatabase.getInstance(this).juryProjectsDao().getProjectsIdFromJuryId(juriesArrayList.get(i).getIdJury());
                    for(int j=0;j<projectsIdFromJuryIdList.size();j++){
                        juryProjectsArrayList.add(AppDatabase.getInstance(this).projectsDao().getProjectById(projectsIdFromJuryIdList.get(j)));
                    }
                    counterNotesRequest += juryProjectsArrayList.size();
                    for(int j=0;j<juryProjectsArrayList.size();j++){
                        sendServiceRequestGetNotesTeamMember(appUserAccount, juryProjectsArrayList.get(j).getIdProject());
                    }
                }
            } else {
                this.waitingDialog.dismiss();
            }
        }
    }

    //////////////////////////////
    // CALL FROM DIALOG METHODS //
    //////////////////////////////

    /**
     * Process called to display the login activity
     */
    public void disconnectUser() {
        AppDatabase.getInstance(this).deleteAll();
        finishWithResult(ServiceActivity.DISPLAY_ACTIVITY_LOGIN);
    }

    /**
     * Process called to quit the application with the right result code
     */
    public void quitApplication() {
        finishWithResult(RESULT_OK);
    }

    public void retrySendingRequest(){

    }

    ///////////////////////
    // OVERWRITE METHODS //
    ///////////////////////

    /**
     * Process called to close the drawer of the context
     */
    protected void closeDrawer() {

    }

    ///////////////////////
    // NETWORK OVERWRITE //
    ///////////////////////

    protected void onRequestFailed(String message) {
        LogUtils.d(LogUtils.DEBUG_TAG, "Message From ESEO Server => " + message);
    }

    protected void onRequestCanceled() {
    }

    protected void onAuthenticateUserSucceed(String message) {
        LogUtils.d(LogUtils.DEBUG_TAG, "Message From ESEO Server => " + message);
    }

    protected void onGetJuryInformationSucceed(String message) {
        LogUtils.d(LogUtils.DEBUG_TAG, "Message From ESEO Server => " + message);
    }

    protected void onGetJuryListSucceed(String message) {
        LogUtils.d(LogUtils.DEBUG_TAG, "Message From ESEO Server => " + message);
    }

    protected void onGetMyJuryListSucceed(String message) {
        LogUtils.d(LogUtils.DEBUG_TAG, "Message From ESEO Server => " + message);
    }

    protected void onGetMyProjectListSucceed(String message) {
        LogUtils.d(LogUtils.DEBUG_TAG, "Message From ESEO Server => " + message);
    }

    protected void onGetNotesTeamMemberSucceed(String message) {
        LogUtils.d(LogUtils.DEBUG_TAG, "Message From ESEO Server => " + message);
        counterNotesRequest--;
        if(counterNotesRequest == 0){
            if(this.waitingDialog!=null && this.waitingDialog.isShowing()){
                this.waitingDialog.dismiss();
            }
        }
    }

    protected void onGetPosterOfProjectSucceed(String message) {
        LogUtils.d(LogUtils.DEBUG_TAG, "Message From ESEO Server => " + message);
    }

    protected void onGetProjectInformationSucceed(String message) {
        LogUtils.d(LogUtils.DEBUG_TAG, "Message From ESEO Server => " + message);
    }

    protected void onGetProjectListSucceed(String message) {
        LogUtils.d(LogUtils.DEBUG_TAG, "Message From ESEO Server => " + message);
    }

    protected void onGetRangeProjectListSucceed(String message) {
        LogUtils.d(LogUtils.DEBUG_TAG, "Message From ESEO Server => " + message);
    }

    protected void onPostNewNoteForStudentSucceed(String message) {
        LogUtils.d(LogUtils.DEBUG_TAG, "Message From ESEO Server => " + message);
        counterSyncMark--;
        if(counterSyncMark == 0){
            this.waitingDialog.dismiss();
            counterNotesRequest = 0;

            // In this case the user is a jury member
            List<Integer> myJuriesIdList = AppDatabase.getInstance(this).juryMembersDao().getMyJuriesIdList(JuryMembers.MY_JURY_ID);
            ArrayList<Juries> juriesArrayList = new ArrayList<>();
            ArrayList<Projects> juryProjectsArrayList = new ArrayList<>();

            if(myJuriesIdList.size()>0){
                for(int i=0;i<myJuriesIdList.size();i++){
                    juriesArrayList.add(AppDatabase.getInstance(this).juriesDao().getJuryById(myJuriesIdList.get(i)));
                }
                for (int i = 0; i < juriesArrayList.size(); i++) {
                    List<Integer> projectsIdFromJuryIdList = AppDatabase.getInstance(this).juryProjectsDao().getProjectsIdFromJuryId(juriesArrayList.get(i).getIdJury());
                    for(int j=0;j<projectsIdFromJuryIdList.size();j++){
                        juryProjectsArrayList.add(AppDatabase.getInstance(this).projectsDao().getProjectById(projectsIdFromJuryIdList.get(j)));
                    }
                    counterNotesRequest += juryProjectsArrayList.size();
                    for(int j=0;j<juryProjectsArrayList.size();j++){
                        sendServiceRequestGetNotesTeamMember(appUserAccount, juryProjectsArrayList.get(j).getIdProject());
                    }
                }
            } else {
                this.waitingDialog.dismiss();
            }
        }
    }
}