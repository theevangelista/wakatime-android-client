package com.wakatime.android.support.presenter;

/**
 * @author Joao Pedro Evangelista
 */
public interface LifecycleAware {

    void onInit();

    void onFinish();

    void onRefresh();
}
