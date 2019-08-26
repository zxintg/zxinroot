/************************************************************
 * Copyright 2015-2016 Nextev Developments.
 * All rights reserved.
 *
 * Description : 图吧业务相关的sd卡路径使用的工具类
 * History :
 * v1.0, 2016-09-21,  create
 ************************************************************/
package com.zxin.root.util.storage;

import com.zxin.root.util.GlobalUtil;

public class ZxinSdcardUtil {

    public static final String productPath = "zxin";
    private static final String productpathNoSlash = "zxin";
    private static final String poiImagePath = "zxin/poi/image";

    /**
     * 获取SDCARD下mapbar的路径，一般是/mnt/sdcard/mapbar
     *
     * @return 外置SD卡mapbar路径
     */
    protected static String getSdcardMapbarPathNoSlash() {
        return SdcardUtil.getInstance(GlobalUtil.getContext()).getSdcardPath() + productpathNoSlash;
    }

    protected static String getSdcard2MapbarPathNoSlash() {
        return SdcardUtil.getInstance(GlobalUtil.getContext()).getSdcard2Path() + productpathNoSlash;
    }

    /**
     * 获取SDCARD下mapbar的路径，一般是/mnt/sdcard/mapbar/
     *
     * @return 外置SD卡mapbar路径
     */
    protected static String getSdcardMapbarPath() {
        return SdcardUtil.getInstance(GlobalUtil.getContext()).getSdcardPath() + productPath;
    }

    protected static String getSdcard2MapbarPath() {
        return SdcardUtil.getInstance(GlobalUtil.getContext()).getSdcard2Path() + productPath;
    }

    protected static String getSdcardPoiImagePath() {
        return SdcardUtil.getInstance(GlobalUtil.getContext()).getSdcardPath() + poiImagePath;
    }

    protected static String getSdcard2PoiImagePath() {
        return SdcardUtil.getInstance(GlobalUtil.getContext()).getSdcard2Path() + poiImagePath;
    }
}
