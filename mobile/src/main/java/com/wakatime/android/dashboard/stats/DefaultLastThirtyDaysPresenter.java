package com.wakatime.android.dashboard.stats;

import com.wakatime.android.api.WakatimeClient;
import com.wakatime.android.dashboard.model.Wrapper;
import com.wakatime.android.support.NetworkConnectionWatcher;
import com.wakatime.android.support.net.HeaderFormatter;

import io.realm.Realm;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import timber.log.Timber;

/**
 * @author Joao Pedro Evangelista
 */
public class DefaultLastThirtyDaysPresenter extends StatsPresenter implements LastThirtyDaysPresenter {

    private final Realm realm;

    private final WakatimeClient wakatimeClient;

    private final Scheduler ioScheduler;

    private final Scheduler uiScheduler;

    private ViewModel viewModel;

    private Subscription mSubscription;

    public DefaultLastThirtyDaysPresenter(Realm realm, WakatimeClient wakatimeClient,
                                          Scheduler ioScheduler, Scheduler uiScheduler) {
        this.realm = realm;
        this.wakatimeClient = wakatimeClient;
        this.ioScheduler = ioScheduler;
        this.uiScheduler = uiScheduler;
    }


    @Override
    public void onInit() {
        viewModel.showLoader();
        fetchData(() -> viewModel.hideLoader());
    }

    @Override
    public void onFinish() {
        cancelSubscription(mSubscription);
    }

    @Override
    public void onRefresh() {
        fetchData(() -> viewModel.completeRefresh());
    }


    @Override
    public void bind(LastThirtyDaysViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void unbind() {
        this.viewModel = null;
    }

    private void fetchData(Action0 termination) {
        this.mSubscription = this.wakatimeClient.fetchLastMonth(HeaderFormatter.get(realm))
            .observeOn(uiScheduler)
            .subscribeOn(ioScheduler)
            .map(Wrapper::getData)
            .doOnError(viewModel::notifyError)
            .doOnTerminate(termination)
            .subscribe(
                data -> {
                    viewModel.setData(data);
                    viewModel.setRotationCache(data);
                }, throwable -> Timber.e(throwable, "Error while fetching data")
            );

    }
}
