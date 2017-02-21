package com.shuqu.microcredit.NetWork;

import android.content.Context;
import android.os.AsyncTask;
import com.shuqu.microcredit.Content.Urls;
import com.shuqu.microcredit.Model.DeviceParams;
import com.shuqu.microcredit.Model.UserInfo;
import java.util.HashMap;

/**
 * Class Info ：
 * Created by Lyndon on 16/9/6.
 */
public class StatisticsService {

    Context mContext;
    HashMap<String, String> mParams;

    public StatisticsService(Context context, HashMap<String, String> params){
        mContext = context;
        mParams = params;
        //拼接字段
        mParams.put("uid", UserInfo.getUid());
        params.put("channel", DeviceParams.channelID);
        try {
            new StatisticsAsyn().execute();
        }catch ( Exception e){
        }
    }

    //统计
    protected class StatisticsAsyn extends AsyncTask<Void, Void, String> {

        @Override protected String doInBackground(Void... voids) {
            return NetUtil.requestData(mContext, "POST", "", mParams, Urls.getUrl(Urls.METHOD_STATISTICS));
        }

        @Override protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
