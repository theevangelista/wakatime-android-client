package com.wakatime.android.duration;

import java.util.List;

/**
 * @author Joao Pedro Evangelista
 */

public final class TotalTimeCalculator {
    private TotalTimeCalculator() {
        //no instance
    }
    public static long calc(List<Duration> durations) {
        long result = 0;

        for (Duration duration : durations) {
            result += duration.getTime();
        }

        return result;
    }
}
