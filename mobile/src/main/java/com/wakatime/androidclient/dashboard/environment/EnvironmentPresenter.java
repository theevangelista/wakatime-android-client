package com.wakatime.androidclient.dashboard.environment;

import com.wakatime.androidclient.support.presenter.WithBinding;

/**
 * @author Joao Pedro Evangelista
 */
public interface EnvironmentPresenter extends WithBinding<ViewModel> {

    void onInit();

    void onFinish();
}
