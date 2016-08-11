package com.wakatime.android.dashboard.model;

import lombok.Data;

/**
 * @author Joao Pedro Evangelista
 */
@Data
public class Wrapper<T> {

    private T data;

    private String message;
}
