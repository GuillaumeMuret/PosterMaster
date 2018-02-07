package com.murey.poster.postermaster.view.activity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.murey.poster.postermaster.R;
import com.murey.poster.postermaster.model.database.AppDatabase;
import com.murey.poster.postermaster.model.database.tables.Juries;
import com.murey.poster.postermaster.model.database.tables.JuryMembers;
import com.murey.poster.postermaster.model.database.tables.Projects;
import com.murey.poster.postermaster.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AbstractActivity {

    private EditText etLoginUsername;
    private EditText etLoginPassword;
    private TextView tvMessageError;

    private View layoutConnectionInProgress;
    private ProgressBar pcLoginAuth;

    private Button btnLoginAction;
    private Button btnLoginActionVisitor;

    private boolean isModeVisitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLoginUsername = (EditText) findViewById(R.id.etLoginUsername);
        etLoginPassword = (EditText) findViewById(R.id.etLoginPassword);
        tvMessageError = (TextView) findViewById(R.id.tvMessageError);

        layoutConnectionInProgress = findViewById(R.id.layoutConnectionInProgress);
        pcLoginAuth = (ProgressBar) findViewById(R.id.pcLoginAuth);
        btnLoginAction = (Button) findViewById(R.id.btnLoginAction);
        btnLoginActionVisitor = (Button) findViewById(R.id.btnLoginActionVisitor);

        tvMessageError.setVisibility(View.INVISIBLE);
        initView();
        initListener();
    }

    private void initView() {
        layoutConnectionInProgress.setVisibility(View.INVISIBLE);
        pcLoginAuth.getIndeterminateDrawable().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
        btnLoginAction.setVisibility(View.VISIBLE);

        btnLoginActionVisitor .setVisibility(View.VISIBLE);
    }

    private void initListener() {
        btnLoginAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isModeVisitor = false;
                btnLoginAction.setVisibility(View.INVISIBLE);
                btnLoginActionVisitor.setVisibility(View.INVISIBLE);
                layoutConnectionInProgress.setVisibility(View.VISIBLE);

                sendServiceRequestAuthenticateUser(etLoginUsername.getText().toString(),etLoginPassword.getText().toString());
            }
        });

        btnLoginActionVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isModeVisitor = true;
                btnLoginAction.setVisibility(View.INVISIBLE);
                btnLoginActionVisitor.setVisibility(View.INVISIBLE);
                layoutConnectionInProgress.setVisibility(View.VISIBLE);

                sendServiceRequestAuthenticateUser(etLoginUsername.getText().toString(),etLoginPassword.getText().toString());
            }
        });
    }

    ///////////////////////
    // NETWORK OVERWRITE //
    ///////////////////////

    @Override
    protected void onRequestCanceled() {
        super.onRequestCanceled();
        initView();
        tvMessageError.setVisibility(View.VISIBLE);
        tvMessageError.setText(this.getResources().getString(R.string.activity_login_request_canceled));
    }

    @Override
    protected void onRequestFailed(String message) {
        super.onRequestFailed(message);
        initView();
        tvMessageError.setVisibility(View.VISIBLE);
        tvMessageError.setText(this.getResources().getString(R.string.activity_login_request_failed));
        AppDatabase.getInstance(this).deleteAll();
    }

    @Override
    protected void onAuthenticateUserSucceed(String message) {
        super.onAuthenticateUserSucceed(message);
        this.appUserAccount = AppDatabase.getInstance(this).usersDao().getAppUser();
        this.appUserAccount.setModeVisitor(isModeVisitor);
        this.appUserAccount.setMandatoryDataReceived(false);
        AppDatabase.getInstance(this).usersDao().updateUsers(this.appUserAccount);
        sendServiceRequestGetProjectList(appUserAccount);
    }

    @Override
    protected void onGetProjectListSucceed(String message) {
        super.onGetProjectListSucceed(message);
        sendServiceRequestGetJuryList(appUserAccount);
    }

    @Override
    protected void onGetJuryListSucceed(String message) {
        super.onGetJuryListSucceed(message);
        if(appUserAccount.isModeVisitor()){
            sendServiceRequestGetRangeProjectList(appUserAccount);
        }else{
            sendServiceRequestGetMyJuryList(appUserAccount);
        }
    }

    @Override
    protected void onGetMyJuryListSucceed(String message) {
        super.onGetMyJuryListSucceed(message);
        sendServiceRequestGetMyProjectList(appUserAccount);
    }

    @Override
    protected void onGetMyProjectListSucceed(String message) {
        super.onGetMyProjectListSucceed(message);

        counterNotesRequest = 0;

        // In this case the user is a jury member
        List<Integer> myJuriesIdList = AppDatabase.getInstance(this).juryMembersDao().getMyJuriesIdList(JuryMembers.MY_JURY_ID);
        ArrayList<Juries> juriesArrayList = new ArrayList<>();
        ArrayList<Projects> juryProjectsArrayList = new ArrayList<>();

        if(myJuriesIdList.size()>0 && !appUserAccount.isModeVisitor()){
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
            finishWhenAllDataReceived();
        }
    }

    @Override
    protected void onGetNotesTeamMemberSucceed(String message) {
        counterNotesRequest--;
        if(counterNotesRequest == 0){
            finishWhenAllDataReceived();
        }
    }

    @Override
    protected void onGetRangeProjectListSucceed(String message) {
        super.onGetRangeProjectListSucceed(message);
        finishWhenAllDataReceived();
    }

    private void finishWhenAllDataReceived(){
        this.appUserAccount.setMandatoryDataReceived(true);
        AppDatabase.getInstance(this).usersDao().updateUsers(this.appUserAccount);
        finishWithResult(ServiceActivity.DISPLAY_ACTIVITY_PROJECT_LIST, mapParams);
    }
}
