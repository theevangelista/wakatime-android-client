package com.wakatime.android.user;

import com.wakatime.android.di.ApplicationModule;

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
