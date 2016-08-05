package com.wakatime.androidclient.dashboard.leaderboard;

import com.wakatime.androidclient.support.presenter.WithBinding;

/**
 * @author Joao Pedro Evangelista
 */
public interface LeaderboardPresenter extends WithBinding<ViewModel> {

    void onInit();

    void onFinish();
}
