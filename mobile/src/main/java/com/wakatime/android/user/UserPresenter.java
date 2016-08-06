package com.wakatime.android.user;

import com.wakatime.android.support.presenter.WithBinding;

/**
 * @author Joao Pedro Evangelista
 */
public interface UserPresenter extends WithBinding<ViewModel> {

    void saveUserData(String key);

    void checkIfKeyPresent();
}
