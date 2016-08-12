package com.wakatime.android.support;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Joao Pedro Evangelista
 */
public class PositionedLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
    public PositionedLinkedHashMap(int size) {
        super(size);
    }

    public PositionedLinkedHashMap() {
        super();
    }

    public V getValue(int pos) {
        Entry<K, V> entry = this.getEntry(pos);
        if (entry != null) {
            return entry.getValue();
        } else {
            return null;
        }
    }

    public Map.Entry<K, V> getEntry(int pos) {
        Set<Entry<K, V>> entries = entrySet();
        int j = 0;

        for (Map.Entry<K, V> entry : entries) {
            if (j++ == pos) {
                return entry;
            }
        }
        return null;
    }
}
