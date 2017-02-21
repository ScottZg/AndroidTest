package com.shuqu.microcredit.Push;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.shuqu.microcredit.Content.ParamKeys;
import com.shuqu.microcredit.UI.MainActivity;
import org.json.JSONException;
import org.json.JSONObject;

public class PushUtils {
    private static final String TAG = "PushUtils";

    public static final int MAINACTIVITY = 1; // 主页面

    public static void onReceiveRegistrationId(Context context, String regId) {
        Log.d(TAG, "接收Registration Id : " + regId);
    }


    /**
     * 收到透传消息
     */
    public static void onMessageReceived(Context context, String msg) {
        Intent i = new Intent(context, CustomerPushService.class);
        i.putExtra(ParamKeys.PUSH_MESSAGE, msg);
        context.startService(i);
    }


    /**
     * 收到通知消息
     */
    public static void onNotificationReceived(Context context, String msg) {
        Intent i = new Intent(context, CustomerPushService.class);
        i.putExtra(ParamKeys.PUSH_MESSAGE, msg);
        context.startService(i);
    }


    public static void onNotificationClicked(final Context context, final Intent intent) {
        Log.d(TAG, "用户点击打开了通知");
        startActivity(context, intent);
    }


    public static void startActivity(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        bundle = intent.getExtras();
        int type = bundle.getInt(ParamKeys.PUSH_TYPE);
        String push_id = bundle.getString(ParamKeys.PUSH_ID);
        switch (type) {
            case MAINACTIVITY:
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setClass(context, MainActivity.class);

                break;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public static Bundle parseData(String data) {
        Bundle bundle = new Bundle();
        JSONObject jsonObj;
        try {
            jsonObj = new JSONObject(data);
            String id = jsonObj.optString("id");
            String uid = jsonObj.optString("uid");
            String title = jsonObj.optString("title");
            String msg = jsonObj.optString("message");
            int type = jsonObj.optInt("type");
            String param = jsonObj.optString("param");

            bundle.putInt(ParamKeys.PUSH_TYPE, type);
            bundle.putString(ParamKeys.PUSH_ID, id);
            bundle.putString(ParamKeys.PUSH_UID, uid);
            bundle.putString(ParamKeys.PUSH_TITLE, title);
            bundle.putString(ParamKeys.PUSH_MESSAGE, msg);
            bundle.putString(ParamKeys.PUSH_PARAM, param);
            bundle.putBoolean(ParamKeys.FROM_PUSH, true);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bundle;
    }


    public static void onReceiveRichpushCallback(Context context, String msg) {
        Log.d(TAG, "用户收到到RICH PUSH CALLBACK: " + msg);
    }
}
