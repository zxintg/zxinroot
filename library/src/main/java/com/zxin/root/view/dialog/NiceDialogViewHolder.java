package com.zxin.root.view.dialog;

import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

public class NiceDialogViewHolder {

    private SparseArray<View> views;
    private View convertView;

    private NiceDialogViewHolder(View view) {
        convertView = view;
        views = new SparseArray<>();
    }

    public static NiceDialogViewHolder create(View view) {
        return new NiceDialogViewHolder(view);
    }

    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return convertView;
    }

    public void setText(int viewId, String text) {
        TextView textView = getView(viewId);
        textView.setText(text);
    }

    public void setText(int viewId, int textId) {
        TextView textView = getView(viewId);
        textView.setText(textId);
    }

    public void setVisibility(int viewId,boolean isShow){
        View view = getView(viewId);
        view.setVisibility(isShow?View.VISIBLE:View.GONE);
    }


    public void setTextColor(int viewId, int colorId) {
        TextView textView = getView(viewId);
        textView.setTextColor(colorId);
    }

    public void setOnClickListener(int viewId, View.OnClickListener clickListener) {
        View view = getView(viewId);
        view.setOnClickListener(clickListener);
    }

    public void setBackgroundResource(int viewId, int resId) {
        View view = getView(viewId);
        view.setBackgroundResource(resId);
    }

    public void setBackgroundColor(int viewId, int colorId) {
        View view = getView(viewId);
        view.setBackgroundColor(colorId);
    }
}
