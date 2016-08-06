package com.wakatime.android.dashboard.project;

import com.wakatime.android.api.ApiClient;
import com.wakatime.android.dashboard.model.Project;
import com.wakatime.android.dashboard.model.Stats;
import com.wakatime.android.dashboard.model.Wrapper;
import com.wakatime.android.support.NetworkConnectionWatcher;
import com.wakatime.android.support.net.HeaderFormatter;

import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import rx.Scheduler;
import rx.Subscription;
import timber.log.Timber;

import static com.wakatime.android.util.Collections.copyIterator;

/**
 * @author Joao Pedro Evangelista
 */
public class DefaultProjectPresenter implements ProjectPresenter {

    private final Realm realm;

    private final ApiClient apiClient;

    private final Scheduler ioScheduler;

    private final Scheduler uiScheduler;

    private final NetworkConnectionWatcher watcher;

    private ViewModel viewModel;

    private Subscription trackingSubscription;

    public DefaultProjectPresenter(Realm realm, ApiClient apiClient,
                                   Scheduler ioScheduler, Scheduler uiScheduler,
                                   NetworkConnectionWatcher watcher) {
        this.realm = realm;
        this.apiClient = apiClient;
        this.ioScheduler = ioScheduler;
        this.uiScheduler = uiScheduler;
        this.watcher = watcher;
    }

    @Override
    public void onInit() {
        viewModel.showLoader();
        if (watcher.isNetworkAvailable()) {
            this.trackingSubscription = apiClient.fetchLastSevenDays(HeaderFormatter.get(realm))
                    .subscribeOn(ioScheduler)
                    .observeOn(uiScheduler)
                    .doOnTerminate(() -> viewModel.hideLoader())
                    .map(Wrapper::getData)
                    .map(Stats::getProjects)
                    .map(projects -> copyIterator(projects.iterator()))
                    .onErrorReturn(error -> fetchFromDatabase())
                    .subscribe(projects -> {
                        viewModel.setProjects(projects);
                        viewModel.setRotationCache(projects);

                        realm.beginTransaction();
                        realm.delete(Project.class);
                        realm.commitTransaction();

                        realm.beginTransaction();
                        realm.insert(projects);
                        realm.commitTransaction();
                    }, error -> Timber.e("Error fetching projects", error));
        } else {
            viewModel.setProjects(fetchFromDatabase());
            viewModel.hideLoader();
        }


    }

    @Override
    public void onFinish() {
        if (this.trackingSubscription != null &&
                !this.trackingSubscription.isUnsubscribed()) {
            this.trackingSubscription.unsubscribe();
        }
    }

    @Override
    public void bind(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void unbind() {
        this.viewModel = null;
    }


    private List<Project> fetchFromDatabase() {
        Iterator<Project> name = realm.where(Project.class).findAllSorted("name").iterator();
        return copyIterator(name);
    }
}
