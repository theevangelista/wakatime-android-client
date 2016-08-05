package com.wakatime.androidclient.di;

import com.wakatime.androidclient.start.StartActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Joao Pedro Evangelista
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
}
