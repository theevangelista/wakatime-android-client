package com.wakatime.androidclient.user;

import com.wakatime.androidclient.api.ApiClient;
import com.wakatime.androidclient.di.IOScheduler;
import com.wakatime.androidclient.di.NetworkModule;
import com.wakatime.androidclient.di.UIScheduler;
import com.wakatime.androidclient.support.context.NetworkConnectionWatcher;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import rx.Scheduler;

/**
 * @author Joao Pedro Evangelista
 */
@Module(includes = NetworkModule.class)
public class UserModule {

    @Provides
    @Singleton
    UserPresenter apiKeyPresenter(Realm realm, ApiClient apiClient,
                                  NetworkConnectionWatcher watcher,
                                  @IOScheduler Scheduler ioScheduler,
                                  @UIScheduler Scheduler uiScheduler) {
        return new DefaultUserPresenter(realm, apiClient, watcher, ioScheduler, uiScheduler);
    }
}
