package com.wakatime.android.dashboard;

import com.wakatime.android.api.ApiClient;
import com.wakatime.android.dashboard.environment.DefaultEnvironmentPresenter;
import com.wakatime.android.dashboard.environment.EnvironmentPresenter;
import com.wakatime.android.dashboard.leaderboard.DefaultLeaderboardPresenter;
import com.wakatime.android.dashboard.leaderboard.LeaderboardPresenter;
import com.wakatime.android.dashboard.project.DefaultProjectPresenter;
import com.wakatime.android.dashboard.project.ProjectPresenter;
import com.wakatime.android.di.IOScheduler;
import com.wakatime.android.di.NetworkModule;
import com.wakatime.android.di.UIScheduler;
import com.wakatime.android.support.NetworkConnectionWatcher;

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
                                              @UIScheduler Scheduler uiScheduler,
                                              NetworkConnectionWatcher watcher) {
        return new DefaultEnvironmentPresenter(realm, apiClient, ioScheduler, uiScheduler, watcher);
    }

    @Provides
    @Singleton
    ProjectPresenter projectPresenter(Realm realm, ApiClient apiClient,
                                      @IOScheduler Scheduler ioScheduler,
                                      @UIScheduler Scheduler uiScheduler,
                                      NetworkConnectionWatcher watcher) {
        return new DefaultProjectPresenter(realm, apiClient, ioScheduler, uiScheduler, watcher);
    }

    @Provides
    @Singleton
    LeaderboardPresenter leaderboardPresenter(Realm realm, ApiClient apiClient,
                                              @IOScheduler Scheduler ioScheduler,
                                              @UIScheduler Scheduler uiScheduler,
                                              NetworkConnectionWatcher watcher) {
        return new DefaultLeaderboardPresenter(realm, apiClient, ioScheduler, uiScheduler, watcher);
    }
}
