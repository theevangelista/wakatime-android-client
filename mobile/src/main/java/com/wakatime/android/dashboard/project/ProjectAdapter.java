package com.wakatime.android.dashboard.project;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wakatime.android.R;
import com.wakatime.android.dashboard.model.Project;
import com.wakatime.android.dashboard.project.ProjectFragment.OnProjectFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Joao Pedro Evangelista
 */

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    private final LayoutInflater inflater;

    private List<Project> projects;

    /**
     * Holds data because projects collection gets modified by the query
     */
    private List<Project> projectsCache;
    private OnProjectFragmentInteractionListener mListener;

    public ProjectAdapter(Context context, List<Project> projects, OnProjectFragmentInteractionListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.projects = projects;
        this.projectsCache = new ArrayList<>(projects);
        this.mListener = listener;
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
        holder.item = project;
        holder.itemView.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.showProjectPage(project);
            }
        });
        holder.mTextViewProjectName.setText(project.getName());
        holder.mTextViewProjectTime.setText(project.getText());

    }

    public Project remove(int pos) {
        final Project project = projects.remove(pos);
        notifyItemRemoved(pos);
        return project;
    }

    public void add(int pos, Project item) {
        projects.add(pos, item);
        notifyItemInserted(pos);
    }

    public void move(int from, int to) {
        final Project project = projects.remove(from);
        projects.add(to, project);
        notifyItemMoved(from, to);
    }

    public void animateTo(List<Project> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateMovedItems(List<Project> models) {
        for (int toPosition = models.size() - 1; toPosition >= 0; toPosition--) {
            final Project model = models.get(toPosition);
            final int fromPosition = projects.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                move(fromPosition, toPosition);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Project> models) {
        for (int i = 0, count = models.size(); i < count; i++) {
            final Project model = models.get(i);
            if (!projects.contains(model)) {
                add(i, model);
            }
        }
    }

    private void applyAndAnimateRemovals(List<Project> models) {
        for (int i = projects.size() - 1; i >= 0; i--) {
            final Project model = projects.get(i);
            if (!models.contains(model)) {
                remove(i);
            }
        }
    }

    public List<Project> filter(String query) {
        String lowerQuery = query.toLowerCase();
        if (wasModified() && query.isEmpty()) {
            // add back the items when the query is empty
            return projectsCache;
        } else {
            List<Project> collector = new ArrayList<>();
            for (Project project : projects) {
                String name = project.getName().toLowerCase();
                if (name.contains(lowerQuery)) {
                    collector.add(project);
                }
            }
            return collector;
        }

    }

    private boolean wasModified() {
        return projectsCache.size() != projects.size();
    }

    @Override
    public int getItemCount() {
        return projects != null ? projects.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final View itemView;
        Project item;
        @BindView(R.id.text_view_project_name)
        TextView mTextViewProjectName;

        @BindView(R.id.text_view_project_hours)
        TextView mTextViewProjectTime;


        ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
