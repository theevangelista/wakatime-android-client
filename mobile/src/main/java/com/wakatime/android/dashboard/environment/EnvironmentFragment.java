package com.wakatime.android.dashboard.environment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
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
import com.wakatime.android.support.view.Animations;
import com.wakatime.android.util.Charts;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnProgrammingFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EnvironmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnvironmentFragment extends Fragment implements ViewModel {

    public static final String KEY = "programming-fragment";

    private static final String LIST_STATE = "list-state";


    @BindView(R.id.text_view_logged_time)
    TextView mTextViewLoggedTime;

    @BindView(R.id.loader_programming)
    SpinKitView mLoaderProgramming;

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

    private Linguist linguist;

    private Tracker mTracker;

    public EnvironmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EnvironmentFragment.
     */
    public static EnvironmentFragment newInstance() {
        return new EnvironmentFragment();
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
        this.linguist = Linguist.init(getActivity());
        // we don't have any data loaded, so lets do it nor the key,
        // cause we can have anything more on the bundle
        if (savedInstanceState == null || !savedInstanceState.containsKey(LIST_STATE)) {
            this.mEnvironmentPresenter.onInit();
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
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return Animations.createMoveAnimation(enter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.mEnvironmentPresenter.onFinish();
        this.mEnvironmentPresenter.unbind();
    }

    @Override
    public void setData(Stats data) {
        this.mTextViewLoggedTime.setText(data.getHumanReadableTotal());
        setEditorData(data);
        setLanguageData(data);
        setOSChart(data);
    }

    @Override
    public void setRotationCache(Stats data) {
        this.rotationCache = data;
    }

    @Override
    public void notifyError(Throwable error) {
        Snackbar snackbar = Snackbar.make(mContainer,
                R.string.could_not_fetch, Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction(R.string.retry, view -> {
            mEnvironmentPresenter.onInit();
            snackbar.dismiss();
        });

        snackbar.show();
    }

    private void setLanguageData(Stats data) {
        defaultPieChartConfig(mChartLanguages);
        Charts.defaultLanguageChart(data.getLanguages(), mChartLanguages, linguist);
    }

    private void setOSChart(Stats data) {
        defaultPieChartConfig(mChartOS);
        Charts.defaultOSChart(data.getOperatingSystems(), mChartOS, linguist);
    }

    private void setEditorData(Stats data) {
        defaultPieChartConfig(mChartEditors);
        Charts.defaultEditorsChart(data.getEditors(), mChartEditors);
    }

    private void defaultPieChartConfig(PieChart chart) {
        Charts.setDefaultPieChartConfig(chart);
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
