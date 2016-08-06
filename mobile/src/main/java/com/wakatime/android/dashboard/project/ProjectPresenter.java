package com.wakatime.android.dashboard.project;

import com.wakatime.android.support.presenter.WithBinding;

/**
 * @author Joao Pedro Evangelista
 */
public interface ProjectPresenter extends WithBinding<ViewModel> {

    void onInit();

    void onFinish();

}
