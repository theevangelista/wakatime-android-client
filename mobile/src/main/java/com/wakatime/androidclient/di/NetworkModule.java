package com.wakatime.androidclient.di;

import android.app.Application;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.squareup.picasso.Picasso;
import com.wakatime.androidclient.BuildConfig;
import com.wakatime.androidclient.R;
import com.wakatime.androidclient.api.ApiClient;
import com.wakatime.androidclient.support.NetworkConnectionWatcher;

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
    OkHttpClient okHttpClient(Cache cache) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY));
        if (BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(new StethoInterceptor());
        } else {
            builder.readTimeout(3500, TimeUnit.MILLISECONDS)
                    .writeTimeout(3500, TimeUnit.MILLISECONDS);
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
    ApiClient apiClient(Retrofit retrofit) {
        return retrofit.create(ApiClient.class);
    }
}
