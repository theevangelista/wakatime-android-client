package com.wakatime.androidclient.dashboard.programming;

import com.wakatime.androidclient.api.ApiClient;
import com.wakatime.androidclient.support.net.HeaderFormatter;
import com.wakatime.androidclient.dashboard.model.Wrapper;

import io.realm.Realm;
import rx.Scheduler;
import rx.Subscription;
import timber.log.Timber;

import static java.lang.String.format;

/**
 * @author Joao Pedro Evangelista
 */
public class DefaultProgrammingPresenter implements ProgrammingPresenter {

    private final Realm realm;

    private final ApiClient apiClient;

    private final Scheduler uiScheduler;

    private final Scheduler ioScheduler;

    private ViewModel viewModel;

    private Subscription tracker;

    public DefaultProgrammingPresenter(Realm realm, ApiClient apiClient,
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
                .subscribe(
                        data -> {
                            viewModel.setData(data);
                            viewModel.setRotationCache(data);
                        },
                        throwable -> Timber.e(throwable, "Error while fetching data")
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
