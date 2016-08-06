package com.wakatime.android.dashboard.project;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wakatime.android.R;
import com.wakatime.android.dashboard.model.Project;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Joao Pedro Evangelista
 */

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    private final LayoutInflater inflater;

    private List<Project> projects;

    public ProjectAdapter(Context context, List<Project> projects) {
        this.inflater = LayoutInflater.from(context);
        this.projects = projects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_project_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (projects == null) return;

        Project project = projects.get(position);
        holder.mTextViewProjectName.setText(project.getName());
        holder.mTextViewProjectTime.setText(project.getText());

    }

    @Override
    public int getItemCount() {
        return projects != null ? projects.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_view_project_name)
        TextView mTextViewProjectName;

        @BindView(R.id.text_view_project_hours)
        TextView mTextViewProjectTime;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
