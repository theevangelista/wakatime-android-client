package com.wakatime.android.dashboard.leaderboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wakatime.android.dashboard.model.Language;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class RunningTotal extends RealmObject {

    private long dailyAverage;

    private String humanReadableDailyAverage;

    private String humanReadableTotal;

    private RealmList<Language> languages;

    private Date modifiedAt;

    private long totalSeconds;
}
