package com.wakatime.android.api;

import com.wakatime.android.dashboard.leaderboard.LeaderWrapper;
import com.wakatime.android.dashboard.model.Stats;
import com.wakatime.android.dashboard.model.Wrapper;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author Joao Pedro Evangelista
 */

public interface WakatimeClient {

    @GET("users/current")
    Observable<Wrapper<User>> fetchUser(@Header("Authorization") String authorizationKey);

    @GET("users/current/stats/last_7_days")
    Observable<Wrapper<Stats>> fetchLastSevenDays(@Header("Authorization") String authorizationKey);

    @GET("users/current/stats/last_7_days")
    Observable<Wrapper<Stats>> fetchProjectLastSevenDays(
            @Header("Authorization") String authorizationKey,
            @Query("project") String projectName);

    @GET("leaders")
    Observable<LeaderWrapper> fetchLeaders(@Header("Authorization") String authorizationKey);
}
