package com.shuqu.microcredit.Admin;

import android.content.Context;
import com.shuqu.microcredit.NetWork.StatisticsService;
import java.util.HashMap;

/**
 * Class Info ：统计埋点工具
 * Created by Lyndon on 16/7/4.
 */
public class StatisticsMgr {

    public static void init(Context context){
//        MobclickAgent.setDebugMode(Config.IS_DEBUG);
    }

    public static void onPageStart(String pageName) {
//        MobclickAgent.onPageStart(pageName);
    }

    public static void onPageEnd(String pageName) {
//        MobclickAgent.onPageEnd(pageName);
    }

    public static void setDebugMode(boolean debugMode) {
//        MobclickAgent.setDebugMode(debugMode);
    }

    public static void onPause(Context context) {
//        MobclickAgent.onPause(context);
    }

    public static void onResume(Context context) {
//        MobclickAgent.onResume(context);
    }

    public static void reportError(Context context, String errorInfo) {
//        MobclickAgent.reportError(context, errorInfo);
    }

    public static void reportError(Context context, Throwable errorInfo) {
//        MobclickAgent.reportError(context, errorInfo);
    }


    public static void onEvent(Context context, String eventId) {
//        MobclickAgent.onEvent(context, eventId);
    }

    public static void onEvent(Context context, String eventId, String eventName) {
//        MobclickAgent.onEvent(context, eventId, eventName);
    }

    public static void onEvent(Context context, String eventId, HashMap<String, String> datas) {
//        MobclickAgent.onEvent(context, eventId, datas);
    }

    public static void onEvent(Context context, HashMap<String, String> datas) {
        try {
            new StatisticsService(context, datas);
        }catch (Exception e){
            //
        }
    }

}
