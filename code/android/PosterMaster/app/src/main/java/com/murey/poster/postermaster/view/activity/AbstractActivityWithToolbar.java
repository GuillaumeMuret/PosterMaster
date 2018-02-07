package com.murey.poster.postermaster.view.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.murey.poster.postermaster.R;
import com.murey.poster.postermaster.model.database.AppDatabase;
import com.murey.poster.postermaster.model.database.tables.Juries;
import com.murey.poster.postermaster.model.database.tables.JuryMembers;
import com.murey.poster.postermaster.model.database.tables.Projects;
import com.murey.poster.postermaster.model.database.tables.StudentMarks;
import com.murey.poster.postermaster.utils.DialogUtils;
import com.murey.poster.postermaster.utils.ImageUtils;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public abstract class AbstractActivityWithToolbar extends AbstractActivity {

    protected DrawerLayout drawerLayout;
    protected NavigationView nvMainActivity;
    protected Toolbar toolbar;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected RelativeLayout rlActivityContainer;
    protected TextView tvHeaderName;
    protected TextView tvHeaderEmail;
    protected TextView tvHeaderUsername;
    protected ImageView icHeaderPhoto;
    protected String currentPathPhoto;

    private AbstractActivity me;

    /**
     * Process called when the context is created
     * @param savedInstanceState : the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        me = this;

        // Set Layout
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.dlMainActivity);
        nvMainActivity = (NavigationView) findViewById(R.id.nvMainActivity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rlActivityContainer = (RelativeLayout) findViewById(R.id.rlActivityContainer);

        updateHeaderView();
        setUpActionBar();
        setUpNavigationView();
        initListener();
    }

    /**
     * Process called to inflate the layout of the context that have a drawer layout
     * @param menuSelected : the menu selected on the navigation view
     * @param layoutId : the layout id of the context
     * @param layout : the layout of the context
     */
    protected void inflateLayout(int menuSelected, int layoutId, ViewGroup layout) {
        nvMainActivity.getMenu().getItem(menuSelected).setChecked(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View childLayout = inflater.inflate(layoutId, layout);
        rlActivityContainer.addView(childLayout);
        childLayout.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT)
        );
    }

    /**
     * Process called to update the header informations (names, e-mail, photo)
     */
    protected void updateHeaderView(){
        tvHeaderName = (TextView) nvMainActivity.getHeaderView(0).findViewById(R.id.tvHeaderName);
        tvHeaderName.setText(String.format("%s %s",
                appUserAccount.getSurname(),
                appUserAccount.getForename())
        );
        tvHeaderEmail = (TextView) nvMainActivity.getHeaderView(0).findViewById(R.id.tvHeaderEmail);
        tvHeaderUsername = (TextView) nvMainActivity.getHeaderView(0).findViewById(R.id.tvHeaderUsername);
        tvHeaderEmail.setText(appUserAccount.getEmail());
        tvHeaderUsername.setText(appUserAccount.getUsername());

        icHeaderPhoto = (CircleImageView) nvMainActivity.getHeaderView(0).findViewById(R.id.icHeaderPhoto);
        currentPathPhoto = appUserAccount.getPathPhoto();
        if (currentPathPhoto != null && ImageUtils.isFileImage(currentPathPhoto)) {
            ImageUtils.loadPhoto(currentPathPhoto, icHeaderPhoto);
        } else {
            icHeaderPhoto.setImageResource(R.drawable.ic_avatar);
        }
    }


    /**
     * Process called to set up the action bar
     */
    private void setUpActionBar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                    R.string.activity_login_subtitle, R.string.login_message_error) {

                public void onDrawerClosed(View view) {
                    supportInvalidateOptionsMenu();
                    drawerOpen = false;
                }

                public void onDrawerOpened(View drawerView) {
                    supportInvalidateOptionsMenu();
                    drawerOpen = true;
                }
            };
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            drawerLayout.setDrawerListener(mDrawerToggle);
            mDrawerToggle.syncState();
        }
    }

    /**
     * Process called to init the listeners of the context
     */
    private void initListener() {
        nvMainActivity.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                finishWithResult(ServiceActivity.DISPLAY_ACTIVITY_MY_ACCOUNT, mapParams);
            }
        });
    }


    /**
     * Process called to create the navigation menu view
     */
    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        nvMainActivity.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                // Check the item
                // uncheckAllItems();
                // menuItem.setChecked(true);

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.action_sync_all:
                        closeDrawer();
                        syncAllMarks();
                        break;

                    case R.id.action_projects:
                        closeDrawer();
                        uncheckAllItems();
                        menuItem.setChecked(true);
                        finishWithResult(ServiceActivity.DISPLAY_ACTIVITY_PROJECT_LIST);
                        break;

                    case R.id.action_juries:
                        closeDrawer();
                        uncheckAllItems();
                        menuItem.setChecked(true);
                        finishWithResult(ServiceActivity.DISPLAY_ACTIVITY_JURIES_LIST);
                        break;

                    case R.id.action_open_house:
                        closeDrawer();
                        if(appUserAccount.isModeVisitor()){
                            errorConnnectioDialog = DialogUtils.displayDialogNonValidUser(me);
                        }else{
                            uncheckAllItems();
                            menuItem.setChecked(true);
                            finishWithResult(ServiceActivity.DISPLAY_ACTIVITY_OPEN_HOUSE);
                        }
                        break;

                    case R.id.action_disconnect:
                        closeDrawer();
                        DialogUtils.displayDialogSureToDisconnectApp(me);
                        break;

                }
                return false;
            }
        });

        List<Integer> myJuriesIdList = AppDatabase.getInstance(this).juryMembersDao().getMyJuriesIdList(JuryMembers.MY_JURY_ID);
        if(myJuriesIdList.size()==0){
            nvMainActivity.getMenu().findItem(R.id.action_sync_all).setVisible(false);
        }

        if(appUserAccount.isModeVisitor()){
            nvMainActivity.getMenu().findItem(R.id.action_sync_all).setVisible(false);
            nvMainActivity.getMenu().findItem(R.id.action_open_house).setVisible(false);
        }

        List<StudentMarks> unsyncMarks = AppDatabase.getInstance(this).studentMarksDao().getUnsyncStudentMarks(true);
        int counterSyncMark = unsyncMarks.size();
        TextView view = (TextView) nvMainActivity.getMenu().findItem(R.id.action_sync_all).getActionView();
        view.setText(counterSyncMark > 0 ? String.valueOf(counterSyncMark) : null);
    }

    public void onClickItem(Projects project) {

    }

    public void clickProjectItemInJury(Juries juries, Projects projects) {

    }

    /**
     * Process called to uncheck all items of the navigation view
     */
    private void uncheckAllItems() {
        for (int i = 0; i < nvMainActivity.getMenu().size(); i++) {
            nvMainActivity.getMenu().getItem(i).setChecked(false);
        }
    }

    /**
     * Process called to close the drawer and
     */
    @Override
    protected void closeDrawer() {
        drawerLayout.closeDrawer(Gravity.START);
        drawerOpen = false;
    }

    @Override
    protected void onGetNotesTeamMemberSucceed(String message) {
        super.onGetNotesTeamMemberSucceed(message);
        List<StudentMarks> unsyncMarks = AppDatabase.getInstance(this).studentMarksDao().getUnsyncStudentMarks(true);
        int counterSyncMark = unsyncMarks.size();
        TextView view = (TextView) nvMainActivity.getMenu().findItem(R.id.action_sync_all).getActionView();
        view.setText(counterSyncMark > 0 ? String.valueOf(counterSyncMark) : null);
    }
}
