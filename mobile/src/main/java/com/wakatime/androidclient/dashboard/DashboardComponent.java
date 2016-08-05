package com.wakatime.androidclient.dashboard;

import com.wakatime.androidclient.dashboard.environment.EnvironmentFragment;
import com.wakatime.androidclient.dashboard.leaderboard.LeaderboardFragment;
import com.wakatime.androidclient.dashboard.project.ProjectFragment;

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
}
