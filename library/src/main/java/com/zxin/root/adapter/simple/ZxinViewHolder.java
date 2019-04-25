package com.zxin.root.adapter.simple;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.zxin.root.util.ImageUtil;
import com.zxin.root.util.logger.LogUtils;

/**
 * liukui 2017/11/24 17:38
 */
public class ZxinViewHolder extends RecyclerView.ViewHolder {
    private static final LogUtils.Tag TAG = new LogUtils.Tag("ZxinViewHolder");

    private SparseArrayCompat<View> mViews;
    private View mConvertView;
    private Context mContext;

    public ZxinViewHolder(Context context, View itemView, ViewGroup parent) {
        super(itemView);
        mContext = context;
        mConvertView = itemView;
        mViews = new SparseArrayCompat<>();
    }

    public static ZxinViewHolder get(Context context, ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        ZxinViewHolder holder = new ZxinViewHolder(context, itemView, parent);
        return holder;
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    /****以下为辅助方法*****/

    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param text
     * @return
     */
    public ZxinViewHolder setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        if (tv == null) {
            LogUtils.d(TAG, "setText CharSequence TextView is null or type is wrong");
            return this;
        }
        tv.setText(text);
        return this;
    }

    /*****
     * 设置TextView的值
     * @param viewId
     * @param text
     * @return
     */
    public ZxinViewHolder setText(int viewId, Spanned text) {
        TextView tv = getView(viewId);
        if (tv == null) {
            LogUtils.d(TAG, "setText Spanned TextView is null or type is wrong");
            return this;
        }
        tv.setText(text);
        return this;
    }

    public ZxinViewHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        if (view == null) {
            LogUtils.d(TAG, "setImageResource ImageView is null or type is wrong");
            return this;
        }
        view.setImageResource(resId);
        return this;
    }

    public ZxinViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        if (view == null) {
            LogUtils.d(TAG, "setImageBitmap ImageView is null or type is wrong");
            return this;
        }
        view.setImageBitmap(bitmap);
        return this;
    }

    public ZxinViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        if (view == null) {
            LogUtils.d(TAG, "setImageDrawable ImageView is null or type is wrong");
            return this;
        }
        view.setImageDrawable(drawable);
        return this;
    }

    public ZxinViewHolder setImageURL(int viewId, String url, int errorImage) {
        ImageView view = getView(viewId);
        if (view == null) {
            LogUtils.d(TAG, "setImageURL ImageView is null or type is wrong");
            return this;
        }
        ImageUtil.getInstance(mContext).loadImageViewLoding(url, view, errorImage);
        return this;
    }

    public ZxinViewHolder setCircleImageURL(int viewId, String url, int errorImage) {
        ImageView view = getView(viewId);
        if (view == null) {
            LogUtils.d(TAG, "setImageURL ImageView is null or type is wrong");
            return this;
        }
        ImageUtil.getInstance(mContext).loadCircleImageView(url, view, errorImage);
        return this;
    }

    public ZxinViewHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        if (view == null) {
            LogUtils.d(TAG, "setBackgroundColor View is null or type is wrong");
            return this;
        }
        view.setBackgroundColor(color);
        return this;
    }

    public ZxinViewHolder setBackgroundDrawable(int viewId, GradientDrawable color) {
        View view = getView(viewId);
        if (view == null) {
            LogUtils.d(TAG, "setBackgroundDrawable View is null or type is wrong");
            return this;
        }
        view.setBackgroundDrawable(color);
        return this;
    }

    public ZxinViewHolder setBackgroundColor(int color) {
        getConvertView().setBackgroundColor(color);
        return this;
    }

    public ZxinViewHolder setBackgroundRes(int viewId, int backgroundRes) {
        View view = getView(viewId);
        if (view == null) {
            LogUtils.d(TAG, "setBackgroundRes View is null or type is wrong");
            return this;
        }
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    public ZxinViewHolder setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        if (view == null) {
            LogUtils.d(TAG, "setTextColor TextView is null or type is wrong");
            return this;
        }
        view.setTextColor(textColor);
        return this;
    }

    public ZxinViewHolder setTextColorRes(int viewId, int textColorRes) {
        TextView view = getView(viewId);
        if (view == null) {
            LogUtils.d(TAG, "setTextColorRes TextView is null or type is wrong");
            return this;
        }
        view.setTextColor(mContext.getResources().getColor(textColorRes));
        return this;
    }

    @SuppressLint("NewApi")
    public ZxinViewHolder setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    public ZxinViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public ZxinViewHolder setInVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        return this;
    }

    public ZxinViewHolder linkify(int viewId) {
        TextView view = getView(viewId);
        if (view == null) {
            LogUtils.d(TAG, "linkify TextView is null or type is wrong");
            return this;
        }
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    public ZxinViewHolder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = getView(viewId);
            if (view == null) {
                LogUtils.d(TAG, "setTypeface TextView is null or type is wrong");
                continue;
            }
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    public ZxinViewHolder setProgress(int viewId, int progress) {
        ProgressBar view = getView(viewId);
        if (view == null) {
            LogUtils.d(TAG, "setProgress ProgressBar is null or type is wrong");
            return this;
        }
        view.setProgress(progress);
        return this;
    }

    public ZxinViewHolder setProgress(int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        if (view == null) {
            LogUtils.d(TAG, "setProgress ProgressBar is null or type is wrong");
            return this;
        }
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    public ZxinViewHolder setMax(int viewId, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        return this;
    }

    public ZxinViewHolder setRating(int viewId, float rating) {
        RatingBar view = getView(viewId);
        view.setRating(rating);
        return this;
    }

    public ZxinViewHolder setRating(int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    public ZxinViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public ZxinViewHolder setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public ZxinViewHolder setChecked(int viewId, boolean checked) {
        Checkable view = getView(viewId);
        view.setChecked(checked);
        return this;
    }

    public ZxinViewHolder setEnabled(int viewId, boolean enabled) {
        View view = getView(viewId);
        view.setEnabled(enabled);
        return this;
    }

    /**
     * 关于事件的
     */
    public ZxinViewHolder setOnClickListener(int viewId,
                                             View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public ZxinViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    public ZxinViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    public ZxinViewHolder setOnItemLongClickListener(View.OnLongClickListener listener) {
        getConvertView().setOnLongClickListener(listener);
        return this;
    }

    public ZxinViewHolder setOnItemListener(View.OnClickListener listener) {
        getConvertView().setOnClickListener(listener);
        return this;
    }

    public String getTextViewStr(int viewId) {
        View view = getView(viewId);
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            return textView.getText().toString();
        }
        return "";
    }

}
