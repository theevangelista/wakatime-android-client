package com.wakatime.androidclient.user;

import com.wakatime.androidclient.support.view.WithLoader;

import java.util.Map;

/**
 * @author Joao Pedro Evangelista
 */
interface ViewModel extends WithLoader {

    void sendUserToDashboard();

    /**
     * Set the errors occurred on validation
     * @param errors a map contaning the name of error and the string id of it
     */
    void setErrors(Map<String, Integer> errors);

    void showError();
}
