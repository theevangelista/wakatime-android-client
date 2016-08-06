package com.wakatime.android.dashboard;

import com.wakatime.android.support.Func0;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;

/**
 * @author Joao Pedro Evangelista
 */
@Singleton
public class LogoutHandler {

    private final Realm realm;

    @Inject
    public LogoutHandler(Realm realm) {
        this.realm = realm;
    }

    public void clearData(Func0 callback) {
        realm.executeTransactionAsync(r -> r.deleteAll(), callback::apply);
    }
}
