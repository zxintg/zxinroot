package com.zxin.root.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.widget.EditText;

import com.zxin.root.util.logger.LogUtils;

import java.io.File;

public class GlobalUtil {
    private static String mLogPath;
    private static String mCachePath;
    public static String mAppSDDir;
    //是否记录日志
    private static boolean isRecordLog = true;
    //删除日志时间
    private static int mDeleteLogDay = 3;
    public static final long ONE_DAY = (24 * 60 * 60 * 1000);
    private static Context mContext;

    /****
     * application初始化的时候设置
     * @param context
     */
    public static void setContext(Context context) {
        GlobalUtil.mContext = context;
    }

    public static String getAppLogDirPath() {
        return getSdPath() + File.separator + mLogPath;
    }

    public static String getAppCache() {
        return getSdPath() + File.separator + mCachePath;
    }

    public static String getSdPath() {
        return mAppSDDir;
    }

    public static void SetSdPath(String appSDDir) {
        mAppSDDir = appSDDir;
    }

    /*****
     * 获取文本框输入的长度上限
     *
     * kui.liu 2019/04/10 16:40
     *
     * @param mEtInput
     * @return
     */
    public static int getMaxLenght(EditText mEtInput) {
        if (mEtInput == null) {
            return 0;
        }
        for (InputFilter filter : mEtInput.getFilters()) {
            if (filter instanceof InputFilter.LengthFilter) {
                return ((InputFilter.LengthFilter) filter).getMax();
            }
        }
        return 0;
    }

    public static Context getContext() {
        return mContext.getApplicationContext();
    }

    public static void setRecordLog(boolean recordLog) {
        isRecordLog = recordLog;
    }

    public static boolean getIsRecordLog() {
        return isRecordLog;
    }

    public static int getDayToDeleteLog() {
        return mDeleteLogDay;
    }

    public static void setDayToDeleteLog(int deleteLogDay) {
        mDeleteLogDay = deleteLogDay;
    }
}
