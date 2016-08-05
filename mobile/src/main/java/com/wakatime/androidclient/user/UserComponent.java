package com.wakatime.androidclient.user;

import com.wakatime.androidclient.di.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Joao Pedro Evangelista
 */
@Singleton
@Component(modules = {UserModule.class, ApplicationModule.class})
public interface UserComponent {

    void inject(UserStartActivity activity);
}
