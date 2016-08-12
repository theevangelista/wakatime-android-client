package com.wakatime.android.dashboard.environment;

/**
 * @author Joao Pedro Evangelista
 */
public interface LastSevenDaysViewModel {
    void onLastSevenDaysInitialization();

    void onLastSevenDaysTermination();

    void onLastSevenDaysRefresh();
}
