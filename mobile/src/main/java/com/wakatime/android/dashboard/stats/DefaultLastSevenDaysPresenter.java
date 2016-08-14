package com.wakatime.android.dashboard.stats;

import com.wakatime.android.api.WakatimeClient;
import com.wakatime.android.dashboard.model.Duration;
import com.wakatime.android.dashboard.model.DurationWrapper;
import com.wakatime.android.dashboard.model.Wrapper;
import com.wakatime.android.support.net.HeaderFormatter;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;

import io.realm.Realm;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import timber.log.Timber;

/**
 * @author Joao Pedro Evangelista
 */
public class DefaultLastSevenDaysPresenter extends StatsPresenter implements LastSevenDaysPresenter {

    private final Realm realm;

    private final WakatimeClient wakatimeClient;

    private final Scheduler uiScheduler;

    private final Scheduler ioScheduler;

    private LastSevenDaysViewModel viewModel;

    private Subscription tracker;

    private Subscription durationTracker;

    public DefaultLastSevenDaysPresenter(Realm realm, WakatimeClient wakatimeClient,
                                         Scheduler ioScheduler,
                                         Scheduler uiScheduler) {
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
        cancelSubscription(durationTracker, tracker);
    }

    @Override
    public void onRefresh() {
        fetchData(() -> viewModel.completeRefresh());
    }


    @Override
    public void bind(LastSevenDaysViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void unbind() {
        this.viewModel = null;
    }

    private long sumDurations(List<Duration> durations) {
        long time = 0;
        for (Duration duration : durations) {
            time += duration.getDuration();
        }
        return time;
    }

    private String formatTime(long time) {
        return LocalTime.ofSecondOfDay(time).format(DateTimeFormatter.ofPattern("HH:mm"));

    }

    private void fetchData(Action0 termination) {
        this.tracker = this.wakatimeClient.fetchLastSevenDays(HeaderFormatter.get(realm))
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

        this.durationTracker = this.wakatimeClient.fetchDurations(HeaderFormatter.get(realm),
            LocalDate.now().format(DateTimeFormatter.ISO_DATE))
            .observeOn(uiScheduler)
            .subscribeOn(ioScheduler)
            .map(DurationWrapper::getData)
            .map(this::sumDurations)
            .map(this::formatTime)
            .doOnError(err -> Timber.w(err, "Error during processing durations"))
            .onErrorReturn(error -> "Not available")
            .subscribe(time -> viewModel.setTodayTime(time), error ->
                Timber.w(error, "Error parsing time"));
    }
}
