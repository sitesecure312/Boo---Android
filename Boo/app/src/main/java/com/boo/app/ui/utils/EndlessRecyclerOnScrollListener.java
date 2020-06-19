package com.boo.app.ui.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager mLinearLayoutManager;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        totalItemCount = mLinearLayoutManager.getItemCount();
        lastVisibleItem = mLinearLayoutManager
                .findLastVisibleItemPosition();
        if (!loading
                && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
            loading = true;
            loadMore();

        }
    }

    public void setLoaded() {
        loading = false;
        Log.d("Loading", "setLoaded");
    }
    public void setLoading() {
        loading = true;
        Log.d("Loading", "setLoaded");
    }

    public abstract void loadMore();

}