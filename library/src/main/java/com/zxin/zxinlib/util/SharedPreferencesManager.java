package com.zxin.zxinlib.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zxin.zxinlib.bean.MenuEntity;
import com.zxin.zxinlib.bean.User;
import java.util.List;

/****
 * 共享文件工具
 */
public class SharedPreferencesManager extends SharePrefUtils {
    
    private static volatile SharedPreferencesManager manager = null;

    private SharedPreferencesManager(Context mContext) {
        super(mContext);
    }

    public static SharedPreferencesManager getInstance(Context mContext){
        if (manager == null)
            synchronized (SharedPreferencesManager.class) {
                if (manager == null) {
                    manager = new SharedPreferencesManager(mContext);
                }
            }
        return manager;
    }

    /****
     * 保存用户信息
     * @param user
     */
    public void setUserInfo(User user) {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.UserInfo_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        Gson gson = new Gson();
        editor.putString("user", gson.toJson(user));
        editor.commit();
    }

    /****
     * 获取用户信息
     * @return
     */
    public User getUserInfo() {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.UserInfo_Datas, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        try {
            User user = gson.fromJson(sp.getString("user", ""), User.class);
            return user;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    /****
     * 保存用户信息
     * @param data
     */
    public void setPersonaInfo(String data) {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.UserInfo_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("Persona", data);
        editor.commit();
    }

    /*****
     * 保存是否第一次使用
     * @param isEnter
     */
    public void setIsFirstEnter(boolean isEnter) {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.System_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean("isEnter", isEnter);
        editor.commit();
    }

    /****
     * 获取是否第一次装APP
     * @return
     */
    public boolean getIsFirstEnter() {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.System_Datas, Context.MODE_PRIVATE);
        return sp.getBoolean("isEnter", true);
    }

    public void setIsMarryEnter(boolean isEnter) {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.System_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean("isMarryEnter", isEnter);
        editor.commit();
    }

    public boolean getIsMarryFirstEnter() {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.System_Datas, Context.MODE_PRIVATE);
        return sp.getBoolean("isMarryEnter", true);
    }

    public String getDeviceId() {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.System_Datas, Context.MODE_PRIVATE);
        return sp.getString("device_id", "");
    }


    public void setDeviceId(String param) {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.System_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("device_id", param);
        editor.commit();
    }

    public void setMarryVersionName(String versionName) {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Marry_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("VERSION_NAME", versionName);
        editor.commit();
    }

    public String getMarryVersionName() {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Marry_Datas, Context.MODE_PRIVATE);
        return sp.getString("VERSION_NAME","1.0.1");
    }

    public void setMarryUserName(String versionName) {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Marry_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("UserName", versionName);
        editor.commit();
    }

    public String getMarryUsernName() {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Marry_Datas, Context.MODE_PRIVATE);
        return sp.getString("UserName","");
    }

    public void setMarryPassword(String password) {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Marry_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("Password", password);
        editor.commit();
    }

    public String getMarryPassword() {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Marry_Datas, Context.MODE_PRIVATE);
        return sp.getString("Password","");
    }

    public void setMarryUid(String uid) {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Marry_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("uid", uid);
        editor.commit();
    }

    public String getMarryUid() {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Marry_Datas, Context.MODE_PRIVATE);
        return sp.getString("uid","");
    }

    /****
     * 保存用户信息
     * @param user
     */
    public <T> void setMarryUserInfo(T user) {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Marry_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        Gson gson = new Gson();
        editor.putString("UserInfo", gson.toJson(user));
        editor.commit();
    }

    /****
     * 获取用户信息
     * @return
     */
    public <T> T getMarryUserInfo(Class<T> clazz) {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Marry_Datas, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        try {
            return gson.fromJson(sp.getString("UserInfo", ""), clazz);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> void setInfoEntity(T user) {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Marry_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        Gson gson = new Gson();
        editor.putString("InfoEntity", gson.toJson(user));
        editor.commit();
    }

    public <T> T getInfoEntity(Class<T> clazz) {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Marry_Datas, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        try {
            return gson.fromJson(sp.getString("InfoEntity", ""), clazz);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setEcshop(String ecshop) {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Marry_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("ecshop", ecshop);
        editor.commit();
    }

    public String getEcshop(Context paramContext) {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Marry_Datas, Context.MODE_PRIVATE);
        return sp.getString("ecshop","");
    }

    public void setIsLogins(boolean islogin) {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Marry_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean("islogin", islogin);
        editor.commit();
    }

    public boolean getIsLogin() {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Marry_Datas, Context.MODE_PRIVATE);
        return sp.getBoolean("islogin",false);
    }

    public void setIsGif(boolean isGif) {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Marry_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean("isGif", isGif);
        editor.commit();
    }

    public boolean getIsGif() {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Marry_Datas, Context.MODE_PRIVATE);
        return sp.getBoolean("isGif",false);
    }

    public void setShopCart(String shopCart) {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Marry_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("shopCart", shopCart);
        editor.commit();
    }

    public String getShopCart() {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Marry_Datas, Context.MODE_PRIVATE);
        return sp.getString("shopCart","");
    }

    public void setMarrayDate(String marrayDate) {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Marry_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("marrayDate", marrayDate);
        editor.commit();
    }

    public String getMarrayDate() {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Marry_Datas, Context.MODE_PRIVATE);
        return sp.getString("marrayDate","");
    }

    public <T> void setMarryCity(T user) {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Marry_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        Gson gson = new Gson();
        editor.putString("City", gson.toJson(user));
        editor.commit();
    }

    /****
     * 获取用户信息
     * @return
     */
    public <T> T getMarryCity(Class<T> clazz) {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Marry_Datas, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        try {
            if (!BaseStringUtils.textIsEmpty(sp.getString("City", "")))
            return gson.fromJson(sp.getString("City", ""), clazz);
            return null;
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public void clearMarryDatas() {
        Editor UserInfo_Datas = mContext.getSharedPreferences(BaseStringUtils.Marry_Datas, Context.MODE_PRIVATE).edit();
        UserInfo_Datas.clear();
        UserInfo_Datas.commit();
    }

    /*****
     * 获取地址信息
     * @return
     */
    public String getCityId() {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Share_Datas, Context.MODE_PRIVATE);
        return sp.getString("cityCode", "3101");
    }

    /****
     * 保存地址信息
     * @param cityCode
     */
    public void saveCityId(String cityCode) {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Share_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("cityCode", cityCode.substring(0, 4));
        editor.commit();
    }

    /****
     * 消息通知角标数
     * @return
     */
    public int getBadgeCount() {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.BadgeCount, Context.MODE_PRIVATE);
        return sp.getInt("BadgeCount_parent", 0);
    }

    /****
     * 消息通知角标数
     */
    public void setBadgeCount(int BadgeCount) {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.BadgeCount, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt("BadgeCount_parent", BadgeCount);
        editor.commit();
    }

    /*****
     * 获取手机号码
     */
    public String getPhoneNum() {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.System_Datas, Context.MODE_PRIVATE);
        return sp.getString(BaseStringUtils.Share_Phone, "");
    }


    /*****
     * 保存手机号码
     * @param phoneNum
     */
    public void savePhoneNum(String phoneNum) {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.System_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(BaseStringUtils.Share_Phone, phoneNum);
        editor.commit();
    }

    /****
     * 保存区县
     * @param subject
     */
    public void saveArea(String subject) {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Share_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(BaseStringUtils.Share_Area, subject);
        editor.commit();
    }


    /****
     * 保存城市
     * @param subject
     */
    public void saveCity(String subject) {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Share_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(BaseStringUtils.Share_City, subject);
        editor.commit();
    }

    /****
     * 清空用户本地缓存信息
     */
    public void clearDatas() {
        Editor UserInfo_Datas = mContext.getSharedPreferences(BaseStringUtils.UserInfo_Datas, Context.MODE_PRIVATE).edit();
        UserInfo_Datas.clear();
        UserInfo_Datas.commit();
    }

    public List<MenuEntity> getMineMenuList() {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Share_Datas, Context.MODE_PRIVATE);
        String subject = sp.getString(BaseStringUtils.Share_MineMenu, "");
        List<MenuEntity> menuList = TextUtils.isEmpty(subject) ? null : (List<MenuEntity>) new Gson().fromJson(subject, new TypeToken<List<MenuEntity>>() {
        }.getType());

        if (menuList==null||menuList.isEmpty()){
            menuList = FileUtil.getInstance(mContext).getAllMenuList();
            saveMineMenu(menuList);
        }
        return menuList;
    }

    public void saveMineMenu(List<MenuEntity> menuList) {
        SharedPreferences sp = mContext.getSharedPreferences(BaseStringUtils.Share_Datas, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        Gson gson = new Gson();
        editor.putString(BaseStringUtils.Share_MineMenu, gson.toJson(menuList));
        editor.commit();
    }

    public String getAccessToken() {
        SharedPreferences sp = getSharedBeiKePreferences();
        return sp.getString(BaseStringUtils.access_token, "");
    }

    public void setAccessToken(String access_token) {
        SharedPreferences.Editor editor = getSharedBeiKePreferences().edit();
        editor.putString(BaseStringUtils.access_token, access_token);
        editor.commit();
    }

    public SharedPreferences getSharedBeiKePreferences() {
        return mContext.getSharedPreferences(BaseStringUtils.Share_BeiKe_Datas, Context.MODE_PRIVATE);
    }

}
