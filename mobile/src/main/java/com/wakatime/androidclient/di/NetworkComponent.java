package com.wakatime.androidclient.di;

import com.wakatime.androidclient.dashboard.DashboardActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Joao Pedro Evangelista
 */
@Singleton
@Component(modules = NetworkModule.class)
public interface NetworkComponent {
    void inject(DashboardActivity dashboardActivity);
}
