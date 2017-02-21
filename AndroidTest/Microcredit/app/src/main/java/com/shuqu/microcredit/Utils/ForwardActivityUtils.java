package com.shuqu.microcredit.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.shuqu.microcredit.UI.LoginActivity;

/**
 * Class Info ：
 * Created by Lyndon on 16/6/6.
 */
public class ForwardActivityUtils {

    /**
     * @param context
     * @param clazz forward class
     */
    public static void forwardActivity(Context context, Class clazz){
        forwardActivity(context, clazz, null, false);
    }

    /**
     * @param context
     * @param clazz forward class
     * @param finishedCurrent finish current activity or not
     */
    public static void forwardActivity(Context context, Class clazz, boolean finishedCurrent){
        forwardActivity(context, clazz, null, finishedCurrent);
    }


    /**
     *
     * @param context
     * @param clazz target class
     * @param data data for target class
     * @param finishedCurrent finished current activity or not
     */
    public static void forwardActivity(Context context, Class clazz, Bundle
            data, boolean finishedCurrent){
        Intent intent = new Intent(context, clazz);
        if (null != data) {
            intent.putExtras(data);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        if (finishedCurrent && context instanceof Activity) {
            ((Activity) context).finish();
        }
    }

    /**
     *
     * @param context
     * @param finishedCurrent finished current activity or not
     */
    public static void forwardLogin(Context context, boolean finishedCurrent){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        if (finishedCurrent && context instanceof Activity) {
            ((Activity) context).finish();
        }
    }
}
