package com.wakatime.android.di;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Joao Pedro Evangelista
 */
@Module
public class ApplicationModule {

    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides @Singleton
    SharedPreferences sharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    Realm realm() {
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder(application).build());
        return Realm.getDefaultInstance();
    }

    @Provides
    @Singleton
    LocalBroadcastManager localBroadcastManager() {
        return LocalBroadcastManager.getInstance(application);
    }

    @Provides
    @Singleton
    @UIScheduler
    Scheduler uiScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @Singleton
    @IOScheduler
    Scheduler ioScheduler() {
        return Schedulers.io();
    }

    @Provides
    Application application() {
        return this.application;
    }


}
