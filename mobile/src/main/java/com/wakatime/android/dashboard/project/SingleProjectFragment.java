package com.wakatime.android.dashboard.project;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wakatime.android.R;
import com.wakatime.android.WakatimeApplication;
import com.wakatime.android.dashboard.model.Editor;
import com.wakatime.android.dashboard.model.Language;
import com.wakatime.android.dashboard.model.OperatingSystem;
import com.wakatime.android.dashboard.model.Stats;
import com.wakatime.android.dashboard.support.Linguist;
import com.wakatime.android.support.JsonParser;
import com.wakatime.android.support.view.Animations;
import com.wakatime.android.util.Charts;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Handle the single project information
 */
public class SingleProjectFragment extends Fragment implements SingleProjectViewModel {

    private static final String PROJECT_NAME_ARG = "project-name";
    private static final String ROTATION_KEY = "ROTATION-KEY";

    @Inject
    SingleProjectPresenter mPresenter;

    @BindView(R.id.text_view_project_name)
    TextView mTextViewProjectName;

    @BindView(R.id.text_view_avg_daily)
    TextView mTextViewAvgDaily;

    @BindView(R.id.text_view_total_time)
    TextView mTextViewTotalTime;

    @BindView(R.id.chart_languages)
    PieChart mChartLanguages;

    @BindView(R.id.chart_editors)
    PieChart mChartEditors;

    @BindView(R.id.chart_os)
    PieChart mChartOs;

    @BindView(R.id.container)
    ScrollView mContainer;

    @BindView(R.id.constraint_layout)
    View mDataLayout;

    @BindView(R.id.loader_single_project)
    SpinKitView mLoaderSingleProject;

    private String mProjectName;

    private Tracker mTracker;

    private OnSingleProjectInteractionListener mListener;

    private Stats mCache;

    private Linguist linguist;

    public SingleProjectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param projectName Project name to query
     * @return A new instance of fragment SingleProjectFragment.
     */
    public static SingleProjectFragment newInstance(String projectName) {
        SingleProjectFragment fragment = new SingleProjectFragment();
        Bundle args = new Bundle();
        args.putString(PROJECT_NAME_ARG, projectName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSingleProjectInteractionListener) {
            mListener = (OnSingleProjectInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSingleProjectInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mProjectName = getArguments().getString(PROJECT_NAME_ARG);
        }

        WakatimeApplication app = (WakatimeApplication) this.getActivity().getApplication();
        app.useDashboardComponent().inject(this);
        mTracker = app.getTracker();
        this.linguist = Linguist.init(this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_project, container, false);
        ButterKnife.bind(this, view);
        this.mPresenter.bind(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null || !savedInstanceState.containsKey(ROTATION_KEY)) {
            mPresenter.onInit(mProjectName);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(ROTATION_KEY)) {
            this.mCache = JsonParser.read(savedInstanceState.getString(ROTATION_KEY), Stats.class);
            this.setData(mCache);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName("Dashboard-SingleProject");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ROTATION_KEY, JsonParser.write(mCache));
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return Animations.createMoveAnimation(enter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.mPresenter.onFinish();
        this.mPresenter.unbind();
    }

    @Override
    public void notifyError(Throwable error) {
        Snackbar snackbar = Snackbar.make(mContainer,
                R.string.could_not_fetch, Snackbar.LENGTH_LONG);

        snackbar.setAction(R.string.retry, view -> {
            mPresenter.onInit(mProjectName);
            snackbar.dismiss();
        });

        snackbar.show();
    }


    @Override
    public void hideLoader() {
        mLoaderSingleProject.setVisibility(View.GONE);
        mDataLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoader() {
        mLoaderSingleProject.setVisibility(View.VISIBLE);
        mDataLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void setRotationCache(Stats data) {
        this.mCache = data;
    }

    @Override
    public void setData(Stats data) {
        renderName(mProjectName);
        renderLanguageChart(data.getLanguages());
        renderEditorChart(data.getEditors());
        renderOSChart(data.getOperatingSystems());
        renderTimes(data);
    }

    private void renderOSChart(List<OperatingSystem> operatingSystems) {
        defaultChartConfig(mChartOs);
        Charts.defaultOSChart(operatingSystems, mChartOs, linguist);
    }

    private void renderEditorChart(List<Editor> editors) {
        defaultChartConfig(mChartEditors);
        Charts.defaultEditorsChart(editors, mChartEditors);
    }

    private void renderLanguageChart(List<Language> languages) {
        defaultChartConfig(mChartLanguages);
        Charts.defaultLanguageChart(languages, mChartLanguages, linguist);
    }

    private void renderName(String name) {
        this.mTextViewProjectName.setText(name);
    }

    private void renderTimes(Stats stats) {
        this.mTextViewAvgDaily.setText(stats.getHumanReadableDailyAverage());
        this.mTextViewTotalTime.setText(stats.getHumanReadableTotal());
    }

    private void defaultChartConfig(PieChart chart) {
        Charts.setDefaultPieChartConfig(chart);
    }

    public interface OnSingleProjectInteractionListener {
    }
}
