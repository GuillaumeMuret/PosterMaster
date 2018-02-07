package com.murey.poster.postermaster.view.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.murey.poster.postermaster.R;
import com.murey.poster.postermaster.communication.message.data.Project;
import com.murey.poster.postermaster.communication.message.data.Supervisor;
import com.murey.poster.postermaster.communication.requests.protocole.ProtocolVocabulary;
import com.murey.poster.postermaster.model.RootScreen;
import com.murey.poster.postermaster.model.database.AppDatabase;
import com.murey.poster.postermaster.model.database.tables.Juries;
import com.murey.poster.postermaster.model.database.tables.Projects;
import com.murey.poster.postermaster.model.database.tables.Supervisors;
import com.murey.poster.postermaster.model.database.tables.Teams;
import com.murey.poster.postermaster.model.database.tables.Users;
import com.murey.poster.postermaster.utils.DialogUtils;
import com.murey.poster.postermaster.utils.LogUtils;
import com.murey.poster.postermaster.view.adapter.ProjectListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProjectListActivity extends AbstractActivityWithToolbar {

    private ProjectListAdapter projectListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.inflateLayout(1,R.layout.activity_project_list,(ViewGroup) findViewById(R.id.activity_project_list));

        Bundle data = getIntent().getExtras();
        Projects selectedProject = null;
        try {
            selectedProject = (Projects) data.getParcelable(ProtocolVocabulary.BUNDLE_KEY_PROJECT);
        }catch (NullPointerException npe){
            LogUtils.e(LogUtils.DEBUG_TAG, "No selected project");
        }

        RecyclerView recycler = (RecyclerView) findViewById(R.id.cardList);
        recycler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(llm);
        projectListAdapter = new ProjectListAdapter(this);
        recycler.setAdapter(projectListAdapter);
        loadAllProjectsData();

        if(selectedProject != null){
            recycler.smoothScrollToPosition(projectListAdapter.getPositionOfItem(selectedProject));
        }
    }

    private void loadAllProjectsData() {
        projectListAdapter.setProjectList(AppDatabase.getInstance(this).projectsDao().getAll());
    }

    private void loadMyProjectsData() {
        List<Supervisors> supervisorList = AppDatabase.getInstance(this).supervisorsDao().getAll();
        ArrayList<Projects> projectArrayList = new ArrayList<>();

        if(supervisorList.size()>0){
            for(int i=0;i<supervisorList.size();i++){
                projectArrayList.add(AppDatabase.getInstance(this).projectsDao().getProjectById(supervisorList.get(i).getIdProject()));
            }
        }
        projectListAdapter.setProjectList(projectArrayList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        List<Supervisors> supervisorList = AppDatabase.getInstance(this).supervisorsDao().getAll();

        // In this case the user is a supervisor
        if(supervisorList.size()>0){
            getMenuInflater().inflate(R.menu.menu_activity_project_list, menu);
        }
        // When user is not a supervisor nothing to display on app bar
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_my_projects:
                loadMyProjectsData();
                return true;

            case R.id.action_all_projects:
                loadAllProjectsData();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickItem(Projects project) {
        mapParams = new HashMap<String, Parcelable>();
        mapParams.put(ProtocolVocabulary.BUNDLE_KEY_PROJECT, project);
        mapParams.put(ProtocolVocabulary.BUNDLE_KEY_ROOT_SCREEN, new RootScreen(ServiceActivity.DISPLAY_ACTIVITY_PROJECT_LIST));
        finishWithResult(ServiceActivity.DISPLAY_ACTIVITY_PROJECT_DETAILS, mapParams);
    }

    @Override
    protected void onRequestFailed(String message) {
        if(this.waitingDialog != null && this.waitingDialog.isShowing()){
            this.waitingDialog.dismiss();
        }
        if(this.errorConnnectioDialog == null || !this.errorConnnectioDialog.isShowing()){
            this.errorConnnectioDialog = DialogUtils.displayDialogErrorConnection(this);
        }
    }
}