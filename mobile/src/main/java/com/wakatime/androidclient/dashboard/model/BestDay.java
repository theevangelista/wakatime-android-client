package com.wakatime.androidclient.dashboard.model;

import java.util.Date;

import lombok.Data;

/**
 * @author Joao Pedro Evangelista
 */
@Data
public class BestDay {

    private String date;

    private Date createdAt;

    private String id;

    private long totalSeconds;

    private Date modified_at;
}
