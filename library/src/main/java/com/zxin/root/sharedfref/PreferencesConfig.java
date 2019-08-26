/*******************************************************************************
 *  Copyright 2015-2016 Nextev Developments.
 *  All rights reserved.
 *
 *  Description : 导航时设置管理
 *  History :
 *  v1.0, 2016-09-21  ，create
 *  2018-12-01 modified by mike
 *
 ******************************************************************************/
package com.zxin.root.sharedfref;

import android.content.Context;

import com.zxin.root.util.GlobalUtil;

public class PreferencesConfig {
    /**
     * 初始化
     */
    public static final SharedPreferencesWrapper SHARED_PREFERENCES_INIT = new SharedPreferencesWrapper(GlobalUtil.getContext(), "init", Context.MODE_PRIVATE);

    /**
     *
     */
    public static final IntPreferences SDCARD_STATE = new IntPreferences(SHARED_PREFERENCES_INIT, "sdcard_state_key", -1);
}
