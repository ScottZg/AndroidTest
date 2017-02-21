package com.shuqu.microcredit.Model;

import com.shuqu.microcredit.Content.Urls;

import org.json.JSONObject;

/**
 * Created by wuxin on 16/9/11.
 */
public class MainDrawerOptionItem {
    public String mTitle;
    public String mTitleColorActive;
    public String mTitleColorDefault;

    public String mIconActive;
    public String mIconDefault;

    public String mIconSource;

    public String mForwardUrl;
    public boolean mNeedLogin;

    public static MainDrawerOptionItem parse(JSONObject data){
        if(data != null){
            MainDrawerOptionItem item = new MainDrawerOptionItem();
            item.mIconSource = data.optString("iconSource");
            item.mIconActive = data.optString("iconActive", "");
            item.mIconDefault = data.optString("iconDefault", "");
            item.mTitle = data.optString("title");
            item.mTitleColorActive = data.optString("titleActiveColor");
            item.mTitleColorDefault = data.optString("titleDefaultColor");
            item.mForwardUrl = data.optString("url", "");
            item.mNeedLogin = data.optBoolean("needLogin");
            if(!item.mForwardUrl.contains("http")) {
                item.mForwardUrl = Urls.DAILY_WEB_URL + item.mForwardUrl;
            }
            return item;
        }
        return null;
    }
}
