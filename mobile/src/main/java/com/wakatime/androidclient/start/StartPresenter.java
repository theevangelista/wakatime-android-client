package com.wakatime.androidclient.start;

import com.wakatime.androidclient.support.WithBinding;

/**
 * @author Joao Pedro Evangelista
 */
public interface StartPresenter extends WithBinding<ViewModel> {

    void saveUserData(String key);

    void checkIfKeyPresent();
}
