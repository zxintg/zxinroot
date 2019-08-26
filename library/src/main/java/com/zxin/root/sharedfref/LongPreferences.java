package com.zxin.root.sharedfref;

import android.content.SharedPreferences;

import com.zxin.root.util.ThreadManager;

/**
 * @author jingzuo
 * Modified by Mike, change log: commit->apply to avoid anr
 */
public class LongPreferences extends BasePreferences {

    private long defaultValue;

    public LongPreferences(SharedPreferencesWrapper sharedPreferencesWrapper, String sharedPreferencesKey, long sharedPreferencesDefaultValue) {
        super(sharedPreferencesWrapper, sharedPreferencesKey);
        defaultValue = sharedPreferencesDefaultValue;
    }

    public void set(long value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit().putLong(getSharedPreferencesKey(), value);
        if (ThreadManager.isUIThread()){
            editor.apply();
        }else{
            editor.commit();
        }
    }

    public long get() {
        return getSharedPreferences().getLong(getSharedPreferencesKey(), getDefaultValue());
    }

    @Override
    public IData.DataType getStoredDataType() {
        return IData.DataType.LONG;
    }

    public long getDefaultValue() {
        return defaultValue;
    }
}
