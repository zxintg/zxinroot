package com.zxin.root.adapter.simple;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxin.root.util.Utils;

import java.util.List;

/**
 * Created by kui.liu on 2016/9/27
 */
public abstract class SimpleAdapter<T> extends BaseTypeAdapter {
    private int[] mLayoutArr;
    private int[] viewTypeArr;

    /*****
     * 同一布局 不同数据源
     * @param context
     * @param mLayoutId
     * @param datas
     */
    public SimpleAdapter(Context context, int mLayoutId, List<T>... datas) {
        super(context, datas);
        setLayoutArr(new int[]{mLayoutId});
    }

    /****
     * 不同数据源
     * @param context
     * @param datas
     */
    public SimpleAdapter(Context context, List<T>... datas) {
        super(context, datas);
    }

    /*****
     * 同一数据源 , 不同布局
     * @param context
     * @param datas
     * @param mLayoutArr
     */
    public SimpleAdapter(Context context, List<T> datas, int... mLayoutArr) {
        super(context, datas);
        setLayoutArr(mLayoutArr);
    }

    /*****
     * 单一数据源，单一布局
     * @param context
     * @param datas
     * @param mLayoutId
     */
    public SimpleAdapter(Context context, List<T> datas, int mLayoutId) {
        super(context, datas);
        setLayoutArr(new int[]{mLayoutId});
    }

    /*****
     * layoutId 与 viewType 必须一一对应的
     * @param mLayoutArr
     */
    public void setLayoutArr(int... mLayoutArr) {
        this.mLayoutArr = mLayoutArr;
    }

    /*****
     * viewType与 layoutId 必须一一对应的
     * @param viewTypeArr
     */
    public void setViewType(int... viewTypeArr) {
        this.viewTypeArr = viewTypeArr;
    }


    @Override
    public ZxinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ZxinViewHolder mViewHolder = null;
        for (int mLayoutId : mLayoutArr) {
            View mView = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);
            mViewHolder = new ZxinViewHolder(mContext, mView, parent);
        }
        return mViewHolder;
    }


    @Override
    protected int getLayoutIdByType(int viewType) {
        return mLayoutArr.length == 1 ? mLayoutArr[0] : mLayoutArr[Utils.getIndex(viewType, viewTypeArr)];
    }

    @Override
    public int getItemViewType(int position) {
        return viewTypeArr == null || viewTypeArr.length == 0 ? -1 : viewTypeArr[position];
    }

    @Override
    protected void onBindViewHolder(ZxinViewHolder holder, int type, Object data) {
        onBindViewHolder(holder, (T) data, type);
    }

    /**
     * 子类需实现以下方法
     */

    protected abstract void onBindViewHolder(ZxinViewHolder holder, T data, int type);


}
