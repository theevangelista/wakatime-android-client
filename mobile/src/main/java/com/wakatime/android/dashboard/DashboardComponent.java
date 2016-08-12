package com.wakatime.android.dashboard;

import com.wakatime.android.dashboard.stats.LastMonthFragment;
import com.wakatime.android.dashboard.stats.LastSevenDaysFragment;
import com.wakatime.android.dashboard.leaderboard.LeaderProfileFragment;
import com.wakatime.android.dashboard.leaderboard.LeaderboardFragment;
import com.wakatime.android.dashboard.project.ProjectFragment;
import com.wakatime.android.dashboard.project.SingleProjectFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Joao Pedro Evangelista
 */
@Singleton
@Component(modules = {DashboardModule.class})
public interface DashboardComponent {

    void inject(LastSevenDaysFragment lastSevenDaysFragment);

    void inject(ProjectFragment projectFragment);

    void inject(LeaderboardFragment leaderboardFragment);

    void inject(LeaderProfileFragment leaderProfileFragment);

    void inject(SingleProjectFragment singleProjectFragment);

    void inject(LastMonthFragment lastMonthFragment);
}
