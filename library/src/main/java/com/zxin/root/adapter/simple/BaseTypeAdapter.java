package com.zxin.root.adapter.simple;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */
public abstract class BaseTypeAdapter extends RecyclerView.Adapter<ZxinViewHolder> {

    protected String TAG;
    protected Context mContext;
    protected List mDatas;

    public BaseTypeAdapter(Context mContext, List... mDatas) {
        this.mContext = mContext;
        this.mDatas = new ArrayList();
        for (List list : mDatas) {
            this.mDatas.addAll(this.mDatas.size(), list);
        }
        this.TAG = getClass().getSimpleName();
    }

    @Override
    public ZxinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ZxinViewHolder.get(mContext, parent, getLayoutIdByType(viewType));
    }

    @Override
    public void onBindViewHolder(ZxinViewHolder holder, int position) {
        onBindViewHolder(holder, getItemViewType(position), mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        if (mDatas == null)
            return 0;
        return mDatas.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * 子类需实现以下三个方法
     */

    protected abstract int getLayoutIdByType(int viewType);

    @Override
    public abstract int getItemViewType(int position);

    protected abstract void onBindViewHolder(ZxinViewHolder holder, int type, Object data);

}
