package com.wakatime.android.dashboard.leaderboard;

import com.wakatime.android.api.ApiClient;
import com.wakatime.android.support.NetworkConnectionWatcher;
import com.wakatime.android.support.net.HeaderFormatter;

import java.util.List;

import io.realm.Realm;
import rx.Scheduler;
import rx.Subscription;
import timber.log.Timber;

import static com.wakatime.android.util.Collections.copyIterator;

/**
 * @author Joao Pedro Evangelista
 */
public class DefaultLeaderboardPresenter implements LeaderboardPresenter {

    private final Realm realm;

    private final ApiClient apiClient;

    private final Scheduler ioScheduler;

    private final Scheduler uiScheduler;

    private final NetworkConnectionWatcher watcher;

    private ViewModel viewModel;

    private Subscription trackingSubscription;

    public DefaultLeaderboardPresenter(Realm realm, ApiClient apiClient,
                                       Scheduler ioScheduler, Scheduler uiScheduler, NetworkConnectionWatcher watcher) {
        this.realm = realm;
        this.apiClient = apiClient;
        this.ioScheduler = ioScheduler;
        this.uiScheduler = uiScheduler;
        this.watcher = watcher;
    }


    @Override
    public void bind(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void unbind() {
        this.viewModel = null;
    }

    @Override
    public void onInit() {
        viewModel.showLoader();
        if (watcher.isNetworkAvailable()) {
            this.trackingSubscription = this.apiClient.fetchLeaders(HeaderFormatter.get(realm))
                    .observeOn(uiScheduler)
                    .subscribeOn(ioScheduler)
                    .doOnTerminate(() -> viewModel.hideLoader())

                    .map(LeaderWrapper::getData)
                    .map(leaders -> leaders.subList(0, 21))
                    .onErrorReturn(error -> {
                        Timber.e(error, "Error fetching most recent data, resuming with database");
                        return fetchFromDatabase();
                    })
                    .subscribe(leaders -> {
                        viewModel.setData(leaders);
                        viewModel.setRotationCache(leaders);
                        saveOnDatabase(leaders);
                    }, error -> Timber.e(error, "Error fetching leaders"));
        } else {
            viewModel.setData(fetchFromDatabase());
            viewModel.hideLoader();
        }
    }

    private void saveOnDatabase(List<Leader> leaders) {
        realm.executeTransaction(r -> r.delete(Leader.class));
        realm.executeTransaction(r -> r.copyToRealmOrUpdate(leaders));
    }

    private List<Leader> fetchFromDatabase() {
        return copyIterator(realm.where(Leader.class).findAll().iterator());
    }

    @Override
    public void onFinish() {

    }
}
