package com.wakatime.androidclient.dashboard.model;

import lombok.Data;

/**
 * @author Joao Pedro Evangelista
 */
@Data
public class Wrapper<T> {

    private T data;
}
