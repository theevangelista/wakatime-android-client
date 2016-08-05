package com.wakatime.androidclient.dashboard.leaderboard;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wakatime.androidclient.R;
import com.wakatime.androidclient.dashboard.leaderboard.LeaderboardFragment.OnLeaderListFragmentInteractionListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.wakatime.androidclient.util.Strings.join;

/**
 */
public class LeaderAdapter extends RecyclerView.Adapter<LeaderAdapter.ViewHolder> {

    private final List<Leader> mValues;
    private final OnLeaderListFragmentInteractionListener mListener;

    public LeaderAdapter(List<Leader> items, OnLeaderListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_leader, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mLeaderLanguages.setText(
                join(holder.mItem.getRunningTotal().getLanguages(), ", "));

        holder.mLeaderName.setText(holder.mItem.getUser().getName());
        holder.mLeaderTime.setText(holder.mItem.getRunningTotal().getHumanReadableTotal());
        holder.mLeaderRank.setText(String.valueOf(holder.mItem.getRank()));
        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final View mView;

        Leader mItem;

        @BindView(R.id.text_view_leader_languages)
        TextView mLeaderLanguages;

        @BindView(R.id.text_view_leader_name)
        TextView mLeaderName;

        @BindView(R.id.text_view_leader_time)
        TextView mLeaderTime;

        @BindView(R.id.text_view_leader_rank)
        TextView mLeaderRank;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }

    }
}
