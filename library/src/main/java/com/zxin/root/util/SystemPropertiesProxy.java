package com.zxin.root.util;


import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexFile;

import android.content.Context;

import com.zxin.root.util.logger.LogUtils;


/*****
 *
 * 在一个Android应用中因为要获取系统的属性, 比如说型号, model等一些属性, 通过下列方法就可以获取到.首先查看手机/system目录下build.prop文件。
 * 在手机/system目录下build.prop文件存放手机相关系统属性。
 * build.prop是如何生成的呢？
 * Android的build.prop文件是在Android编译时刻收集的各种property。
 * 编译完成之后，文件生成在/out/target/product/huaqin75_cu_ics/system/目录下。
 * 在Android运行时刻可以通过SystemProperties_get*()读取这些属性值。
 * build.prop的生成是由make系统解析build/core/Makefile完成。
 *
 *
 *方法一：
 * 主要通过adb命令shell命令查看此文件信息，导出build.prop文件：adb pull /system/build.prop  d:/test   将文件复制到电脑D盘test目录下
 * 通过Java反射机制获取SystemProperties.java内容，因为SystemProperties.java已被系统隐藏，通过get和set方法来读取build.prop里面的内容
 * 在此我通过反射机制来读取系统属性：
 *
 *
 * 方法二：
 *      build.gradle 添加一下代码
 *
 *      //以下是为了找到android.os.SystemProperties这个隐藏的类
 *     String SDK_DIR = System.getenv("ANDROID_SDK_HOME")
 *     //("TAG", "SDK_DIR = " + SDK_DIR );
 *     if(SDK_DIR == null) {
 *         Properties props = new Properties()
 *         props.load(new FileInputStream(project.rootProject.file("local.properties")))
 *         SDK_DIR = props.get('sdk.dir');
 *     }
 *     dependencies {
 *         implementation files("${SDK_DIR}/platforms/android-21/data/layoutlib.jar")
 *     }
 *
 *
 */
public class SystemPropertiesProxy {

    private static LogUtils.Tag TAG = new LogUtils.Tag("SystemPropertiesProxy");

    private static volatile SystemPropertiesProxy proxy = null;
    private Context mContext;


    private SystemPropertiesProxy(Context mContext) {
        this.mContext = mContext;
    }

    public static SystemPropertiesProxy getInstance(Context mContext) {
        if (proxy == null) {
            synchronized (SystemPropertiesProxy.class) {
                if (proxy == null) {
                    proxy = new SystemPropertiesProxy(mContext);
                }
            }
        }
        return proxy;
    }


    /**
     * 根据给定Key获取值.
     *
     * @return 如果不存在该key则返回空字符串
     * @throws IllegalArgumentException 如果key超过32个字符则抛出该异常
     */
    public String get(String key) throws IllegalArgumentException {
        String ret = "";
        try {
            ClassLoader cl = mContext.getClassLoader();
            @SuppressWarnings("rawtypes")
            Class SystemProperties = cl.loadClass("android.os.SystemProperties");
            //参数类型
            @SuppressWarnings("rawtypes")
            Class[] paramTypes = new Class[1];
            paramTypes[0] = String.class;
            Method get = SystemProperties.getMethod("get", paramTypes);
            //参数
            Object[] params = new Object[1];
            params[0] = new String(key);
            ret = (String) get.invoke(SystemProperties, params);
        } catch (IllegalArgumentException iAE) {
            throw iAE;
        } catch (Exception e) {
            ret = "";
            //TODO
        }
        return ret;
    }

    /**
     * 根据Key获取值.
     *
     * @return 如果key不存在, 并且如果def不为空则返回def否则返回空字符串
     * @throws IllegalArgumentException 如果key超过32个字符则抛出该异常
     */
    public String get(String key, String def) throws IllegalArgumentException {
        String ret = def;
        try {
            ClassLoader cl = mContext.getClassLoader();
            @SuppressWarnings("rawtypes")
            Class SystemProperties = cl.loadClass("android.os.SystemProperties");
            //参数类型
            @SuppressWarnings("rawtypes")
            Class[] paramTypes = new Class[2];
            paramTypes[0] = String.class;
            paramTypes[1] = String.class;
            Method get = SystemProperties.getMethod("get", paramTypes);
            //参数
            Object[] params = new Object[2];
            params[0] = new String(key);
            params[1] = new String(def);
            ret = (String) get.invoke(SystemProperties, params);
        } catch (IllegalArgumentException iAE) {
            throw iAE;
        } catch (Exception e) {
            ret = def;
            //TODO
        }
        return ret;
    }

    /**
     * 根据给定的key返回int类型值.
     *
     * @param key 要查询的key
     * @param def 默认返回值
     * @return 返回一个int类型的值, 如果没有发现则返回默认值
     * @throws IllegalArgumentException 如果key超过32个字符则抛出该异常
     */
    public Integer getInt(String key, int def) throws IllegalArgumentException {
        Integer ret = def;
        try {
            ClassLoader cl = mContext.getClassLoader();
            @SuppressWarnings("rawtypes")
            Class SystemProperties = cl.loadClass("android.os.SystemProperties");
            //参数类型
            @SuppressWarnings("rawtypes")
            Class[] paramTypes = new Class[2];
            paramTypes[0] = String.class;
            paramTypes[1] = int.class;
            Method getInt = SystemProperties.getMethod("getInt", paramTypes);
            //参数
            Object[] params = new Object[2];
            params[0] = new String(key);
            params[1] = new Integer(def);
            ret = (Integer) getInt.invoke(SystemProperties, params);
        } catch (IllegalArgumentException iAE) {
            throw iAE;
        } catch (Exception e) {
            ret = def;
            //TODO
        }
        return ret;
    }

    /**
     * 根据给定的key返回long类型值.
     *
     * @param key 要查询的key
     * @param def 默认返回值
     * @return 返回一个long类型的值, 如果没有发现则返回默认值
     * @throws IllegalArgumentException 如果key超过32个字符则抛出该异常
     */
    public Long getLong(String key, long def) throws IllegalArgumentException {
        Long ret = def;
        try {
            ClassLoader cl = mContext.getClassLoader();
            @SuppressWarnings("rawtypes")
            Class SystemProperties = cl.loadClass("android.os.SystemProperties");
            //参数类型
            @SuppressWarnings("rawtypes")
            Class[] paramTypes = new Class[2];
            paramTypes[0] = String.class;
            paramTypes[1] = long.class;
            Method getLong = SystemProperties.getMethod("getLong", paramTypes);
            //参数
            Object[] params = new Object[2];
            params[0] = new String(key);
            params[1] = new Long(def);
            ret = (Long) getLong.invoke(SystemProperties, params);
        } catch (IllegalArgumentException iAE) {
            throw iAE;
        } catch (Exception e) {
            ret = def;
            //TODO
        }
        return ret;
    }

    /**
     * 根据给定的key返回boolean类型值.
     * 如果值为 'n', 'no', '0', 'false' or 'off' 返回false.
     * 如果值为'y', 'yes', '1', 'true' or 'on' 返回true.
     * 如果key不存在, 或者是其它的值, 则返回默认值.
     *
     * @param key 要查询的key
     * @param def 默认返回值
     * @return 返回一个boolean类型的值, 如果没有发现则返回默认值
     * @throws IllegalArgumentException 如果key超过32个字符则抛出该异常
     */
    public Boolean getBoolean(String key, boolean def) throws IllegalArgumentException {
        Boolean ret = def;
        try {
            ClassLoader cl = mContext.getClassLoader();
            @SuppressWarnings("rawtypes")
            Class SystemProperties = cl.loadClass("android.os.SystemProperties");
            //参数类型
            @SuppressWarnings("rawtypes")
            Class[] paramTypes = new Class[2];
            paramTypes[0] = String.class;
            paramTypes[1] = boolean.class;
            Method getBoolean = SystemProperties.getMethod("getBoolean", paramTypes);
            //参数
            Object[] params = new Object[2];
            params[0] = new String(key);
            params[1] = new Boolean(def);
            ret = (Boolean) getBoolean.invoke(SystemProperties, params);
        } catch (IllegalArgumentException iAE) {
            throw iAE;
        } catch (Exception e) {
            ret = def;
            //TODO
        }
        return ret;
    }

    /**
     * 根据给定的key和值设置属性, 该方法需要特定的权限才能操作.
     *
     * @throws IllegalArgumentException 如果key超过32个字符则抛出该异常
     * @throws IllegalArgumentException 如果value超过92个字符则抛出该异常
     */
    public void set(String key, String val) throws IllegalArgumentException {
        try {
            @SuppressWarnings("unused")
            DexFile df = new DexFile(new File("/system/app/Settings.apk"));
            @SuppressWarnings("unused")
            ClassLoader cl = mContext.getClassLoader();
            @SuppressWarnings("rawtypes")
            Class SystemProperties = Class.forName("android.os.SystemProperties");
            //参数类型
            @SuppressWarnings("rawtypes")
            Class[] paramTypes = new Class[2];
            paramTypes[0] = String.class;
            paramTypes[1] = String.class;
            Method set = SystemProperties.getMethod("set", paramTypes);
            //参数
            Object[] params = new Object[2];
            params[0] = new String(key);
            params[1] = new String(val);
            set.invoke(SystemProperties, params);
        } catch (IllegalArgumentException iAE) {
            throw iAE;
        } catch (Exception e) {
            //TODO
        }
    }
}
