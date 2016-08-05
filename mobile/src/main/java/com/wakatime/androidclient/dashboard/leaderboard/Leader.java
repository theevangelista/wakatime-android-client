package com.wakatime.androidclient.dashboard.leaderboard;

import com.wakatime.androidclient.api.User;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Joao Pedro Evangelista
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Leader extends RealmObject {

    @PrimaryKey
    private int rank;

    private RunningTotal runningTotal;

    private User user;
}
