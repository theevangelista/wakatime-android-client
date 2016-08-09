package com.wakatime.android.duration;

import java.util.List;

import lombok.Data;

/**
 * @author Joao Pedro Evangelista
 */
@Data
public class DurationWrapper {
    private List<String> branches;

    private List<Duration> data;
}
