package com.wakatime.android.dashboard.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

import io.realm.RealmObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Joao Pedro Evangelista
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true) //ignore properties since realm seems to add some
public class BestDay extends RealmObject {

    private String date;

    private Date createdAt;

    private String id;

    private long totalSeconds;

    private Date modified_at;
}
