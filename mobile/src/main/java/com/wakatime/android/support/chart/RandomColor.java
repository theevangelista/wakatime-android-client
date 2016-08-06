package com.wakatime.android.support.chart;

import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.Random;

/**
 * Pick a random color from a collection of colors
 * @author Joao Pedro Evangelista
 */
public final class RandomColor {

    private static final int[] colors = ColorTemplate.JOYFUL_COLORS;

    private RandomColor(){

    }

    public static int generate() {
        return colors[new Random().nextInt(colors.length)];
    }
}
