package com.wakatime.androidclient.dashboard.environment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.ybq.android.spinkit.SpinKitView;
import com.wakatime.androidclient.R;
import com.wakatime.androidclient.WakatimeApplication;
import com.wakatime.androidclient.dashboard.model.Editor;
import com.wakatime.androidclient.dashboard.model.Language;
import com.wakatime.androidclient.dashboard.model.OperatingSystem;
import com.wakatime.androidclient.dashboard.model.Stats;
import com.wakatime.androidclient.dashboard.support.Linguist;
import com.wakatime.androidclient.support.JsonParser;

import java.util.ArrayList;
import java.util.List;

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

    @Inject
    EnvironmentPresenter mEnvironmentPresenter;

    private Stats rotationCache;

    private Linguist linguist;

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
        ((WakatimeApplication) this.getActivity().getApplication())
                .useDashboardComponent().inject(this);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LIST_STATE, JsonParser.write(rotationCache));
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

    private void setLanguageData(Stats data) {
        defaultPieChartConfig(mChartLanguages);
        mChartLanguages.setCenterText(getString(R.string.title_languages));
        List<Language> languages = data.getLanguages();

        List<PieEntry> dataSet = new ArrayList<>(languages.size());
        for (Language language : languages) {
            dataSet.add(new PieEntry(language.getPercent(), language.getName()));
        }

        List<Integer> colors = new ArrayList<>(languages.size());
        for (Language language : languages) {
            int color = linguist.decode(language.getName());
            colors.add(color);
        }
        PieDataSet pieDataSet = new PieDataSet(dataSet, getString(R.string.title_languages));
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextSize(14f);
        mChartLanguages.setData(new PieData(pieDataSet));
    }

    private void setOSChart(Stats data) {
        defaultPieChartConfig(mChartOS);
        mChartOS.setCenterText(getString(R.string.title_os));

        List<OperatingSystem> operatingSystems = data.getOperatingSystems();
        List<PieEntry> entries = new ArrayList<>(operatingSystems.size());
        List<Integer> colors = new ArrayList<>(operatingSystems.size());
        for (OperatingSystem operatingSystem : operatingSystems) {
            entries.add(new PieEntry(operatingSystem.getPercent(), operatingSystem.getName()));
            colors.add(linguist.decodeOS(operatingSystem.getName()));
        }
        PieDataSet pieDataSet = new PieDataSet(entries, getString(R.string.title_os));
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextSize(14f);
        pieDataSet.setValueTextColor(Color.WHITE);
        mChartOS.setData(new PieData(pieDataSet));
    }

    private void setEditorData(Stats data) {
        defaultPieChartConfig(mChartEditors);
        mChartEditors.setCenterText(getString(R.string.title_editors));
        List<PieEntry> dataSet = new ArrayList<>(data.getEditors().size());
        //noinspection Convert2streamapi
        for (Editor editor : data.getEditors()) {
            dataSet.add(new PieEntry(editor.getPercent(), editor.getName()));
        }

        PieDataSet pieDataSet = new PieDataSet(dataSet, getString(R.string.title_editors));
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(14f);
        mChartEditors.setData(new PieData(pieDataSet));
    }

    private void defaultPieChartConfig(PieChart chart) {
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);
        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);
        chart.setDescription("");
        chart.setUsePercentValues(true);
        chart.setEntryLabelColor(Color.WHITE);
        chart.setDrawCenterText(true);

        chart.setCenterTextSize(18f);
        chart.setCenterTextColor(ContextCompat.getColor(getActivity(), R.color.colorSecondaryText));
        chart.setHighlightPerTapEnabled(true);

        chart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
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
