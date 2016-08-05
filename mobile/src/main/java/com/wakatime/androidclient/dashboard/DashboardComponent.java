package com.wakatime.androidclient.dashboard;

import com.wakatime.androidclient.dashboard.programming.ProgrammingFragment;
import com.wakatime.androidclient.dashboard.project.ProjectFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Joao Pedro Evangelista
 */
@Singleton
@Component(modules = {DashboardModule.class})
public interface DashboardComponent {

    void inject(ProgrammingFragment programmingFragment);

    void inject(ProjectFragment projectFragment);
}
