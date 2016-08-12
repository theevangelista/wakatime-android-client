package com.wakatime.android.dashboard.stats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.ybq.android.spinkit.SpinKitView;
import com.wakatime.android.R;
import com.wakatime.android.WakatimeApplication;
import com.wakatime.android.dashboard.model.Stats;
import com.wakatime.android.dashboard.support.Linguist;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Joao Pedro Evangelista
 */
public class LastThirtyDaysFragment extends AbstractStatsChartAwareFragment
    implements LastThirtyDaysViewModel, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.loader_last_month)
    SpinKitView mLoaderLastMonth;

    @BindView(R.id.text_view_logged_time)
    TextView mTextViewLoggedTime;

    @BindView(R.id.chart_languages)
    PieChart mChartLanguages;

    @BindView(R.id.chart_editors)
    PieChart mChartEditors;

    @BindView(R.id.chart_os)
    PieChart mChartOs;

    @BindView(R.id.container_charts)
    RelativeLayout mContainerCharts;

    @BindView(R.id.swipe_month_container)
    SwipeRefreshLayout mSwipeContainer;

    @Inject
    LastThirtyDaysPresenter mPresenter;

    private Stats mRotationCache;

    public LastThirtyDaysFragment() {
        // Required empty public constructor
    }

    public static LastThirtyDaysFragment newInstance() {
        return new LastThirtyDaysFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((WakatimeApplication) this.getActivity().getApplication())
            .useDashboardComponent().inject(this);
        super.setLinguist(Linguist.init(this.getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_last_thirty_days, container, false);
        ButterKnife.bind(this, view);
        mPresenter.bind(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeContainer.setOnRefreshListener(this);
        mSwipeContainer.setColorSchemeResources(R.color.colorAccent, R.color.colorDarkAccent);
        mPresenter.onInit();
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
    public void onRefresh() {
        mPresenter.onRefresh();
    }

    @Override
    public void setData(Stats data) {
        mTextViewLoggedTime.setText(data.getHumanReadableTotal());
        super.setEditorData(data, mChartEditors);
        super.setLanguageData(data, mChartLanguages);
        super.setOSChart(data, mChartOs);
    }

    @Override
    public void setRotationCache(Stats data) {
        this.mRotationCache = data;
    }

    @Override
    public void completeRefresh() {
        this.mSwipeContainer.setRefreshing(false);
    }


    @Override
    public void notifyError(Throwable error) {
        Snackbar snackbar = Snackbar.make(mSwipeContainer,
            R.string.could_not_fetch, Snackbar.LENGTH_LONG);

        snackbar.setAction(R.string.retry, view -> {
            mPresenter.onFinish();
            snackbar.dismiss();
        });

        snackbar.show();
    }

    @Override
    public void hideLoader() {
        this.mLoaderLastMonth.setVisibility(View.GONE);
        this.mContainerCharts.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoader() {
        this.mLoaderLastMonth.setVisibility(View.VISIBLE);
        this.mContainerCharts.setVisibility(View.GONE);
    }
}
