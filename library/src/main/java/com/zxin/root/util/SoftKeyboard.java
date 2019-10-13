package com.zxin.root.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class SoftKeyboard {

    private static volatile SoftKeyboard keyboard = null;

    private Context mContext;

    private SoftKeyboard(Context mContext) {
        this.mContext = mContext;
    }

    public static SoftKeyboard getInstance(Context mContext) {
        if (keyboard == null) {
            synchronized (SoftKeyboard.class) {
                if (keyboard == null) {
                    keyboard = new SoftKeyboard(mContext.getApplicationContext());
                }
            }
        }
        return keyboard;
    }

    /**
     * 打开软键盘
     */
    public void openKeybord(EditText mEditText) {
        mEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 关闭软键盘
     */
    public void closeKeybord(EditText mEditText) {
        Activity mActivity = AppManager.getAppManager().currentActivity();
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && mActivity.getCurrentFocus() != null) {
            if (mActivity.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public void hide(EditText editText) {
        editText.clearFocus();
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    //此方法只是关闭软键盘
    public void hide() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        Activity mActivity = AppManager.getAppManager().currentActivity();
        if (imm.isActive() && mActivity.getCurrentFocus() != null) {
            if (mActivity.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

}
