package com.zxin.root.util;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.EnvironmentCompat;
import android.text.TextUtils;

import com.zxin.root.util.logger.LogUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * SD卡状态操作工具类
 */
public class SdcardUtil {
    private static volatile SdcardUtil mInstance = null;
    private static final LogUtils.Tag TAG = new LogUtils.Tag("SdcardUtil");
    private static final String mVirtualHeader = "/mnt";
    private static IntentFilter mSdcardIntentFilter = new IntentFilter();
    private List<String> mSdcard2Paths;
    private String mSdcard1Path;
    private String mSdcard2Path;
    private File[] files = null;
    private String newMapDataPath;

    static {
        mSdcardIntentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
        mSdcardIntentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        mSdcardIntentFilter.addAction(Intent.ACTION_MEDIA_CHECKING);
        mSdcardIntentFilter.addAction(Intent.ACTION_MEDIA_NOFS);
        mSdcardIntentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        mSdcardIntentFilter.addAction(Intent.ACTION_MEDIA_SHARED);
        mSdcardIntentFilter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
        mSdcardIntentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTABLE);
        mSdcardIntentFilter.addAction(Intent.ACTION_MEDIA_EJECT);
        mSdcardIntentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
        mSdcardIntentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        mSdcardIntentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mSdcardIntentFilter.addAction(Intent.ACTION_MEDIA_BUTTON);
        mSdcardIntentFilter.addDataScheme("file");
    }

    private Context mContext;
    private List<OnSdcardChangedListener> mListenerList = new LinkedList<OnSdcardChangedListener>();
    private BroadcastReceiver mSdcardBroadcastReceiver = new SdcardBroadcastReceiver();

    private SdcardUtil(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 获取单实例
     *
     * @return Sdcard对象
     */
    public static SdcardUtil getInstance(Context mContext) {
        if (mInstance == null) {
            synchronized (SdcardUtil.class) {
                if (mInstance == null) {
                    mInstance = new SdcardUtil(mContext);
                }
            }
        }
        return mInstance;
    }

    public void init(String path) {
        mInstance.registerReceiver();
        newMapDataPath = path;
        LogUtils.d(TAG, "newMapDataPath:" + newMapDataPath);
        if (Build.VERSION.SDK_INT < 19) {
            mSdcard1Path = newMapDataPath;
            mInstance.initSdcard2Paths();
        } else {
            files = ContextCompat.getExternalFilesDirs(mContext, null);
            if (files != null) {
                if (files.length > 0 && files[0] != null) {
                    mSdcard1Path = newMapDataPath;//resetPath(context, files[0].getAbsolutePath());
                }
                if (files.length > 1 && files[1] != null) {
                    mSdcard2Path = newMapDataPath;//resetPath(context, files[1].getAbsolutePath());
                }
            }
        }
    }

    private void initSdcard2() {
        String sdcardPath = getSdcardPathNoSlash();
        int count = mSdcard2Paths.size();
        for (int index = 0; index < count; index++) {
            boolean isSame = isSamePath(sdcardPath, mSdcard2Paths.get(index));
            if (isSame) {
                continue;
            }
            boolean isExsits = isExsitsPath(mSdcard2Paths.get(index));
            if (isExsits && !isSameSdcard(sdcardPath, mSdcard2Paths.get(index))) {
                mSdcard2Path = mSdcard2Paths.get(index);
                break;
            }
        }
    }

    private boolean isSameSdcard(String sdcard1, String sdcard2) {
        long sdcard1Size = getSdcardSize(sdcard1);
        long sdcard2Size = getSdcardSize(sdcard2);
        if (sdcard1Size != sdcard2Size) {
            return false;
        }
        sdcard1Size = getSdcardAvailableSize(sdcard1);
        sdcard2Size = getSdcardAvailableSize(sdcard2);
        if (sdcard1Size != sdcard2Size) {
            return false;
        }

        File f1 = new File(sdcard1);
        File f2 = new File(sdcard2);

        String[] fileList1 = f1.list();
        String[] fileList2 = f2.list();

        //都是空，则认为是同一个目录
        if (fileList1 == null && fileList2 == null) {
            return true;
        }

        //有一个为空，则认为是不同目录
        if (fileList1 == null || fileList2 == null) {
            return false;
        }

        //不一样多的文件，则认为不同目录
        if (fileList1.length != fileList2.length) {
            return false;
        }
        return true;
    }

    private boolean isExsitsPath(String path) {
        File f = new File(path);
        return f.exists() && f.canWrite();
    }

    private boolean isSamePath(String path, String path2) {
        // 名称有空则认为一样
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(path2)) {
            return true;
        }
        if (path.trim().toLowerCase().equals(path2.trim().toLowerCase())) {
            return true;
        }
        //添加/mnt
        if (path2.trim().toLowerCase().equals((mVirtualHeader + path).trim().toLowerCase())) {
            return true;
        }
        //添加/mnt
        return path.trim().toLowerCase().equals((mVirtualHeader + path2).trim().toLowerCase());
    }

    private String resetPath(Context context, String path) {
        String tempPath = path.replace("/Android/data/" + context.getPackageName() + "/files", "");
        File file = new File(tempPath, "temp" + System.currentTimeMillis() + ".txt");
        try {
            if (file.createNewFile()) {
                file.delete();
                return tempPath;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    public void unInitInstance() {
        mInstance.unregisterReceiver();
    }

    public String getSdcardState() {
        return Environment.getExternalStorageState();
    }

    public String getSdcard2State() {
        if (isExsitsSdcard2()) {
            return Environment.MEDIA_MOUNTED;
        }
        return Environment.MEDIA_UNMOUNTED;
    }

    /**
     * sdcard是否存在
     *
     * @return true 存在，false不存在
     */
    public boolean isExsitsSdcard() {
        if (Build.VERSION.SDK_INT >= 19) {
            if (mSdcard1Path == null) {
                return false;
            }
            return Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(files[0]));
        } else {
            return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        }
    }

    public boolean isExsitsSdcard2() {
        if (TextUtils.isEmpty(getSdcard2PathNoSlash())) {
            return false;
        }
        File f = new File(getSdcard2PathNoSlash());
        return f.exists() && f.canWrite();
    }

    public String getSdcardPath(String path) {
        return getSdcardPath() + path;
    }

    public String getSdcard2Path(String path) {
        return getSdcard2Path() + path;
    }

    /**
     * 获取内置卡路径。而有些手机刚好相反
     */
    public String getSdcard2Path() {
        return getSdcard2PathNoSlash() + "/";
    }

    public String getSdcard2PathNoSlash() {
        return mSdcard2Path;
    }

    /**
     * 此方法获取的不一定是外置sd卡，有些三星手机相反
     */
    public String getSdcardPath() {
        return getSdcardPathNoSlash() + "/";
    }

    /**
     * 获取SDCARD路径，一般是/mnt/sdcard/,现在有些手机sdcard路径不是标准的，希望各位通过这种方法获取SDCARD路径
     *
     * @return SDCARD路径
     */
    public String getSdcardPathNoSlash() {
        return mSdcard1Path;
    }

    /**
     * 返回SD卡大小，单位是Byte
     *
     * @return
     */
    public long getSdcardSize() {
        if (!isExsitsSdcard()) {
            return 0;
        }
        File sdcard = /*Environment.getExternalStorageDirectory()*/new File(mSdcard1Path);
        return getSdcardSize(sdcard.getPath());
    }

    public long getSdcard2Size() {
        if (!isExsitsSdcard2()) {
            return 0;
        }
        File sdcard = new File(mSdcard2Path);
        return getSdcardSize(sdcard.getPath());
    }

    /**
     * 返回SD卡剩余大小，单位是Byte
     *
     * @return
     */
    public long getSdcardAvailSize() {
        if (!isExsitsSdcard()) {
            return 0;
        }
        File sdcard = /*Environment.getExternalStorageDirectory()*/new File(mSdcard1Path);
        return getSdcardAvailableSize(sdcard.getPath());
    }

    /**
     * 返回SD卡剩余大小，单位是Byte
     *
     * @return
     */
    public long getSdcard2AvailSize() {
        if (!isExsitsSdcard2()) {
            return 0;
        }
        File sdcard = new File(mSdcard2Path);
        return getSdcardAvailableSize(sdcard.getPath());
    }

    private long getSdcardSize(String sdcardPath) {
        long size = 0;
        try {
            StatFs statFs = new StatFs(sdcardPath);
            int blockSize = statFs.getBlockSize();
            int totalBlocks = statFs.getBlockCount();
            size = (long) totalBlocks * (long) blockSize;
        } catch (Exception e) {
        }
        return size;
    }

    private long getSdcardAvailableSize(String sdcardPath) {
        long size = 0;
        try {
            StatFs statFs = new StatFs(sdcardPath);
            int blockSize = statFs.getBlockSize();
            int availableBlocks = statFs.getAvailableBlocks();
            size = (long) availableBlocks * (long) blockSize;
        } catch (Exception e) {
        }
        return size;
    }

    /**
     * sd是否可以卸载，如果可以卸载则是外置卡
     *
     * @return
     */
    @SuppressLint("NewApi")
    public boolean isSdcardStorageRemovable() {
        if (Build.VERSION.SDK_INT < 9) {
            return true;
        } else if (Build.VERSION.SDK_INT >= 9 && Build.VERSION.SDK_INT < 19) {
            //测试发现4.4该方法返回false,因此屏蔽4.4以上版本校验
            return Environment.isExternalStorageRemovable();
        } else {
            return false;
        }
    }

    @SuppressLint("SdCardPath")
    private void initSdcard2Paths() {
        // 3.2及以上SDK识别路径
        mSdcard2Paths = getSdcard2Paths();
        mSdcard2Paths.add("/mnt/emmc");
        mSdcard2Paths.add("/mnt/extsdcard");
        mSdcard2Paths.add("/mnt/ext_sdcard");
        mSdcard2Paths.add("/sdcard-ext");
        mSdcard2Paths.add("/mnt/sdcard-ext");
        mSdcard2Paths.add("/sdcard2");
        mSdcard2Paths.add("/sdcard");
        mSdcard2Paths.add("/mnt/sdcard2");
        mSdcard2Paths.add("/mnt/sdcard");
        mSdcard2Paths.add("/sdcard/sd");
        mSdcard2Paths.add("/sdcard/external");
        mSdcard2Paths.add("/flash");
        mSdcard2Paths.add("/mnt/flash");
        mSdcard2Paths.add("/mnt/sdcard/external_sd");

        mSdcard2Paths.add("/mnt/external1");
        mSdcard2Paths.add("/mnt/sdcard/extra_sd");
        mSdcard2Paths.add("/mnt/sdcard/_ExternalSD");
        mSdcard2Paths.add("/mnt/extrasd_bin");
        //4.1SDK 识别路径
        mSdcard2Paths.add("/storage/extSdCard");
        mSdcard2Paths.add("/storage/sdcard0");
        mSdcard2Paths.add("/storage/sdcard1");
        initSdcard2();
    }

    @SuppressLint("InlinedApi")
    private List<String> getSdcard2Paths() {
        List<String> paths = new LinkedList<String>();
        if (Build.VERSION.SDK_INT < 13) {
            return paths;
        }
        StorageManager sm = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        try {
            Class<? extends StorageManager> clazz = sm.getClass();
            Method mlist = clazz.getMethod("getVolumeList", (Class[]) null);
            Class<?> cstrvol = Class.forName("android.os.storage.StorageVolume");
            Method mvol = cstrvol.getMethod("getPath", (Class[]) null);
            Object[] objects = (Object[]) mlist.invoke(sm);
            if (objects != null && objects.length > 0) {
                for (Object obj : objects) {
                    paths.add((String) mvol.invoke(obj));
                }
            }
        } catch (Exception e) {
        }
        return paths;
    }

    private void registerReceiver() {
        mContext.registerReceiver(mSdcardBroadcastReceiver, mSdcardIntentFilter);
    }

    private void unregisterReceiver() {
        removeAllListener();
        mContext.unregisterReceiver(mSdcardBroadcastReceiver);
    }

    /**
     * 添加SDCARD改变事件监听
     *
     * @param listener 监听
     */
    public void addListener(OnSdcardChangedListener listener) {
        mListenerList.add(listener);
    }

    /**
     * 移除监听，可以不移除在系统关闭的时候会自动移除所有监听
     *
     * @param listener 监听
     * @return 是否移除成功
     */
    public boolean removeListener(OnSdcardChangedListener listener) {
        boolean isContains = false;
        synchronized (mListenerList) {
            isContains = mListenerList.contains(listener);
            if (isContains) {
                mListenerList.remove(listener);
            }
        }
        return isContains;
    }

    /**
     * 移除所有监听
     */
    public void removeAllListener() {
        synchronized (mListenerList) {
            mListenerList.clear();
        }
    }

    /**
     * SDCARD状态改变接口
     */
    public interface OnSdcardChangedListener {
        void onSdcardChanged(SdcardUtil sender);
    }

    public class SdcardBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            synchronized (mListenerList) {
                for (int index = mListenerList.size() - 1; index > -1; index--) {
                    OnSdcardChangedListener listener = mListenerList.get(index);
                    if (listener == null) {
                        continue;
                    }
                    listener.onSdcardChanged(SdcardUtil.this);
                }
            }
        }
    }
}
