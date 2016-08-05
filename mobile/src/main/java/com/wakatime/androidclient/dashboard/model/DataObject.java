package com.wakatime.androidclient.dashboard.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * @author Joao Pedro Evangelista
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataObject {

    private BestDay bestDay;

    private Date createdAt;

    private long dailyAverage;

    private int daysIncludingHolidays;

    private int daysMinusHoliday;

    private List<Editor> editors;

    private Date end;

    private int holidays;

    private String humanReadableDailyAverage;

    private String humanReadableTotal;

    private String id;

    private boolean isAlreadyUpdating;

    private boolean isStuck;

    private boolean isUpToDate;

    private List<Language> languages;

    private Date modifiedAt;

    private List<OperatingSystem> operatingSystems;

    private List<Project> projects;

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
