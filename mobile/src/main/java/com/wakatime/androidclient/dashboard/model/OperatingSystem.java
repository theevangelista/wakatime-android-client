package com.wakatime.androidclient.dashboard.model;

import lombok.Data;

/**
 * @author Joao Pedro Evangelista
 */
@Data
public class OperatingSystem {

    private String digital;

    private int hours;

    private int minutes;

    private String name;

    private float percent;

    private String text;

    private long totalSeconds;
}
