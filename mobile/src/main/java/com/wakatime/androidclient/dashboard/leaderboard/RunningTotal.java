package com.wakatime.androidclient.dashboard.leaderboard;

import com.wakatime.androidclient.dashboard.model.Language;

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
public class RunningTotal extends RealmObject {

    private long dailyAverage;

    private String humanReadableDailyAverage;

    private String humanReadableTotal;

    private RealmList<Language> languages;

    private Date modifiedAt;

    private long totalSeconds;
}
