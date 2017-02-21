package com.shuqu.microcredit.NetWork;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import com.shuqu.microcredit.Content.Config;
import com.shuqu.microcredit.Content.Urls;
import com.shuqu.microcredit.Model.CheckVersionResult;
import com.shuqu.microcredit.Model.DeviceInfo;
import com.shuqu.microcredit.Utils.StringUtil;
import org.json.JSONObject;

/**
 * Created by wuxin on 16/6/29.
 */
public class CheckVersionService {

    public static void checkVersion(Context context, OnCheckVersionListener onCheckVersionListener) {
        new ChechVersionAsyn(context, onCheckVersionListener).execute();
    }

    private static class ChechVersionAsyn extends AsyncTask<Void, Void, String> {

        private Context mContext;
        private OnCheckVersionListener mOnCheckVersionListener;

        public ChechVersionAsyn(Context context, OnCheckVersionListener onCheckVersionListener) {
            mContext = context;
            mOnCheckVersionListener = onCheckVersionListener;
        }

        @Override
        protected String doInBackground(Void... voids) {

            String checkVersionUrl = String.format(Urls.METHOS_CHECK_VERSION, Config.CHECK_VERSION_ID, Config.CHECK_VERSION_API_TOKEN);


            String result = NetUtil.requestData(mContext, "GET", null, null, false, checkVersionUrl);
            if(StringUtil.checkStr(result)) {
                try {
                    CheckVersionResult checkVersionResult = new CheckVersionResult();
                    JSONObject jsonObject = new JSONObject(result);
                    checkVersionResult.setName(jsonObject.optString("name"));
                    checkVersionResult.setBuild(jsonObject.optString("build"));
                    checkVersionResult.setVersionShort(jsonObject.optString("versionShort"));
                    checkVersionResult.setUpdate_url(jsonObject.optString("update_url"));

                    if(Integer.parseInt(checkVersionResult.getBuild()) > DeviceInfo.getInstance().getAppVersionCode()) {
                        return checkVersionResult.getUpdate_url();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(StringUtil.checkStr(result) && mOnCheckVersionListener != null) {
                mOnCheckVersionListener.onShouldUpdateVersion(result);
            }
        }
    }

    public static void showVersionDialog(final Context context, final String updateUrl) {
        if(StringUtil.checkStr(updateUrl)) {
            new AlertDialog.Builder(context)
                    .setTitle("温馨提示")
                    .setMessage("发现新版本")
                    .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
                            context.startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        }
    }

    public interface OnCheckVersionListener{
        void onShouldUpdateVersion(String updateUrl);
    }
}
