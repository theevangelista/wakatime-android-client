package com.wakatime.android.util;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.wakatime.android.R;
import com.wakatime.android.dashboard.model.Editor;
import com.wakatime.android.dashboard.model.Language;
import com.wakatime.android.dashboard.model.OperatingSystem;
import com.wakatime.android.dashboard.support.Linguist;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Joao Pedro Evangelista
 */
public final class Charts {
    private Charts() {

    }

    public static PieChart setDefaultPieChartConfig(PieChart chart) {
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);
        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);
        chart.setDragDecelerationFrictionCoef(0.95f);
        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);
        chart.setDescription("");
        chart.setUsePercentValues(true);
        chart.setEntryLabelColor(Color.WHITE);
        chart.setDrawCenterText(true);
        chart.setCenterTextSize(18f);
        chart.setCenterTextColor(ContextCompat.getColor(chart.getContext(), R.color.colorSecondaryText));
        chart.setRotationAngle(0);
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        chart.setMaxHighlightDistance(400f);
        chart.setCenterTextTypeface(Typeface.createFromAsset(chart.getContext().getAssets(), "fonts/Lato-Regular.ttf"));
        chart.setEntryLabelTypeface(Typeface.createFromAsset(chart.getContext().getAssets(), "fonts/Lato-Regular.ttf"));
        chart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        return chart;
    }

    public static PieChart defaultLanguageChart(List<Language> languages, PieChart chart, Linguist linguist) {
        chart.setCenterText(chart.getContext().getString(R.string.title_languages));

        List<PieEntry> dataSet = new ArrayList<>(languages.size());
        List<Integer> colors = new ArrayList<>(languages.size());

        for (Language language : languages) {
            dataSet.add(new PieEntry(language.getPercent(), language.getName()));
            int color = linguist.decode(language.getName());
            colors.add(color);
        }
        PieDataSet pieDataSet = new PieDataSet(dataSet, chart.getContext().getString(R.string.title_languages));
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextSize(14f);
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        chart.setData(pieData);
        return chart;
    }

    public static PieChart defaultOSChart(List<OperatingSystem> operatingSystems, PieChart chart, Linguist linguist) {
        chart.setCenterText(chart.getContext().getString(R.string.title_os));

        List<PieEntry> entries = new ArrayList<>(operatingSystems.size());
        List<Integer> colors = new ArrayList<>(operatingSystems.size());
        for (OperatingSystem operatingSystem : operatingSystems) {
            entries.add(new PieEntry(operatingSystem.getPercent(), operatingSystem.getName()));
            colors.add(linguist.decodeOS(operatingSystem.getName()));
        }
        PieDataSet pieDataSet = new PieDataSet(entries, chart.getContext().getString(R.string.title_os));
        pieDataSet.setColors(colors);
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setValueTextSize(14f);
        pieDataSet.setValueTextColor(Color.WHITE);
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        chart.setData(pieData);
        return chart;
    }

    public static PieChart defaultEditorsChart(List<Editor> editors, PieChart chart) {
        chart.setCenterText(chart.getContext().getString(R.string.title_editors));
        int size = 0;
        if (editors != null) {
            size = editors.size();
        }
        List<PieEntry> dataSet = new ArrayList<>(size);
        //noinspection Convert2streamapi
        for (Editor editor : editors) {
            dataSet.add(new PieEntry(editor.getPercent(), editor.getName()));
        }

        PieDataSet pieDataSet = new PieDataSet(dataSet, chart.getContext().getString(R.string.title_editors));
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(14f);
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(5f);
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        chart.setData(pieData);
        return chart;
    }
}
