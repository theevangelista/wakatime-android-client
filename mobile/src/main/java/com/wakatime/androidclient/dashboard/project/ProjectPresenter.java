package com.wakatime.androidclient.dashboard.project;

import com.wakatime.androidclient.support.presenter.WithBinding;

/**
 * @author Joao Pedro Evangelista
 */
public interface ProjectPresenter extends WithBinding<ViewModel> {

    void onInit();

    void onFinish();

}
