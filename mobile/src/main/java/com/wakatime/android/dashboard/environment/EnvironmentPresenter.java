package com.wakatime.android.dashboard.environment;

import com.wakatime.android.support.presenter.WithBinding;

/**
 * @author Joao Pedro Evangelista
 */
public interface EnvironmentPresenter extends WithBinding<ViewModel> {

    void onInit();

    void onFinish();
}
