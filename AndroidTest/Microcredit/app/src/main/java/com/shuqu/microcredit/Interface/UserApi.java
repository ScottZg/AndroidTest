package com.shuqu.microcredit.Interface;

import com.shuqu.microcredit.Model.UserInfo;
import com.shuqu.microcredit.Utils.StringUtil;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by wuxin on 16/9/20.
 */

public class UserApi {

    private static LinkedList<IUserLoginCallback> mUserLoginCallbacks = new LinkedList<>();

    //判断是否已登录
    public static boolean userLogined(){
//        if(Config.IS_DEBUG) return true;
        if(StringUtil.checkStr(UserInfo.getAccess_token()) &&
                StringUtil.checkStr(UserInfo.getUid())){
            return true;
        }
        return false;
    }

    public static void registLoginCallback(IUserLoginCallback callback) {
        mUserLoginCallbacks.addLast(callback);
        if(userLogined()) {
            callback.onUserLogin();
        } else {
            callback.onUserLogout();
        }
    }

    public static void unRegistLoginCallback(IUserLoginCallback callback) {
        mUserLoginCallbacks.remove(callback);
    }

    public static void notifyUserLogin() {
        Iterator<IUserLoginCallback> iterator = mUserLoginCallbacks.iterator();
        while (iterator.hasNext()) {
            IUserLoginCallback callback = iterator.next();
            callback.onUserLogin();
        }
    }
    public static void notifyUserLogout() {
        Iterator<IUserLoginCallback> iterator = mUserLoginCallbacks.iterator();
        while (iterator.hasNext()) {
            IUserLoginCallback callback = iterator.next();
            callback.onUserLogout();
        }
    }
}
