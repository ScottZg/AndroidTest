package com.shuqu.microcredit.Model;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class Info ：menu item
 * Created by Lyndon on 16/6/2.
 */
public class MenuItem {
    //menu icon
    public String mIcon;
    // menu title
    public String mTitle;
    //event id
    public String mEventId;

    //parse menu datas
    public static ArrayList<MenuItem> parse(String datas){
        ArrayList<MenuItem> menuDatas = new ArrayList<MenuItem>();
        try {
            JSONArray json = new JSONArray(datas);
            if(json != null){
                JSONObject jsonObject = null;
                for (int i = 0; i < json.length(); ++i) {
                    jsonObject = json.optJSONObject(i);
                    if(jsonObject != null){
                        MenuItem item = new MenuItem();
                        item.mIcon = jsonObject.optString("icon");
                        item.mTitle = jsonObject.optString("title");
                        item.mEventId = jsonObject.optString("eventId");
                        menuDatas.add(item);
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return menuDatas;
    }
}
