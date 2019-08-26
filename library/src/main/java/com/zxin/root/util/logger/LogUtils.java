package com.zxin.root.util.logger;

import android.annotation.SuppressLint;
import android.util.Log;

import com.zxin.root.util.AppManager;
import com.zxin.root.util.SystemPropertiesProxy;

/**
 * 日志-下载接口
 * Created by kui.liu on 2019/04/24.
 */

public class LogUtils {
    private static final Tag TAG = new Tag("L");
    private static final String TAG_PREFIX = "BN_";

    public static final String KEY_DEBUG_LEVEL = "persist.zxin.log.level";
    public static final class Tag {
        private String mTag;
        private static final int MAX_TAG_LENGTH = 23;
        public Tag(String tag) {
            final int maxLength = MAX_TAG_LENGTH - TAG_PREFIX.length();
            if (tag.length() > maxLength) {
                LogUtils.e(TAG, "Tags max length must less that " + maxLength + " : " + tag);
                mTag = TAG_PREFIX + tag.substring(0, maxLength);
            } else {
                mTag = TAG_PREFIX + tag;
            }
        }

        public String toString() {
            return mTag;
        }
    }

    private static int DEBUG_LEVEL;


    static {
        updateDebugLevel();
        initLogger();
    }

    public static void updateDebugLevel() {
        try {
            DEBUG_LEVEL = SystemPropertiesProxy.getInstance(AppManager.getAppManager().currentActivity()).getInt(KEY_DEBUG_LEVEL, Log.VERBOSE);
        } catch (Exception e) {
            DEBUG_LEVEL = Log.DEBUG;
        }
    }

    /**
     * 初始化Logger打印和存储
     */
    private static void initLogger() {
        Logger.addLogAdapter(new AndroidLogAdapter());
        Logger.addLogAdapter(new DiskLogAdapter());
    }

    public static boolean isLoggable(int level) {
        return level >= DEBUG_LEVEL;
    }

    public static final void e(Tag tag, String msg) {
        Logger.t(tag.toString()).e(msg);
    }

    @SuppressLint("LogTagMismatch")
    public static final void d(Tag tag, String msg) {
        if (isLoggable(Log.DEBUG)) {
            Logger.t(tag.toString()).d(msg);
        }
    }

    public static final void w(Tag tag, String msg) {
        Logger.t(tag.toString()).w(msg);
    }

    public static final void f(Tag tag, String msg) {
        Log.wtf(tag.toString(), msg);
    }

    @SuppressLint("LogTagMismatch")
    public static final void v(Tag tag, String msg) {
        if (isLoggable(Log.VERBOSE)) {
            Logger.t(tag.toString()).v(msg);
        }
    }

    @SuppressLint("LogTagMismatch")
    public static final void i(Tag tag, String msg) {
        if (isLoggable(Log.INFO)) {
            Logger.t(tag.toString()).i(msg);
        }
    }

    public static final void printStackTrace(Tag tag, Throwable e) {
        if (e != null) {
            LogUtils.e(tag, e.toString());
            StackTraceElement[] elements = e.getStackTrace();
            if (elements != null) {
                for (StackTraceElement ele : elements) {
                    Logger.t(tag.toString()).e(" \t\t" + ele.toString());
                }
            }
        }
    }

}
