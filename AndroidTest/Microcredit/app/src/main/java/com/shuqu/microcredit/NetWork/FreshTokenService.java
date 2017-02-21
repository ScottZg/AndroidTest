package com.shuqu.microcredit.NetWork;

import android.content.Context;
import android.os.AsyncTask;

import com.shuqu.microcredit.Content.ParamKeys;
import com.shuqu.microcredit.Content.Urls;
import com.shuqu.microcredit.Model.UserInfo;
import com.shuqu.microcredit.Utils.PrefsUtils;
import com.shuqu.microcredit.Utils.StringUtil;
import com.shuqu.microcredit.Utils.TimeUtils;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Class Info ：fresh token
 * Created by Lyndon on 16/6/8.
 */
public class FreshTokenService {
    public static final String ACCESS = "ACCESS";
    public static final String REFRESH = "REFRESH";

    private static onFreshToken mListener;
    public static void freshToken(Context context, onFreshToken listener){
        try {
            mListener = listener;
            new AsyFreshToken(context).execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void freshToken(Context context, String type, onFreshToken listener){
        try {
            mListener = listener;
            new AsyFreshToken(context, type).execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    static class AsyFreshToken extends AsyncTask<Void, Void, Void> {

        Context mContext;
        String mType = ACCESS;
        public AsyFreshToken(Context context){
            mContext = context;
        }

        public AsyFreshToken(Context context, String type){
            mContext = context;
            mType = type;
        }

        @Override protected Void doInBackground(Void... params) {
            String data = "";
            try {
                HashMap<String, String> paramsMap = new HashMap<>();
                paramsMap.put("appId", ParamKeys.APPID);
                paramsMap.put("refreshToken", UserInfo.getRefresh_token());
                paramsMap.put("type", mType);
                paramsMap.put("uid", UserInfo.getUid());
                data = NetUtil.requestData(mContext, "POST", "", paramsMap, false, Urls.getUrl(Urls.FRESH_TOKEN));
                if(StringUtil.checkStr(data)) {
                    JSONObject jsonData = new JSONObject(data);
                    if (jsonData.optInt("code") == 0) {
                        if (REFRESH.equals(mType)) {
                            UserInfo.setRefresh_token(jsonData.optString("token"));
                            PrefsUtils.putValue(mContext, Header.BRFRESHTOKEN,
                                    UserInfo.getRefresh_token());
                            PrefsUtils.putValue(mContext, ParamKeys.TOKEN_FRESH_TIME,
                                    TimeUtils.getCurrentDataTimeFull());
                        }
                        else {
                            UserInfo.setAccess_token(jsonData.optString("token"));
                        }
                    }
                    else {
                        PrefsUtils.putValue(mContext, Header.BRFRESHTOKEN, "");
                        PrefsUtils.putValue(mContext, ParamKeys.TOKEN_FRESH_TIME, "");
                    }
                }else{
                    PrefsUtils.putValue(mContext, Header.BRFRESHTOKEN, "");
                    PrefsUtils.putValue(mContext, ParamKeys.TOKEN_FRESH_TIME, "");
                }
            } catch (Exception e) {
                UserInfo.setAccess_token("");
                PrefsUtils.putValue(mContext, Header.BRFRESHTOKEN, "");
                PrefsUtils.putValue(mContext, ParamKeys.TOKEN_FRESH_TIME, "");
                e.printStackTrace();
            }
            return null;
        }


        @Override protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(mListener != null){
                mListener.onTokenFreshed();
            }
        }
    }

    public interface onFreshToken{
        void onTokenFreshed();
    }
}
