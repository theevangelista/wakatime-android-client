package com.wakatime.androidclient.dashboard.environment;

import com.wakatime.androidclient.dashboard.model.Stats;
import com.wakatime.androidclient.support.view.WithLoader;

/**
 * @author Joao Pedro Evangelista
 */
interface ViewModel extends WithLoader {

    void setData(Stats data);

    void setRotationCache(Stats data);
}
