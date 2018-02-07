package com.murey.poster.postermaster.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.murey.poster.postermaster.R;
import com.murey.poster.postermaster.model.database.AppDatabase;
import com.murey.poster.postermaster.model.database.tables.StudentMarks;
import com.murey.poster.postermaster.model.database.tables.Users;
import com.murey.poster.postermaster.utils.DialogUtils;
import com.murey.poster.postermaster.view.activity.ProjectDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class ProjectTeamAdapter extends RecyclerView.Adapter<ProjectTeamAdapter.StudentViewHolder> {

    private final ProjectDetailsActivity projectDetailsActivity;
    private List<Users> projectStudents;
    private int projectId;
    private List<Integer> myJuryProjectsId;
    private Users appUser;

    public ProjectTeamAdapter(ProjectDetailsActivity projectDetailsActivity, int projectId, Users appUser) {
        this.projectDetailsActivity = projectDetailsActivity;
        this.projectStudents = new ArrayList<>();
        this.projectId = projectId;
        this.appUser = appUser;
    }

    public void setProjectUsers(List<Users> projectUsers, List<Integer> myProjectId) {
        this.projectStudents = projectUsers;
        this.myJuryProjectsId = myProjectId;
    }

    public int getItemCount() {
        return projectStudents.size();
    }

    public StudentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View studentView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_view_student_name_and_note, viewGroup, false);
        return new StudentViewHolder(studentView);
    }


    public void onBindViewHolder(final StudentViewHolder studentViewHolder, int i) {
        final Users user = projectStudents.get(i);
        studentViewHolder.studentName.setText(String.format("%s %s", user.getForename(), user.getSurname()));

        // In this case, the project is one of the jury project (my project)
        if(myJuryProjectsId.contains(projectId)){
            final StudentMarks studentMark = AppDatabase.getInstance(projectDetailsActivity).studentMarksDao().getStudentMarkById(user.getIdServerEseoUser());
            studentViewHolder.layoutNotes.setVisibility(View.VISIBLE);
            studentViewHolder.studentAverageNote.setText(studentMark.getAverageMark() != -1f
                    ? String.format(" %s /20", studentMark.getAverageMark())
                    : " - /20"
            );
            studentViewHolder.studentMyNote.setText(String.format(" %s /20", studentMark.getMyMark()));
            studentViewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtils.displayDialogSetStudentNote(projectDetailsActivity, user, projectId, appUser);
                }
            });

            if(studentMark.isNeedToSync()){
                studentViewHolder.studentMyNote.setBackgroundColor(projectDetailsActivity.getResources().getColor(R.color.colorRed));
                studentViewHolder.studentMyNote.setText(String.format(" %s /20", studentMark.getMyMarkToSynchronize()));
            }else{
                studentViewHolder.studentMyNote.setBackgroundColor(projectDetailsActivity.getResources().getColor(R.color.colorTransparent));
            }
            if(studentViewHolder.myTextWatcher != null){
                studentViewHolder.etJustification.removeTextChangedListener(studentViewHolder.myTextWatcher);
            }
            studentViewHolder.myTextWatcher = new MyTextWatcher(studentMark, studentViewHolder);
            studentViewHolder.etJustification.setText(studentMark.getJustification());
            studentViewHolder.etJustification.addTextChangedListener(studentViewHolder.myTextWatcher);
        }else{
            studentViewHolder.layoutNotes.setVisibility(View.GONE);
            studentViewHolder.layoutJustification.setVisibility(View.GONE);
        }
    }

    class MyTextWatcher implements TextWatcher{

        private StudentMarks studentMark;
        private StudentViewHolder studentViewHolder;

        public MyTextWatcher(StudentMarks studentMark, StudentViewHolder studentViewHolder) {
            this.studentMark = studentMark;
            this.studentViewHolder = studentViewHolder;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            studentMark.setJustification(studentViewHolder.etJustification.getText().toString());
            AppDatabase.getInstance(projectDetailsActivity).studentMarksDao().update(studentMark);
        }
    }

    class StudentViewHolder extends RecyclerView.ViewHolder {

        private final View view;
        private final TextView studentName;
        private final View layoutNotes;
        private final TextView studentAverageNote;
        private final TextView studentMyNote;
        private final View layoutJustification;
        private final EditText etJustification;
        private MyTextWatcher myTextWatcher;

        public StudentViewHolder(View view) {
            super(view);
            this.view = view;
            layoutNotes = view.findViewById(R.id.layout_notes);
            studentName = (TextView) view.findViewById(R.id.tv_student_name);
            studentAverageNote = (TextView) view.findViewById(R.id.tv_student_note_moyenne);
            studentMyNote = (TextView) view.findViewById(R.id.tv_student_note_personal);
            layoutJustification = (View) view.findViewById(R.id.layout_justification);
            etJustification = (EditText) view.findViewById(R.id.etJustification);
        }
    }
}
