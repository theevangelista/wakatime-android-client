package com.wakatime.android.dashboard.stats;

import com.wakatime.android.api.WakatimeClient;
import com.wakatime.android.dashboard.model.Stats;
import com.wakatime.android.dashboard.model.Wrapper;
import com.wakatime.android.support.NetworkConnectionWatcher;
import com.wakatime.android.support.net.HeaderFormatter;

import io.realm.Realm;
import io.realm.internal.IOException;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import timber.log.Timber;

/**
 * @author Joao Pedro Evangelista
 */
public class DefaultLastMonthPresenter extends StatsPresenter implements LastMonthPresenter {

    private final Realm realm;

    private final WakatimeClient wakatimeClient;

    private final Scheduler ioScheduler;

    private final Scheduler uiScheduler;

    private final NetworkConnectionWatcher watcher;

    private ViewModel viewModel;

    private Subscription sub;

    public DefaultLastMonthPresenter(Realm realm, WakatimeClient wakatimeClient,
                                     Scheduler ioScheduler, Scheduler uiScheduler,
                                     NetworkConnectionWatcher watcher) {
        this.realm = realm;
        this.wakatimeClient = wakatimeClient;
        this.ioScheduler = ioScheduler;
        this.uiScheduler = uiScheduler;
        this.watcher = watcher;
    }


    @Override
    public void onInit() {
        viewModel.showLoader();
        fetchData(() -> viewModel.hideLoader());
    }

    @Override
    public void onFinish() {
        cancelSubscription(sub);
    }

    @Override
    public void onRefresh() {
        fetchData(() -> viewModel.completeRefresh());
    }


    @Override
    public void bind(LastMonthViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void unbind() {
        this.viewModel = null;
    }

    private void fetchData(Action0 termination) {
        if (watcher.isNetworkAvailable()) {
            this.sub = this.wakatimeClient.fetchLastMonth(HeaderFormatter.get(realm))
                .observeOn(uiScheduler)
                .subscribeOn(ioScheduler)
                .map(Wrapper::getData)
                .doOnError(viewModel::notifyError)
                .doOnTerminate(termination)
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
        } else {
            viewModel.notifyError(new IOException("No connection available"));
            termination.call();
        }

    }
}
