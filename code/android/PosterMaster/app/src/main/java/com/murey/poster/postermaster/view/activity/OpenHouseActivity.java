package com.murey.poster.postermaster.view.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.murey.poster.postermaster.R;
import com.murey.poster.postermaster.communication.message.WebMessage;
import com.murey.poster.postermaster.communication.requests.protocole.ProtocolVocabulary;
import com.murey.poster.postermaster.model.RootScreen;
import com.murey.poster.postermaster.model.database.AppDatabase;
import com.murey.poster.postermaster.model.database.tables.Juries;
import com.murey.poster.postermaster.model.database.tables.JuryMembers;
import com.murey.poster.postermaster.model.database.tables.JuryProjects;
import com.murey.poster.postermaster.model.database.tables.Projects;
import com.murey.poster.postermaster.model.database.tables.Supervisors;
import com.murey.poster.postermaster.utils.DialogUtils;
import com.murey.poster.postermaster.utils.LogUtils;
import com.murey.poster.postermaster.view.adapter.JuriesListAdapter;
import com.murey.poster.postermaster.view.adapter.ProjectListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OpenHouseActivity extends AbstractActivityWithToolbar {

    private JuriesListAdapter juriesListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.inflateLayout(3, R.layout.activity_juries_list,(ViewGroup) findViewById(R.id.activity_project_list));

        Bundle data = getIntent().getExtras();
        Juries selectedJury = null;
        try {
            selectedJury = (Juries) data.getParcelable(ProtocolVocabulary.BUNDLE_KEY_JURY);
        }catch (NullPointerException npe){
            LogUtils.e(LogUtils.DEBUG_TAG, "No selected jury");
        }

        RecyclerView recycler = (RecyclerView) findViewById(R.id.cardList);
        recycler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(llm);
        juriesListAdapter = new JuriesListAdapter(this);
        recycler.setAdapter(juriesListAdapter);
        loadOpenHouseData();
        if(selectedJury != null){
            recycler.smoothScrollToPosition(juriesListAdapter.getPositionOfItem(selectedJury));
        }
    }

    @Override
    protected void onGetRangeProjectListSucceed(String message) {
        super.onGetRangeProjectListSucceed(message);
        if(waitingDialog != null && waitingDialog.isShowing()){
            waitingDialog.dismiss();
            loadOpenHouseData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_open_house, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_get_new_jury:
                // Remove open house jury project
                List<JuryProjects> juryProjects = AppDatabase.getInstance(this).juryProjectsDao().getAll();
                for(int i=0;i<juryProjects.size();i++){
                    if(juryProjects.get(i).getIdJury() == JuryMembers.JURY_ID_OPEN_HOUSE){
                        AppDatabase.getInstance(this).juryProjectsDao().delete(juryProjects.get(i));
                    }
                }
                sendServiceRequestGetRangeProjectList(appUserAccount);
                waitingDialog = DialogUtils.getWaitingDialog(this, false);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void loadOpenHouseData() {
        List<JuryProjects> juryProjects = AppDatabase.getInstance(this).juryProjectsDao().getAll();
        ArrayList<Integer> openHouseJuryList = new ArrayList<>();

        boolean containOpenHouseJury = false;
        for(int i=0;i<juryProjects.size();i++){
            if(juryProjects.get(i).getIdJury() == JuryMembers.JURY_ID_OPEN_HOUSE){
                containOpenHouseJury = true;
                openHouseJuryList.add(juryProjects.get(i).getIdJury());
            }
        }
        if(containOpenHouseJury){
            ArrayList<Juries> juriesList = new ArrayList<>();
            juriesList.add(AppDatabase.getInstance(this).juriesDao().getJuryById(JuryMembers.JURY_ID_OPEN_HOUSE));
            juriesListAdapter.setJuriesList(juriesList);
        }else{
            sendServiceRequestGetRangeProjectList(appUserAccount);
            waitingDialog = DialogUtils.getWaitingDialog(this, false);
        }
    }

    public void clickProjectItemInJury(Juries juries, Projects projects) {
        mapParams = new HashMap<String, Parcelable>();
        mapParams.put(ProtocolVocabulary.BUNDLE_KEY_PROJECT, projects);
        mapParams.put(ProtocolVocabulary.BUNDLE_KEY_JURY, juries);
        mapParams.put(ProtocolVocabulary.BUNDLE_KEY_ROOT_SCREEN, new RootScreen(ServiceActivity.DISPLAY_ACTIVITY_OPEN_HOUSE));
        finishWithResult(ServiceActivity.DISPLAY_ACTIVITY_PROJECT_DETAILS, mapParams);
    }

    @Override
    protected void onRequestFailed(String message) {
        if(this.waitingDialog != null && this.waitingDialog.isShowing()){
            this.waitingDialog.dismiss();
        }
        if(message != null && message.equals(WebMessage.NON_VALID_USER)){
            this.errorConnnectioDialog = DialogUtils.displayDialogNonValidUser(this);
            this.errorConnnectioDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finishWithResult(ServiceActivity.DISPLAY_ACTIVITY_PROJECT_LIST);
                }
            });
        }else{
            if(this.errorConnnectioDialog == null || !this.errorConnnectioDialog.isShowing()){
                this.errorConnnectioDialog = DialogUtils.displayDialogErrorConnection(this);
            }
        }
    }
}
