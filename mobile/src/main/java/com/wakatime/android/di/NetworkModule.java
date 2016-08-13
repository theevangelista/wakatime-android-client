package com.wakatime.android.di;

import android.app.Application;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.squareup.picasso.Picasso;
import com.wakatime.android.BuildConfig;
import com.wakatime.android.R;
import com.wakatime.android.api.WakatimeClient;
import com.wakatime.android.support.NetworkConnectionWatcher;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * @author Joao Pedro Evangelista
 */
@Module(includes = ApplicationModule.class)
public class NetworkModule {

    @Provides
    @Singleton
    Cache cache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        return new Cache(application.getCacheDir(), cacheSize);
    }

    @Provides
    @Singleton
    ObjectMapper objectMapper() {
        return new ObjectMapper()
            .setPropertyNamingStrategy(new PropertyNamingStrategy.SnakeCaseStrategy());
    }

    @Provides
    @Singleton
    OkHttpClient okHttpClient(Cache cache, NetworkConnectionWatcher watcher) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS) // api is slow when getting today's durations
            .cache(cache)
            .addInterceptor(new CacheUpgraderInterceptor(watcher))
            .addInterceptor(new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.HEADERS));
        if (BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(new StethoInterceptor());
        }


        return builder.build();
    }

    @Provides
    @Singleton
    Retrofit retrofit(Application application, ObjectMapper mapper, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
            .baseUrl(application.getString(R.string.api))
            .client(okHttpClient)
            .addConverterFactory(JacksonConverterFactory.create(mapper))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();
    }

    @Provides
    @Singleton
    NetworkConnectionWatcher networkConnectionWatcher(Application application) {
        return new NetworkConnectionWatcher(application);
    }

    @Provides
    @Singleton
    Picasso picasso(Application application) {
        return Picasso.with(application);
    }

    @Provides
    @Singleton
    WakatimeClient apiClient(Retrofit retrofit) {
        return retrofit.create(WakatimeClient.class);
    }
}
