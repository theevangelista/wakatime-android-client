package com.wakatime.android.support;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * A recycler adapter that implements a simple logic to filter the contents of a list given
 * a query.
 * <p>
 * It stores the same values with a different pointer to maintain a original copy of the list
 * so when the user clear the search box or closes it can be used to restore the original state
 * of it.
 * <p>
 * Implementing it still needs all binding stuff and view holder
 *
 * @author Joao Pedro Evangelista
 */
public abstract class SearchableRecyclerAdapter<VH extends RecyclerView.ViewHolder, T>
        extends RecyclerView.Adapter<VH> {

    protected final List<T> mValues;

    private final List<T> mValuesCache;

    protected SearchableRecyclerAdapter(List<T> mValues) {
        this.mValues = mValues;
        this.mValuesCache = new ArrayList<>(mValues);
    }

    public T remove(int pos) {
        final T type = mValues.remove(pos);
        notifyItemRemoved(pos);
        return type;
    }

    public void add(int pos, T item) {
        mValues.add(pos, item);
        notifyItemInserted(pos);
    }

    private void move(int from, int to) {
        final T value = mValues.remove(from);
        mValues.add(to, value);
        notifyItemMoved(from, to);
    }

    public void animateTo(List<T> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateMovedItems(List<T> models) {
        for (int toPosition = models.size() - 1; toPosition >= 0; toPosition--) {
            final T model = models.get(toPosition);
            final int fromPosition = mValues.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                move(fromPosition, toPosition);
            }
        }
    }

    private void applyAndAnimateAdditions(List<T> models) {
        for (int i = 0, count = models.size(); i < count; i++) {
            final T model = models.get(i);
            if (!mValues.contains(model)) {
                add(i, model);
            }
        }
    }

    private void applyAndAnimateRemovals(List<T> models) {
        for (int i = mValues.size() - 1; i >= 0; i--) {
            final T model = mValues.get(i);
            if (!models.contains(model)) {
                remove(i);
            }
        }
    }

    protected List<T> filter(String query, Comparable<T> comparable) {
        String lowerQuery = query.toLowerCase();
        if (wasModified() && query.isEmpty()) {
            // add back the items when the query is empty
            return mValuesCache;
        } else {
            List<T> collector = new ArrayList<>();
            for (T value : mValues) {
                if (comparable.compare(lowerQuery, value)) {
                    collector.add(value);
                }
            }
            return collector;
        }

    }

    private boolean wasModified() {
        return mValuesCache.size() != mValues.size();
    }

}
