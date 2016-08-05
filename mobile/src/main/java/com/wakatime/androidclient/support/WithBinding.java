package com.wakatime.androidclient.support;

/**
 * @author Joao Pedro Evangelista
 */

public interface WithBinding<V> {

    void bind(V viewModel);

    void unbind();
}
