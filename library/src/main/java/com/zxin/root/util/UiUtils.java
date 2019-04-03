package com.zxin.root.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.zxin.root.R;
import com.zxin.root.view.CustomGridLayoutManager;
import com.zxin.root.view.CustomLinearLayoutManager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UiUtils {

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
                    uiUtils = new UiUtils(mContext);
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
        return String.format(mContext.getResources().getString(strRes), str);
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
        return mContext.getResources()
                .getDimension(resId);
    }

    public String getFromAssets(String fileName) {
        try {
            InputStream paramString = mContext.getResources().getAssets().open(fileName);
            byte[] arrayOfByte = new byte[paramString.available()];
            paramString.read(arrayOfByte);
            String txt = new String(arrayOfByte, "UTF-8");
            return txt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public float getCornerRadius(int dimen){
       return mContext.getResources().getDimensionPixelOffset(dimen);
    }

    public String[] getStringArray(int resId){
       return mContext.getResources().getStringArray(resId);
    }
}
