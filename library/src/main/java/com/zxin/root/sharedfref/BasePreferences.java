/**
 *
 */
package com.zxin.root.sharedfref;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import com.zxin.root.listener.Listener;
import com.zxin.root.listener.WeakSuccinctListeners;
import com.zxin.root.util.ThreadManager;

/**
 *
 */
public abstract class BasePreferences {

    private SharedPreferencesWrapper sharedPreferencesWrapper;
    private String sharedPreferencesKey;
    private SharedPreferenceChangeListener sharedPreferenceChangeListener;

    BasePreferences(SharedPreferencesWrapper sharedPreferencesWrapper, String sharedPreferencesKey) {
        this.sharedPreferencesWrapper = sharedPreferencesWrapper;
        this.sharedPreferencesKey = sharedPreferencesKey;
        String dataTypeKey = DataTypeHelper.generateDataTypeKey(sharedPreferencesWrapper, sharedPreferencesKey);
        DataTypeHelper.setDataType(dataTypeKey, getStoredDataType());
    }

    public void remove() {
        SharedPreferences.Editor editor = getSharedPreferences().edit().remove(sharedPreferencesKey);
        if (ThreadManager.isUIThread()) {//apply() 先修改内存的 然后异步commit 修改磁盘 故 无返回值
            editor.apply();
        } else {//直接修改磁盘数据 有返回值
            editor.commit();
        }
    }

    public boolean contains() {
        return getSharedPreferences().contains(sharedPreferencesKey);
    }

    /**
     * @return the {@link #sharedPreferencesKey}
     */
    public String getSharedPreferencesKey() {
        return sharedPreferencesKey;
    }

    /**
     * @return
     */
    public SharedPreferencesWrapper getSharedPreferences() {
        return sharedPreferencesWrapper;
    }

    /**
     * @param listener
     */
    public void addListener(Listener.SuccinctListener listener) {
        if (null == sharedPreferenceChangeListener) {
            sharedPreferenceChangeListener = new SharedPreferenceChangeListener(getSharedPreferences(), sharedPreferencesKey);
        }
        sharedPreferenceChangeListener.add(listener);
    }

    public abstract IData.DataType getStoredDataType();

    /**
     *
     */
    private static final class SharedPreferenceChangeListener implements OnSharedPreferenceChangeListener {

        private String sharedPreferencesKey;
        private WeakSuccinctListeners listeners = new WeakSuccinctListeners();

        /**
         *
         */
        private SharedPreferenceChangeListener(SharedPreferencesWrapper sharedPreferencesWrapper, String sharedPreferencesKey) {
            sharedPreferencesWrapper.registerOnSharedPreferenceChangeListener(this);
            this.sharedPreferencesKey = sharedPreferencesKey;
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(sharedPreferencesKey)) {
                listeners.conveyEvent();
            }
        }

        /**
         * @param listener
         */
        public void add(Listener.SuccinctListener listener) {
            listeners.add(listener);
        }

    }

}
