package com.wakatime.androidclient;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.stetho.Stetho;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.squareup.leakcanary.LeakCanary;
import com.wakatime.androidclient.dashboard.DaggerDashboardComponent;
import com.wakatime.androidclient.dashboard.DashboardComponent;
import com.wakatime.androidclient.dashboard.DashboardModule;
import com.wakatime.androidclient.di.ApplicationComponent;
import com.wakatime.androidclient.di.ApplicationModule;
import com.wakatime.androidclient.di.DaggerApplicationComponent;
import com.wakatime.androidclient.di.DaggerNetworkComponent;
import com.wakatime.androidclient.di.NetworkComponent;
import com.wakatime.androidclient.di.NetworkModule;
import com.wakatime.androidclient.user.DaggerUserComponent;
import com.wakatime.androidclient.user.UserComponent;
import com.wakatime.androidclient.user.UserModule;

import timber.log.Timber;

/**
 * @author Joao Pedro Evangelista
 */
@SuppressWarnings("deprecation")
public class WakatimeApplication extends Application {

    private ApplicationModule applicationModule = new ApplicationModule(this);

    private NetworkModule networkModule = new NetworkModule();

    private ApplicationComponent applicationComponent;

    private UserComponent userComponent;

    private NetworkComponent networkComponent;

    private DashboardComponent dashboardComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        AndroidThreeTen.init(this);
        Stetho.initializeWithDefaults(this);
        Timber.plant(new Timber.DebugTree());
        this.registerApplicationComponent();
        this.registerApiKeyComponent();
        this.registerNetworkComponent();
        this.registerDashboardComponent();
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }


    private void registerApplicationComponent() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(applicationModule).build();
    }

    private void registerApiKeyComponent() {
        userComponent = DaggerUserComponent.builder()
                .applicationModule(applicationModule)
                .networkModule(networkModule)
                .userModule(new UserModule())
                .build();
    }

    private void registerNetworkComponent() {
        networkComponent = DaggerNetworkComponent.builder()
                .applicationModule(applicationModule)
                .networkModule(networkModule)
                .build();
    }

    private void registerDashboardComponent() {
        dashboardComponent = DaggerDashboardComponent.builder()
                .applicationModule(applicationModule)
                .networkModule(networkModule)
                .dashboardModule(new DashboardModule())
                .build();
    }

    public ApplicationComponent useApplicationComponent() {
        return applicationComponent;
    }

    public UserComponent useApiKeyComponent() {
        return userComponent;
    }

    public NetworkComponent useNetworkComponent() {
        return networkComponent;
    }

    public DashboardComponent useDashboardComponent() {
        return dashboardComponent;
    }
}
