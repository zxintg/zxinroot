package com.zxin.root.util;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.zxin.root.R;
import com.zxin.root.util.logger.LogUtils;

/**
 * Created by hy on 2017/10/16.
 */
public class ToastUtil {
    private LogUtils.Tag TAG = new LogUtils.Tag("ToastUtils");
    private static Toast sToast;
    private static boolean isCanShow = false;
    private static volatile ToastUtil toastUtil = null;

    private Context mContext;

    private ToastUtil(Context mContext) {
        this.mContext = mContext;
    }

    public static ToastUtil getInstance(Context mContext, boolean canShow) {
        if (canShow != isCanShow) {
            isCanShow = canShow;
        }
        if (toastUtil == null) {
            synchronized (ToastUtil.class) {
                if (toastUtil == null) {
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
        if (BaseStringUtils.isNull(str)) {
            LogUtils.e(TAG, "topToast str == null");
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
        if (BaseStringUtils.isNull(str)) {
            LogUtils.e(TAG, "topToast str == null");
            return;
        }
        Toast mToast = Toast.makeText(mContext, str, Toast.LENGTH_LONG);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

    public void showShort(String no_result) {
        if (BaseStringUtils.isNull(no_result)) {
            LogUtils.e(TAG, "topToast str == null");
            return;
        }
        Toast.makeText(mContext, no_result, Toast.LENGTH_SHORT).show();
    }


    private void checkToast() {
        if (sToast == null) {
            synchronized (ToastUtils.class) {
                if (sToast == null) {
                    if (mContext == null) {
                        throw new RuntimeException("Please init ToastUtils via call init(Application app) when application start");
                    }
                    sToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
                }
            }
        }
    }

    public void show(int id, boolean force) {
        show(UiUtils.getInstance(mContext).getString(id), Toast.LENGTH_SHORT, force);
    }

    public void show(String message) {
        show(message, Toast.LENGTH_SHORT);
    }

    /**
     * Show a toast message
     *
     * @param message  the message.
     * @param duration duration, value must be of {@link Toast#LENGTH_LONG} or {@link Toast#LENGTH_SHORT}
     */
    public void show(String message, int duration) {
        show(message, duration, false);
    }

    private void show(String message, int duration, boolean force) {
        if (!isCanShow && !force) {
            return;
        }
        if (sToast != null) {
            sToast.cancel();
        }
        sToast = Toast.makeText(mContext, message, duration);
        sToast.show();
    }

    /**
     * Show a toast message
     *
     * @param message the string id.
     */
    public void show(int message) {
        show(message, Toast.LENGTH_SHORT);
    }


    /**
     * Show a toast message
     *
     * @param message  the string id.
     * @param duration duration, value must be of {@link Toast#LENGTH_LONG} or {@link Toast#LENGTH_SHORT}
     */
    public void show(int message, int duration) {
        if (!isCanShow) {
            return;
        }
        show(UiUtils.getInstance(mContext).getString(message), duration);
    }

}
