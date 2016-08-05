package com.wakatime.androidclient.dashboard.project;

import com.wakatime.androidclient.api.ApiClient;
import com.wakatime.androidclient.dashboard.model.DataObject;
import com.wakatime.androidclient.dashboard.model.Project;
import com.wakatime.androidclient.dashboard.model.Wrapper;
import com.wakatime.androidclient.support.net.HeaderFormatter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import rx.Scheduler;
import rx.Subscription;
import timber.log.Timber;

/**
 * @author Joao Pedro Evangelista
 */
public class DefaultProjectPresenter implements ProjectPresenter {

    private final Realm realm;

    private final ApiClient apiClient;

    private final Scheduler ioScheduler;

    private final Scheduler uiScheduler;

    private ViewModel viewModel;

    private Subscription trackingSubscription;

    public DefaultProjectPresenter(Realm realm, ApiClient apiClient,
                                   Scheduler ioScheduler, Scheduler uiScheduler) {
        this.realm = realm;
        this.apiClient = apiClient;
        this.ioScheduler = ioScheduler;
        this.uiScheduler = uiScheduler;
    }

    @Override
    public void onInit() {
        viewModel.showLoader();
        this.trackingSubscription = apiClient.fetchLastSevenDays(HeaderFormatter.get(realm))
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .doOnTerminate(() -> viewModel.hideLoader())
                .map(Wrapper::getData)
                .map(DataObject::getProjects)
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

    private <T> List<T> copyIterator(Iterator<T> iterator) {
        List<T> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }


    private List<Project> fetchFromDatabase() {
        Iterator<Project> name = realm.where(Project.class).findAllSorted("name").iterator();
        return this.copyIterator(name);
    }
}
