package com.wakatime.androidclient.dashboard;

import com.wakatime.androidclient.api.ApiClient;
import com.wakatime.androidclient.dashboard.programming.DefaultProgrammingPresenter;
import com.wakatime.androidclient.dashboard.programming.ProgrammingPresenter;
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
    ProgrammingPresenter programmingPresenter(Realm realm, ApiClient apiClient,
                                              @IOScheduler Scheduler ioScheduler,
                                              @UIScheduler Scheduler uiScheduler) {
        return new DefaultProgrammingPresenter(realm, apiClient, ioScheduler, uiScheduler);
    }
}
