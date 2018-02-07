package com.murey.poster.postermaster.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.murey.poster.postermaster.model.database.tables.Juries;
import com.murey.poster.postermaster.model.database.tables.Projects;
import com.murey.poster.postermaster.R;
import com.murey.poster.postermaster.view.activity.AbstractActivityWithToolbar;
import com.murey.poster.postermaster.view.activity.ProjectListActivity;

import java.util.ArrayList;
import java.util.List;

public class ProjectListAdapter extends
        RecyclerView.Adapter<ProjectListAdapter.ProjectViewHolder> {

    private AbstractActivityWithToolbar activity;
    private List<Projects> projectsList;
    private List<String> projectIdExpanded;

    public ProjectListAdapter(AbstractActivityWithToolbar projectListActivity) {
        this.activity = projectListActivity;
        setProjectList(new ArrayList<Projects>());
        projectIdExpanded = new ArrayList<>();
    }

    public void setProjectList(List<Projects> projectsList) {
        this.projectsList = projectsList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return projectsList.size();
    }

    public int getPositionOfItem(Projects project){
        for(int i = 0;i<projectsList.size();i++){
            if(projectsList.get(i).getIdProject() == project.getIdProject()){
                return i;
            }
        }
        return -1;
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View filmView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_list_project, parent, false);
        return new ProjectViewHolder(filmView);
    }

    @Override
    public void onBindViewHolder(final ProjectViewHolder holder, final int position) {
        final Projects project = projectsList.get(position);
        holder.tvProjectTitle.setText(project.getTitle());
        holder.tvProjectId.setText(String.valueOf(project.getIdProject()));
        holder.tvProjectSupervisor.setText(String.format("%s %s",
                project.getSupervisorForename(),project.getSupervisorSurname()));
        holder.tvProjectPosterEnable.setText(project.isPosterEnable() ? "Oui" : "Non");
        holder.tvProjectDescription.setText(project.getDescription());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onClickItem(project);
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (projectIdExpanded.contains(String.valueOf(project.getIdProject()))) {
                    projectIdExpanded.remove(String.valueOf(project.getIdProject()));
                    holder.retractView();
                } else {
                    projectIdExpanded.add(String.valueOf(project.getIdProject()));
                    holder.expandView();
                }
                return true;
            }
        });

        if(projectIdExpanded.contains(String.valueOf(project.getIdProject()))){
            holder.expandView();
        }else{
            holder.retractView();
        }
    }

    class ProjectViewHolder extends RecyclerView.ViewHolder {

        private final View view;

        private final TextView tvProjectTitle;
        private final TextView tvProjectSupervisor;
        private final TextView tvProjectPosterEnable;
        private final TextView tvProjectDescription;
        private final TextView tvProjectId;

        public ProjectViewHolder(View view) {
            super(view);
            this.view = view;
            tvProjectTitle = (TextView) view.findViewById(R.id.tv_project_title);
            tvProjectId = (TextView) view.findViewById(R.id.tv_project_id);
            tvProjectSupervisor = (TextView) view.findViewById(R.id.tv_project_supervisor);
            tvProjectPosterEnable = (TextView) view.findViewById(R.id.tv_project_poster_enable);
            tvProjectDescription = (TextView) view.findViewById(R.id.tv_project_description);
            retractView();
        }

        public void expandView() {
            this.tvProjectDescription.setMaxLines(500);
        }

        public void retractView() {
            this.tvProjectDescription.setEllipsize(TextUtils.TruncateAt.END);
            this.tvProjectDescription.setMaxLines(1);
        }
    }
}