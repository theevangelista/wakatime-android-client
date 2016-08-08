package com.wakatime.android.user;

import com.wakatime.android.api.WakatimeClient;
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
public class UserModule {

    @Provides
    @Singleton
    UserPresenter apiKeyPresenter(Realm realm, WakatimeClient wakatimeClient,
                                  NetworkConnectionWatcher watcher,
                                  @IOScheduler Scheduler ioScheduler,
                                  @UIScheduler Scheduler uiScheduler) {
        return new DefaultUserPresenter(realm, wakatimeClient, watcher, ioScheduler, uiScheduler);
    }
}
