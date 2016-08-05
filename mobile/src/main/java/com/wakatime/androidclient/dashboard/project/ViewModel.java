package com.wakatime.androidclient.dashboard.project;

import com.wakatime.androidclient.dashboard.model.Project;
import com.wakatime.androidclient.support.view.WithLoader;

import java.util.List;

/**
 * @author Joao Pedro Evangelista
 */
interface ViewModel extends WithLoader {

    void setProjects(List<Project> projects);

    void setRotationCache(List<Project> projects);
}
