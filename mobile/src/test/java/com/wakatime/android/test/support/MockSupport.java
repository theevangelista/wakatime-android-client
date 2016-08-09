package com.wakatime.android.test.support;

import com.wakatime.android.api.WakatimeClient;
import com.wakatime.android.support.NetworkConnectionWatcher;

import io.realm.Realm;

import static com.wakatime.android.test.support.FakeSupport.fakeAuthKey;
import static com.wakatime.android.test.support.FakeSupport.fakeSevenDaysStats;
import static com.wakatime.android.test.support.FakeSupport.wrapper;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static rx.Observable.just;

/**
 * Provide several utilities methods to mock instances
 *
 * @author Joao Pedro Evangelista
 */
public class MockSupport {

    public static Realm mockRealm() {
        Realm realm = mock(Realm.class);
        when(Realm.getDefaultInstance()).thenReturn(realm);
        return realm;
    }

    public static NetworkConnectionWatcher mockWatcherWithConn() {
        NetworkConnectionWatcher watcher = mock(NetworkConnectionWatcher.class);
        when(watcher.isNetworkAvailable()).thenReturn(true);
        return watcher;
    }

    public static NetworkConnectionWatcher mockWatcherWithoutConn() {
        NetworkConnectionWatcher watcher = mock(NetworkConnectionWatcher.class);
        when(watcher.isNetworkAvailable()).thenReturn(false);
        return watcher;
    }

    public static WakatimeClient mockWakatimeClient() {
        WakatimeClient client = mock(WakatimeClient.class);
        when(client.fetchLastSevenDays(fakeAuthKey())).thenReturn(just(wrapper(fakeSevenDaysStats())));
        return client;
    }
}
