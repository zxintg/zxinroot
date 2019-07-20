package com.zxin.root.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zxin.root.util.logger.LogUtils;

/**
 * Created by Administrator on 2017/12/12.
 */

public class CustomLinearLayoutManager extends LinearLayoutManager {
    private static final LogUtils.Tag TAG = new LogUtils.Tag("CustomLinearLayoutManager");
    private boolean isScrollEnabled = true;

    public CustomLinearLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            LogUtils.e(TAG,"RecyclerView 快速滚动 崩溃");
        }
    }

}
