package com.wakatime.androidclient.start;

import com.wakatime.androidclient.api.ApiClient;
import com.wakatime.androidclient.di.IOScheduler;
import com.wakatime.androidclient.di.NetworkModule;
import com.wakatime.androidclient.di.UIScheduler;
import com.wakatime.androidclient.support.NetworkConnectionWatcher;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import rx.Scheduler;

/**
 * @author Joao Pedro Evangelista
 */
@Module(includes = NetworkModule.class)
public class StartModule {

    @Provides
    @Singleton
    StartPresenter apiKeyPresenter(Realm realm, ApiClient apiClient,
                                   NetworkConnectionWatcher watcher,
                                   @IOScheduler Scheduler ioScheduler,
                                   @UIScheduler Scheduler uiScheduler) {
        return new DefaultStartPresenter(realm, apiClient, watcher, ioScheduler, uiScheduler);
    }
}
