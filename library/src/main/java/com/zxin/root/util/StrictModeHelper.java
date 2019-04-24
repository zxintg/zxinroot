package com.zxin.root.util;

import android.os.Build;
import android.os.StrictMode;

import com.zxin.root.util.logger.LogUtils;

/****
 * 在Application中调用
 */
public class StrictModeHelper {
    private static final LogUtils.Tag TAG = new LogUtils.Tag("StrictModeHelper");
    private static boolean mEnabled = false;

    public static void enablePolicyCheck() {
        if (Build.VERSION.SDK_INT >= 9) {
            if (mEnabled) {
                return;
            }

            mEnabled = true;
            LogUtils.d(TAG, "enablePolicyCheck");
            setThreadPolicy();
            setVmPolicy();
        }
    }

    //线程策略检测
    private static void setThreadPolicy() {
        StrictMode.ThreadPolicy.Builder builder = new StrictMode.ThreadPolicy.Builder()
                .detectAll() //detectAll() 检测下述所有
//                .detectCustomSlowCalls()   //自定义耗时调用
//                .detectDiskReads()         //磁盘读取操作
//                .detectDiskWrites()        //磁盘写入操作
//                .detectNetwork()            //网络操作
//                .detectResourceMismatches()  //资源类型不匹配 android 23增加
                .penaltyLog();                 //打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
        StrictMode.setThreadPolicy(builder.build());
    }

    //虚拟机策略检测
    private static void setVmPolicy() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder()
                .detectAll()                         //检测下述所有
//                .detectActivityLeaks()             //Activity泄漏
//                .detectLeakedClosableObjects()     //未关闭Closable对象泄漏
//                .detectLeakedSqlLiteObjects()      //SqlLite对象泄漏
//                .detectCleartextNetwork()           //网络流量监控 android 23增加
//                .detectLeakedRegistrationObjects()   //广播或者服务等未注销导致泄漏  android 23增加
//                .detectFileUriExposure()             //文件uri暴露   android增加
                .penaltyLog();                        //打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
        StrictMode.setVmPolicy(builder.build());
    }
}
