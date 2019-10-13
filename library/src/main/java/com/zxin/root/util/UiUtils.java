package com.zxin.root.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.content.res.Resources;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;

import com.zxin.root.R;
import com.zxin.root.view.CustomGridLayoutManager;
import com.zxin.root.view.CustomLinearLayoutManager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UiUtils {
    private static final String TYPE_DRAWABLE = "drawable";
    private static final String TYPE_LAYOUT = "layout";
    private static final String TYPE_STRING = "string";
    private static final String TYPE_DIMEN = "dimen";
    private static final String TYPE_ANIM = "anim";
    private static final String TYPE_ID = "id";

    public enum LayoutManager{
        HORIZONTAL,
        VERTICAL;
    }

    private static volatile UiUtils uiUtils = null;
    private Context mContext;
    private UiUtils(Context mContext){
        this.mContext = mContext;
    }


    public static UiUtils getInstance(Context mContext){
        if (uiUtils==null){
            synchronized (UiUtils.class){
                if (uiUtils==null){
                    uiUtils = new UiUtils(mContext.getApplicationContext());
                }
            }
        }
        return uiUtils;
    }

    /******
     * 获取资源颜色
     * @param color
     * @return
     * liukui 2017/06/23 10:38
     */
    public int getColor(int color){
        return ContextCompat.getColor(mContext,color);
    }

    public ColorStateList getColorStateList(int color){
        return ContextCompat.getColorStateList(mContext,color);
    }

    public List<Integer> getColorList() {
        List<Integer> colorList = new ArrayList<>();
        colorList.add(0,getColor(R.color.color_df0000));
        colorList.add(1,getColor(R.color.color_ff8000));
        colorList.add(2,getColor(R.color.color_c71585));
        colorList.add(3,getColor(R.color.color_9370db));
        colorList.add(4,getColor(R.color.color_db7093));
        colorList.add(5,getColor(R.color.color_f8080));
        colorList.add(6,getColor(R.color.color_ffa500));
        return colorList;
    }

    /*****
     * 获取本地资源文件
     * @param resources
     * @return
     *
     * liukui 2017/06/23 10:38
     *
     */
    public Drawable getDrawable(int resources){
        return ContextCompat.getDrawable(mContext,resources);
    }

    /*****
     * 格式化字符串
     * @param strRes
     * @param str
     * @return
     *
     * liukui 2017/06/23 10:38
     *
     */
    public String getFormatString(int strRes ,Object str){
        return String.format(getResources().getString(strRes), str);
    }


    /**
     * 根据百分比改变颜色透明度
     */
    public int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
    }

    /****
     * 获取RecyclerView布局
     * @param orientation
     * @return
     */
    public RecyclerView.LayoutManager getLayoutManager(LayoutManager orientation){
        CustomLinearLayoutManager linearLayoutManager = new CustomLinearLayoutManager(mContext);
        switch (orientation){
            case HORIZONTAL:
                linearLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
                break;

            case VERTICAL:
                linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
                break;
        }
        return linearLayoutManager;
    }

    public RecyclerView.LayoutManager getGridLayoutManager(int spanCount){
        return new CustomGridLayoutManager(mContext,spanCount);
    }

    public String getString(int resId) {
        return mContext.getString(resId);
    }

    public float getDimension(int resId) {
        return getResources().getDimension(resId);
    }

    public String getFromAssets(String fileName) {
        try {
            InputStream paramString = getResources().getAssets().open(fileName);
            byte[] arrayOfByte = new byte[paramString.available()];
            paramString.read(arrayOfByte);
            String txt = new String(arrayOfByte, "UTF-8");
            return txt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public Resources getResources(){
        return mContext.getResources();
    }

    public Locale getLocale(){
        return getResources().getConfiguration().locale;
    }

    public float getCornerRadius(int dimen){
       return getResources().getDimensionPixelOffset(dimen);
    }

    public String[] getStringArray(int resId){
       return getResources().getStringArray(resId);
    }


    /**
     * 设置textView显示局部字体颜色改变
     *
     * @param builder 源数据
     * @param color   字体颜色
     * @param start   文字开始下标
     * @param len     修改文字长度
     * @return 修改后的数据源
     */
    public static SpannableStringBuilder setTextPartColor(SpannableStringBuilder builder
            , int color
            , int start
            , int len) {
        ForegroundColorSpan csp = new ForegroundColorSpan(color);
        builder.setSpan(csp, start, start + len, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return builder;
    }

    /*****
     * 设置textView显示局部字体点击事件
     * @param builder 源数据
     * @param listener 回调函数
     * @param start 文字开始下标
     * @param len 修改文字长度
     * @return 修改后的数据源
     */
    public static SpannableStringBuilder setTextPartClick(final SpannableStringBuilder builder
            , final View.OnClickListener listener
            , int start
            , int len) {

        //设置局部可点击事件
        builder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                //局部点击的响应事件
                if (listener == null) {
                    return;
                }
                listener.onClick(view);
            }
        }, start, start + len, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return builder;
    }


    /****
     * 根据ID获取Dimen
     * @param resId 资源id
     * @return
     */
    public int getDimensionById(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    /****
     * 根据name获取Dimen
     * @param resName
     * @return
     */
    public int getDimensionByName(String resName) {
        int resId = getDimenId(resName);
        return getDimensionById(resId);
    }

    /****
     * 获取布局
     * @param id
     * @param <V>
     * @return
     */
    public <V extends View> V getViewById(int id) {
        return (V) LayoutInflater.from(mContext).inflate(id, null);
    }

    /*****
     * 厘米转换px
     * 72dpi 1厘米
     * @param cm 单位 厘米
     * @return
     */
    public float cm2px(float cm){
        float scale = getResources().getDisplayMetrics().density;
        return cm * 72 * scale + 0.5f;
    }

    public int getIdentifierId(String name) {
        return getResourceId(name, TYPE_ID);
    }

    public int getDrawableId(String name) {
        return getResourceId(name, TYPE_DRAWABLE);
    }

    public int getStringId(Context context, String name) {
        return getResourceId(name, TYPE_STRING);
    }

    public int getLayoutId(String name) {
        return getResourceId(name, TYPE_LAYOUT);
    }

    public int getDimenId(String name) {
        return getResourceId(name, TYPE_DIMEN);
    }

    public int getAnimId(String name) {
        return getResourceId(name, TYPE_ANIM);
    }

    public int getResourceId(String name, String defType) {
        return getResources().getIdentifier(name,defType,mContext.getPackageName());
    }
}
