package com.wakatime.androidclient.dashboard.leaderboard;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.squareup.picasso.Picasso;
import com.wakatime.androidclient.R;
import com.wakatime.androidclient.WakatimeApplication;
import com.wakatime.androidclient.dashboard.model.Language;
import com.wakatime.androidclient.dashboard.support.Linguist;
import com.wakatime.androidclient.support.context.JsonParser;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.RealmList;

import static android.support.v4.content.ContextCompat.getColor;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaderProfileFragment extends Fragment {

    private static final String ARG_LEADER = "arg-leader";
    @BindView(R.id.leader_profile_image)
    CircleImageView mLeaderProfileImage;
    @BindView(R.id.leader_rank)
    TextView mLeaderRank;
    @BindView(R.id.leader_name)
    TextView mLeaderName;
    @BindView(R.id.chart_leader_languages)
    PieChart mChartLeaderLanguages;

    @Inject
    Picasso picasso;

    private Linguist linguist;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((WakatimeApplication) this.getActivity().getApplication())
                .useDashboardComponent().inject(this);
        this.linguist = Linguist.init(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leader_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        renderData();
    }


    private void renderData() {
        final Leader leader = JsonParser.read(
                getArguments().getString(ARG_LEADER), Leader.class);

        mLeaderName.setText(leader.getUser().getName());

        // Async
        picasso.load(leader.getUser().getPhoto())
                .centerCrop()
                .resizeDimen(R.dimen.leader_image, R.dimen.leader_image)
                .into(mLeaderProfileImage);

        mLeaderRank.setText(String.valueOf(leader.getRank()));
        renderChart(leader);

    }

    private void renderChart(Leader leader) {
        mChartLeaderLanguages.setHighlightPerTapEnabled(true);
        mChartLeaderLanguages.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        mChartLeaderLanguages.setDescription(getString(R.string.values_in_minutes));
        mChartLeaderLanguages.setDescriptionColor(getColor(this.getActivity(), R.color.colorSecondaryText));
        mChartLeaderLanguages.setDescriptionTextSize(13f);
        mChartLeaderLanguages.setCenterTextColor(getColor(this.getActivity(), R.color.colorSecondaryText));
        mChartLeaderLanguages.setCenterText(leader.getRunningTotal().getHumanReadableTotal());
        mChartLeaderLanguages.setCenterTextSize(16f);

        RealmList<Language> languages = leader.getRunningTotal().getLanguages();
        List<PieEntry> entries = new ArrayList<>(languages.size());
        List<Integer> colors = new ArrayList<>(languages.size());
        for (Language language : languages) {
            entries.add(new PieEntry(toMinutes(language.getTotalSeconds()), language.getName()));
            colors.add(linguist.decode(language.getName()));
        }
        PieDataSet pieDataSet = new PieDataSet(entries, getString(R.string.languages));

        pieDataSet.setColors(colors);
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(16f);
        pieData.setValueTextColor(Color.WHITE);

        mChartLeaderLanguages.setData(pieData);
    }

    private float toMinutes(long totalSeconds) {
        return totalSeconds / 60;
    }
}
