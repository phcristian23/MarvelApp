package com.phc.marvelapp.ui.adapter.base;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Horatiu on 4/15/2016.
 */
public abstract class SearchableAdapter<Item, H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<H> {

    private volatile ArrayList<Item> itemList;
    private volatile ArrayList<Item> filteredList;

    public SearchableAdapter() {
        this.filteredList = new ArrayList<>();
        this.itemList = new ArrayList<>();
    }

    protected void addItem(Item item, boolean refreshList) {
        this.filteredList.add(item);
        this.itemList.add(item);

        if (refreshList) {
            notifyItemInserted(itemList.size() - 1);
        }
    }

    protected void addItems(List<Item> itemList, boolean refreshList) {
        this.filteredList.addAll(itemList);
        this.itemList.addAll(itemList);

        if (refreshList) {
            notifyItemRangeInserted(itemList.size() - 1, itemList.size() - 1);
        }
    }

    protected Item getItem(int itemPosition) {
        return filteredList.get(itemPosition);
    }

    // Disregards the filter
    protected Item getAbsoluteItem(int itemPosition) {
        return itemList.get(itemPosition);
    }

    protected ArrayList<Item> getItems() {
        return filteredList;
    }

    protected ArrayList<Item> getAbsoluteItems() {
        return itemList;
    }

    protected void clearList() {
        this.itemList.clear();
        this.filteredList.clear();
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public int getUnfilteredItemCount() {
        return itemList.size();
    }

    //region Filter
    private Item removeItem(int position) {
        final Item item = filteredList.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    private void addItem(int position, Item item) {
        filteredList.add(position, item);
        notifyItemInserted(position);
    }

    private void moveItem(int fromPosition, int toPosition) {
        final Item item = filteredList.remove(fromPosition);
        filteredList.add(toPosition, item);
        notifyItemMoved(fromPosition, toPosition);
    }

    private void animateTo(List<Item> items) {
        applyAndAnimateRemovals(items);
        applyAndAnimateAdditions(items);
        applyAndAnimateMovedItems(items);
    }

    private void applyAndAnimateRemovals(List<Item> newItems) {
        for (int i = filteredList.size() - 1; i >= 0; i--) {
            final Item item = filteredList.get(i);
            if (!newItems.contains(item)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Item> newItems) {
        for (int i = 0, count = newItems.size(); i < count; i++) {
            final Item item = newItems.get(i);
            if (!filteredList.contains(item)) {
                addItem(i, item);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Item> newItems) {
        for (int toPosition = newItems.size() - 1; toPosition >= 0; toPosition--) {
            final Item item = newItems.get(toPosition);
            final int fromPosition = filteredList.indexOf(item);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    protected void filter(String query, Runnable callback) {
        query = query.toLowerCase();
        animateTo(filterLogic(itemList, query));
        if (callback != null) callback.run();
    }

    public abstract ArrayList<Item> filterLogic(List<Item> originalList, String query);
    //endregion
}
