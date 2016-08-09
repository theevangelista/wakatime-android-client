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
import com.wakatime.android.support.SearchableRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Joao Pedro Evangelista
 */
class ProjectAdapter extends SearchableRecyclerAdapter<ProjectAdapter.ViewHolder, Project> {

    private final LayoutInflater inflater;

    /**
     * Holds data because projects collection gets modified by the query
     */
    private OnProjectFragmentInteractionListener mListener;

    ProjectAdapter(Context context, List<Project> projects, OnProjectFragmentInteractionListener listener) {
        super(projects);
        this.inflater = LayoutInflater.from(context);
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_project_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mValues == null) return;

        Project project = mValues.get(position);
        holder.item = project;
        holder.itemView.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.showProjectPage(project);
            }
        });
        holder.mTextViewProjectName.setText(project.getName());
        holder.mTextViewProjectTime.setText(project.getText());

    }


    List<Project> filter(String query) {
        return super.filter(query, (lowerQuery, value) -> value.getName().toLowerCase().contains(lowerQuery));
    }


    @Override
    public int getItemCount() {
        return mValues != null ? mValues.size() : 0;
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
