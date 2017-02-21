package com.shuqu.microcredit.Utils;

import android.support.v4.util.ArrayMap;
import com.shuqu.microcredit.R;

/**
 * Class Info ：
 * Created by Lyndon on 16/6/12.
 */
public class MenuIconMap {
    public static int ERROR_ICON_ID = -1;
    private static ArrayMap<String, Integer> mName2IdMap = new ArrayMap<>();
    static {
        mName2IdMap.put("menu_home", R.mipmap.ic_home);
        mName2IdMap.put("menu_edit", R.mipmap.ic_edit);
        mName2IdMap.put("menu_share", R.mipmap.ic_share);
        mName2IdMap.put("menu_scan", R.mipmap.ic_scan);
        mName2IdMap.put("tab_home_normal", R.mipmap.tab_home_normal);
        mName2IdMap.put("tab_home_active", R.mipmap.tab_home_active);
        mName2IdMap.put("tab_myorder_normal", R.mipmap.tab_myorder_normal);
        mName2IdMap.put("tab_myorder_active", R.mipmap.tab_myorder_active);
        mName2IdMap.put("tab_assistant_normal", R.mipmap.tab_assistant_normal);
        mName2IdMap.put("tab_assistant_active", R.mipmap.tab_assistant_active);
        mName2IdMap.put("tab_user_normal", R.mipmap.tab_user_normal);
        mName2IdMap.put("tab_user_active", R.mipmap.tab_user_active);
        mName2IdMap.put("item_my_borrow_money_active", R.mipmap.item_my_borrow_money_active);
        mName2IdMap.put("tab_my_info_active", R.mipmap.tab_my_info_active);
        mName2IdMap.put("tab_my_setting_active", R.mipmap.tab_my_setting_active);
        mName2IdMap.put("tab_problems_active", R.mipmap.tab_problems_active);
        mName2IdMap.put("tab_feedback_active", R.mipmap.tab_feedback_active);
        mName2IdMap.put("tab_about_us_active", R.mipmap.tab_about_us_active);
    }

    public static int getMenuIcon(String name){
        if(mName2IdMap.containsKey(name)){
            return mName2IdMap.get(name);
        }
        return ERROR_ICON_ID;
    }


}
