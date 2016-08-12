package com.wakatime.android.dashboard.stats;

import rx.Subscription;

/**
 * @author Joao Pedro Evangelista
 */
public class StatsPresenter {

    protected void cancelSubscription(Subscription... subscriptions) {
        for (Subscription subscription : subscriptions) {
            if (subscription != null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }
    }


}
