package com.shuqu.microcredit.UI.fragment;

import android.os.AsyncTask;
import android.view.View;
import com.lyndon.jsbridge.Interface.IBrCommonOption;
import com.lyndon.jsbridge.Interface.IBrNativeOption;
import com.lyndon.jsbridge.Interface.IBrSysOption;
import com.lyndon.jsbridge.Interface.IFreshOption;
import com.shuqu.microcredit.Interface.IBrMenuOption;
import com.shuqu.microcredit.Interface.INetCallBack;
import com.shuqu.microcredit.Model.DeviceParams;
import com.shuqu.microcredit.NetWork.NetUtil;
import com.shuqu.microcredit.R;
import com.shuqu.microcredit.Utils.StringUtil;
import com.shuqu.microcredit.widget.CustomWebView;
import com.shuqu.microcredit.widget.LoadingView;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wuxin on 16/9/11.
 */
public abstract class BaseWebFragment extends BaseFragment implements IBrSysOption,
        IBrCommonOption, IBrNativeOption,
        IBrMenuOption,
        IFreshOption {
    protected CustomWebView mWebView;

    protected void initView() {
        mRootView.findViewById(R.id.tv_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlerScan(mParentActivity);
            }
        });
        mLoadingView = (LoadingView) mRootView.findViewById(R.id.lv_loading);
    }

    protected void requestData(String params, INetCallBack call) {
        try {
            new AsyRequestData(params, call).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class AsyRequestData extends AsyncTask<Void, Void, String> {

        String mParams;
        INetCallBack mCallBack;

        public AsyRequestData(String params, INetCallBack callBack) {
            mParams = params;
            mCallBack = callBack;
            try {
                JSONObject data = new JSONObject(mParams);
                if(data.optBoolean("showLoading", false)){
                    showLoadingDialog("");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... params) {

            String method = "";
            String url = "";
            Map param = null;
            try {
                JSONObject data = new JSONObject(mParams);
                if (data != null) {
                    method = data.optString("type").toUpperCase();
                    url = data.optString("url");
                    if(!StringUtil.checkStr(method)){
                        method = "GET";
                    }
                    String paramDatas = data.optString("params");
                    param = StringUtil.string2Map(paramDatas);
                }

                return NetUtil.requestData(mParentActivity, method, "", param, false, url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            hideLoadingDialog();
            if (mCallBack != null && !isCancelled()) {
                mCallBack.onDataLoaded(s);
            }
        }
    }

    //user feed
    class AsyUserFeed extends AsyncTask<Void, Void, String> {

        String mParams;
        INetCallBack mCallBack;

        public AsyUserFeed(String params, INetCallBack callBack) {
            mParams = params;
            mCallBack = callBack;
        }

        @Override
        protected String doInBackground(Void... params) {

            String method = "";
            String url = "";
            Map param = null;
            try {
                JSONObject data = new JSONObject(mParams);
                if (data != null) {
                    method = data.optString("type").toUpperCase();
                    url = data.optString("url");
                    if(!StringUtil.checkStr(method)){
                        method = "GET";
                    }
                    String paramDatas = data.optString("params");
                    param = StringUtil.string2Map(paramDatas);
                    param.put("deviceModel", DeviceParams.deviceModel);
                    param.put("release", DeviceParams.systemVersion);
                    param.put("resolution", DeviceParams.deviceSize);
                    param.put("versionName", DeviceParams.appVersion);
                    param.put("netType", DeviceParams.currentNetType);
                }
                return NetUtil.requestData(mParentActivity, method, "", param, false, url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (mCallBack != null && !isCancelled()) {
                mCallBack.onDataLoaded(s);
            }
        }
    }

}
