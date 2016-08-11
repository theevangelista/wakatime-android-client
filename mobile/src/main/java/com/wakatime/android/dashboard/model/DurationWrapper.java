package com.wakatime.android.dashboard.model;

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
