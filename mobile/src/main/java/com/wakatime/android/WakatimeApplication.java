package com.wakatime.android;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.squareup.leakcanary.LeakCanary;
import com.wakatime.android.dashboard.DaggerDashboardComponent;
import com.wakatime.android.dashboard.DashboardComponent;
import com.wakatime.android.dashboard.DashboardModule;
import com.wakatime.android.di.ApplicationComponent;
import com.wakatime.android.di.ApplicationModule;
import com.wakatime.android.di.DaggerApplicationComponent;
import com.wakatime.android.di.DaggerNetworkComponent;
import com.wakatime.android.di.NetworkComponent;
import com.wakatime.android.di.NetworkModule;
import com.wakatime.android.support.log.CrashlyticsTree;
import com.wakatime.android.user.DaggerUserComponent;
import com.wakatime.android.user.UserComponent;
import com.wakatime.android.user.UserModule;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * @author Joao Pedro Evangelista
 */
@SuppressWarnings("deprecation")
public class WakatimeApplication extends Application {

    private Tracker mTracker;

    private ApplicationModule applicationModule = new ApplicationModule(this);

    private NetworkModule networkModule = new NetworkModule();

    private ApplicationComponent applicationComponent;

    private UserComponent userComponent;

    private NetworkComponent networkComponent;

    private DashboardComponent dashboardComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        LeakCanary.install(this);
        AndroidThreeTen.init(this);
        Stetho.initializeWithDefaults(this);
        installTimber();
        this.registerApplicationComponent();
        this.registerApiKeyComponent();
        this.registerNetworkComponent();
        this.registerDashboardComponent();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Lato-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    private void installTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashlyticsTree());
        }
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

    public synchronized Tracker getTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        mTracker.enableAdvertisingIdCollection(true);
        return mTracker;
    }
}
