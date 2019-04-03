package com.zxin.root.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.zxin.root.R;

/**
 * Created by hy on 2017/10/16.
 */
public class ToastUtil {
    static String tag = ToastUtil.class.getSimpleName();

    private static volatile ToastUtil toastUtil = null;

    private Context mContext;
    private ToastUtil(Context mContext){
        this.mContext = mContext;
    }

    public static ToastUtil getInstance(Context mContext){
        if (toastUtil==null){
            synchronized (ToastUtil.class){
                if (toastUtil==null){
                    toastUtil = new ToastUtil(mContext);
                }
            }
        }
        return toastUtil;
    }

    /**
     * 顶部Toast
     * 非队列形式
     *
     * @param str
     */
    public void topToast(String str) {
        if (TextUtils.isEmpty(str)) {
            Log.e(tag, "topToast str == null");
            return;
        }
        Toast mToast = Toast.makeText(mContext, str, Toast.LENGTH_LONG);
        mToast.setDuration(Toast.LENGTH_LONG);
        int topHeight = (int) mContext.getResources()
                .getDimension(R.dimen.top_hight);
        mToast.setGravity(Gravity.TOP, 0, SystemInfoUtil.getInstance(mContext).dip2px(topHeight));
        mToast.show();

    }

    /****
     * 提示信息显示
     * @param str
     */
    public void showToast(String str) {
        if (TextUtils.isEmpty(str)) {
            Log.e(tag, "topToast str == null");
            return;
        }
        Toast mToast = Toast.makeText(mContext, str, Toast.LENGTH_LONG);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

    public void showShort(String no_result) {
        if (TextUtils.isEmpty(no_result)) {
            Log.e(tag, "topToast str == null");
            return;
        }
        Toast.makeText(mContext, no_result, Toast.LENGTH_SHORT).show();
    }

}
