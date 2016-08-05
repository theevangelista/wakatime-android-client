package com.wakatime.androidclient.dashboard;

import com.wakatime.androidclient.api.ApiClient;
import com.wakatime.androidclient.dashboard.environment.DefaultEnvironmentPresenter;
import com.wakatime.androidclient.dashboard.environment.EnvironmentPresenter;
import com.wakatime.androidclient.dashboard.leaderboard.DefaultLeaderboardPresenter;
import com.wakatime.androidclient.dashboard.leaderboard.LeaderboardPresenter;
import com.wakatime.androidclient.dashboard.project.DefaultProjectPresenter;
import com.wakatime.androidclient.dashboard.project.ProjectPresenter;
import com.wakatime.androidclient.di.IOScheduler;
import com.wakatime.androidclient.di.NetworkModule;
import com.wakatime.androidclient.di.UIScheduler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import rx.Scheduler;

/**
 * @author Joao Pedro Evangelista
 */
@Module(includes = NetworkModule.class)
public class DashboardModule {


    @Provides
    @Singleton
    EnvironmentPresenter programmingPresenter(Realm realm, ApiClient apiClient,
                                              @IOScheduler Scheduler ioScheduler,
                                              @UIScheduler Scheduler uiScheduler) {
        return new DefaultEnvironmentPresenter(realm, apiClient, ioScheduler, uiScheduler);
    }

    @Provides
    @Singleton
    ProjectPresenter projectPresenter(Realm realm, ApiClient apiClient,
                                      @IOScheduler Scheduler ioScheduler,
                                      @UIScheduler Scheduler uiScheduler) {
        return new DefaultProjectPresenter(realm, apiClient, ioScheduler, uiScheduler);
    }

    @Provides
    @Singleton
    LeaderboardPresenter leaderboardPresenter(Realm realm, ApiClient apiClient,
                                              @IOScheduler Scheduler ioScheduler,
                                              @UIScheduler Scheduler uiScheduler) {
        return new DefaultLeaderboardPresenter(realm, apiClient, ioScheduler, uiScheduler);
    }
}
