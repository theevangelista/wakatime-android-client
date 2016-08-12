package com.wakatime.android.dashboard.environment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wakatime.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabbedEnvironmentFragment extends Fragment {

    public static final String KEY = "tabbed-env-fragment";

    @BindView(R.id.tab_host)
    FragmentTabHost mTabHost;

    public TabbedEnvironmentFragment() {
        // Required empty public constructor
    }

    public static TabbedEnvironmentFragment newInstance() {
        return new TabbedEnvironmentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tabbed_environment, container, false);
        ButterKnife.bind(this, view);
        setupTabHost();
        return view;
    }

    private void setupTabHost() {
        mTabHost.setup(this.getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(
            mTabHost.newTabSpec("last_7_days")
                .setIndicator(getString(R.string.last_seven_days)), LastSevenDaysFragment.class, Bundle.EMPTY
        );
    }
}
