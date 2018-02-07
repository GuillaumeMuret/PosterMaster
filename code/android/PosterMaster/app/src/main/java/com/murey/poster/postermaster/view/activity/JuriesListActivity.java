package com.murey.poster.postermaster.view.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.murey.poster.postermaster.R;
import com.murey.poster.postermaster.communication.requests.protocole.ProtocolVocabulary;
import com.murey.poster.postermaster.model.RootScreen;
import com.murey.poster.postermaster.model.database.AppDatabase;
import com.murey.poster.postermaster.model.database.tables.Juries;
import com.murey.poster.postermaster.model.database.tables.JuryMembers;
import com.murey.poster.postermaster.model.database.tables.Projects;
import com.murey.poster.postermaster.utils.DialogUtils;
import com.murey.poster.postermaster.utils.LogUtils;
import com.murey.poster.postermaster.view.adapter.JuriesListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class JuriesListActivity extends AbstractActivityWithToolbar {

    private JuriesListAdapter juriesListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.inflateLayout(2,R.layout.activity_juries_list,(ViewGroup) findViewById(R.id.activity_juries_list));

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
        loadAllJuriesData();
        if(selectedJury != null){
            recycler.smoothScrollToPosition(juriesListAdapter.getPositionOfItem(selectedJury));
        }
    }

    private void loadAllJuriesData() {
        juriesListAdapter.setJuriesList(AppDatabase.getInstance(this).juriesDao().getAll());
    }

    private void loadMyJuriesData() {
        List<Integer> myJuriesIdList = AppDatabase.getInstance(this).juryMembersDao().getMyJuriesIdList(JuryMembers.MY_JURY_ID);
        ArrayList<Juries> juriesArrayList = new ArrayList<>();

        if(myJuriesIdList.size()>0){
            for(int i=0;i<myJuriesIdList.size();i++){
                juriesArrayList.add(AppDatabase.getInstance(this).juriesDao().getJuryById(myJuriesIdList.get(i)));
            }
        }
        LogUtils.d(LogUtils.DEBUG_TAG,"size my Jury => " + juriesArrayList.size());
        juriesListAdapter.setJuriesList(juriesArrayList);
    }

    public void clickProjectItemInJury(Juries juries, Projects projects) {
        mapParams = new HashMap<String, Parcelable>();
        mapParams.put(ProtocolVocabulary.BUNDLE_KEY_PROJECT, projects);
        mapParams.put(ProtocolVocabulary.BUNDLE_KEY_JURY, juries);
        mapParams.put(ProtocolVocabulary.BUNDLE_KEY_ROOT_SCREEN, new RootScreen(ServiceActivity.DISPLAY_ACTIVITY_JURIES_LIST));
        finishWithResult(ServiceActivity.DISPLAY_ACTIVITY_PROJECT_DETAILS, mapParams);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        List<Integer> myJuriesIdList = AppDatabase.getInstance(this).juryMembersDao().getMyJuriesIdList(JuryMembers.MY_JURY_ID);

        // In this case the user is a jury member
        if(myJuriesIdList.size()>0){
            getMenuInflater().inflate(R.menu.menu_activity_juries_list, menu);
        }
        // When user is not a supervisor nothing to display on app bar
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_my_juries:
                loadMyJuriesData();
                return true;

            case R.id.action_all_juries:
                loadAllJuriesData();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRequestFailed(String message) {
        if(this.waitingDialog != null && this.waitingDialog.isShowing()){
            this.waitingDialog.dismiss();
        }        if(this.errorConnnectioDialog == null || !this.errorConnnectioDialog.isShowing()){
            this.errorConnnectioDialog = DialogUtils.displayDialogErrorConnection(this);
        }
    }
}
