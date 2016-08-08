package com.wakatime.android.dashboard.project;

import com.wakatime.android.dashboard.model.Project;
import com.wakatime.android.support.view.ErrorHandler;
import com.wakatime.android.support.view.WithLoader;

import java.util.List;

/**
 * @author Joao Pedro Evangelista
 */
interface ViewModel extends WithLoader, ErrorHandler {

    void setProjects(List<Project> projects);

    void setRotationCache(List<Project> projects);
}
