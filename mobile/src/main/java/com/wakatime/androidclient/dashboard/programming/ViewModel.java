package com.wakatime.androidclient.dashboard.programming;

import com.wakatime.androidclient.support.WithLoader;
import com.wakatime.androidclient.dashboard.model.DataObject;

/**
 * @author Joao Pedro Evangelista
 */
interface ViewModel extends WithLoader {

    void setData(DataObject data);

    void setRotationCache(DataObject data);
}
