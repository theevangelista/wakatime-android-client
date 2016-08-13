package com.wakatime.android.di;

import com.wakatime.android.support.NetworkConnectionWatcher;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

import static java.lang.String.format;

/**
 * @author Joao Pedro Evangelista
 */
class CacheUpgraderInterceptor implements Interceptor {

    private static final String CACHE_CONTROL = "Cache-Control";

    private static final String CACHE_AGE_FMT = "public, max-age=%s";
    private static final String STATE_FMT = "public, only-if-cached, max-stale=%s";

    private final NetworkConnectionWatcher mWatcher;

    CacheUpgraderInterceptor(NetworkConnectionWatcher watcher) {
        mWatcher = watcher;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response original = chain.proceed(chain.request());
        if (mWatcher.isNetworkAvailable()) {
            int max = 60; // read for 1 minute
            return original.newBuilder()
                .header(CACHE_CONTROL, format(CACHE_AGE_FMT, max))
                .build();
        } else {
            int maxStale = 86400; // 1 day
            return original.newBuilder()
                .header(CACHE_CONTROL, format(STATE_FMT, maxStale))
                .build();
        }
    }
}
