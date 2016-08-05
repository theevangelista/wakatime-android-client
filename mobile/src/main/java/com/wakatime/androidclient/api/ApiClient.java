package com.wakatime.androidclient.api;

import com.wakatime.androidclient.dashboard.model.Stats;
import com.wakatime.androidclient.dashboard.model.Wrapper;

import retrofit2.http.GET;
import retrofit2.http.Header;
import rx.Observable;

/**
 * @author Joao Pedro Evangelista
 */

public interface ApiClient {

    @GET("users/current")
    Observable<Wrapper<User>> fetchUser(@Header("Authorization") String authorizationKey);

    @GET("users/current/stats/last_7_days")
    Observable<Wrapper<Stats>> fetchLastSevenDays(@Header("Authorization") String authorizationKey);
}
