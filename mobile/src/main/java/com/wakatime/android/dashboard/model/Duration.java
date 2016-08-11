package com.wakatime.android.dashboard.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.Data;

/**
 * @author Joao Pedro Evangelista
 */
@Data
public class Duration {

    private List<String> dependencies;

    private float duration;

    @JsonProperty("is_debugging")
    private boolean isDebugging;

    private String project;

    private long time;
}
