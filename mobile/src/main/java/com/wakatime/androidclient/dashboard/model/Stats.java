package com.wakatime.androidclient.dashboard.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Joao Pedro Evangelista
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true) //ignore properties since realm seems to add some
public class Stats extends RealmObject {

    private BestDay bestDay;

    private Date createdAt;

    private long dailyAverage;

    private int daysIncludingHolidays;

    private int daysMinusHoliday;

    private RealmList<Editor> editors;

    private Date end;

    private int holidays;

    private String humanReadableDailyAverage;

    private String humanReadableTotal;

    private String id;

    private boolean isAlreadyUpdating;

    private boolean isStuck;

    private boolean isUpToDate;

    private RealmList<Language> languages;

    private Date modifiedAt;

    private RealmList<OperatingSystem> operatingSystems;

    private RealmList<Project> projects;

    private String range;

    private Date start;

    private String status;

    private int timeout;

    private String timezone;

    private long totalSeconds;

    private String userId;

    private String username;

    private boolean writesOnly;

}
