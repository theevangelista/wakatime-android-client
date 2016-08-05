package com.wakatime.androidclient.dashboard.leaderboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import lombok.Data;

/**
 * @author Joao Pedro Evangelista
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LeaderWrapper {

    private Leader currentUser;

    private List<Leader> data;
}
