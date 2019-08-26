/************************************************************
 * Copyright 2015-2016 Nextev Developments.
 * All rights reserved.
 *
 * Description : sd卡相关操作工具类
 * History :
 * v1.0, 2016-09-21,  create
 ************************************************************/
package com.zxin.root.util.storage;

import com.zxin.root.sharedfref.PreferencesConfig;
import com.zxin.root.util.BaseStringUtils;
import com.zxin.root.util.GlobalUtil;

import java.io.File;

public class SdcardExtendUtil {
    /**
     * 内置存储卡
     */
    public static final int CHOOSE_INSIDE = 0;
    /**
     *  外置存储卡
     */
    public static final int CHOOSE_OUTSIDE = 1;
    /**
     *  当前选择类型
     */
    private static int mIsChooseState = -1;

    /**
     * 获取待操作的sd卡类型
     */
    public static int getCurrentChooseState() {
        if (mIsChooseState == -1) {
            mIsChooseState = getCacheChooseState();
        }
        return mIsChooseState;
    }

    /**
     * 获取保存的sd卡类型
     */
    public static int getCacheChooseState() {
        int result = PreferencesConfig.SDCARD_STATE.get();
        return result;
    }

    /**
     * 保存sd卡类型
     */
    public static void setCacheChooseState(int state) {
        if (state == CHOOSE_INSIDE || state == CHOOSE_OUTSIDE) {
            PreferencesConfig.SDCARD_STATE.set(state);
        } else
            PreferencesConfig.SDCARD_STATE.set(CHOOSE_OUTSIDE);
    }

    public static boolean isExsitsApp2sd() {
        if (!SdcardUtil.getInstance(GlobalUtil.getContext()).isExsitsSdcard2())
            return false;

        return getSdcardSizeIn() != getSdcardSizeOut();
    }

    public static boolean isSystemSdcard() {
        //不存在双卡，直接返回
        if (!isExsitsApp2sd()) {
            return true;
        }
        // 内置
        if (getCurrentChooseState() == CHOOSE_INSIDE) {
            return systemSdcardIsInternal();
        }
        // 外置
        if (getCurrentChooseState() == CHOOSE_OUTSIDE) {
            return !systemSdcardIsInternal();
        }
        // 自动选择
        return isAutoChooseSdcard();
    }

    /**
     * 系统获取的是否是内置卡
     */
    private static boolean systemSdcardIsInternal() {
        return !SdcardUtil.getInstance(GlobalUtil.getContext()).isSdcardStorageRemovable();
    }

    private static boolean isAutoChooseSdcard() {
        // 自动
        // 不存在第二存储卡
        if (!SdcardUtil.getInstance(GlobalUtil.getContext()).isExsitsSdcard2()) {
            return true;
        }

        // 外置存储卡上有mapbar路径
        if (systemSdcardIsInternal()) {
            // 卡2是外置卡
            if (isFileExists(ZxinSdcardUtil.getSdcard2MapbarPathNoSlash())) {
                setCacheChooseState(CHOOSE_OUTSIDE);
                return false;
            }
        } else {
            // 卡1是外置卡
            if (isFileExists(ZxinSdcardUtil.getSdcardMapbarPathNoSlash())) {
                setCacheChooseState(CHOOSE_OUTSIDE);
                return true;
            }
        }
        // 内置存储卡上有mapbar路径
        if (systemSdcardIsInternal()) {
            // 卡1是内置卡
            if (isFileExists(ZxinSdcardUtil.getSdcardMapbarPathNoSlash())) {
                setCacheChooseState(CHOOSE_INSIDE);
                return true;
            }
        } else {
            // 卡2是内置卡
            if (isFileExists(ZxinSdcardUtil.getSdcard2MapbarPathNoSlash())) {
                setCacheChooseState(CHOOSE_INSIDE);
                return false;
            }
        }

        // 都不存在mapbar路径，优先选择外卡
        setCacheChooseState(CHOOSE_OUTSIDE);
        return !systemSdcardIsInternal();
    }

    private static boolean isFileExists(String path) {
        File f = new File(path);
        return f.exists() && f.isDirectory() && f.canRead() && f.canWrite();
    }

    /**
     * sdcard是否存在
     *
     * @return true 存在，false不存在
     */
    public static boolean isExsitsSdcard() {
        if (isSystemSdcard()) {
            return SdcardUtil.getInstance(GlobalUtil.getContext()).isExsitsSdcard();
        } else {
            return SdcardUtil.getInstance(GlobalUtil.getContext()).isExsitsSdcard2();
        }
    }

    /**
     * 获取SDCARD下mapbar的路径，一般是/mnt/sdcard/mapbar/
     *
     * @return 外置SD卡mapbar路径
     */
    public static String getSdcardMapbarPath() {
        if (isSystemSdcard()) {
            return ZxinSdcardUtil.getSdcardMapbarPath();
        } else {
            return ZxinSdcardUtil.getSdcard2MapbarPath();
        }
    }

    /**
     * 获取SDCARD下mapbar的路径，一般是/mnt/sdcard/mapbar
     *
     * @return 外置SD卡mapbar路径
     */
    public static String getSdcardMapbarPathNoSlash() {
        if (isSystemSdcard()) {
            return ZxinSdcardUtil.getSdcardMapbarPathNoSlash();
        } else {
            return ZxinSdcardUtil.getSdcard2MapbarPathNoSlash();
        }
    }

    /**
     * 获取SDCARD路径，一般是/mnt/sdcard/,现在有些手机sdcard路径不是标准的，希望各位通过这种方法获取SDCARD路径
     *
     * @return SDCARD路径
     */
    public static String getSdcardPath() {
        if (isSystemSdcard()) {
            return SdcardUtil.getInstance(GlobalUtil.getContext()).getSdcardPath();
        } else {
            return SdcardUtil.getInstance(GlobalUtil.getContext()).getSdcard2Path();
        }
    }

    /**
     * 获取SDCARD路径，一般是/mnt/sdcard/,现在有些手机sdcard路径不是标准的，希望各位通过这种方法获取SDCARD路径
     *
     * @return SDCARD路径
     */
    public static String getSdcardPathNoSlash() {
        if (isSystemSdcard()) {
            return SdcardUtil.getInstance(GlobalUtil.getContext()).getSdcardPathNoSlash();
        } else {
            return SdcardUtil.getInstance(GlobalUtil.getContext()).getSdcard2PathNoSlash();
        }
    }

    public static String getExternalStorageState() {
        if (isSystemSdcard()) {
            return SdcardUtil.getInstance(GlobalUtil.getContext()).getSdcardState();
        } else {
            return SdcardUtil.getInstance(GlobalUtil.getContext()).getSdcard2State();
        }
    }

    /**
     * 获取内部存储卡的大小
     *
     * @return
     */
    public static long getSdcardSizeIn() {
        if (systemSdcardIsInternal()) {
            return SdcardUtil.getInstance(GlobalUtil.getContext()).getSdcardSize();
        } else {
            return SdcardUtil.getInstance(GlobalUtil.getContext()).getSdcard2Size();
        }
    }

    /**
     * 获取外部存储卡的大小
     *
     * @return
     */
    public static long getSdcardSizeOut() {
        if (!systemSdcardIsInternal()) {
            return SdcardUtil.getInstance(GlobalUtil.getContext()).getSdcardSize();
        } else {
            return SdcardUtil.getInstance(GlobalUtil.getContext()).getSdcard2Size();
        }
    }

    /**
     * 获取内部存储卡的剩余大小
     *
     * @return
     */
    public static long getSdcardAvailSizeIn() {
        if (systemSdcardIsInternal()) {
            return SdcardUtil.getInstance(GlobalUtil.getContext()).getSdcardAvailSize();
        } else {
            return SdcardUtil.getInstance(GlobalUtil.getContext()).getSdcard2AvailSize();
        }
    }

    /**
     * 获取外部存储卡的剩余大小
     *
     * @return
     */
    public static long getSdcardAvailSizeOut() {
        if (!systemSdcardIsInternal()) {
            return SdcardUtil.getInstance(GlobalUtil.getContext()).getSdcardAvailSize();
        } else {
            return SdcardUtil.getInstance(GlobalUtil.getContext()).getSdcard2AvailSize();
        }
    }

    /**
     * 获取内部存储卡的大小
     *
     * @return
     */
    public static String getSdcardMapbarPathIn() {
        if (systemSdcardIsInternal()) {
            return ZxinSdcardUtil.getSdcardMapbarPath();
        } else {
            return ZxinSdcardUtil.getSdcard2MapbarPath();
        }
    }


    /**
     * 获取外部存储卡的大小
     *
     * @return
     */
    public static String getSdcardMapbarPathOut() {
        if (!systemSdcardIsInternal()) {
            return ZxinSdcardUtil.getSdcardMapbarPath();
        } else {
            return ZxinSdcardUtil.getSdcard2MapbarPath();
        }
    }

    /**
     * 获取外部存储卡的poi图片缓存路径
     *
     * @return
     */
    public static String getSDcardPoiImagePath() {
        if (!systemSdcardIsInternal()) {
            return ZxinSdcardUtil.getSdcard2PoiImagePath();
        } else {
            return ZxinSdcardUtil.getSdcardPoiImagePath();
        }
    }

    /**
     * 格式化存储大小
     *
     * @param size
     * @return
     */
    public static String formatStoreSize(long size) {
        //K
        size /= 1024;
        //M
        size /= 1024;
        int m = (int) (size % 1024);
        int g = (int) (size / 1024);
        String msg = "";
        if (g > 0) {
            if (m > 0) {
                msg = g + "." + m * 100 / 1024 + "GB";
            } else {
                msg = g + "GB";
            }
        } else {
            msg += m + "MB";
        }
        if (BaseStringUtils.isNull(msg)) {
            msg = "0MB";
        }
        return msg;
    }

}
