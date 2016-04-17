package com.phc.marvelapp.ui.adapter.listener;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Horatiu on 4/15/2016.
 */
public class LoadScrollListener extends RecyclerView.OnScrollListener {

    public interface OnLoadListener{
        void onLoad(int itemCount);
    }

    private int visibleThreshold = 12;
    private int lastVisibleItem;
    private int totalItemCount;
    private boolean isLoading;
    private OnLoadListener onLoadListener;

    private GridLayoutManager layoutManager;

    public LoadScrollListener(RecyclerView recyclerView, OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;

        if (recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

            if (layoutManager != null && layoutManager instanceof GridLayoutManager) {
                this.layoutManager = (GridLayoutManager) layoutManager;
            }
        }

        if (this.layoutManager == null) {
            throw new NullPointerException("You need to attach a linear layout manager first.");
        }

        this.visibleThreshold = layoutManager.getSpanCount() * 3;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        totalItemCount = layoutManager.getItemCount();
        lastVisibleItem = layoutManager.findLastVisibleItemPosition();

        if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
            if (onLoadListener != null) {
                this.onLoadListener.onLoad(totalItemCount);
            }
        }

    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}
