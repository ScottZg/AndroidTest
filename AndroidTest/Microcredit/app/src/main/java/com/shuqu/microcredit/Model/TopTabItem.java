package com.shuqu.microcredit.Model;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class Info ：
 * Created by Lyndon on 16/9/12.
 */
public class TopTabItem {
    public String mTitleName;
    public String mUrl;
    public boolean mFocus;

    public String mBorderColor;
    public boolean mRefresh;

    public String mDefaultFontColor;
    public String mDefaultBgColor;
    public String mFocusFontColor;
    public String mFocusBgColor;


    public static ArrayList<TopTabItem> parse(String datas){
        ArrayList<TopTabItem> tabItems = new ArrayList<>();
        try {
            JSONObject jsonDatas = new JSONObject(datas);
            JSONArray tabs = jsonDatas.optJSONArray("tabs");
            boolean refresh = jsonDatas.optBoolean("refresh");
            String borderColor = jsonDatas.optString("borderColor");
            String defaultFontColor = jsonDatas.optString("defaultFontColor");
            String defaultBgColor = jsonDatas.optString("defaultBgColor");
            String focusFontColor = jsonDatas.optString("focusFontColor");
            String focusBgColor = jsonDatas.optString("focusBgColor");
            if(tabs != null){
                for (int i = 0; i< tabs.length(); ++i){
                    JSONObject jsonObject = tabs.optJSONObject(i);
                    TopTabItem item = new TopTabItem();
                    item.mTitleName = jsonObject.optString("name");
                    item.mUrl = jsonObject.optString("url");
                    item.mFocus = jsonObject.optBoolean("focus");

                    item.mBorderColor = borderColor;
                    item.mDefaultBgColor = defaultBgColor;
                    item.mDefaultFontColor = defaultFontColor;
                    item.mFocusBgColor = focusBgColor;
                    item.mFocusFontColor = focusFontColor;
                    item.mRefresh = refresh;
                    tabItems.add(item);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tabItems;
    }
}
