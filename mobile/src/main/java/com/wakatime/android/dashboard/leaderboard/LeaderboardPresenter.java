package com.wakatime.android.dashboard.leaderboard;

import com.wakatime.android.support.presenter.WithBinding;

/**
 * @author Joao Pedro Evangelista
 */
public interface LeaderboardPresenter extends WithBinding<ViewModel> {

    void onInit();

    void onFinish();
}
