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
}
