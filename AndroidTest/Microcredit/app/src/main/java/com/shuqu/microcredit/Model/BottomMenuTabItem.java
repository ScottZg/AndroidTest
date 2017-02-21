package com.shuqu.microcredit.Model;

import org.json.JSONObject;

/**
 * Class Info ：
 * Created by Lyndon 16/8/3.
 */
public class BottomMenuTabItem {
    public int index;
    //menu icon
    public String mIconActive;
    public String mIconDefault;

    public String mIconSource;

    // menu title
    public String mTitle;

    public String mTitleColorActive;
    public String mTitleColorDefault;

    public String mForwardUrl;
    public boolean mNeedLogin;

    public static BottomMenuTabItem parse(JSONObject data){
        if(data != null){
            BottomMenuTabItem item = new BottomMenuTabItem();
            item.mIconSource = data.optString("iconSource");
            item.mIconActive = data.optString("iconActive", "");
            item.mIconDefault = data.optString("iconDefault", "");
            item.mTitle = data.optString("title");
            item.mTitleColorActive = data.optString("titleActiveColor");
            item.mTitleColorDefault = data.optString("titleDefaultColor");
            item.mForwardUrl = data.optString("url", "");
            item.mNeedLogin = data.optBoolean("needLogin");
            return item;
        }
        return null;
    }
}
