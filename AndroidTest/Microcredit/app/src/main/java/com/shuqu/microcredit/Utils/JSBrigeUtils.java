package com.shuqu.microcredit.Utils;

/**
 * Created by wuxin on 16/6/7.
 */
public class JSBrigeUtils {

    public static String getJSSucessUrl(String mCurrentMethodId, Object json){
        return "javascript:" + "BrBridge.onSuccess" + "(" +
                mCurrentMethodId + ","+ json +")";
    }

    public static String getJSFailurUrl(String mCurrentMethodId, Object json){
        return "javascript:" + "BrBridge.onFailure" + "(" +
                mCurrentMethodId + ","+ json +")";
    }

    public static String getJSNativeSucessUrl(String mCurrentMethodId, Object
            json){
        return "javascript:" + "BrBridge.onSuccess" + "(" +
                mCurrentMethodId + ","+ json +")";
    }

    public static String getJSNativeFailurUrl(String mCurrentMethodId, Object
            json){
        return "javascript:" + "BrBridge.onFailure" + "(" +
                mCurrentMethodId + ","+ json +")";
    }
}
