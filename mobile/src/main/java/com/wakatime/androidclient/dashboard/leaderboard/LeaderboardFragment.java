package com.wakatime.androidclient.dashboard.leaderboard;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.ybq.android.spinkit.SpinKitView;
import com.wakatime.androidclient.R;
import com.wakatime.androidclient.WakatimeApplication;
import com.wakatime.androidclient.support.context.JsonParser;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnLeaderListFragmentInteractionListener}
 * interface.
 */
public class LeaderboardFragment extends Fragment implements ViewModel {

    public static final String KEY = "leader-fragment";
    private static final String ROTATION_CACHE = "rotation-cache";

    @BindView(R.id.recycler_leader)
    RecyclerView mRecyclerLeader;

    @BindView(R.id.loader_leaders)
    SpinKitView mLoaderLeaders;

    @Inject
    LeaderboardPresenter mPresenter;

    private OnLeaderListFragmentInteractionListener mListener;

    private List<Leader> rotationCache;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LeaderboardFragment() {
    }


    @SuppressWarnings("unused")
    public static LeaderboardFragment newInstance() {
        return new LeaderboardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((WakatimeApplication) this.getActivity().getApplication())
                .useDashboardComponent().inject(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(ROTATION_CACHE)) {
            this.rotationCache = JsonParser.read(savedInstanceState.getString(ROTATION_CACHE),
                    new TypeReference<List<Leader>>() {
                    });
            this.setData(this.rotationCache);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ROTATION_CACHE, JsonParser.write(this.rotationCache));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leader_list, container, false);
        ButterKnife.bind(this, view);
        this.mPresenter.bind(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null || !savedInstanceState.containsKey(ROTATION_CACHE)) {
            this.mPresenter.onInit();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLeaderListFragmentInteractionListener) {
            mListener = (OnLeaderListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLeaderListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.mPresenter.unbind();
    }

    @Override
    public void hideLoader() {
        this.mRecyclerLeader.setVisibility(View.VISIBLE);
        this.mLoaderLeaders.setVisibility(View.GONE);
    }

    @Override
    public void showLoader() {
        this.mRecyclerLeader.setVisibility(View.GONE);
        this.mLoaderLeaders.setVisibility(View.VISIBLE);
    }

    @Override
    public void setData(List<Leader> leaders) {
        this.mRecyclerLeader.setAdapter(new LeaderAdapter(leaders, mListener));
        this.mRecyclerLeader.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public void setRotationCache(List<Leader> leaders) {
        this.rotationCache = leaders;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnLeaderListFragmentInteractionListener {

        void onListFragmentInteraction(Leader leader);
    }
}
