package com.zxin.root.util;

import android.os.Build;
import android.os.StrictMode;

import com.zxin.root.BuildConfig;
import com.zxin.root.util.logger.LogUtils;

/****
 *
 * StrictMode类是Android 2.3 （API 9）引入的一个工具类，可以用来帮助开发者发现代码中的一些不规范的问题，
 * 以达到提升应用响应能力的目的。举个例子来说，如果开发者在UI线程中进行了网络操作或者文件系统的操作，
 * 而这些缓慢的操作会严重影响应用的响应能力，甚至出现ANR对话框。为了在开发中发现这些容易忽略的问题，
 * 我们使用StrictMode，系统检测出主线程违例的情况并做出相应的反应，最终帮助开发者优化和改善代码逻辑。
 *
 *官网文档：http://developer.android.com/reference/android/os/StrictMode.html
 *
 *
 * 严格模式的开启可以放在Application或者Activity以及其他组件的onCreate方法。为了更好地分析应用中的问题，
 * 建议放在Application的onCreate方法中。其中，我们只需要在app的开发版本下使用 StrictMode，线上版本避
 * 免使用 StrictMode，这里定义了一个布尔值变量DEV_MODE来进行控制,也可以使用gradle中的
 * BuildConfig.BUILD_TYPE和BuildConfig.DEBUG来判断。
 *
 * StrictMode具体能检测什么
 * 严苛模式主要检测两大问题，一个是线程策略，即TreadPolicy，另一个是VM策略，即VmPolicy。
 *
 * Android StrictMode 详解 原文1：https://blog.csdn.net/chenshun123/article/details/51050731
 * Android严苛模式StrictMode使用详解 原文2：https://www.cnblogs.com/yaowen/p/6024690.html
 *
 */
public class StrictModeHelper {
    private static final LogUtils.Tag TAG = new LogUtils.Tag("StrictModeHelper");

    public static void enablePolicyCheck() {
        if (Build.VERSION.SDK_INT >= 9) {
            LogUtils.d(TAG, "enablePolicyCheck");
            setThreadPolicy();
            setVmPolicy();
        }
    }

    //线程策略检测
    private static void setThreadPolicy() {
        StrictMode.ThreadPolicy.Builder builder = new StrictMode.ThreadPolicy.Builder()
                .detectCustomSlowCalls()   //自定义耗时调用
                .detectDiskReads()         //磁盘读取操作
                .detectDiskWrites()        //磁盘写入操作
                .detectNetwork()            //网络操作
                .detectResourceMismatches()  //资源类型不匹配 android 23增加
                .penaltyLog();                 //打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
        StrictMode.setThreadPolicy(builder.build());
    }

    //虚拟机策略检测
    private static void setVmPolicy() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder()
                .detectActivityLeaks()             //Activity泄漏
                .detectLeakedClosableObjects()     //未关闭Closable对象泄漏
                .detectLeakedSqlLiteObjects()      //SqlLite对象泄漏
                .detectCleartextNetwork()           //网络流量监控 android 23增加
                .detectLeakedRegistrationObjects()   //广播或者服务等未注销导致泄漏  android 23增加
                .detectFileUriExposure()             //文件uri暴露   android增加
                .penaltyLog();                        //打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
        StrictMode.setVmPolicy(builder.build());
    }

    /*******
     * 1> setThreadPolicy()是一个静态函数，因此不需要实例化StrictMode对象。在内部，setThreadPolicy()将对当前线程应
     * 用该策略。如果不指定检测函数，也可以用detectAll()来替代。penaltyLog()表示将警告输出到LogCat，可以使用其他或增加
     * 新的惩罚（penalty）函数，例如使用penaltyDeath()的话，一旦StrictMode消息被写到LogCat后应用就会崩溃
     * 2> 不要频繁打开严苛模式（StrictMode），可以在主活动的 onCreate()函数中打开它，也可以在Application派生类的
     * OnCreate()函数中设置严苛模式（StrictMode）。线程中运行的任何代码都可以设置严苛模式（StrictMode），但注意只需要
     * 设置一次，一次就够了
     *
     */

    /*****
     * VmPolicy虚拟机策略检测
     * Activity泄露 使用detectActivityLeaks()开启
     * 未关闭的Closable对象泄露 使用detectLeakedClosableObjects()开启
     * 泄露的Sqlite对象 使用detectLeakedSqlLiteObjects()开启
     * 检测实例数量 使用setClassInstanceLimit()开启
     */
    public static void startStrictModeVmPolicy() {
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectAll() //检测下述所有
                .penaltyLog().build());
    }

    /****
     * ThreadPolicy线程策略检测
     * 线程策略检测的内容有
     * 自定义的耗时调用 使用detectCustomSlowCalls()开启
     * 磁盘读取操作 使用detectDiskReads()开启
     * 磁盘写入操作 使用detectDiskWrites()开启
     * 网络操作 使用detectNetwork()开启
     */
    public static void startStrictModeThreadPolicy() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()//检测下述所有
                .penaltyLog().build());
    }

}
