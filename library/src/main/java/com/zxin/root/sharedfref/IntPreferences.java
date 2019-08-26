package com.zxin.root.sharedfref;

import android.content.SharedPreferences;

import com.zxin.root.util.ThreadManager;

/**
 * @author jingzuo
 * Modified by Mike, change log: commit->apply to avoid anr
 */
public class IntPreferences extends BasePreferences {
    private int defaultValue;

    public IntPreferences(SharedPreferencesWrapper sharedPreferencesWrapper, String sharedPreferencesKey, int sharedPreferencesDefaultValue) {
        super(sharedPreferencesWrapper, sharedPreferencesKey);
        defaultValue = sharedPreferencesDefaultValue;
    }

    public void set(final int value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit().putInt(getSharedPreferencesKey(), value);
        if (ThreadManager.isUIThread()){
            editor.apply();
        }else{
            editor.commit();
        }
    }

    public int get() {
        return getSharedPreferences().getInt(getSharedPreferencesKey(), getDefaultValue());
    }

    @Override
    public IData.DataType getStoredDataType() {
        return IData.DataType.INT;
    }

    public int getDefaultValue() {
        return defaultValue;
    }
}
