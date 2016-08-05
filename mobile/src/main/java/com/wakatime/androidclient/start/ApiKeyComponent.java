package com.wakatime.androidclient.start;

import com.wakatime.androidclient.di.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Joao Pedro Evangelista
 */
@Singleton
@Component(modules = {StartModule.class, ApplicationModule.class})
public interface ApiKeyComponent {

    void inject(StartActivity activity);
}
