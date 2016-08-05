package com.wakatime.androidclient.dashboard.programming;

import com.wakatime.androidclient.support.presenter.WithBinding;

/**
 * @author Joao Pedro Evangelista
 */
public interface ProgrammingPresenter extends WithBinding<ViewModel> {

    void onInit();

    void onFinish();
}
