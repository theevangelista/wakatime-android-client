package com.wakatime.android.dashboard.project;

import com.wakatime.android.dashboard.model.Stats;
import com.wakatime.android.support.view.ErrorHandler;
import com.wakatime.android.support.view.WithLoader;

/**
 * @author Joao Pedro Evangelista
 */
public interface SingleProjectViewModel extends WithLoader, ErrorHandler {

    void setData(Stats data);

    void setRotationCache(Stats data);
}
