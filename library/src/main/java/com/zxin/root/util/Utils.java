package com.zxin.root.util;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Provides convenient methods to some common operations
 */
public final class Utils {

    private Utils() {
        // Hidden constructor.
    }

    @NonNull
    public static <T> T checkNotNull(@Nullable final T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

    public static <T>int getIndex(T obj, T... objArr) {
        if (objArr == null || objArr.length == 1) {
            return 0;
        }
        for (int i = 0; i < objArr.length; i++) {
            T mObj = objArr[i];
            if (obj == mObj || mObj.equals(obj)) {
                return i;
            }
        }
        return 0;
    }

}
