package com.zxin.root.util;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import com.zxin.root.bean.AppInfoBean;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SystemInfoUtil {

    public static final String TAG = SystemInfoUtil.class.getSimpleName();

    private static volatile SystemInfoUtil infoUtil = null;
    private static String mVersionName;
    private static String mVersionCode;

    private Context mContext;
    private SystemInfoUtil(Context mContext){
        this.mContext = mContext;
    }

    public static SystemInfoUtil getInstance(Context mContext){
        if (infoUtil==null){
            synchronized (SystemInfoUtil.class){
                if (infoUtil==null){
                    infoUtil = new SystemInfoUtil(mContext.getApplicationContext());
                }
            }
        }
        return infoUtil;
    }

    /**
     * 得到sdk 版本号
     *
     * @return
     */
    public int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 判断网络是否可用
     *
     * @return
     */
    public boolean isNetworkAvailable() {
        try {
            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netWorkInfo = cm.getActiveNetworkInfo();
            return (netWorkInfo != null && netWorkInfo.isAvailable());// 检测网络是否可用
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public String getSystemModel() {
        return Build.MODEL;
    }

    public String getProjectName() {
        PackageManager pm = mContext.getPackageManager();
        String appName = mContext.getApplicationInfo().loadLabel(pm).toString();
        return appName;
    }

    /**
     * 得到版本名称
     *
     * @return
     */
    public String getAppVersionName() {
        if (TextUtils.isEmpty(mVersionName)) {
            try {
                PackageManager packageManager = mContext.getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
                String appVersion = packageInfo.versionName; // appVersion
                mVersionName = appVersion;
            } catch (Exception e) {
                return mVersionName = "";
            }
        }
        return mVersionName;
    }

    /**
     * 得到版本号
     *
     * @return
     */
    public String getAppVersionCode() {
        if (BaseStringUtils.isNull(mVersionCode)) {
            try {
                PackageManager packageManager = mContext.getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
                String appVersion = String.valueOf(packageInfo.versionCode); // appVersion
                mVersionCode = appVersion;
            } catch (Exception e) {
                return mVersionCode = "";
            }
        }
        return mVersionCode;
    }

    /****
     * 是否强行更新app
     * @return
     */
    public boolean isUpdateApp(String webAppCode) {
        String[] localCode = getApplicationName().split("\\.");
        String[] webCode = webAppCode.split("\\.");
        if (localCode.length != webCode.length)
            return true;
        if (localCode.length != 3)
            return true;
        return Integer.parseInt(localCode[0]) > Integer.parseInt(webCode[0]) || Integer.parseInt(localCode[1]) > Integer.parseInt(webCode[1]) || Integer.parseInt(localCode[2]) > Integer.parseInt(webCode[2]) + 2;
    }


    public String getPackageName(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            if (info != null) {
                return info.packageName;
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getApplicationName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = mContext.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(getPackageName(mContext), 0);
        } catch (NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName =
                (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

    @SuppressLint("MissingPermission")
    public String getPhoneNum() {
        TelephonyManager phoneMgr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        return phoneMgr.getLine1Number();
    }

    @SuppressLint("MissingPermission")
    public String getIMEI(Context _context) {
        TelephonyManager _manager = (TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE);
        String _imei = _manager.getDeviceId(); // 取出IMEI
        return _imei;
    }

    @SuppressLint("MissingPermission")
    public String getICCID(Context _context) {
        TelephonyManager _manager = (TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE);
        String _iccid = _manager.getSimSerialNumber(); // 取出ICCID
        return _iccid;
    }

    @SuppressLint("MissingPermission")
    public String getIMSI(Context _context) {
        TelephonyManager _manager = (TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE);
        String _imsi = _manager.getSubscriberId(); // 取出IMSI
        return _imsi;
    }

    public boolean isPad(Context _context) {
        if (Build.VERSION.SDK_INT >= 11) {
            Configuration con = _context.getResources().getConfiguration();
            try {
                Method mIsLayoutSizeAtLeast = con.getClass().getMethod("isLayoutSizeAtLeast", int.class);
                return (Boolean) mIsLayoutSizeAtLeast.invoke(con, 0x00000004);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public Intent call_1(String _phoneNum) {

        return new Intent(Intent.ACTION_CALL_BUTTON, Uri.parse("tel:" + _phoneNum));
    }

    /*****
     * 去拨号界面
     * @param _phoneNum
     * @return
     */
    public void callDialing(String _phoneNum) {
        ActivityCompat.startActivity(mContext, new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + _phoneNum)), null);
    }

    /**
     * 得到设备的唯一Id
     *
     * @return
     */
    public String getInstallationId() {
        return Build.SERIAL;
    }

    /**
     * 获取屏幕参数
     */
    public DisplayMetrics getDisplayMetrics() {
        return mContext.getResources().getDisplayMetrics();
    }

    /**
     * 得到设备屏幕的宽度
     */
    public int getScreenWidth() {
        return getDisplayMetrics().widthPixels;
    }

    /**
     * 得到设备屏幕的高度
     */
    public int getScreenHeight() {
        return getDisplayMetrics().heightPixels;
    }

    /**
     * 得到设备的密度
     */
    public float getScreenDensity() {
        return getDisplayMetrics().density;
    }

    public int dpTpPx(float value) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getDisplayMetrics()) + 0.5);
    }

    /**
     * 把密度转换为像素
     */
    public int dip2px(float px) {
        final float scale = getScreenDensity();
        return (int) (px * scale + 0.5);
    }

    public int px2dip(float pxValue) {
        final float scale = getScreenDensity();
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 跳转到权限设置界面
     */
    public void getSettingIntent() {

        // vivo 点击设置图标>加速白名单>我的app
        //点击软件管理>软件管理权限>软件>我的app>信任该软件
        Intent appIntent = mContext.getPackageManager().getLaunchIntentForPackage("com.iqoo.secure");
        if (appIntent != null) {
            mContext.startActivity(appIntent);
            return;
        }

        // oppo 点击设置图标>应用权限管理>按应用程序管理>我的app>我信任该应用
        //点击权限隐私>自启动管理>我的app
        appIntent = mContext.getPackageManager().getLaunchIntentForPackage("com.oppo.safe");
        if (appIntent != null) {
            mContext.startActivity(appIntent);
            return;
        }

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(mContext), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName(mContext));
        }
        mContext.startActivity(intent);
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /*****
     * 查询本机所有可以分享
     * @return
     * @param share
     */
    public List<AppInfoBean> canShareList(List<String> share) {
        List<AppInfoBean> canList = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");//设置分享内容的类型 image/*、text/plain
        PackageManager pm = mContext.getPackageManager();
        List<ResolveInfo> resInfoList = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resInfo : resInfoList) {
            String pkgName = resInfo.activityInfo.packageName; // 获得应用程序的包名
            if (share == null || share.isEmpty() || share.contains(pkgName)) {
                String activityName = resInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
                String appLabel = (String) resInfo.loadLabel(pm); // 获得应用程序的Label
                String appName = resInfo.activityInfo.applicationInfo.loadLabel(pm).toString();
                Drawable icon = resInfo.loadIcon(pm); // 获得应用程序图标
                // 为应用程序的启动Activity 准备Intent
                Intent launchIntent = new Intent();
                launchIntent.setComponent(new ComponentName(pkgName, activityName));
                // 创建一个AppInfo对象，并赋值
                AppInfoBean appInfo = new AppInfoBean();
                appInfo.setAppName(appName);
                appInfo.setAppLabel(appLabel);
                appInfo.setPkgName(pkgName);
                appInfo.setAppIcon(icon);
                appInfo.setIntent(launchIntent);
                canList.add(appInfo);
            }
        }
        return canList;
    }

    /**
     * Retrieve launcher activity name of the application from the context
     *
     * @param context The context of the application package.
     * @return launcher activity name of this application. From the
     * "android:name" attribute.
     */
    public String getLauncherClassName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setPackage(context.getPackageName());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // All Application must have 1 Activity at least.
        // Launcher activity must be found!
        ResolveInfo info = packageManager
                .resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        // get a ResolveInfo containing ACTION_MAIN, CATEGORY_LAUNCHER
        // if there is no Activity which has filtered by CATEGORY_DEFAULT
        if (info == null) {
            info = packageManager.resolveActivity(intent, 0);
        }
        //////////////////////另一种实现方式//////////////////////
        // ComponentName componentName = context.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName()).getComponent();
        // return componentName.getClassName();
        //////////////////////另一种实现方式//////////////////////
        return info.activityInfo.name;
    }

    /******
     * 获取当前
     * @return
     */
    public View getCurrentView() {
        FrameLayout contentParent = (FrameLayout) AppManager.getAppManager().currentActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        return contentParent.getFocusedChild();
    }

    /*****
     * 判断手机中是否安装了
     * @param packageName
     * @return
     */
    public boolean isPackageInstalled(String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = mContext.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        } finally {
            return packageInfo != null;
        }
    }

    /****
     * 剪切板
     * @param title
     * @param mesg
     */
    public void systemClipBoard(String title, String mesg) {
        ((ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText(title, mesg));
    }

    public String getActivityMetaData(String tagName) {
        ActivityInfo info = null;
        try {
            info = mContext.getPackageManager()
                    .getActivityInfo(AppManager.getAppManager().currentActivity().getComponentName(),
                            PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return info == null? "" : info.metaData.getString(tagName);
    }

    public String getAppMetaData(String tagName) {
        ApplicationInfo info = null;
        try {
            info = mContext.getPackageManager()
                    .getApplicationInfo(mContext.getPackageName(),
                            PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return info == null? "" : info.metaData.getString(tagName);
    }

    public String getServiceMetaData(String tagName,Class service) {
        ServiceInfo info = null;
        try {
            info = mContext.getPackageManager()
                    .getServiceInfo(new ComponentName(mContext, service),
                            PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return info == null? "" : info.metaData.getString(tagName);
    }

    public String getReceiverMetaData(String tagName,Class service) {
        ActivityInfo info = null;
        try {
            info = mContext.getPackageManager()
                    .getReceiverInfo(new ComponentName(mContext, service),
                            PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return info == null? "" : info.metaData.getString(tagName);
    }

    public String getAndroidId(){
        return Settings.System.getString(mContext.getContentResolver(), Settings.System.ANDROID_ID);
    }

    @SuppressLint("InvalidWakeLockTag")
    public PowerManager.WakeLock getPowerManager(){
        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        return pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "liveTAG");
    }

    public String getOSVer() {
        return Build.DISPLAY;
    }

    public String getModel() {
        return Build.MODEL;
    }

    public String getLanguage() {
        return UiUtils.getInstance(mContext).getLocale().getLanguage();
    }

    public String getRegion() {
        return UiUtils.getInstance(mContext).getLocale().getCountry();
    }

    /***
     *
     * 获取去Window 实例
     *
     * kui.liu 2019/04/10 10:02
     * @return
     */
    public Window getWindow() {
        return AppManager.getAppManager().currentActivity().getWindow();
    }

    /*****
     * 获取WindowToken 实例
     *
     * kui.liu 2019/04/10 16:40
     *
     * @param mView
     * @return
     */
    public IBinder getWindowToken(View mView) {
        return mView == null ? getWindow().getDecorView().getWindowToken() : mView.getWindowToken();
    }

    public String getProcessName() {
        if (mContext != null && mContext.getApplicationInfo() != null) {
            return mContext.getApplicationInfo().processName;
        }
        return "unknown";
    }

}
