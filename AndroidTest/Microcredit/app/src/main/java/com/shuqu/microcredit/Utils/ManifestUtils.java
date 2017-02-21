package com.shuqu.microcredit.Utils;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * Class Info ：
 * Created by Lyndon on 16/6/8.
 */
public class ManifestUtils {
    private static final String TAG = ManifestUtils.class.getSimpleName();
    private static final boolean DEBUG = false;


    public static int getAppVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
        }
        return info.versionCode;
    }

    public static String getAppVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
        }
        return info.versionName;
    }

    /**
     * 获取<meta-data>的值
     * @param context
     * @param metaKey
     * @return
     */
    public static String getMetaValue(Context context, Class<?> cls, String metaKey) {
        Bundle metaData = null;
        String value = null;
        if (context == null || TextUtils.isEmpty(metaKey)) {
            return null;
        }
        try {
            PackageItemInfo pii = null;
            if (Application.class.isAssignableFrom(cls)) {
                pii = context.getPackageManager()
                             .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            } else if (Activity.class.isAssignableFrom(cls)) {
                pii = context.getPackageManager()
                             .getActivityInfo(new ComponentName(context, cls), PackageManager.GET_META_DATA);
            } else if (Service.class.isAssignableFrom(cls)) {
                pii = context.getPackageManager()
                             .getServiceInfo(new ComponentName(context, cls), PackageManager.GET_META_DATA);
            } else if (BroadcastReceiver.class.isAssignableFrom(cls)) {
                pii = context.getPackageManager()
                             .getReceiverInfo(new ComponentName(context, cls), PackageManager.GET_META_DATA);
            }
            if (pii != null) {
                metaData = pii.metaData;
            }
            if (metaData != null) {
                value = String.valueOf(metaData.get(metaKey));
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        return value;
    }


    private ManifestUtils() {/*Do not new me*/}
}
