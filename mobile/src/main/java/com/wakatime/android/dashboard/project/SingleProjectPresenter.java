package com.wakatime.android.dashboard.project;

import com.wakatime.android.support.presenter.WithBinding;

/**
 * @author Joao Pedro Evangelista
 */
public interface SingleProjectPresenter extends WithBinding<SingleProjectViewModel> {

    void onInit(String project);

    void onFinish();
}
