package com.wakatime.androidclient.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Joao Pedro Evangelista
 */

public final class Strings {

    public static <T> String join(Collection<T> collection, String delimiter) {
        StringBuilder sb = new StringBuilder();
        Iterator<?> iter = collection.iterator();
        if (iter.hasNext())
            sb.append(iter.next().toString());
        while (iter.hasNext()) {
            sb.append(delimiter);
            sb.append(iter.next().toString());
        }
        return sb.toString();
    }
}
