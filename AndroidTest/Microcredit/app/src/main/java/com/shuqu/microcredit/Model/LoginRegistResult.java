package com.shuqu.microcredit.Model;

/**
 * Created by wuxin on 16/6/7.
 */
public class LoginRegistResult {

    private String access_token;
    private int code = Integer.MIN_VALUE;
    private String message;
    private String refresh_token;
    private String uid;

    private String userPhone;
    private String userPwd;


    public String getUserPhone() {
        return userPhone;
    }


    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }


    public String getUserPwd() {
        return userPwd;
    }


    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
