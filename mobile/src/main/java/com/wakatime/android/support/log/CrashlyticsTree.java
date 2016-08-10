package com.wakatime.android.support.log;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import timber.log.Timber;

/**
 * @author Joao Pedro Evangelista
 */
public class CrashlyticsTree extends Timber.Tree {
    private static String formatMessage(String message, Throwable throwable) {
        String traceAsString = Log.getStackTraceString(throwable);
        return String.format("%s. Nested exception is: %s", message, traceAsString);
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        if (priority == Log.ERROR) {
            Crashlytics.log(priority, tag, formatMessage(message, t));
        }
    }
}
