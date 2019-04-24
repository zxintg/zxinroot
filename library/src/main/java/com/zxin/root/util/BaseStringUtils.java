package com.zxin.root.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import com.zxin.root.util.logger.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liukui on 2017/12/5.
 */

public class BaseStringUtils {
    public static final String AppFromParent = "parent";
    public static final String fileprovider = "com.zxin.fileprovider";
    public static final String pageNameParent = "com.zxin";
    public static final String Share_Datas = "Share_Datas";
    public static final String System_Datas = "System_Datas";
    public static final String JiuXian_Datas = "JiuXian_Datas";
    public static final String YoWu_Datas = "YoWu_Datas";
    public static final String MeiPicture_Datas = "MeiPicture_Datas";
    public static final String Marry_Datas = "Marry_Datas";
    public static final String UserInfo_Datas = "UserInfo_Datas";
    public static final String HomeBean_Datas = "HomeBean_Datas";
    public static final String Share_Area = "Area";
    public static final String Share_City = "City";
    public static final String Share_MineMenu = "MineMenu";
    public static final String ClASS_CITY = "ClassCity";
    public static final String Share_Phone = "Phone";
    public static final String duration = "duration";
    public static final String calendar = "calendar";
    public static final String data = "data";
    public static final String num = "num";
    public static final String price = "price";
    public static final String WeekList = "week";
    public static final String EVENT_ID = "eventId";
    public static final String EVENT_DATA = "data";
    public static final String EVENT_DATA2 = "data2";
    public static final String HANDLER_DATA = "data";
    public static final String HANDLER_DATA2 = "data2";
    public static final String ACTIVITY_DATA = "data";
    public static final String ACTIVITY_DATA2 = "data2";
    public static final String FRAGMENT_DATA = "data";
    public static final String ACTIVITY_title = "title";
    public static final String ORDER_ID = "id";
    public static final String FRAGMENT_INDEX = "index";
    public static final String cityCode = "cityCode";
    public static final String url = "url";
    public static final String activcity_from = "from";
    public static final String select_payStatus = "payStatus";
    public static final String WEB_TYPE = "web_type";
    public static final String WEB_TYPE_fileHtml5 = "fileHtml5";
    public static final String WEB_TYPE_url = "url";
    public static final String WEB_TYPE_html5 = "html5";
    public static final String RegistrationID = "RegistrationID";
    public static final String BadgeCount = "BadgeCount";
    public static final String Share_BeiKe_Datas = "Share_BeiKe_Datas";
    public static final String access_token = "access_token";

    /****
     * 空字符串
     * @param str
     * @return
     */
    public static boolean textIsEmpty(String str) {
        return TextUtils.isEmpty(str) || str.equals("null") || str.equals(" ") || str.length() == 0;
    }

    /**
     * <i> 判断输入字符串是否为空 </i>
     *
     * @param inputStr 指定的字符串
     * @return 如果为空则返回true 如果不为空则返回false
     * @author <a href="mailto:wangyf@mapbar.com">wangyf</a>
     * @date 2011-5-25 下午02:15:02
     */
    public static boolean isNull(Object inputStr) {
        return (null == inputStr) || "".equals(inputStr) || "null".equals(inputStr) || "".equals(inputStr.toString().trim());
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static String trim(String str) {
        return isNull(str) || textIsEmpty(str) ? "" : str.trim();
    }

    /*****
     * 获取格式化字符串
     * @param str
     * @return
     */
    public static String textStr(String str) {
        return textIsEmpty(str) ? "" : str;
    }

    /*****
     * 格式化Html
     * @param mesg
     * @return
     */
    public static Spanned textFormatHtml(String mesg) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(mesg, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(mesg);
        }
    }

    /****
     * 性别
     * @param sex
     * @return
     */
    public static String getSex(int sex) {
        return sex == 1 ? "男" : sex == 0 ? "女" : "";
    }

    /****
     * 转换男女
     * @param sex
     * @return
     */
    public static int getSexInt(String sex) {
        return sex.equals("男") ? 1 : 0;
    }

    /*****
     * EditText每4位自动添加空格
     * @param content
     * @return
     */
    public static String addSpeaceByCredit(String content) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        //去空格
        content = content.replaceAll(" ", "");
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        //卡号限制为16位
        if (content.length() > 19) {
            content = content.substring(0, 19);
        }
        StringBuilder newString = new StringBuilder();
        for (int i = 1; i <= content.length(); i++) {
            //当为第4位时，并且不是最后一个第4位时
            //拼接字符的同时，拼接一个空格
            //如果在最后一个第四位也拼接，会产生空格无法删除的问题
            //因为一删除，马上触发输入框改变监听，又重新生成了空格
            if (i % 4 == 0 && i != content.length()) {
                newString.append(content.charAt(i - 1) + " ");
            } else {
                //如果不是4位的倍数，则直接拼接字符即可
                newString.append(content.charAt(i - 1));

            }
        }
        return newString.toString();
    }

    /**
     * 版本号对比
     *
     * @param apkVersion
     * @return error : 返回-2 既传入版本号格式有误
     * apkVersion > currentVersion  return -1
     * apkVersion = currentVersion  return 0
     * apkVersion < currentVersion  return 1
     */
    public static int compareVersions(Context mContext, String apkVersion) {
        //返回结果: -2 ,-1 ,0 ,1
        int result = 0;
        String matchStr = "[0-9]+(\\.[0-9]+)*";
        apkVersion = apkVersion.replace("V", "").replace("v", "").trim();
        String currentVersion = SystemInfoUtil.getInstance(mContext).getAppVersionName().trim();
        //非版本号格式,返回error
        if (!apkVersion.matches(matchStr)) {
            return -2;
        }
        String[] oldVs = apkVersion.split("\\.");
        String[] newVs = currentVersion.split("\\.");
        int oldLen = oldVs.length;
        int newLen = newVs.length;
        int len = oldLen > newLen ? oldLen : newLen;
        for (int i = 0; i < len; i++) {
            if (i < oldLen && i < newLen) {
                int o = Integer.parseInt(oldVs[i]);
                int n = Integer.parseInt(newVs[i]);
                if (o > n) {
                    result = -1;
                    break;
                } else if (o < n) {
                    result = 1;
                    break;
                }
            } else {
                if (oldLen > newLen) {
                    for (int j = i; j < oldLen; j++) {
                        if (Integer.parseInt(oldVs[j]) > 0) {
                            result = -1;
                        }
                    }
                    break;
                } else if (oldLen < newLen) {
                    for (int j = i; j < newLen; j++) {
                        if (Integer.parseInt(newVs[j]) > 0) {
                            result = 1;
                        }
                    }
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 判断某个字符串是否存在于数组中
     *
     * @param stringArray 原数组
     * @param subStr      查找的字符串
     * @return 是否找到
     */
    public static boolean contains(String[] stringArray, String subStr) {
        for (String s : stringArray) {
            if (subStr.indexOf(s) != -1) {
                return true;
            }
        }
        return false;
    }

    private static final String mMunicipality[] = {"上海", "北京", "天津", "重庆", "香港", "澳门", "台湾"};
    private static final String regex = "([^省]+自治区|.*?省)?([^市]+自治州|.*?行政区|.*?地区|.*?行政单位|.+盟|市辖区|.*?市|.*?县)?([^县]+县|.+区|.+市|.+旗|.+海域|.+岛)?(.*)";

    /**
     * 解析地址字符串获取省、市、区、路段
     *
     * @param address 地址字符串
     * @return 解析数组，0为province，1为city，2为area，3为road
     */
    @Nullable
    public static String[] parseAddress(@NonNull String address) {

        Matcher m = Pattern.compile(regex).matcher(address);
        String province, city, area, road;

        if (m.find()) {
            String[] result = new String[4];
            province = m.group(1);
            result[0] = province == null ? "" : province.trim();
            city = m.group(2);
            result[1] = city == null ? "" : city.trim();

            /*处理没有省份的直辖市*/
            if ("".equals(result[0]) && contains(mMunicipality, result[1])) {
                result[0] = result[1];
            }

            area = m.group(3);
            result[2] = area == null ? "" : area.trim();
            road = m.group(4);
            result[3] = road == null ? "" : road.trim();

            //L.d(TAG, "provice:" + result[0] + ", city:" + result[1] + ", area:" + result[2] + ", road:" + result[3]);
            return result;
        }
        return null;
    }

    /**
     * 去除字符串中的空格、回车、换行符、制表符等
     * @param str
     * @return
     */
    public static String replaceSpecialStr(String str) {
        String repl = "";
        if (str != null) {
            String regEx="[`~!@#$%^&*()+=|{}':;',\\\\s*\\\\t\\\\r\\\\n\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？-]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            repl = m.replaceAll("").trim();
        }
        return repl;
    }

    /*****
     * 对象toString()
     * @param object
     * @return
     */
    public static String toString(Object object) {
        if (object == null) {
            return "null";
        }
        if (!object.getClass().isArray()) {
            return object.toString();
        }
        if (object instanceof boolean[]) {
            return Arrays.toString((boolean[]) object);
        }
        if (object instanceof byte[]) {
            return Arrays.toString((byte[]) object);
        }
        if (object instanceof char[]) {
            return Arrays.toString((char[]) object);
        }
        if (object instanceof short[]) {
            return Arrays.toString((short[]) object);
        }
        if (object instanceof int[]) {
            return Arrays.toString((int[]) object);
        }
        if (object instanceof long[]) {
            return Arrays.toString((long[]) object);
        }
        if (object instanceof float[]) {
            return Arrays.toString((float[]) object);
        }
        if (object instanceof double[]) {
            return Arrays.toString((double[]) object);
        }
        if (object instanceof Object[]) {
            return Arrays.deepToString((Object[]) object);
        }
        return "Couldn't find a correct type for the object";
    }

    /****
     * 比较两个字符串是否相等
     * @param a
     * @param b
     * @return
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        if (a != null && b != null) {
            int length = a.length();
            if (length == b.length()) {
                if (a instanceof String && b instanceof String) {
                    return a.equals(b);
                } else {
                    for (int i = 0; i < length; i++) {
                        if (a.charAt(i) != b.charAt(i)) return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Copied from "android.util.Log.getStackTraceString()" in order to avoid usage of Android stack
     * in unit tests.
     *
     * @return Stack trace in form of String
     */
    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    /****
     * 日志等级获取
     * @param value
     * @return
     */
    public static String logLevel(int value) {
        switch (value) {
            case Logger.VERBOSE:
                return "V";
            case Logger.DEBUG:
                return "D";
            case Logger.INFO:
                return "I";
            case Logger.WARN:
                return "W";
            case Logger.ERROR:
                return "E";
            case Logger.ASSERT:
                return "A";
            default:
                return "U";
        }
    }

    /**
     * 转换储存空间信息
     *
     * @param nSize
     *
     * @return
     */
    public static String byteToString(long nSize) {
        try {
            java.text.DecimalFormat df = new java.text.DecimalFormat();
            String strSize;
            if (nSize < 1024) {
                strSize = nSize + "B";
            } else if (nSize < 1048576) {
                // 1024*1024
                df.applyPattern("0");
                double d = (double) nSize / 1024;
                strSize = df.format(d) + "K";
            } else if (nSize < 1073741824) {
                // 1024*1024*1024
                df.applyPattern("0.0");
                double d = (double) nSize / 1048576;
                strSize = df.format(d) + "M";
            } else {
                // >1G
                df.applyPattern("0.0");
                double d = (double) nSize / 1073741824;
                strSize = df.format(d) + "G";
            }
            return strSize;
        } catch (Exception e) {
            return "0";
        }
    }

}
