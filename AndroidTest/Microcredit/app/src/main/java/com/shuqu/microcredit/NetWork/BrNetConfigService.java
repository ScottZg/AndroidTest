package com.shuqu.microcredit.NetWork;

import android.content.Context;
import android.os.AsyncTask;
import com.shuqu.microcredit.Content.Config;
import com.shuqu.microcredit.Content.ParamKeys;
import com.shuqu.microcredit.Content.Urls;
import com.shuqu.microcredit.Model.BrNetConfig;
import com.shuqu.microcredit.Utils.FileUtils;
import com.shuqu.microcredit.Utils.PrefsUtils;
import com.shuqu.microcredit.Utils.StringUtil;
import com.shuqu.microcredit.Utils.TimeUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wuxin on 16/6/21.
 */
public class BrNetConfigService {

    public static BrNetConfig mBrNetConfig;

    public static void getNetConfig(Context context) {
        new NetConfigAsyn(context).execute();
    }

    public static void getNetConfig(Context context, OnNetConfigListener onNetConfigListener) {

        new NetConfigAsyn(context, onNetConfigListener).execute();
    }

    /**
     * 检查是否需要向服务器获取白名单
     * 1、文件不存在,直接请求
     * 2、存在,判断时间
     * @return
     */
    public static boolean checkShouldNetSupportHost(Context context){

        if(!FileUtils.isFileExists(context.getFilesDir().getAbsolutePath() + Config.SUPPORT_HOST_FILE_NAME)) {
            return true;
        }

        Long lastUpdateTime = PrefsUtils.getLong(context, ParamKeys.LAST_NET_SUPPORT_HOST_TIME);
        return !TimeUtils.isToday(lastUpdateTime);
    }

    private static class NetConfigAsyn extends AsyncTask<Void, Void, Void> {

        Context mContext;
        OnNetConfigListener mOnNetConfigListener;

        public NetConfigAsyn(Context context, OnNetConfigListener onNetConfigListener){
            mContext = context;
            mOnNetConfigListener = onNetConfigListener;
        }

        public NetConfigAsyn(Context context) {
            mContext = context;
        }

        @Override
        protected Void doInBackground(Void... params) {

            Map<String, String> hostsParams = new HashMap<>();
            hostsParams.put("appId", ParamKeys.APPID);

            String mNetData;

            mNetData = NetUtil.requestData(mContext, "POST", "", hostsParams, false, Urls.getUrl(Urls.METHOD_GET_HOSTS));


            try {
                if (StringUtil.checkStr(mNetData)) {

                    JSONObject jsonObject = new JSONObject(mNetData);
                    int code = jsonObject.optInt("code", Integer.MAX_VALUE);
                    if(code == 0) {
                        PrefsUtils.putValue(mContext, ParamKeys.LAST_NET_SUPPORT_HOST_TIME, TimeUtils.getCurrentTimeInLong());
                        JSONArray hosts = jsonObject.optJSONArray("hosts");
                        if(hosts.length() > 0) {
                            if(mBrNetConfig == null)
                                mBrNetConfig = new BrNetConfig();
                            List<String> hostList = new ArrayList<>();
                            for (int i = 0; i < hosts.length(); i++) {
                                hostList.add(hosts.getString(i));
                            }
                            mBrNetConfig.setHosts(hostList);

                            FileUtils.writeObjToFile(mContext.getFilesDir().getAbsolutePath() + Config.SUPPORT_HOST_FILE_NAME, mBrNetConfig);
                        }
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(mOnNetConfigListener != null) {
                mOnNetConfigListener.onNetConfigSucess(mBrNetConfig);
            }

        }
    }

    public interface OnNetConfigListener {
        void onNetConfigSucess(BrNetConfig brNetConfig);
    }
}
