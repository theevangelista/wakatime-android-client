package com.wakatime.android.dashboard.environment;

import com.wakatime.android.api.WakatimeClient;
import com.wakatime.android.dashboard.model.Stats;
import com.wakatime.android.dashboard.model.Wrapper;
import com.wakatime.android.support.NetworkConnectionWatcher;
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

    private final WakatimeClient wakatimeClient;

    private final Scheduler uiScheduler;

    private final Scheduler ioScheduler;

    private final NetworkConnectionWatcher watcher;

    private ViewModel viewModel;

    private Subscription tracker;

    public DefaultEnvironmentPresenter(Realm realm, WakatimeClient wakatimeClient,
                                       Scheduler ioScheduler,
                                       Scheduler uiScheduler, NetworkConnectionWatcher watcher) {
        this.realm = realm;
        this.wakatimeClient = wakatimeClient;
        this.ioScheduler = ioScheduler;
        this.uiScheduler = uiScheduler;
        this.watcher = watcher;
    }


    @Override
    public void onInit() {
        viewModel.showLoader();
        if (watcher.isNetworkAvailable()) {
            this.tracker = this.wakatimeClient.fetchLastSevenDays(HeaderFormatter.get(realm))
                    .observeOn(uiScheduler)
                    .subscribeOn(ioScheduler)
                    .doOnTerminate(() -> viewModel.hideLoader())
                    .map(Wrapper::getData)
                    .doOnError(viewModel::notifyError)
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
            viewModel.setData(fetchFromDatabase());
            viewModel.hideLoader();
        }

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

    private Stats fetchFromDatabase() {
        return realm.where(Stats.class).findFirst();
    }
}
