package com.zxin.root.sharedfref;

import android.content.SharedPreferences;

import com.zxin.root.util.ThreadManager;

/**
 * @author liang.tian
 * Modified by Mike, change log: commit->apply to avoid anr
 */
public class StringPreferences extends BasePreferences {

    private String defaultValue;

    public StringPreferences(SharedPreferencesWrapper sharedPreferencesWrapper, String sharedPreferencesKey, String sharedPreferencesDefaultValue) {
        super(sharedPreferencesWrapper, sharedPreferencesKey);
        defaultValue = sharedPreferencesDefaultValue;
    }

    public void set(String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit().putString(getSharedPreferencesKey(), value);
        if (ThreadManager.isUIThread()){
            editor.apply();
        }else{
            editor.commit();
        }
    }

    public String get() {
        return getSharedPreferences().getString(getSharedPreferencesKey(), getDefaultValue());
    }

    @Override
    public IData.DataType getStoredDataType() {
        return IData.DataType.STRING;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
