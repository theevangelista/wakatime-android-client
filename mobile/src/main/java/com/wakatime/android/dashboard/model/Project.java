package com.wakatime.android.dashboard.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.realm.RealmObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Joao Pedro Evangelista
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true) //ignore properties since realm seems to add some
public class Project extends RealmObject {

    private String digital;

    private int hours;

    private int minutes;

    private String name;

    private float percent;

    private String text;

    private long totalSeconds;
}
