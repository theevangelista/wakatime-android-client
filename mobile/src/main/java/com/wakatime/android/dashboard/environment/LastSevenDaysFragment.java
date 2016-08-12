package com.wakatime.android.dashboard.environment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.mikephil.charting.charts.PieChart;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wakatime.android.R;
import com.wakatime.android.WakatimeApplication;
import com.wakatime.android.dashboard.model.Stats;
import com.wakatime.android.dashboard.support.Linguist;
import com.wakatime.android.support.JsonParser;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment to show the overall status of the programming environment the user uses
 * such as the languages percentages, editors and OSes
 *
 * @author Joao Pedro Evangelista
 */
public class LastSevenDaysFragment extends AbstractStatsChartAwareFragment {

    public static final String KEY = "programming-fragment";

    private static final String LIST_STATE = "list-state";

    private static final String TIME_STATE = "time-state";

    @BindView(R.id.text_view_logged_time)
    TextView mTextViewLoggedTime;

    @BindView(R.id.text_view_today_time)
    TextView mTextViewTodayTime;

    @BindView(R.id.loader_programming)
    SpinKitView mLoaderProgramming;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeContainer;

    @BindView(R.id.container_charts)
    RelativeLayout mContainerCharts;

    @BindView(R.id.chart_editors)
    PieChart mChartEditors;

    @BindView(R.id.chart_languages)
    PieChart mChartLanguages;

    @BindView(R.id.chart_os)
    PieChart mChartOS;

    @BindView(R.id.container)
    View mContainer;

    @Inject
    EnvironmentPresenter mEnvironmentPresenter;

    private Stats rotationCache;

    private String timeCache;

    private Tracker mTracker;

    public LastSevenDaysFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LastSevenDaysFragment.
     */
    public static LastSevenDaysFragment newInstance() {
        return new LastSevenDaysFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WakatimeApplication application = (WakatimeApplication) this.getActivity().getApplication();
        application.useDashboardComponent().inject(this);
        mTracker = application.getTracker();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof OnProgrammingFragmentInteractionListener)) {
            throw new RuntimeException(context.toString()
                + " must implement OnProgrammingFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(LIST_STATE)) {
            this.rotationCache = JsonParser.read(savedInstanceState.getString(LIST_STATE),
                new TypeReference<Stats>() {
                });
            this.setData(this.rotationCache);
        }
        if (savedInstanceState != null && savedInstanceState.containsKey(TIME_STATE)) {
            this.timeCache = savedInstanceState.getString(TIME_STATE);
            this.setTodayTime(timeCache);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_programming, container, false);
        ButterKnife.bind(this, view);
        this.mEnvironmentPresenter.bind(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setLinguist(Linguist.init(getActivity()));

        mSwipeContainer.setOnRefreshListener(this);
        mSwipeContainer.setColorSchemeResources(R.color.colorAccent, R.color.colorDarkAccent);

        // we don't have any data loaded, so lets do it nor the key,
        // cause we can have anything more on the bundle
        if (savedInstanceState == null ||
            !savedInstanceState.containsKey(LIST_STATE) ||
            !savedInstanceState.containsKey(TIME_STATE)) {
            this.mEnvironmentPresenter.onLastSevenDaysInitialization();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName("Dashboard-Environment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LIST_STATE, JsonParser.write(rotationCache));
        outState.putString(TIME_STATE, this.timeCache);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.mEnvironmentPresenter.onLastSevenDaysTermination();
        this.mEnvironmentPresenter.unbind();
    }

    @Override
    public void setData(Stats data) {
        this.mTextViewLoggedTime.setText(data.getHumanReadableTotal());
        super.setEditorData(data, mChartEditors);
        super.setLanguageData(data, mChartLanguages);
        super.setOSChart(data, mChartOS);
    }

    @Override
    public void setRotationCache(Stats data) {
        this.rotationCache = data;
    }

    @Override
    public void setTodayTime(String time) {
        String fmtTime = humanTime(time);
        this.timeCache = fmtTime;
        this.mTextViewTodayTime.setText(fmtTime);
    }

    @Override
    public void completeRefresh() {
        this.mSwipeContainer.setRefreshing(false);
    }

    private String humanTime(String time) {
        String[] parts = time.split(":");
        if (parts.length == 2) {
            String hour = parts[0];
            String min = parts[1];
            return String.format("%s hours %s minutes", hour, min);
        } else {
            return time;
        }


    }

    @Override
    public void notifyError(Throwable error) {
        Snackbar snackbar = Snackbar.make(mContainer,
            R.string.could_not_fetch, Snackbar.LENGTH_LONG);

        snackbar.setAction(R.string.retry, view -> {
            mEnvironmentPresenter.onLastSevenDaysInitialization();
            snackbar.dismiss();
        });

        snackbar.show();
    }

    @Override
    public void hideLoader() {
        this.mLoaderProgramming.setVisibility(View.GONE);
        this.mContainerCharts.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoader() {
        this.mLoaderProgramming.setVisibility(View.VISIBLE);
        this.mContainerCharts.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        this.mEnvironmentPresenter.onLastSevenDaysRefresh();
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
    public interface OnProgrammingFragmentInteractionListener {

    }
}
