package com.wakatime.android.dashboard.stats;

import android.support.v4.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.wakatime.android.dashboard.model.Stats;
import com.wakatime.android.dashboard.support.Linguist;
import com.wakatime.android.util.Charts;

/**
 * @author Joao Pedro Evangelista
 */
abstract class AbstractStatsChartAwareFragment extends Fragment {

    private Linguist mLinguist;

    protected void setLinguist(Linguist linguist) {
        this.mLinguist = linguist;
    }

    protected void setLanguageData(Stats data, PieChart mChartLanguages) {
        defaultPieChartConfig(mChartLanguages);
        Charts.defaultLanguageChart(data.getLanguages(), mChartLanguages, mLinguist);
    }

    protected void setOSChart(Stats data, PieChart mChartOS) {
        defaultPieChartConfig(mChartOS);
        Charts.defaultOSChart(data.getOperatingSystems(), mChartOS, mLinguist);
    }

    protected void setEditorData(Stats data, PieChart mChartEditors) {
        defaultPieChartConfig(mChartEditors);
        Charts.defaultEditorsChart(data.getEditors(), mChartEditors);
    }

    protected void defaultPieChartConfig(PieChart chart) {
        Charts.setDefaultPieChartConfig(chart);
    }
}
