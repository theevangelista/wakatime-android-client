package com.wakatime.androidclient.dashboard.leaderboard;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wakatime.androidclient.R;
import com.wakatime.androidclient.support.context.JsonParser;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaderProfileFragment extends Fragment {

    private static final String ARG_LEADER = "arg-leader";

    public LeaderProfileFragment() {
        // Required empty public constructor
    }


    public static LeaderProfileFragment newInstance(Leader leader) {
        LeaderProfileFragment fragment = new LeaderProfileFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString(ARG_LEADER, JsonParser.write(leader));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leader_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private void renderData() {
        final Leader leader = JsonParser.read(
                getArguments().getString(ARG_LEADER), Leader.class);


    }
}
