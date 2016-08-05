package com.wakatime.androidclient.dashboard.project;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ybq.android.spinkit.SpinKitView;
import com.wakatime.androidclient.R;
import com.wakatime.androidclient.WakatimeApplication;
import com.wakatime.androidclient.dashboard.model.Project;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Project data fragment
 *
 * @author Joao Pedro Evangelista
 */
public class ProjectFragment extends Fragment implements ViewModel {

    public static final String KEY = "project-fragment";

    @BindView(R.id.recycler_projects)
    RecyclerView mRecyclerProjects;

    @BindView(R.id.loader_projects)
    SpinKitView mLoaderProjects;

    @Inject
    ProjectPresenter mPresenter;

    private List<Project> rotationCache;

    public ProjectFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment ProjectFragment.
     */
    public static ProjectFragment newInstance() {
        return new ProjectFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((WakatimeApplication) this.getActivity().getApplication())
                .useDashboardComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_project, container, false);
        ButterKnife.bind(this, view);
        mPresenter.bind(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.onInit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof OnProjectFragmentInteractionListener)) {
            throw new RuntimeException(context.toString()
                    + " must implement OnProjectFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onFinish();
        mPresenter.unbind();
    }

    @Override
    public void setProjects(List<Project> projects) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        ProjectAdapter adapter = new ProjectAdapter(getActivity(), projects);
        mRecyclerProjects.setLayoutManager(layoutManager);
        mRecyclerProjects.setAdapter(adapter);
    }

    @Override
    public void setRotationCache(List<Project> projects) {
        this.rotationCache = projects;
    }

    @Override
    public void hideLoader() {
        this.mLoaderProjects.setVisibility(View.GONE);
        this.mRecyclerProjects.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoader() {
        this.mLoaderProjects.setVisibility(View.VISIBLE);
        this.mRecyclerProjects.setVisibility(View.GONE);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnProjectFragmentInteractionListener {

    }
}
