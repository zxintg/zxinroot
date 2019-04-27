package com.zxin.root.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.IBinder;
//import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;

import com.zxin.root.util.logger.LogUtils;

import java.io.File;

public class GlobalUtil {
    private static final String ENGINE_CACHE_DIR = "ZxinCache";
    private static final String APP_LOG_CACHE_DIR = "ZxinAppLog";
    private static final String BAIDU_LOG_DIR = "ZxinSDK/log";
    private static final String SDK_LOG_TEACE_DIR = "trace";
    private static final String PATH_PROPERTY_NAME = "persist.sys.nav_res_path";
    private static final String LAUNCHER_PACKAGE_NAME_KEY = "persist.sys.launcher_pkg";
    public static final String PREFERENCE_DAY_TO_DELET_LOG = "delete_log_time";
    public static final String PREFERENCE_IS_RECORD_LOG = "record_log_yes_or_no";
    private static final String PREFERENCE_IS_ACTIVATE_SDK = "activate_sdk_yes_or_no";

    /**
     * Nio vehicle Rom res path is : "/storage/nionavi"; Nio Pad Rom res path is :
     * "/storage/emulated/nionavi"; Other rom res path is /sdcard/nionavi
     */
    public static final String PATH_VALUE_ON_CUSTOMER = "/sdcard/nionavi";
    public static final String LAUNCHER_PACKAGE_NAME_DEFAULT = "com.nextev.home";

    public static final int PATH_VALUE_DAY_TO_DELETE_LOG = 3;
    public static final long ONE_DAY = (24 * 60 * 60 * 1000);
    public static final String PATH_VALUE_ON_CAR = "/storage/nionavi";
    private static final String DEBUG_MAGIC_STRING = "#*123456*#";
    private static final LogUtils.Tag TAG = new LogUtils.Tag("GlobalUtil");
    /*关闭SDK log的默认记录*/
    private static boolean mIsLog = false;
    private static int mDeleteLogDay = 3;
    private static Context mContext;
    // 离线数据存储路径
    private static String mOfflineDataPath = "";

    private static boolean mIsOnCar = false;

    public static void setContext(Context context) {
        GlobalUtil.mContext = context;
    }

    /*  平板：/storage/emulated/nionavi/NioMapCache/BaiduMapAutoSDK/log/NioAppLog */
    /*  车机：/storage/nionavi/NioMapCache/BaiduMapAutoSDK/log/NioAppLog */
    public static String getAppLogDirPath() {
        return GlobalUtil.getBaiduLogDirPath() + File.separator + APP_LOG_CACHE_DIR;
    }

    /*  平板：/storage/emulated/nionavi/NioMapCache/BaiduMapAutoSDK/log/trace */
    /*  车机：/storage/nionavi/NioMapCache/BaiduMapAutoSDK/log/trace */
    public static String getRecordTracePath() {
        return getBaiduLogDirPath() + File.separator + SDK_LOG_TEACE_DIR;
    }

    /*  平板：/storage/emulated/nionavi/NioMapCache/BaiduMapAutoSDK/log */
    /*  车机：/storage/nionavi/NioMapCache/BaiduMapAutoSDK/log */
    public static String getBaiduLogDirPath() {
        return GlobalUtil.getWorkDirectory() + File.separator + BAIDU_LOG_DIR;
    }

    /*这个方法是因为百度SDK现在记录log有两个地方*/
    /*  平板：/storage/emulated/nionavi/NioMapCache/log */
    /*  车机：/storage/nionavi/NioMapCache/log */
    public static String getBaiduLogDirPath2() {
        return GlobalUtil.getWorkDirectory() + File.separator + "log";
    }

    /*  平板：/storage/emulated/nionavi/NioMapCache */
    /*  车机：/storage/nionavi/NioMapCache */
    public static String getWorkDirectory() {
        if (mOfflineDataPath == "") {
            mOfflineDataPath = getSdPath() + File.separator + ENGINE_CACHE_DIR;
            LogUtils.d(TAG, "mOfflineDataPath:" + mOfflineDataPath);
        }

        return mOfflineDataPath;
    }

    /*  平板：/storage/emulated/nionavi */
    /*  车机：/storage/nionavi */
    private static String getSdPath() {
       /* String naviPath = SystemProperties.get(PATH_PROPERTY_NAME,
                PATH_VALUE_ON_CUSTOMER);

        return naviPath;*/
       return "";
    }

    /**
     * 该方法获取连接launcher service时包名
     * 可以通过以下命令设置:
     * setprop persist.sys.launcher_pkg [com.nextev.nav4launcher|com.nextev.home]
     *
     * @return 需要连接的Launcher包名
     */
    public static String getLauncherPackageName() {
        /*String pkgName = SystemProperties.get(LAUNCHER_PACKAGE_NAME_KEY,
                LAUNCHER_PACKAGE_NAME_DEFAULT);
        LogUtils.d(TAG, "launcher package name: " + pkgName);
        return pkgName;*/
        return "";
    }

    public static long getDayToDeleteLog() {
        return mDeleteLogDay;
    }

    public static void setDayToDeleteLog(Integer val) {
        mDeleteLogDay = val;
        SharedPreferences preference;
        if (null != mContext) {
            preference = PreferenceManager.getDefaultSharedPreferences(mContext);
            if (null != preference) {
                SharedPreferences.Editor editor = preference.edit();
                editor.putInt(PREFERENCE_DAY_TO_DELET_LOG, val);
                editor.apply();
            }
        }
    }

    public static Boolean getIsRecordLog() {
        return mIsLog;
    }

    public static void setIsRecordLog(Boolean isRecord) {
        mIsLog = isRecord;
        SharedPreferences preference;
        if (null != mContext) {
            preference = PreferenceManager.getDefaultSharedPreferences(mContext);
            if (null != preference) {
                SharedPreferences.Editor editor = preference.edit();
                editor.putBoolean(PREFERENCE_IS_RECORD_LOG, isRecord);
                editor.apply();
            }
        }
    }

    public static boolean isOnCar() {
        return mIsOnCar;
    }

    public static void setPlatform() {
        if (PATH_VALUE_ON_CAR.equals(getSdPath())) {
            LogUtils.d(TAG, "on car");
            mIsOnCar = true;
        } else {
            LogUtils.d(TAG, "not on car");
            mIsOnCar = false;
        }
    }

    public static void cleanLogFile(final String path) {
        ThreadManager.post(
                new Runnable() {
                    @Override
                    public void run() {
                        File directory = new File(path);
                        if (directory.exists()) {

                            LogUtils.d(TAG, "clean dir:" + path);

                            File[] files = directory.listFiles();

                            for (File file : files) {
                                if (file.isDirectory()) {
                                    cleanLogFile(file.getAbsolutePath());
                                } else if (file.isFile()) {
                                    if (System.currentTimeMillis() - file.lastModified()
                                            >= (getDayToDeleteLog() * ONE_DAY)) {
                                        LogUtils.d(TAG, "remove file:" + file.getName());
                                        file.delete();
                                    }
                                }
                            }
                        }
                    }
                });
    }

    public static String getDebugMagicString() {
        return DEBUG_MAGIC_STRING;
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

}
