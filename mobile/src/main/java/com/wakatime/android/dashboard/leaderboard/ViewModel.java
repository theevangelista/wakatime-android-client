package com.wakatime.android.dashboard.leaderboard;

import com.wakatime.android.support.view.WithLoader;

import java.util.List;

/**
 * @author Joao Pedro Evangelista
 */
interface ViewModel extends WithLoader {

    void setData(List<Leader> leaders);

    void setRotationCache(List<Leader> leaders);
}
