package com.wakatime.androidclient.support.context;

import com.labo.kaji.fragmentanimations.MoveAnimation;

/**
 * @author Joao Pedro Evangelista
 */

public final class Animations {

    private static final int DURATION = 500;

    private Animations() {

    }

    public static MoveAnimation createMoveAnimation(boolean enter) {
        return MoveAnimation.create(MoveAnimation.LEFT, enter, DURATION);
    }


}
