package com.shuqu.microcredit.guider;

import android.content.Context;
import android.content.SharedPreferences;
import com.shuqu.microcredit.Model.DeviceInfo;

public class GuideUtil {

    /**
     * 首先获取本地有无版本记录，如果有那么跟程序对比，如果一样那么不显示，如果不一样那么显示
     *
     * @return
     */
    public static boolean isShouldOpenNaviForCurVersionCode(Context context) {
        int curVersion = DeviceInfo.getInstance().getAppVersionCode();
        SharedPreferences sp = context.getSharedPreferences("local_version", Context.MODE_PRIVATE);

        int localVersion = sp.getInt("local_version", -1);
        if (localVersion == -1) {
            return true;
        } else if (localVersion == curVersion) {
            return false;
        } else {
            return true;
        }

    }

    public static void setShouldOpenNaviFalse(Context context) {
        int curVersion = DeviceInfo.getInstance().getAppVersionCode();
        SharedPreferences sp = context.getSharedPreferences("local_version", Context.MODE_PRIVATE);
        sp.edit().putInt("local_version", curVersion).commit();

    }
}
