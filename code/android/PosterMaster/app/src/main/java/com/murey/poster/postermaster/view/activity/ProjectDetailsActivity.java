package com.murey.poster.postermaster.view.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.murey.poster.postermaster.R;
import com.murey.poster.postermaster.communication.requests.protocole.ProtocolVocabulary;
import com.murey.poster.postermaster.model.RootScreen;
import com.murey.poster.postermaster.model.database.AppDatabase;
import com.murey.poster.postermaster.model.database.tables.Juries;
import com.murey.poster.postermaster.model.database.tables.JuryMembers;
import com.murey.poster.postermaster.model.database.tables.Posters;
import com.murey.poster.postermaster.model.database.tables.Projects;
import com.murey.poster.postermaster.model.database.tables.StudentMarks;
import com.murey.poster.postermaster.model.database.tables.Teams;
import com.murey.poster.postermaster.model.database.tables.Users;
import com.murey.poster.postermaster.utils.DialogUtils;
import com.murey.poster.postermaster.utils.FileUtils;
import com.murey.poster.postermaster.utils.LogUtils;
import com.murey.poster.postermaster.utils.PermissionUtils;
import com.murey.poster.postermaster.view.adapter.ProjectTeamAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProjectDetailsActivity extends AbstractActivity implements TextWatcher {

    private static final int REQUEST_CODE_PHOTO_VIEWER = 2;

    private Toolbar toolbar;
    private TextView tvProjectTitle;
    private TextView tvProjectId;
    private TextView tvProjectConfidentiality;
    private TextView tvProjectPosterEnable;
    private TextView tvProjectSupervisor;
    private ImageView imgPoster;
    private View layoutAllPoster;
    private View layoutAverageNote;
    private View layoutDescription;
    private EditText etPosterNotes;
    private View layoutDownloadPoster;
    private View layoutImagePoster;
    private ProjectTeamAdapter projectTeamAdapter;
    private Projects project;
    private TextView tvProjectDescription;
    private TextView tvProjectAvgNote;
    private int rootScreen;

    private boolean isProjectDescriptionExpand = false;

    private Posters projectPoster;

    private AbstractActivity me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = this;

        setContentView(R.layout.activity_project_details);
        Bundle data = getIntent().getExtras();
        project = (Projects) data.getParcelable(ProtocolVocabulary.BUNDLE_KEY_PROJECT);
        RootScreen myRootScreen = (RootScreen) data.getParcelable(ProtocolVocabulary.BUNDLE_KEY_ROOT_SCREEN);
        rootScreen = myRootScreen.getRootScreen();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        layoutDownloadPoster = (View) findViewById(R.id.layout_download_poster);
        layoutImagePoster = (View) findViewById(R.id.layout_image_poster);
        layoutAllPoster = (View) findViewById(R.id.layout_all_poster);
        layoutAverageNote = (View) findViewById(R.id.layout_average_note);
        layoutDescription = (View) findViewById(R.id.layoutDescription);
        imgPoster = (ImageView) findViewById(R.id.img_poster);
        etPosterNotes = (EditText) findViewById(R.id.etPosterNotes);
        tvProjectTitle = (TextView) findViewById(R.id.tv_details_project_title);
        tvProjectId = (TextView) findViewById(R.id.tv_project_id);
        tvProjectConfidentiality = (TextView) findViewById(R.id.tv_project_confidentiality);
        tvProjectPosterEnable = (TextView) findViewById(R.id.tv_project_poster_enable);
        tvProjectDescription = (TextView) findViewById(R.id.tv_details_project_description);
        tvProjectSupervisor = (TextView) findViewById(R.id.tv_details_supervisor);
        tvProjectAvgNote = (TextView) findViewById(R.id.tv_details_average_note);

        RecyclerView recList = (RecyclerView) findViewById(R.id.project_details_students);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setHasFixedSize(false);
        recList.setLayoutManager(llm);
        recList.setNestedScrollingEnabled(false);
        projectTeamAdapter = new ProjectTeamAdapter(this, project.getIdProject(), this.appUserAccount);
        recList.setAdapter(projectTeamAdapter);

        // Init view
        tvProjectTitle.setText(project.getTitle());
        tvProjectId.setText(String.valueOf(project.getIdProject()));
        tvProjectConfidentiality.setText(String.valueOf(project.getIdConfidentiality()));
        tvProjectPosterEnable.setText(project.isPosterEnable() ? "Oui" : "Non");
        tvProjectSupervisor.setText(String.format("%s %s", project.getSupervisorForename(), project.getSupervisorSurname()));
        tvProjectDescription.setText(project.getDescription());
        retractViewProjectDescription();

        initActionBar();
        initListeners();
        loadProjectDetails();
    }

    private void initActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            /*
            actionBar.setTitle(
                    currentPlayer.getFirstName()
                            + " "
                            + currentPlayer.getLastName()
            );
            */
        }
    }

    private View.OnClickListener photoViewer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                if(isUriOk(projectPoster.getUriPoster())){
                    startViewPoster();
                }else{
                    projectPoster.setUriPoster(FileUtils.saveInSharedDirectory(me,FileUtils.getBitmapFromFilename(me,projectPoster.getFilename())));
                    AppDatabase.getInstance(me).postersDao().update(projectPoster);
                    startViewPoster();
                }
            }catch(Exception e){
                LogUtils.e(LogUtils.DEBUG_TAG, "Exception poster : ",e);
            }
        }
    };

    private boolean isUriOk(String contentUri){
        boolean isUriOk = true;
        ContentResolver cr = getContentResolver();
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cur = cr.query(Uri.parse(contentUri), projection, null, null, null);
        if (cur != null) {
            if (cur.moveToFirst()) {
                String filePath = cur.getString(0);

                if (new File(filePath).exists()) {
                    LogUtils.d(LogUtils.DEBUG_TAG," URI => do something if it exists ");
                    isUriOk = true;
                } else {
                    LogUtils.d(LogUtils.DEBUG_TAG," URI => File was not found");
                    isUriOk = false;
                }
            } else {
                LogUtils.d(LogUtils.DEBUG_TAG," URI => Uri was ok but no entry found.");
                isUriOk = false;
            }
            cur.close();
        } else {
            LogUtils.d(LogUtils.DEBUG_TAG," URI => content Uri was invalid or some other error occurred");
            isUriOk = false;
        }
        return isUriOk;
    }

    private void startViewPoster(){
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(projectPoster.getUriPoster()), "image/png");
        startActivityForResult(intent,REQUEST_CODE_PHOTO_VIEWER);
    }

    public void expandViewProjectDescription() {
        this.tvProjectDescription.setMaxLines(500);
    }

    public void retractViewProjectDescription() {
        this.tvProjectDescription.setEllipsize(TextUtils.TruncateAt.END);
        this.tvProjectDescription.setMaxLines(1);
    }

    private void initListeners(){
        layoutDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isProjectDescriptionExpand){
                    retractViewProjectDescription();
                }else{
                    expandViewProjectDescription();
                }
                isProjectDescriptionExpand = !isProjectDescriptionExpand;
            }
        });
        if(project.isPosterEnable() && project.getDescription()!=null && !project.getDescription().equals(Projects.PROJECTS_DESCRIPTION_CONFIDENTIAL)){
            etPosterNotes.addTextChangedListener(this);
            layoutAllPoster.setVisibility(View.VISIBLE);
            setLayoutFromPoster();
        }else if(project.isPosterEnable() && appUserAccount.isModeVisitor()) {
            etPosterNotes.addTextChangedListener(this);
            layoutAllPoster.setVisibility(View.VISIBLE);
            setLayoutFromPoster();
        }else{
            layoutAllPoster.setVisibility(View.GONE);
        }
    }

    private void setLayoutFromPoster(){
        final AbstractActivity me = this;
        this.projectPoster = AppDatabase.getInstance(this).postersDao().getPosterByProjectId(project.getIdProject());
        if(!projectPoster.getFilename().equals("")){
            layoutImagePoster.setVisibility(View.VISIBLE);
            layoutDownloadPoster.setVisibility(View.GONE);

            FileUtils.setImageBitmapFromFile(imgPoster,projectPoster.getFilename());
            imgPoster.setOnClickListener(photoViewer);
        }else{
            layoutImagePoster.setVisibility(View.GONE);
            layoutDownloadPoster.setVisibility(View.VISIBLE);
            layoutDownloadPoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(PermissionUtils.isStoragePermissionEnable(me)){
                        waitingDialog = DialogUtils.getWaitingDialog(me, false);
                        waitingDialog.show();
                        sendServiceRequestGetPosterOfProject(appUserAccount, project.getIdProject());
                    }
                }
            });
        }
        etPosterNotes.setText(projectPoster.getUserNotes());
    }

    private ArrayList<Users> getProjectTeam(){
        List<Teams> teamsList =  AppDatabase.getInstance(this).teamsDao().getProjectTeam(project.getIdProject());
        ArrayList<Users> projectTeam = new ArrayList<>();
        for(int i=0;i<teamsList.size();i++){
            projectTeam.add(AppDatabase.getInstance(this).usersDao().getUserByEseoId(teamsList.get(i).getIdMember()));
        }
        return projectTeam;
    }

    private void loadProjectDetails() {
        ArrayList<Users> projectTeam = getProjectTeam();

        // In this case the user is a jury member
        List<Integer> myJuriesIdList = AppDatabase.getInstance(this).juryMembersDao().getMyJuriesIdList(JuryMembers.MY_JURY_ID);
        ArrayList<Juries> juriesArrayList = new ArrayList<>();
        List<Integer> myJuryProjectsId = new ArrayList<>();

        if(myJuriesIdList.size()>0){
            for(int i=0;i<myJuriesIdList.size();i++){
                juriesArrayList.add(AppDatabase.getInstance(this).juriesDao().getJuryById(myJuriesIdList.get(i)));
            }
            for (int i = 0; i < juriesArrayList.size(); i++) {
                myJuryProjectsId.addAll(AppDatabase.getInstance(this).juryProjectsDao().getProjectsIdFromJuryId(juriesArrayList.get(i).getIdJury()));
            }
        }
        projectTeamAdapter.setProjectUsers(projectTeam, myJuryProjectsId);

        if(myJuryProjectsId.contains(project.getIdProject())){
            displayAverageGrade();
        } else{
            layoutAverageNote.setVisibility(View.GONE);
        }
    }

    private void displayAverageGrade(){
        ArrayList<Users> projectTeam = getProjectTeam();
        float projectAvgNote;
        float projectSumAverage = 0;
        boolean isAverageEnable = true;
        for(int i=0;i<projectTeam.size();i++){
            StudentMarks studentMark = AppDatabase.getInstance(this).studentMarksDao().getStudentMarkById(projectTeam.get(i).getIdServerEseoUser());
            if(studentMark == null || studentMark.getAverageMark() < 0){
                isAverageEnable = false;
            }
        }
        if(isAverageEnable){
            for(int i=0;i<projectTeam.size();i++){
                StudentMarks studentMark = AppDatabase.getInstance(this).studentMarksDao().getStudentMarkById(projectTeam.get(i).getIdServerEseoUser());
                if(studentMark != null && studentMark.getAverageMark() >= 0){
                    projectSumAverage += studentMark.getAverageMark();
                }
            }
            projectAvgNote = projectSumAverage/projectTeam.size();
            this.tvProjectAvgNote.setText(String.format(" %s /20", projectAvgNote));
        }else{
            this.tvProjectAvgNote.setText(" - / 20");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // In this case the user is a jury member
        List<Integer> myJuriesIdList = AppDatabase.getInstance(this).juryMembersDao().getMyJuriesIdList(JuryMembers.MY_JURY_ID);
        ArrayList<Juries> juriesArrayList = new ArrayList<>();
        List<Integer> myJuryProjectsId = new ArrayList<>();

        if(myJuriesIdList.size()>0){
            for(int i=0;i<myJuriesIdList.size();i++){
                juriesArrayList.add(AppDatabase.getInstance(this).juriesDao().getJuryById(myJuriesIdList.get(i)));
            }
            for (int i = 0; i < juriesArrayList.size(); i++) {
                myJuryProjectsId.addAll(AppDatabase.getInstance(this).juryProjectsDao().getProjectsIdFromJuryId(juriesArrayList.get(i).getIdJury()));
            }
        }
        if(myJuryProjectsId.contains(project.getIdProject()) && !appUserAccount.isModeVisitor()){
            getMenuInflater().inflate(R.menu.menu_activity_project_details, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_sync_all:
                syncAllMarks();
                return true;

            case android.R.id.home:
                onBackPressed();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        projectPoster.setUserNotes(etPosterNotes.getText().toString());
        AppDatabase.getInstance(me).postersDao().update(projectPoster);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.d(LogUtils.DEBUG_TAG, "request code = " + requestCode + " result code => " + resultCode);
    }

    @Override
    public void onBackPressed() {
        finishWithResult(rootScreen, mapParams);
    }

    @Override
    public void retrySendingRequest() {
        super.retrySendingRequest();
    }

    public void newNoteFromDialog(Float newNote, int projectId, int studentId){
        if(newNote != AppDatabase.getInstance(this).studentMarksDao().getStudentMarkById(studentId).getMyMark()){
            this.waitingDialog = DialogUtils.getWaitingDialog(this, false);
            this.waitingDialog.show();

            StudentMarks studentMarks = AppDatabase.getInstance(this).studentMarksDao().getStudentMarkById(studentId);
            studentMarks.setNeedToSync(true);
            studentMarks.setMyMarkToSynchronize(newNote);
            AppDatabase.getInstance(this).studentMarksDao().update(studentMarks);

            sendServiceRequestPostNewNoteForStudent(appUserAccount,projectId,studentId,newNote);
        }else{
            LogUtils.d(LogUtils.DEBUG_TAG,"Nothing to do since the note == the note in database");
            // nothing to do
        }
    }

    public void newNoteFromVisitorDialog(Float newNote, int projectId, int studentId){
        if(newNote != AppDatabase.getInstance(this).studentMarksDao().getStudentMarkById(studentId).getMyMark()){
            StudentMarks studentMarks = AppDatabase.getInstance(this).studentMarksDao().getStudentMarkById(studentId);
            studentMarks.setMyMark(newNote);
            studentMarks.setAverageMark(newNote);
            AppDatabase.getInstance(this).studentMarksDao().update(studentMarks);
            this.projectTeamAdapter.notifyDataSetChanged();
            this.displayAverageGrade();
        }else{
            LogUtils.d(LogUtils.DEBUG_TAG,"Nothing to do since the note == the note in database");
            // nothing to do
        }
    }

    @Override
    protected void onRequestFailed(String message) {
        super.onRequestFailed(message);
        if(this.waitingDialog != null && this.waitingDialog.isShowing()){
            this.waitingDialog.dismiss();
        }
        if(this.errorConnnectioDialog == null || !this.errorConnnectioDialog.isShowing()){
            this.errorConnnectioDialog = DialogUtils.displayDialogErrorConnection(this);
        }
        this.projectTeamAdapter.notifyDataSetChanged();
        this.displayAverageGrade();
    }

    @Override
    protected void onPostNewNoteForStudentSucceed(String message) {
        super.onPostNewNoteForStudentSucceed(message);
        sendServiceRequestGetNotesTeamMember(appUserAccount, project.getIdProject());
    }

    @Override
    protected void onGetNotesTeamMemberSucceed(String message) {
        if(this.waitingDialog != null && this.waitingDialog.isShowing()){
            this.waitingDialog.dismiss();
        }
        this.projectTeamAdapter.notifyDataSetChanged();
        this.displayAverageGrade();
    }

    @Override
    protected void onGetPosterOfProjectSucceed(String message) {
        super.onGetPosterOfProjectSucceed(message);
        if(this.waitingDialog != null && this.waitingDialog.isShowing()){
            this.waitingDialog.dismiss();
        }
        setLayoutFromPoster();
    }
}