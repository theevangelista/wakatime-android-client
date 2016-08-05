package com.wakatime.androidclient.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Joao Pedro Evangelista
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
public class User extends RealmObject {

    @PrimaryKey
    private String id;

    private String createdAt;

    private String displayName;

    private String email;

    private boolean emailPublic;

    private String fullName;

    private String humanReadableWebsite;

    private boolean isHireable;

    private String lastHeartbeat;

    private String lastPlugin;

    private String lastPlugin_name;

    private String lastProject;

    private String location;

    private Date modified_at;

    private String photo;

    private boolean photoPublic;

    private String plan;

    private String timezone;

    private String username;

    private String website;

}
