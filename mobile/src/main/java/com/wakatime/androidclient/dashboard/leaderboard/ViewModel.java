package com.wakatime.androidclient.dashboard.leaderboard;

import com.wakatime.androidclient.support.view.WithLoader;

import java.util.List;

/**
 * @author Joao Pedro Evangelista
 */
interface ViewModel extends WithLoader {

    void setData(List<Leader> leaders);

    void setRotationCache(List<Leader> leaders);
}
