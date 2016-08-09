package com.wakatime.android.duration;

import java.util.List;

import lombok.Data;

/**
 * @author Joao Pedro Evangelista
 */
@Data
public class Duration {

    private List<String> dependencies;
    private float duration;
    private boolean isDebugging;
    private String project;
    private long time;
}
