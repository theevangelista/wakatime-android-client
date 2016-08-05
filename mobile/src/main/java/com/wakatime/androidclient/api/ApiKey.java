package com.wakatime.androidclient.api;

import java.sql.Timestamp;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Joao Pedro Evangelista
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApiKey extends RealmObject {

    @PrimaryKey
    private String id;

    @Required
    private String key;

    private long timestamp = new Date().getTime();
}
