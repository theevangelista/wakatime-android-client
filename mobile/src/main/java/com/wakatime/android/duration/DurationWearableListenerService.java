package com.wakatime.android.duration;

import android.app.Notification;
import android.app.NotificationManager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.wakatime.android.api.WakatimeClient;
import com.wakatime.android.support.net.HeaderFormatter;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import javax.inject.Inject;

import io.realm.Realm;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author Joao Pedro Evangelista
 */
public class DurationWearableListenerService extends WearableListenerService {

    public static final String DURATIONS_PATH = "/durations";
    private static final int NOTIFICATION_ID = 2;
    @Inject
    WakatimeClient mClient;

    @Inject
    Realm mRealm;

    private GoogleApiClient mGoogleApiClient;

    private NotificationManager mNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.w("Started Wear Service");

        mGoogleApiClient = new GoogleApiClient.Builder(this).build();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        //receive a message asking for duration
        String id = messageEvent.getSourceNodeId();
        if (DURATIONS_PATH.equals(messageEvent.getPath())) {
            //process a duration call
            mClient.fetchDurations(HeaderFormatter.get(mRealm), dateAsString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .map(DurationWrapper::getData)
                    .map(TotalTimeCalculator::calc)
                    .map(this::formatTime)
                    .subscribe(time -> Wearable.MessageApi.sendMessage(
                            mGoogleApiClient,
                            DURATIONS_PATH, id, time.getBytes()
                    ).setResultCallback(sendMessageResult -> {
                        if (!sendMessageResult.getStatus().isSuccess()) {
                            alertAboutFailure();
                            Timber.e("Could not delivery the message to wear, \n Status: %s \n Message: %s",
                                    sendMessageResult.getStatus().getStatusCode(),
                                    sendMessageResult.getStatus().getStatusMessage());
                        } else {
                            Timber.d("Successful delivery to wear");
                        }
                    }), error -> Timber.e(error, "Failed to process the duration request"));

        }
    }

    private void alertAboutFailure() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            Notification notification = new Notification.Builder(this)
                    .setContentTitle("Failed to send data to wear")
                    .build();
            mNotificationManager.notify(NOTIFICATION_ID, notification);
        }
    }


    private String dateAsString() {
        return LocalDate.now().format(DateTimeFormatter.ISO_DATE);
    }

    private String formatTime(long time) {
        return LocalTime.ofNanoOfDay(time).format(DateTimeFormatter.ISO_LOCAL_TIME);
    }

}
