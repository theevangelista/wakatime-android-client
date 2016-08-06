package com.wakatime.android.dashboard.environment;

import com.wakatime.android.api.ApiClient;
import com.wakatime.android.dashboard.model.Stats;
import com.wakatime.android.dashboard.model.Wrapper;
import com.wakatime.android.support.net.HeaderFormatter;

import io.realm.Realm;
import rx.Scheduler;
import rx.Subscription;
import timber.log.Timber;

/**
 * @author Joao Pedro Evangelista
 */
public class DefaultEnvironmentPresenter implements EnvironmentPresenter {

    private final Realm realm;

    private final ApiClient apiClient;

    private final Scheduler uiScheduler;

    private final Scheduler ioScheduler;

    private ViewModel viewModel;

    private Subscription tracker;

    public DefaultEnvironmentPresenter(Realm realm, ApiClient apiClient,
                                       Scheduler ioScheduler,
                                       Scheduler uiScheduler) {
        this.realm = realm;
        this.apiClient = apiClient;
        this.ioScheduler = ioScheduler;
        this.uiScheduler = uiScheduler;
    }


    @Override
    public void onInit() {
        viewModel.showLoader();
        this.tracker = this.apiClient.fetchLastSevenDays(HeaderFormatter.get(realm))
                .observeOn(uiScheduler)
                .subscribeOn(ioScheduler)
                .doOnTerminate(() -> viewModel.hideLoader())
                .map(Wrapper::getData)
                .onErrorReturn(throwable -> realm.where(Stats.class).findFirst())
                .subscribe(
                        data -> {
                            viewModel.setData(data);
                            viewModel.setRotationCache(data);

                            realm.beginTransaction();
                            realm.delete(Stats.class);
                            realm.commitTransaction();

                            realm.beginTransaction();
                            realm.insert(data);
                            realm.commitTransaction();

                        }, throwable -> Timber.e(throwable, "Error while fetching data")
                );
    }

    @Override
    public void onFinish() {
        if (this.tracker != null && !this.tracker.isUnsubscribed()) {
            this.tracker.unsubscribe();
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
}
