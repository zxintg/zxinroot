package com.zxin.root.sharedfref;

import android.content.SharedPreferences;

import com.zxin.root.util.ThreadManager;

/**
 * @author jingzuo
 * Modified by Mike, change log: commit->apply to avoid anr
 */
public class BooleanPreferences extends BasePreferences {

    private boolean defaultValue;

    public BooleanPreferences(SharedPreferencesWrapper sharedPreferencesWrapper, String sharedPreferencesKey, boolean sharedPreferencesDefaultValue) {
        super(sharedPreferencesWrapper, sharedPreferencesKey);
        defaultValue = sharedPreferencesDefaultValue;
    }

    public void set(boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit().putBoolean(getSharedPreferencesKey(), value);
        if (ThreadManager.isUIThread()){
            editor.apply();
        }else{
            editor.commit();
        }
    }

    public boolean get() {
        return getSharedPreferences().getBoolean(getSharedPreferencesKey(), getDefaultValue());
    }


    @Override
    public IData.DataType getStoredDataType() {
        return IData.DataType.BOOLEAN;
    }

    public boolean getDefaultValue() {
        return defaultValue;
    }
}
