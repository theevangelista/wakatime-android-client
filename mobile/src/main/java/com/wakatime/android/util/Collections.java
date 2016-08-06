package com.wakatime.android.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Joao Pedro Evangelista
 */

public final class Collections {

    private Collections() {

    }

    public static <T> List<T> copyIterator(Iterator<T> iterator) {
        List<T> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

}
