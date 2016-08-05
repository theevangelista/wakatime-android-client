package com.wakatime.androidclient.dashboard;

import com.wakatime.androidclient.dashboard.programming.ProgrammingFragment;
import com.wakatime.androidclient.di.ApplicationModule;
import com.wakatime.androidclient.di.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Joao Pedro Evangelista
 */
@Singleton
@Component(modules = {DashboardModule.class})
public interface DashboardComponent {

    void inject(ProgrammingFragment programmingFragment);
}
