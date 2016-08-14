package com.wakatime.android.dashboard.leaderboard;

import com.wakatime.android.api.WakatimeClient;
import com.wakatime.android.support.net.HeaderFormatter;

import io.realm.Realm;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import timber.log.Timber;

/**
 * @author Joao Pedro Evangelista
 */
public class DefaultLeaderboardPresenter implements LeaderboardPresenter {

    private final Realm realm;

    private final WakatimeClient wakatimeClient;

    private final Scheduler ioScheduler;

    private final Scheduler uiScheduler;

    private ViewModel viewModel;

    private Subscription trackingSubscription;

    public DefaultLeaderboardPresenter(Realm realm, WakatimeClient wakatimeClient,
                                       Scheduler ioScheduler, Scheduler uiScheduler) {
        this.realm = realm;
        this.wakatimeClient = wakatimeClient;
        this.ioScheduler = ioScheduler;
        this.uiScheduler = uiScheduler;
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
        this.fetchData(() -> viewModel.hideLoader());
    }

    @Override
    public void onFinish() {
        if (this.trackingSubscription != null && !this.trackingSubscription.isUnsubscribed()) {
            this.trackingSubscription.unsubscribe();
        }
    }

    @Override
    public void onRefresh() {
        this.fetchData(() -> viewModel.completeRefresh());
    }

    private void fetchData(Action0 terminator) {
        this.trackingSubscription = this.wakatimeClient.fetchLeaders(HeaderFormatter.get(realm))
            .observeOn(uiScheduler)
            .subscribeOn(ioScheduler)
            .doOnTerminate(terminator)
            .map(LeaderWrapper::getData)
            .map(leaders -> leaders.subList(0, 21))
            .doOnError(viewModel::notifyError)
            .subscribe(leaders -> {
                viewModel.setData(leaders);
                viewModel.setRotationCache(leaders);
            }, error -> Timber.e(error, "Error fetching leaders"));
    }
}
