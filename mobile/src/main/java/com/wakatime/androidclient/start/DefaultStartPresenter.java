package com.wakatime.androidclient.start;

import com.wakatime.androidclient.api.ApiKey;
import com.wakatime.androidclient.support.HeaderFormatter;
import com.wakatime.androidclient.R;
import com.wakatime.androidclient.api.User;
import com.wakatime.androidclient.api.ApiClient;
import com.wakatime.androidclient.dashboard.model.Wrapper;
import com.wakatime.androidclient.support.NetworkConnectionWatcher;

import io.realm.Realm;
import rx.Scheduler;

import static java.util.Collections.singletonMap;

/**
 * @author Joao Pedro Evangelista
 */
public class DefaultStartPresenter implements StartPresenter {

    private ViewModel view;

    private final Realm realm;

    private final ApiClient apiClient;

    private final NetworkConnectionWatcher watcher;

    private final rx.Scheduler uiScheduler;

    private final rx.Scheduler ioScheduler;

    public DefaultStartPresenter(Realm realm, ApiClient apiClient, NetworkConnectionWatcher watcher,
                                 Scheduler ioScheduler, Scheduler uiScheduler) {
        this.realm = realm;
        this.apiClient = apiClient;
        this.watcher = watcher;
        this.ioScheduler = ioScheduler;
        this.uiScheduler = uiScheduler;
    }

    @Override
    public void saveUserData(String key) {
        if (isKeyValid(key)) {
            view.showLoader();
            ApiKey apiKey = new ApiKey();
            apiKey.setKey(key);
            realm.beginTransaction();
            realm.delete(ApiKey.class);
            realm.copyToRealmOrUpdate(apiKey);
            realm.commitTransaction();
            loadUserData();
        }
    }

    private void loadUserData() {
        User first = realm.where(User.class).findFirst();
        if (watcher.isNetworkAvailable()) {
            this.apiClient.fetchUser(HeaderFormatter.get(realm))
                    .observeOn(uiScheduler)
                    .subscribeOn(ioScheduler)
                    .map(Wrapper::getData)
                    .doOnTerminate(() -> view.hideLoader())
                    .subscribe(user -> {
                        realm.beginTransaction();
                        realm.delete(User.class);
                        realm.copyToRealmOrUpdate(user);
                        realm.commitTransaction();
                        view.sendUserToDashboard();
                    }, error -> view.showError());
        } else {
            if (first == null) {
                view.hideLoader();
                view.showError();
            } else  {
                view.sendUserToDashboard();
            }
        }

    }

    private boolean isKeyValid(String key) {
        if (key == null || key.length() != 36) {
            view.setErrors(singletonMap("key_out_of_bounds", R.string.errors_key_out_of_bounds));
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void bind(ViewModel viewModel) {
        this.view = viewModel;
    }

    @Override
    public void unbind() {
        this.view = null;
    }

    @Override
    public void checkIfKeyPresent() {
        long count = realm.where(ApiKey.class).count();
        if (count == 1) {
            view.sendUserToDashboard();
        }
    }
}
