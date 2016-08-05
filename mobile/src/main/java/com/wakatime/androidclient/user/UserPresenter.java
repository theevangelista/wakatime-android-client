package com.wakatime.androidclient.user;

import com.wakatime.androidclient.support.presenter.WithBinding;

/**
 * @author Joao Pedro Evangelista
 */
public interface UserPresenter extends WithBinding<ViewModel> {

    void saveUserData(String key);

    void checkIfKeyPresent();
}
