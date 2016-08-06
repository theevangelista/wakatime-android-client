package com.wakatime.android.dashboard;

import com.wakatime.android.dashboard.environment.EnvironmentFragment;
import com.wakatime.android.dashboard.leaderboard.LeaderProfileFragment;
import com.wakatime.android.dashboard.leaderboard.LeaderboardFragment;
import com.wakatime.android.dashboard.project.ProjectFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Joao Pedro Evangelista
 */
@Singleton
@Component(modules = {DashboardModule.class})
public interface DashboardComponent {

    void inject(EnvironmentFragment environmentFragment);

    void inject(ProjectFragment projectFragment);

    void inject(LeaderboardFragment leaderboardFragment);

    void inject(LeaderProfileFragment leaderProfileFragment);
}
