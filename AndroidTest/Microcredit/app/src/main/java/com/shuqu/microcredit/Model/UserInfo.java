package com.shuqu.microcredit.Model;

import com.shuqu.microcredit.Interface.UserApi;
import com.shuqu.microcredit.Utils.StringUtil;

/**
 * Created by wuxin on 16/6/7.
 */
public class UserInfo {

    public static String access_token = "";
    public static String refresh_token;
    public static String uid;
    public static String mSession;
    public static String userPhone;
    public static String mCookie;

    public static String getCookie() {
        return mCookie == null? "" : mCookie;
    }

    public static void setCookie(String mCookie) {
        if(StringUtil.checkStr(mCookie)) {
            UserInfo.mCookie = mCookie;
        }
    }


    public static String getUserPhone() {
        return userPhone;
    }

    public static void setUserPhone(String userPhone) {
        UserInfo.userPhone = userPhone;
    }

    public static String getRefresh_token() {
        return refresh_token == null? "" : refresh_token;
    }

    public static void setRefresh_token(String token) {
        refresh_token = token;
    }

    public static String getAccess_token() {
        return access_token == null? "" : access_token;
    }

    public static void setAccess_token(String token) {
        synchronized (UserInfo.class) {
            access_token = token;
            if(StringUtil.checkStr(token)) {
                UserApi.notifyUserLogin();
            } else {
                UserApi.notifyUserLogout();
            }
        }
    }

    public static String getUid() {
        return uid == null? "" : uid;
    }

    public static void setUid(String id) {
        uid = id;
    }

    public static String getSession(){
        return mSession == null? "" : mSession;
    }


    public static void setSession(String session) {
        if(StringUtil.checkStr(session)) {
            UserInfo.mSession = session;
        }
    }
}
