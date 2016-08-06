package com.wakatime.android.dashboard.leaderboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wakatime.android.api.User;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Joao Pedro Evangelista
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Leader extends RealmObject {

    @PrimaryKey
    private int rank;

    private RunningTotal runningTotal;

    private User user;
}
