package com.wakatime.android.dashboard.stats;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.wakatime.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabbedEnvironmentFragment extends Fragment {

    public static final String KEY = "tabbed-env-fragment";

    private TabLayout mTabs;

    private AppBarLayout appBarLayout;

    private View mTabHost;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    public TabbedEnvironmentFragment() {
        // Required empty public constructor
    }

    public static TabbedEnvironmentFragment newInstance() {
        return new TabbedEnvironmentFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tabbed_environment, container, false);
        ButterKnife.bind(this, view);
        injectTabHostIntoAppBarLayout(inflater);
        setupTabHost();
        return view;
    }

    private void injectTabHostIntoAppBarLayout(LayoutInflater inflater) {
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.app_bar_layout);
        mTabHost = inflater.inflate(R.layout.tab_host, appBarLayout, false);
        appBarLayout.addView(mTabHost);
        mTabs = (TabLayout) getActivity().findViewById(R.id.tabs);
    }

    private void setupTabHost() {
        TabsAdapter adapter = getConfiguredAdapter();
        mViewPager.setAdapter(adapter);
        mTabs.setupWithViewPager(mViewPager);
    }

    @NonNull
    private TabsAdapter getConfiguredAdapter() {
        TabsAdapter adapter = new TabsAdapter(this.getActivity(), getChildFragmentManager());
        adapter.add(R.string.last_seven_days, LastSevenDaysFragment.newInstance());
        adapter.add(R.string.last_thirty_days, LastThirtyDaysFragment.newInstance());
        return adapter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        appBarLayout.removeView(mTabHost);
    }
}
