package com.wakatime.androidclient;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.stetho.Stetho;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.wakatime.androidclient.dashboard.DaggerDashboardComponent;
import com.wakatime.androidclient.dashboard.DashboardComponent;
import com.wakatime.androidclient.dashboard.DashboardModule;
import com.wakatime.androidclient.di.ApplicationComponent;
import com.wakatime.androidclient.di.ApplicationModule;
import com.wakatime.androidclient.di.DaggerApplicationComponent;
import com.wakatime.androidclient.di.DaggerNetworkComponent;
import com.wakatime.androidclient.di.NetworkComponent;
import com.wakatime.androidclient.di.NetworkModule;
import com.wakatime.androidclient.start.ApiKeyComponent;
import com.wakatime.androidclient.start.StartModule;
import com.wakatime.androidclient.start.DaggerApiKeyComponent;

import timber.log.Timber;

/**
 * @author Joao Pedro Evangelista
 */
public class WakatimeApplication extends Application {

    private ApplicationModule applicationModule = new ApplicationModule(this);

    private NetworkModule networkModule = new NetworkModule();

    private ApplicationComponent applicationComponent;

    private ApiKeyComponent apiKeyComponent;

    private NetworkComponent networkComponent;

    private DashboardComponent dashboardComponent;

    @Override
    public void onCreate() {
        super.onCreate();
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
        apiKeyComponent = DaggerApiKeyComponent.builder()
                .applicationModule(applicationModule)
                .networkModule(networkModule)
                .startModule(new StartModule())
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

    public ApiKeyComponent useApiKeyComponent() {
        return apiKeyComponent;
    }

    public NetworkComponent useNetworkComponent() {
        return networkComponent;
    }

    public DashboardComponent useDashboardComponent() {
        return dashboardComponent;
    }
}
