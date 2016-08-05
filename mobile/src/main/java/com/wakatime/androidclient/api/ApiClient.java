package com.wakatime.androidclient.api;

import com.wakatime.androidclient.dashboard.model.DataObject;
import com.wakatime.androidclient.dashboard.model.Wrapper;

import retrofit2.http.Header;
import rx.Observable;

import retrofit2.http.GET;

/**
 * @author Joao Pedro Evangelista
 */

public interface ApiClient {

    @GET("users/current")
    Observable<Wrapper<User>> fetchUser(@Header("Authorization") String authorizationKey);

    @GET("users/current/stats/last_7_days")
    Observable<Wrapper<DataObject>> fetchLastSevenDays(@Header("Authorization") String authorizationKey);
}
