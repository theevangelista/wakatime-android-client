package com.wakatime.android.dashboard.environment;

import com.wakatime.android.dashboard.model.Stats;
import com.wakatime.android.support.view.WithLoader;

/**
 * @author Joao Pedro Evangelista
 */
interface ViewModel extends WithLoader {

    void setData(Stats data);

    void setRotationCache(Stats data);
}
