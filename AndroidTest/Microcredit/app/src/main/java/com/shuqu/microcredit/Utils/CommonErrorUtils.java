package com.shuqu.microcredit.Utils;

import android.util.SparseArray;
import com.shuqu.microcredit.Content.Config;
import com.shuqu.microcredit.NetWork.BrNetConfigService;

/**
 * Class Info ：common error utils
 * Created by Lyndon on 16/6/4.
 */
public class CommonErrorUtils {

    public static final int SESSION_EMPTY = 11715;//session为空,请重新登录
    public static final int NO_USER_ID = 11759; //请求头没传用户ID
    public static final int NO_SESSION = 11760;//请求头没传SESSIONID
    public static final int NO_ACCESTOKEN = 11761;//请求头没传AccessToken
    public static final int CHECK_TOKEN_ERROR = 10201;//token校验失败
    public static final int FRESH_TOKEN_ERROR = 10202;//刷新token失败
    public static final int TOKEN_ERROR = 10203;//token已过期

    public static final int LOGIN_FAILURE = 10401; //账号或密码错误
    public static final int LOGIN_ALREADY = 11718; //在其他设备登录

    public static final int REGIST_GET_CODE_FAILURE = 10501; //验证码发送失败,请重新发送
    public static final int REGIST_PHONE_USED = 10101; //手机已被注册
    public static final int REGIST_GET_CODE_PHONE_OK = 10102; //手机还未注册

    public static final int REGIST_CODE_ERROR = 10502; //验证码输入有误
    public static final int REGIST_RENSHU_MAN = 11712; //对不起已达测试人数
    public static final int REGIST_PWD_NULL = 11711; //账号或密码为空
    public static final int REGIST_ERROR = 10001; //服务器内部错误
    public static final int FORGET_CODE_NULL = 11727; //验证码为空
    public static final int PARAMETER_ERROR = 10002; //参数错误
    public static final int TOKEN_CHECK_ERROR = 10201; //TOKEN校验失败
    public static final int TOKEN_REFRESH_ERROR = 10202; //刷新TOKEN失败
    public static final int GET_USER_FAILURE = 10301; //获取用户失败
    public static final int USER_NOT_EXIT = 10302; //用户不存在
    public static final int LOGOUT_ERROR = 10402; //退出失败


    public static final int LOGIN_FROZEN = 10403; //连续5次失败,冻结30min

    private static SparseArray<String> sErrorArray = new SparseArray<String>() {
        {
            put(LOGIN_FAILURE, "账号或密码错误");
            put(LOGIN_ALREADY, "您的账户已在其他设备登陆");
            put(REGIST_GET_CODE_FAILURE, "验证码发送失败,请重新发送");
            put(REGIST_PHONE_USED, "手机已被注册");
            put(REGIST_GET_CODE_PHONE_OK, "用户还没有注册");
            put(REGIST_CODE_ERROR, "验证码输入有误");
            put(REGIST_RENSHU_MAN, "对不起已达测试人数");
            put(REGIST_PWD_NULL, "账号或密码为空");
            put(REGIST_ERROR, "服务器内部错误");
            put(FORGET_CODE_NULL, "验证码为空");
            put(PARAMETER_ERROR, "参数错误");
            put(TOKEN_CHECK_ERROR, "TOKEN校验失败");
            put(TOKEN_REFRESH_ERROR, "刷新TOKEN失败");
            put(GET_USER_FAILURE, "获取用户失败");
            put(USER_NOT_EXIT, "用户不存在");
            put(LOGOUT_ERROR, "退出失败");
            put(SESSION_EMPTY, "账户异常,请重新登录");
            put(NO_USER_ID, "账户异常,请重新登录");
            put(NO_SESSION, "账户异常,请重新登录");
            put(NO_ACCESTOKEN, "账户异常,请重新登录");
            put(CHECK_TOKEN_ERROR, "账户异常,请重新登录");
            put(FRESH_TOKEN_ERROR, "账户异常,请重新登录");
            put(TOKEN_ERROR, "账户异常,请重新登录");
        }
    };

    public static String getErrorMessage(int errorCode) {
        return sErrorArray.get(errorCode) == null ? "未知错误" : sErrorArray.get(errorCode);
    }

    //判断是否账号状态异常
    public static boolean isAccountException(int errorCode) {
        if (errorCode == LOGIN_ALREADY ||
                errorCode == SESSION_EMPTY ||
                errorCode == NO_USER_ID ||
                errorCode == NO_SESSION ||
                errorCode == NO_ACCESTOKEN ||
                errorCode == CHECK_TOKEN_ERROR ||
                errorCode == FRESH_TOKEN_ERROR ) {
            return true;
        }
        return false;
    }

    //token 过期
    public static boolean isTokenExpired(int errorCode) {
        if (errorCode == TOKEN_ERROR) {
            return true;
        }
        return false;
    }

    //check host
    public static boolean isSupportHost(String url) {
        if(Config.IS_DEBUG) return true;
        if (BrNetConfigService.mBrNetConfig != null && BrNetConfigService.mBrNetConfig.getHosts() != null && !BrNetConfigService.mBrNetConfig.getHosts().isEmpty()) {
            int start = url.indexOf("//") + 2;
            int end = url.indexOf("/", start + 1);
            String host = url.substring(start, end);
            return BrNetConfigService.mBrNetConfig.getHosts().contains(host);
        }
        return false;
    }

    //error title
    public static boolean isErrorTitle(String title) {
        if(title != null && (title.contains("找不到") || title.contains("404"))){
            return true;
        }
        return false;
    }

}
