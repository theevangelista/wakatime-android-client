package com.wakatime.android.dashboard.environment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wakatime.android.support.PositionedLinkedHashMap;

/**
 * @author Joao Pedro Evangelista
 */

class TabsAdapter extends FragmentPagerAdapter {

    private static final int MAX_FRAGS = 3;

    private final PositionedLinkedHashMap<Integer, Fragment> mFragmentMap = new PositionedLinkedHashMap<>(MAX_FRAGS);

    private Context mContext;

    TabsAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    void add(int titleId, Fragment target) {
        this.mFragmentMap.put(titleId, target);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentMap.getValue(position);
    }

    @Override
    public int getCount() {
        return mFragmentMap.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(mFragmentMap.getEntry(position).getKey());
    }
}
