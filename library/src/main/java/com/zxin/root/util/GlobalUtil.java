package com.zxin.root.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.widget.EditText;

import com.zxin.root.util.logger.LogUtils;

import java.io.File;

public class GlobalUtil {

    public static final long ONE_DAY = (24 * 60 * 60 * 1000);
    private static String mLogPath;
    private static String mCachePath;
    public static String mAppSDDir;
    //是否记录日志
    private static boolean isRecordLog;
    //删除日志时间
    private static int mDeleteLogDay;
    private static Context mContext;

    /****
     * application初始化的时候设置
     * @param context
     */
    protected void setContext(Context context) {
        mContext = context;
    }

    public static Context getContext() {
        return mContext;
    }

    public static String getAppLogDirPath() {
        return getSdPath() + File.separator + mLogPath;
    }

    protected void setLogPath(String logPath){
        mLogPath = logPath;
    }

    public static String getAppCache() {
        return getSdPath() + File.separator + mCachePath;
    }

    protected void setAppCache(String cachePath){
        mCachePath = cachePath;
    }

    public static String getSdPath() {
        return mAppSDDir;
    }

    protected void setSdPath(String appSDDir) {
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

    public static boolean getIsRecordLog() {
        return isRecordLog;
    }

    protected void setRecordLog(boolean recordLog) {
        isRecordLog = recordLog;
    }

    public static int getDayToDeleteLog() {
        return mDeleteLogDay;
    }

    protected void setDayToDeleteLog(int deleteLogDay) {
        mDeleteLogDay = deleteLogDay;
    }
}
