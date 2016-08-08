package com.wakatime.android.support.view;

import com.labo.kaji.fragmentanimations.MoveAnimation;

/**
 * @author Joao Pedro Evangelista
 */

public final class Animations {

    private static final int DURATION = 300;

    private Animations() {

    }

    public static MoveAnimation createMoveAnimation(boolean enter) {
        return MoveAnimation.create(MoveAnimation.LEFT, enter, DURATION);
    }


}
