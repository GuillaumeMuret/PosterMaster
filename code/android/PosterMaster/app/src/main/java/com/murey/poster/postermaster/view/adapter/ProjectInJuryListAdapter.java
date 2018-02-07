package com.murey.poster.postermaster.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.murey.poster.postermaster.R;
import com.murey.poster.postermaster.model.database.tables.Juries;
import com.murey.poster.postermaster.model.database.tables.Projects;
import com.murey.poster.postermaster.view.activity.AbstractActivityWithToolbar;
import com.murey.poster.postermaster.view.activity.JuriesListActivity;

import java.util.ArrayList;
import java.util.List;

public class ProjectInJuryListAdapter extends RecyclerView.Adapter<ProjectInJuryListAdapter.ProjectInJuryListViewHolder> {

    private final AbstractActivityWithToolbar juriesListActivity;
    private Juries jury;
    private List<Projects> juryProjectsList;
    private List<Integer> myProjectId;

    public ProjectInJuryListAdapter(AbstractActivityWithToolbar juriesListActivity) {
        this.juriesListActivity = juriesListActivity;
        this.juryProjectsList = new ArrayList<>();
    }

    public void setJuryProjects(Juries jury, List<Projects> projectsList) {
        this.jury = jury;
        this.juryProjectsList = projectsList;
        this.notifyDataSetChanged();
    }

    public int getItemCount() {
        return juryProjectsList.size();
    }

    public ProjectInJuryListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View studentView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_view_juries_project_name_and_superviseur, viewGroup, false);
        return new ProjectInJuryListViewHolder(studentView);
    }


    public void onBindViewHolder(ProjectInJuryListViewHolder projectInJuryListViewHolder, int i) {
        final Projects project = juryProjectsList.get(i);
        projectInJuryListViewHolder.tvProjectName.setText(project.getTitle());
        projectInJuryListViewHolder.tvProjectSupervisor.setText(
                String.format("%s %s", project.getSupervisorForename(), project.getSupervisorSurname()));

        projectInJuryListViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                juriesListActivity.clickProjectItemInJury(jury, project);
            }
        });
    }

    class ProjectInJuryListViewHolder extends RecyclerView.ViewHolder {

        private final View view;
        private final TextView tvProjectName;
        private final TextView tvProjectSupervisor;

        public ProjectInJuryListViewHolder(View view) {
            super(view);
            this.view = view;
            this.tvProjectName = view.findViewById(R.id.tv_project_name);
            this.tvProjectSupervisor = view.findViewById(R.id.tv_project_supervisor);
        }
    }
}
