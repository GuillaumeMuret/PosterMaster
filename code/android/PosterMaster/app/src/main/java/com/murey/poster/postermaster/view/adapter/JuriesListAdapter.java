package com.murey.poster.postermaster.view.adapter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.murey.poster.postermaster.model.database.AppDatabase;
import com.murey.poster.postermaster.model.database.tables.Juries;
import com.murey.poster.postermaster.R;
import com.murey.poster.postermaster.model.database.tables.JuryMembers;
import com.murey.poster.postermaster.model.database.tables.Projects;
import com.murey.poster.postermaster.utils.DateUtils;
import com.murey.poster.postermaster.utils.LogUtils;
import com.murey.poster.postermaster.view.activity.AbstractActivityWithToolbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class JuriesListAdapter extends
        RecyclerView.Adapter<JuriesListAdapter.JuriesViewHolder> {

    private AbstractActivityWithToolbar activity;
    private List<Juries> juriesList;

    public JuriesListAdapter(AbstractActivityWithToolbar juriesListActivity) {
        this.activity = juriesListActivity;
        setJuriesList(new ArrayList<Juries>());
    }

    public void setJuriesList(List<Juries> juriesList) {
        this.juriesList = juriesList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return juriesList.size();
    }

    public int getPositionOfItem(Juries jury){
        for(int i = 0;i<juriesList.size();i++){
            if(juriesList.get(i).getIdJury() == jury.getIdJury()){
                return i;
            }
        }
        return -1;
    }

    @Override
    public JuriesListAdapter.JuriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View filmView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_list_juries, parent, false);
        return new JuriesListAdapter.JuriesViewHolder(filmView);
    }

    @Override
    public void onBindViewHolder(final JuriesListAdapter.JuriesViewHolder holder, final int position) {
        final Juries jury = juriesList.get(position);
        if(jury != null){
            holder.tvJuryDate.setText(jury.getDate());
            holder.tvJuryId.setText(jury.getIdJury() == JuryMembers.JURY_ID_OPEN_HOUSE
                    ? "Portes ouvertes"
                    : String.valueOf(jury.getIdJury())
            );

            // Alert
            holder.swAlert.setOnCheckedChangeListener(null);
            holder.swAlert.setChecked(jury.isModeAlert());

            holder.swAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Calendar calendar = Calendar.getInstance();
                    LogUtils.d(LogUtils.DEBUG_TAG,"onCheckedChanged");

                    if(isChecked){
                        calendar.setTime(DateUtils.getDateFromString(jury.getAlertDate(), DateUtils.FORMAT_DATE_POSTER_MASTER));
                        activity.beginAlarmManager(calendar, jury);
                        LogUtils.d(LogUtils.DEBUG_TAG,"Begin alarm for jury " + jury.getIdJury()+" on " + DateUtils.getStringFromDate(calendar.getTime()));
                    }else{
                        LogUtils.d(LogUtils.DEBUG_TAG,"Cancel alarm for jury " + jury.getIdJury()+" on " + DateUtils.getStringFromDate(calendar.getTime()));
                        activity.cancelAlarm(jury.getIdJury());
                    }
                    jury.setModeAlert(isChecked);
                    AppDatabase.getInstance(activity).juriesDao().update(jury);
                }
            });
            holder.tvAlertDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Date date = DateUtils.getDateFromString(jury.getAlertDate(),DateUtils.FORMAT_DATE_POSTER_MASTER);
                    Calendar c = Calendar.getInstance();
                    c.setTime(date);
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog =
                            new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date);
                                    calendar.set(year,month,dayOfMonth);
                                    holder.swAlert.setChecked(true);
                                    holder.tvAlertDate.setText(DateUtils.getFriendlyFromDateString(
                                            DateUtils.getStringFromDate(calendar.getTime()),
                                            DateUtils.FORMAT_DATE_POSTER_MASTER
                                    ));
                                    jury.setAlertDate(DateUtils.getStringFromDate(calendar.getTime()));
                                    activity.setAlarm(calendar, jury);
                                    AppDatabase.getInstance(activity).juriesDao().update(jury);
                                }
                            }, mYear, mMonth, mDay);
                    dialog.show();
                }
            });

            if(jury.getAlertDate() == null){
                jury.setAlertDate(DateUtils.getStringFromDate(Calendar.getInstance().getTime()));
            }

            holder.tvAlertDate.setText(DateUtils.getFriendlyFromDateString(jury.getAlertDate(),DateUtils.FORMAT_DATE_POSTER_MASTER));

            final Calendar c = Calendar.getInstance();
            LogUtils.d(LogUtils.DEBUG_TAG,"Date => "+jury.getAlertDate());
            c.setTime(DateUtils.getDateFromString(jury.getAlertDate(),DateUtils.FORMAT_DATE_POSTER_MASTER));
            final int mHour = c.get(Calendar.HOUR_OF_DAY);
            final int mMinute = c.get(Calendar.MINUTE);
            holder.tvAlertTime.setText(String.format("%s:%s", mHour<10?"0"+String.valueOf(mHour):String.valueOf(mHour), mMinute<10?"0"+String.valueOf(mMinute):String.valueOf(mMinute)));

            holder.tvAlertTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get Current Time
                    final Calendar c = Calendar.getInstance();
                    LogUtils.d(LogUtils.DEBUG_TAG,"Date => "+jury.getAlertDate());
                    c.setTime(DateUtils.getDateFromString(jury.getAlertDate(),DateUtils.FORMAT_DATE_POSTER_MASTER));
                    final int mHour = c.get(Calendar.HOUR_OF_DAY);
                    final int mMinute = c.get(Calendar.MINUTE);
                    final Date date = DateUtils.getDateFromString(jury.getAlertDate(),DateUtils.FORMAT_DATE_POSTER_MASTER);
                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(activity,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    holder.tvAlertTime.setText(String.format("%s:%s", hourOfDay<10?"0"+String.valueOf(hourOfDay):String.valueOf(hourOfDay), minute<10?"0"+String.valueOf(minute):String.valueOf(minute)));
                                    holder.swAlert.setChecked(true);
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date);
                                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    calendar.set(Calendar.MINUTE, minute);
                                    calendar.set(Calendar.SECOND, 0);
                                    jury.setAlertDate(DateUtils.getStringFromDate(calendar.getTime()));
                                    activity.setAlarm(calendar, jury);
                                    AppDatabase.getInstance(activity).juriesDao().update(jury);
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog.show();
                }
            });

            // Members
            List<JuryMembers> juryMembers = AppDatabase.getInstance(activity).juryMembersDao().getJuryMembersFromJuryId(jury.getIdJury());
            String juryMemberString = "";
            for(int i = 0;i<juryMembers.size();i++){
                juryMemberString += juryMembers.get(i).getForename() + " " + juryMembers.get(i).getSurname();
                if(i!=juryMembers.size()-1){
                    juryMemberString += ", ";
                }
            }
            holder.tvJuryMembers.setText(juryMemberString);

            List<Integer> juryProjectsIdList = AppDatabase.getInstance(activity).juryProjectsDao().getProjectsIdFromJuryId(jury.getIdJury());
            ArrayList<Projects> juryProjectsList = new ArrayList<>();
            for(int i=0;i<juryProjectsIdList.size();i++){
                juryProjectsList.add(AppDatabase.getInstance(activity).projectsDao().getProjectById(juryProjectsIdList.get(i)));
            }
            holder.projectInJuryListAdapter.setJuryProjects(jury, juryProjectsList);
        }
    }

    class JuriesViewHolder extends RecyclerView.ViewHolder {

        private final View view;

        private final View alertDateLayout;
        private final Switch swAlert;
        private final TextView tvJuryDate;
        private final TextView tvJuryId;
        private final TextView tvAlertDate;
        private final TextView tvAlertTime;
        private final TextView tvJuryMembers;
        private final ProjectInJuryListAdapter projectInJuryListAdapter;

        public JuriesViewHolder(View view) {
            super(view);
            this.view = view;
            tvJuryDate = (TextView) view.findViewById(R.id.tv_juries_date);
            tvJuryId = (TextView) view.findViewById(R.id.tv_jury_id);
            alertDateLayout = (View) view.findViewById(R.id.alert_date_layout);
            swAlert = (Switch) view.findViewById(R.id.swAlert);
            tvAlertDate = (TextView) view.findViewById(R.id.tv_alert_date);
            tvAlertTime = (TextView) view.findViewById(R.id.tv_alert_time);
            tvJuryMembers = (TextView) view.findViewById(R.id.tv_jury_members);

            RecyclerView recycler = (RecyclerView) view.findViewById(R.id.list_projects_jury);
            recycler.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(activity);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recycler.setLayoutManager(llm);
            projectInJuryListAdapter = new ProjectInJuryListAdapter(activity);
            recycler.setAdapter(projectInJuryListAdapter);
        }

    }
}
