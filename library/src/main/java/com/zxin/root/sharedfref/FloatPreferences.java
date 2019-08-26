package com.zxin.root.sharedfref;

import android.content.SharedPreferences;

import com.zxin.root.util.ThreadManager;

/**
 * @author jingzuo
 * Modified by Mike, change log: commit->apply to avoid anr
 */
public class FloatPreferences extends BasePreferences {

    private float defaultValue;

    public FloatPreferences(SharedPreferencesWrapper sharedPreferencesWrapper, String sharedPreferencesKey, float sharedPreferencesDefaultValue) {
        super(sharedPreferencesWrapper, sharedPreferencesKey);
        defaultValue = sharedPreferencesDefaultValue;
    }

    public void set(float value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit().putFloat(getSharedPreferencesKey(), value);
        if (ThreadManager.isUIThread()){
            editor.apply();
        }else{
            editor.commit();
        }
    }

    public float get() {
        return getSharedPreferences().getFloat(getSharedPreferencesKey(), getDefaultValue());
    }

    @Override
    public IData.DataType getStoredDataType() {
        return IData.DataType.FLOAT;
    }

    public float getDefaultValue() {
        return defaultValue;
    }
}
