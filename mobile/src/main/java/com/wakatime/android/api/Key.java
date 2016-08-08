package com.wakatime.android.api;

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
public class Key extends RealmObject {

    @PrimaryKey
    private String id;

    @Required
    private String key;

    private long timestamp = new Date().getTime();
}
