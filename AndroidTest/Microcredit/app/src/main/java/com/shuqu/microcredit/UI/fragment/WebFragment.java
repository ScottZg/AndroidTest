package com.shuqu.microcredit.UI.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.lyndon.jsbridge.Handler.HandleUrlDataUtils;
import com.lyndon.jsbridge.Model.BrMethodInfo;
import com.shuqu.microcredit.Admin.StatisticsMgr;
import com.shuqu.microcredit.BRApplication;
import com.shuqu.microcredit.Content.ParamKeys;
import com.shuqu.microcredit.Interface.INetCallBack;
import com.shuqu.microcredit.Model.DeviceParams;
import com.shuqu.microcredit.Model.MenuItem;
import com.shuqu.microcredit.Model.UserInfo;
import com.shuqu.microcredit.NetWork.Header;
import com.shuqu.microcredit.R;
import com.shuqu.microcredit.UI.AuthenticationActivity;
import com.shuqu.microcredit.UI.ChangePwdActivity;
import com.shuqu.microcredit.UI.LoginActivity;
import com.shuqu.microcredit.UI.MainActivity;
import com.shuqu.microcredit.UI.UploadBankCardActivity;
import com.shuqu.microcredit.UI.WebActivity;
import com.shuqu.microcredit.UI.WebTabActivity;
import com.shuqu.microcredit.Utils.CommonErrorUtils;
import com.shuqu.microcredit.Utils.DialogUtils;
import com.shuqu.microcredit.Utils.DisplayUtil;
import com.shuqu.microcredit.Utils.JSBrigeUtils;
import com.shuqu.microcredit.Utils.MenuIconMap;
import com.shuqu.microcredit.Utils.NetworkUtil;
import com.shuqu.microcredit.Utils.StringUtil;
import com.shuqu.microcredit.Utils.ToastUtils;
import com.shuqu.microcredit.faceRec.activity.FaceRecognitionAc;
import com.shuqu.microcredit.filter.CitySelecteHelper;
import com.shuqu.microcredit.filter.activity.MultiSelectorActivity;
import com.shuqu.microcredit.filter.fragment.SingleSelectorFragment;
import com.shuqu.microcredit.filter.interfac.SelectorCallback;
import com.shuqu.microcredit.widget.CustomWebView;
import com.shuqu.microcredit.widget.MultiSwipeRefreshLayout;
import com.shuqu.microcredit.widget.TopPopMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class Info ：fragment for webview
 * Created by Lyndon on 16/6/1.
 */
public class WebFragment extends BaseWebFragment implements SwipeRefreshLayout
        .OnRefreshListener, MultiSwipeRefreshLayout.CanChildScrollUpCallback,
        CustomWebView.WebScrollTopCallBack {

    private static int S_REQUEST_CODE = 5000;
    private final int REQUEST_CODE_LOGIN = ++S_REQUEST_CODE;
    private final int REQUEST_CODE_CHANGE_PWD = ++S_REQUEST_CODE;
    private final int REQUEST_CODE_MULTI_SELECT = ++S_REQUEST_CODE;
    private final int REQUEST_CODE_CITY_SELECT = ++S_REQUEST_CODE;
    private final int REQUEST_CODE_AUTHENTICATION = ++S_REQUEST_CODE;
    private final int REQUEST_CODE_OPEN_WINDOW = ++S_REQUEST_CODE;
    private final int REQUEST_CODE_OPEN_TAB_WINDOW = ++S_REQUEST_CODE;
    private final int REQUEST_CODE_BANK_CARD_UPLOAD = ++S_REQUEST_CODE;
    private final int REQUEST_CODE_LIVING_BODY_IDENFY = ++S_REQUEST_CODE;


    View mHeaderView;
    MultiSwipeRefreshLayout mRefreshLayout;
    private TopPopMenu mPopMenu = null;
    private int mMenuPositionY = 0;
    private boolean mSwipeRefreshChildScrollUp = false;
    private boolean mWebScrollTop = true;

    Map<String, String> mWebHeader = new HashMap<>();

    // current valide url, defence redirect
    String mCurrentValideUrl = "";

    //消息中心js 时间Id
    private String mJsMsgCenterEventId;

    @Override
    protected int setContentView() {
        return R.layout.fragment_web;
    }

    protected void initView() {
        super.initView();
        mRefreshLayout = (MultiSwipeRefreshLayout) mRootView.findViewById(R.id.swip_ly);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark),
                getResources().getColor(R.color.colorAccent));
        mRefreshLayout.setCanChildScrollUpCallback(this);
        mHeaderView = mRootView.findViewById(R.id.rl_head);
        mWebView = (CustomWebView) mRootView.findViewById(R.id.wb_main);
        mWebView.getSettings().setJavaScriptEnabled(true);
        //mWebView.getSettings().setAppCacheEnabled(false);
        //mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //enable local storage
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = mParentActivity.getApplicationContext().getCacheDir().getAbsolutePath();
        mWebView.getSettings().setAppCachePath(appCachePath);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);

        mWebView.setWebScrollTopListener(this);
        mWebView.addJavascriptInterface(new elementScriptInterface(),
                "AndroidInterface");
        mWebView.setWebViewClient(new CustomerWebClient());
        mWebView.requestFocus();
        mWebView.setWebChromeClient(new CustomerChromeClient());
        String ua = mWebView.getSettings().getUserAgentString();
        String appInfo = "@brapp:" + DeviceParams.appName + ":" + DeviceParams
                .appVersionCode + "@";
        mWebView.getSettings().setUserAgentString(ua + appInfo);
        setLeftButton(R.mipmap.left_back_icon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView != null && mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    clickLeftBtn(false);
                }
            }
        });

        mWebHeader.put(Header.BRTOKEN, UserInfo.getAccess_token());
        mWebHeader.put(Header.BRUID, UserInfo.getUid());
        mWebHeader.put(Header.BRFRESHTOKEN, UserInfo.getRefresh_token());
        mWebHeader.put(Header.BRSESSION, UserInfo.getSession());
        mWebHeader.put("Pragma", "no-cache");
        mWebHeader.put("Cache-Control", "no-cache");
        hideLeftBtn();
        hideRightBtn();
        enableFresh(false);
    }

    protected void initData() {
        super.initData();
        try {
            Bundle bundle = getArguments();
            if (bundle != null) {
                String webUrl = bundle.getString(ParamKeys.WEBURL);
                boolean hideHide = bundle.getBoolean(ParamKeys.HIDEFRAGMENTHEAD);
                if(hideHide){
                    mHeaderView.setVisibility(View.GONE);
                }
                loadData(webUrl);
                showLoading();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadData(String url) {
        if (TextUtils.isEmpty(url)) return;
        if (mWebView != null) {
            mWebView.loadUrl(url, mWebHeader);
        }
        if (!StringUtil.equals(url, mCurrentValideUrl) && CommonErrorUtils.isSupportHost(url)) {
            mCurrentValideUrl = url;
        }
    }

    private void popRightMenu() {
        //pop menu
        if (mPopMenu == null && !mMenuDatas.isEmpty()) {
            mPopMenu = new TopPopMenu(mParentActivity, R.layout.menu_web, WebFragment.this);
            mPopMenu.setMenuData(mMenuDatas);
        }
        if (mPopMenu != null) {
            mMenuPositionY = mHeaderView.getHeight() + DisplayUtil
                    .getStatusHeight(mParentActivity);
            mPopMenu.showAtLocation(mHeaderView,
                    Gravity.TOP | Gravity.RIGHT, 0, mMenuPositionY - 30);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        JSONObject json = new JSONObject();
        String loadUrl = null;

        //登陆
        if (requestCode == REQUEST_CODE_LOGIN) {
            try {
                json.put("code", ParamKeys.JS_SUCCESS);
                json.put("data", data != null ? data.getStringExtra(ParamKeys.NET_DATA) : "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (resultCode == Activity.RESULT_OK) {
                loadUrl = JSBrigeUtils.getJSSucessUrl(mCurrentMethodId, json);
                String tokenUrl = "javascript:" + "BrBridge.token = true";
                mWebView.loadUrl(tokenUrl);

                callJsMethodAndReturn(loadUrl);
            } else if (resultCode == ParamKeys.REGIST_SUCESS) {
                //注册成功
                loadUrl = JSBrigeUtils.getJSSucessUrl(mCurrentMethodId, json);

                String tokenUrl = "javascript:" + "BrBridge.token = true";
                mWebView.loadUrl(tokenUrl);

                callJsMethodAndReturn(loadUrl);
            }
        }
        //修改密码
        else if (requestCode == REQUEST_CODE_CHANGE_PWD) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    json.put("code", ParamKeys.JS_SUCCESS);
                    json.put("data", "{}");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            loadUrl = JSBrigeUtils.getJSSucessUrl(mCurrentMethodId, json);
            callJsMethodAndReturn(loadUrl);
        } else if (requestCode == REQUEST_CODE_MULTI_SELECT) {
            JSONObject multiSelectJson = new JSONObject();

            if (resultCode == Activity.RESULT_OK) {
                String stringExtra = data.getStringExtra(ParamKeys.MULTI_SELECTOR_KEY);
                if (StringUtil.checkStr(stringExtra)) {
                    try {
                        JSONObject jsData = new JSONObject(stringExtra);

                        multiSelectJson.put("code", ParamKeys.JS_SUCCESS);
                        multiSelectJson.put("data", jsData);

                        String jsSucessUrl = JSBrigeUtils.getJSSucessUrl(mCurrentMethodId, multiSelectJson);
                        mWebView.loadUrl(jsSucessUrl);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    multiSelectJson.put("code", ParamKeys.JS_FAILURE);
                    multiSelectJson.put("data", "");

                    String jsSucessUrl = JSBrigeUtils.getJSFailurUrl(mCurrentMethodId, multiSelectJson);
                    mWebView.loadUrl(jsSucessUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        else if (requestCode == REQUEST_CODE_CITY_SELECT) {
            JSONObject citySelectJson = new JSONObject();

            if (resultCode == Activity.RESULT_OK) {
                String stringExtra = data.getStringExtra(ParamKeys.CITY_SELECTE_KEY);
                if (StringUtil.checkStr(stringExtra)) {
                    try {
                        JSONObject jsData = new JSONObject(stringExtra);

                        citySelectJson.put("code", ParamKeys.JS_SUCCESS);
                        citySelectJson.put("data", jsData);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    citySelectJson.put("code", ParamKeys.JS_FAILURE);
                    citySelectJson.put("data", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            String jsSucessUrl = JSBrigeUtils.getJSSucessUrl(mCurrentMethodId, citySelectJson);
            mWebView.loadUrl(jsSucessUrl);
        } else if(requestCode == REQUEST_CODE_AUTHENTICATION){
            JSONObject jsonObject = new JSONObject();
            int stateCode = ParamKeys.JS_FAILURE;
            if(data != null){
                stateCode = data.getIntExtra(ParamKeys.USER_AUTHENTICATION_STATE, ParamKeys.JS_FAILURE);
                try {
                    jsonObject.put("code", stateCode);
                    jsonObject.put("data", new JSONObject(data.getStringExtra(ParamKeys.UPLOAD_IMG_RESULT)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    jsonObject.put("code", ParamKeys.JS_FAILURE);
                    jsonObject.put("data", null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            String jsSucessUrl = stateCode == ParamKeys.JS_SUCCESS ? (JSBrigeUtils.getJSSucessUrl(mCurrentMethodId, jsonObject)) : (JSBrigeUtils.getJSFailurUrl(mCurrentMethodId, jsonObject));
            mWebView.loadUrl(jsSucessUrl);
        } else if(requestCode == REQUEST_CODE_BANK_CARD_UPLOAD){
            JSONObject jsonObject = new JSONObject();
            int stateCode = ParamKeys.JS_FAILURE;
            if(data != null){
                stateCode = data.getIntExtra(ParamKeys.USER_UPLOAD_BANK_CARD_STATE, ParamKeys.JS_FAILURE);
                try {
                    jsonObject.put("code", stateCode);
                    jsonObject.put("data", new JSONObject(data.getStringExtra(ParamKeys.UPLOAD_IMG_RESULT)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    jsonObject.put("code", ParamKeys.JS_FAILURE);
                    jsonObject.put("data", null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            String jsSucessUrl = stateCode == ParamKeys.JS_SUCCESS ? (JSBrigeUtils.getJSSucessUrl(mCurrentMethodId, jsonObject)) : (JSBrigeUtils.getJSFailurUrl(mCurrentMethodId, jsonObject));
            mWebView.loadUrl(jsSucessUrl);
        } else if(requestCode == REQUEST_CODE_LIVING_BODY_IDENFY){
            JSONObject jsonObject = new JSONObject();
            int stateCode = ParamKeys.JS_FAILURE;
            if(data != null){
                stateCode = data.getIntExtra(ParamKeys.USER_LIVING_BODY_IDENFY, ParamKeys.JS_FAILURE);
                try {
                    jsonObject.put("code", stateCode);
                    jsonObject.put("data", new JSONObject(data.getStringExtra(ParamKeys.UPLOAD_IMG_RESULT)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    jsonObject.put("code", ParamKeys.JS_FAILURE);
                    jsonObject.put("data", null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            String jsSucessUrl = stateCode == ParamKeys.JS_SUCCESS ? (JSBrigeUtils.getJSSucessUrl(mCurrentMethodId, jsonObject)) : (JSBrigeUtils.getJSFailurUrl(mCurrentMethodId, jsonObject));
            mWebView.loadUrl(jsSucessUrl);
        }else if(requestCode == REQUEST_CODE_OPEN_WINDOW) {
            if(resultCode == Activity.RESULT_OK) {
                if(data != null) {
                    boolean isRefresh = data.getBooleanExtra(ParamKeys.HISTORY_BACK_REFRESH, false);
                    if(isRefresh && mWebView != null) {
                        mWebView.reload();
                    }
                }
            }
        }
        //打开二维码扫描
        else if (requestCode == REQUEST_CODE_SCANNER) {
            if (data != null) {
                String tempUrl = data.getStringExtra("QRCODE");
                if (StringUtil.checkStr(tempUrl) && mWebView != null) {
                    Intent intent = new Intent(mParentActivity, WebActivity.class);
                    intent.putExtra(ParamKeys.WEBURL, tempUrl);
                    startActivity(intent);
                }
            }
        }
    }

    /**
     * 统一加载js
     */
    private void callJsMethodAndReturn(String loadUrl) {
        Log.e("login_url", loadUrl + "==");
        mWebView.loadUrl(loadUrl);
        return;
    }


    @Override
    public void userLogin(String methodId) {
        Intent intent = new Intent(mParentActivity, LoginActivity.class);
        intent.putExtra(ParamKeys.INTENT_FROM, ParamKeys.INTENT_FROM_JS);
        mCurrentMethodId = methodId;
        startActivityForResult(intent, REQUEST_CODE_LOGIN);
    }


    @Override
    public void userLogout(String methodId) {
        if (mParentActivity != null) {
            mParentActivity.logout();
        }
    }


    @Override
    public void userChangePwd(String methodId) {
        Intent intent = new Intent(mParentActivity, ChangePwdActivity.class);
        intent.putExtra(ParamKeys.INTENT_FROM, ParamKeys.INTENT_FROM_JS);
        mCurrentMethodId = methodId;
        startActivityForResult(intent, REQUEST_CODE_CHANGE_PWD);
    }


    @Override
    public void userPayment(BrMethodInfo info) {
        try {
            JSONObject data = new JSONObject(info.mMethodParams);
            String type = data.optString("type");
            String cost = data.optString("cost");
            //if (ParamKeys.ALIPAY.equals(type)) {
            //    new AsyAlipay(cost, info.mFirstNameId).execute();
            //} else if (ParamKeys.WECHAT.equals(type)) {
            //    new AsyWxPay(cost, info.mFirstNameId).execute();
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void requestNetData(final String methodId, final String params) {
        requestData(params, new INetCallBack() {
            @Override
            public void onDataLoaded(String data) {
                try {
                    mWebView.loadUrl(JSBrigeUtils.getJSSucessUrl(methodId, new JSONObject(data)));
                } catch (Exception e) {
                    mWebView.loadUrl(JSBrigeUtils.getJSFailurUrl(methodId, "{}"));
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void openWindow(BrMethodInfo info, boolean refresh, String url) {
        JSONObject json = new JSONObject();
        try {
            json.put("code", 200);
            json.put("data", "{}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        completeFresh();
        if (refresh) {
            hideRightBtn();
            clearMenuData();
            mWebView.loadUrl(url);
        } else {
            if (!refresh && !TextUtils.isEmpty(url)) {
                Intent intent = new Intent(mParentActivity, WebActivity.class);
                intent.putExtra(ParamKeys.WEBURL, url);
                startActivityForResult(intent, REQUEST_CODE_OPEN_WINDOW);
            }
            mWebView.loadUrl(JSBrigeUtils.getJSSucessUrl(mCurrentMethodId, json));
        }
    }


    @Override public void openTabWindow(BrMethodInfo brMethodInfo) {
        Intent intent = new Intent(mParentActivity, WebTabActivity.class);
        intent.putExtra(ParamKeys.TABWEBPARAM, brMethodInfo.mMethodParams);
        startActivityForResult(intent, REQUEST_CODE_OPEN_TAB_WINDOW);
    }


    @Override public void openUserCenter(BrMethodInfo brMethodInfo) {
        JSONObject json = new JSONObject();
        if(mParentActivity !=null && mParentActivity instanceof MainActivity){
            ((MainActivity)mParentActivity).openUserCenter();
            try {
                json.put("code", ParamKeys.JS_SUCCESS);
                json.put("data", "{}");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mWebView.loadUrl("javascript:" + "BrBridge.onSuccess" + "(" +
                    brMethodInfo.mFirstNameId + "," + json + ")");
        }else {
            try {
                json.put("code", ParamKeys.JS_FAILURE);
                json.put("data", "{}");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mWebView.loadUrl("javascript:" + "BrBridge.onFailure" + "(" +
                    brMethodInfo.mFirstNameId + "," + json + ")");
            mWebView.loadUrl(JSBrigeUtils.getJSFailurUrl(brMethodInfo.mFirstNameId, json));
        }
    }


    @Override
    public void openRootWindow(BrMethodInfo info) {
        JSONObject json = new JSONObject();
        try {
            json.put("code", 200);
            json.put("data", "{}");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            completeFresh();

            JSONObject data = new JSONObject(info.mMethodParams);
            String webUrl = data.optString("url");
            boolean isRefresh = data.optBoolean("refresh");
            Intent intent = new Intent(mParentActivity, MainActivity.class);
            intent.putExtra(ParamKeys.MAIN_TAG_KEY, webUrl);
            intent.putExtra(ParamKeys.MAIN_TAG_REFRESH, isRefresh);
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mWebView.loadUrl(JSBrigeUtils.getJSSucessUrl(info.mFirstNameId, json));
    }


    @Override
    public void setNativeTitle(BrMethodInfo info) {
        JSONObject json = new JSONObject();
        try {
            json.put("code", ParamKeys.JS_SUCCESS);
            json.put("data", "{}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONObject data = new JSONObject(info.mMethodParams);
            String title = data.optString("title");
            setTitle(title);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mWebView.loadUrl(JSBrigeUtils.getJSSucessUrl(info.mFirstNameId, json));
    }


    @Override
    public void toast(BrMethodInfo info) {

        try {
            JSONObject data = new JSONObject(info.mMethodParams);
            String content = data.optString("content");
            Toast.makeText(mParentActivity, content, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject json = new JSONObject();
        try {
            json.put("code", ParamKeys.JS_SUCCESS);
            json.put("data", "{}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mWebView.loadUrl(JSBrigeUtils.getJSSucessUrl(info.mFirstNameId, json));
    }


    @Override
    public void showDialog(final BrMethodInfo info) {
        try {
            JSONObject data = new JSONObject(info.mMethodParams);
            String title = data.optString("title");
            String content = data.optString("content");
            JSONObject left = data.optJSONObject("leftBtn");
            String leftBtn = "";
            if (left != null) {
                leftBtn = left.optString("content");
            }
            JSONObject right = data.optJSONObject("rightBtn");
            String rightBtn = "";
            if (right != null) {
                rightBtn = right.optString("content");
            }
            DialogUtils.showConfirmCancleDialog(mParentActivity, title, content, leftBtn, rightBtn,
                    new DialogUtils.dialogClickListener() {
                        @Override
                        public void onClickLeft() {
                            dialog(info, true);
                        }

                        @Override
                        public void onClickRight() {
                            dialog(info, false);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void dialog(BrMethodInfo info, boolean clickLeftBtn) {
        JSONObject json = new JSONObject();
        try {
            json.put("code", ParamKeys.JS_SUCCESS);
            json.put("data", "{}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (clickLeftBtn) {
            mWebView.loadUrl(JSBrigeUtils.getJSSucessUrl(info.mFirstNameId, json));

        } else {
            mWebView.loadUrl(JSBrigeUtils.getJSFailurUrl(info.mFirstNameId, json));
        }
    }


    @Override
    public void copy(BrMethodInfo info) {
        JSONObject json = new JSONObject();
        try {
            json.put("code", ParamKeys.JS_SUCCESS);
            json.put("data", "{}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mWebView.loadUrl("javascript:" + "BrBridge.onSuccess" + "(" +
                info.mFirstNameId + "," + json + ")");
        mWebView.loadUrl(JSBrigeUtils.getJSSucessUrl(info.mFirstNameId, json));
    }


    @Override
    public void netError(BrMethodInfo info) {
        showErrorView(info.mMethodParams);
    }


    @Override
    public void freshWindow(BrMethodInfo info) {
        try {
            JSONObject data = new JSONObject(info.mMethodParams);
            String fresh = data.optString("active");
            if ("true".equals(fresh)) {
                enableFresh(true);
            } else {
                enableFresh(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onEvent(BrMethodInfo brMethodInfo) {
        String eventId = "";
        HashMap<String, String> eventParams = null;
        try {
            JSONObject event = new JSONObject(brMethodInfo.mMethodParams);
            //eventId = event.optString("id");
            eventParams = StringUtil.string2Map(event.optString("params"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        StatisticsMgr.onEvent(mParentActivity, eventParams);
    }


    @Override
    public void sendMail(BrMethodInfo brMethodInfo) {
        String mailAddress = "";
        try {
            JSONObject data = new JSONObject(brMethodInfo.mMethodParams);
            mailAddress = data.optString("mailAddress");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!StringUtil.checkStr(mailAddress)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        intent.setData(Uri.parse(mailAddress)); // or just "mailto:" for blank
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        startActivity(intent);
    }


    @Override
    public void makePhoneCall(BrMethodInfo brMethodInfo) {
        String phoneNumber = "";
        try {
            JSONObject data = new JSONObject(brMethodInfo.mMethodParams);
            phoneNumber = data.optString("phoneNumber");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!StringUtil.checkStr(phoneNumber)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    @Override
    public void singleSelecte(final BrMethodInfo brMethodInfo) {
        if(mSelectorFragment != null){
            mSelectorFragment.dismiss();
        }
        Bundle bundle = new Bundle();
        String mMethodParams = brMethodInfo.mMethodParams;
        if (CitySelecteHelper.checkJsArrayNull(mMethodParams, "option")) {
            ToastUtils.show(mParentActivity, "没有筛选条件");
            return;
        }
        bundle.putString(ParamKeys.DIALOG_SINGLE_FILTER_KEY, mMethodParams);
        mSelectorFragment = SingleSelectorFragment.newInstance(bundle);
        mSelectorFragment.show(getChildFragmentManager(), ParamKeys.SINGLE_SELECTOR_TAG);
        mSelectorFragment.addSingleCallback(new SelectorCallback() {
            @Override
            public void onSingleSelecte(JSONObject jsonObject) {

                JSONObject jsData = new JSONObject();
                try {
                    jsData.put("code", jsonObject == null ? ParamKeys.JS_FAILURE : ParamKeys.JS_SUCCESS);
                    jsData.put("data", jsonObject);

                    String jsSucessUrl = JSBrigeUtils.getJSSucessUrl(brMethodInfo.mFirstNameId, jsData);

                    mWebView.loadUrl(jsSucessUrl);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void multiSelecte(final BrMethodInfo brMethodInfo) {
        String mMethodParams = brMethodInfo.mMethodParams;
        mCurrentMethodId = brMethodInfo.mFirstNameId;
        if (CitySelecteHelper.checkJsArrayNull(mMethodParams, "option")) {
            ToastUtils.show(mParentActivity, "没有筛选条件");
            return;
        }
        Intent intent = new Intent(mParentActivity, MultiSelectorActivity.class);
        intent.putExtra(ParamKeys.DIALOG_MULTI_FILTER_KEY, mMethodParams);
        startActivityForResult(intent, REQUEST_CODE_MULTI_SELECT);
    }


    @Override public void getUserInfo(BrMethodInfo brMethodInfo) {
        String userId = UserInfo.getUid();
        JSONObject json = new JSONObject();
        JSONObject jsData = new JSONObject();
        try {
            jsData.put("userId", userId);
            json.put("code", ParamKeys.JS_SUCCESS);
            json.put("data", jsData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsJson = JSBrigeUtils.getJSSucessUrl(brMethodInfo.mFirstNameId, json);
        mWebView.loadUrl(jsJson);
    }


    @Override
    public void userFeed(final BrMethodInfo info) {
        try {
            new AsyUserFeed(info.mMethodParams, new INetCallBack() {
                @Override
                public void onDataLoaded(String data) {
                    mWebView.loadUrl("javascript:" + "BrBridge.onSuccess" + "(" + info.mFirstNameId + ",{} )");
                }
            }).execute();
        } catch (Exception e) {
        }
    }

    @Override
    public void citySelector(BrMethodInfo brMethodInfo) {

    }


    @Override public void authentication(BrMethodInfo brMethodInfo) {
        mCurrentMethodId = brMethodInfo.mFirstNameId;
        Intent intent = new Intent(mParentActivity, AuthenticationActivity.class);
        startActivityForResult(intent, REQUEST_CODE_AUTHENTICATION);
    }


    @Override public void uploadBankCard(BrMethodInfo brMethodInfo) {
        mCurrentMethodId = brMethodInfo.mFirstNameId;
        Intent intent = new Intent(mParentActivity, UploadBankCardActivity.class);
        startActivityForResult(intent, REQUEST_CODE_BANK_CARD_UPLOAD);
    }


    @Override public void livingBodyIdentify(BrMethodInfo brMethodInfo) {
        mCurrentMethodId = brMethodInfo.mFirstNameId;
        Intent intent = new Intent(mParentActivity, FaceRecognitionAc.class);
        startActivityForResult(intent, REQUEST_CODE_LIVING_BODY_IDENFY);
    }


    @Override public void createBottomTab(BrMethodInfo brMethodInfo) {
        if(mParentActivity instanceof MainActivity){
            ((MainActivity)mParentActivity).createBottomTab(brMethodInfo.mMethodParams);
        }
    }


    @Override
    public void historyBack(BrMethodInfo info) {
        JSONObject json = new JSONObject();
        try {
            json.put("code", ParamKeys.JS_SUCCESS);
            json.put("data", "{}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            try {
                JSONObject jsonObject = new JSONObject(info.mMethodParams);
                clickLeftBtn(jsonObject.optBoolean("refresh", false));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mWebView.loadUrl("javascript:" + "BrBridge.onNativeCall" + "(" +
                info.mFirstNameId + "," + json + ")");
    }


    @Override
    public void nativeBack(BrMethodInfo info) {
        JSONObject json = new JSONObject();
        try {
            json.put("code", ParamKeys.JS_SUCCESS);
            json.put("data", "{}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            clickBack();
        }
        mWebView.loadUrl("javascript:" + "BrBridge.onNativeCall" + "(" +
                info.mFirstNameId + "," + json + ")");
    }


    @Override
    public void topRightMenu(BrMethodInfo info) {
        JSONObject json = new JSONObject();
        try {
            json.put("code", ParamKeys.JS_SUCCESS);
            json.put("data", "{}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setMenuDatas(info.mMethodParams);
        if (mPopMenu != null) {
            mPopMenu.setMenuData(mMenuDatas);
        }
        setRightButton(R.mipmap.abc_ic_menu_more, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popRightMenu();
            }
        });
        mWebView.loadUrl("javascript:" + "BrBridge.onNativeCall" + "(" +
                info.mFirstNameId + "," + json + ")");
    }


    @Override
    public void topLeftMenu(final BrMethodInfo info) {
        JSONObject jsonDatas = null;
        String eventId = "";

        try {
            jsonDatas = new JSONObject(info.mMethodParams);
            String menuData = jsonDatas.optString("menus");
            ArrayList<MenuItem> menuDatas = MenuItem.parse(menuData);
            eventId = menuDatas.get(0).mEventId;
            int iconId = MenuIconMap.getMenuIcon(menuDatas.get(0).mIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String finalEventId = eventId;
        setLeftButton(R.mipmap.abc_ic_menu_more, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuItemClicked(finalEventId);
            }
        });
    }


    @Override public void messageCenter(BrMethodInfo brMethodInfo) {
        mJsMsgCenterEventId = brMethodInfo.mFirstNameId;
    }


    @Override
    public void menuItemClicked(String eventId) {
        if (mPopMenu != null) {
            mPopMenu.dismiss();
        }
        JSONObject json = new JSONObject();
        try {
            json.put("code", ParamKeys.JS_SUCCESS);
            json.put("data", "{}");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mWebView.loadUrl("javascript:" + "BrBridge.onNativeCall" + "(" +
                eventId + "," + json + ")");
    }


    @Override
    public void startFresh() {
        if (mWebView != null) {
            mWebView.reload();
        }
    }


    @Override
    public void completeFresh() {
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(false);
        }
    }


    @Override
    public void enableFresh(boolean enable) {
        mSwipeRefreshChildScrollUp = !enable;
    }


    @Override
    public void onRefresh() {
        startFresh();
    }


    @Override
    public boolean canSwipeRefreshChildScrollUp() {
        if (mWebScrollTop && !mSwipeRefreshChildScrollUp) {
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onScrollTop(boolean top) {
        mWebScrollTop = top;
    }


    private class CustomerWebClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            completeFresh();
            if (view != null) {
                view.clearCache(true);
                view.loadUrl("javascript:window.AndroidInterface.metaData" +
                        "(document.querySelector('meta[name=\"no-refresh\"]').getAttribute('content'))");
                hideLoading();
                if (!CommonErrorUtils.isSupportHost(url) ||
                        !NetworkUtil.isConnected(mParentActivity) ||
                        CommonErrorUtils.isErrorTitle(mCurrentWebTitle)) {
                    showErrorView(R.mipmap.ic_fresh, "网络异常,请检查手机网络设置!", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadData(mCurrentValideUrl);
                            showLoading();
                        }
                    });
                }

            }
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!TextUtils.isEmpty(url) && url.startsWith(HandleUrlDataUtils.DataPrefix)) {
                HandleUrlDataUtils.parseUrlData(mParentActivity, url, WebFragment.this, WebFragment.this, WebFragment.this);
            } else {
                view.loadUrl(url, mWebHeader);
            }
            showLoading();
            return true;
        }
    }

    private String mCurrentWebTitle = "";

    private class CustomerChromeClient extends WebChromeClient {

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            if (!TextUtils.isEmpty(message) && message.startsWith(HandleUrlDataUtils.DataPrefix)) {
                HandleUrlDataUtils.parseUrlData(mParentActivity, message, WebFragment.this, WebFragment.this, WebFragment.this);
            }
            result.cancel();
            return true;
        }


        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }


        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (!TextUtils.isEmpty(title) && !title.contains("htm") && !title.contains("找不到")) {
                setTitle(title);
            }
            mCurrentWebTitle = title;
        }
    }

    //get html element data
    class elementScriptInterface {
        @JavascriptInterface
        public void metaData(String data) {
            if (!StringUtil.checkStr(data) || "no".equals(data) || "false".equals(data)) {
                //enableFresh(true);
                showFreshButton(true);
                handleParentRefreshButton(true);
            } else {
                //enableFresh(false);
                showFreshButton(false);
            }
        }
    }

    public boolean webGoBack() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }

    //
    protected void showFreshButton(final boolean show) {
        Runnable runable = new Runnable() {
            @Override
            public void run() {
                ImageView ivFresh = (ImageView) mHeaderView.findViewById(R.id.iv_fresh);
                if (ivFresh != null) {
                    ivFresh.setVisibility(View.VISIBLE);
                    if (show) {
                        ivFresh.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (mWebView != null) {
                                    mWebView.reload();
                                }
                            }
                        });
                    } else {
                        ivFresh.setVisibility(View.GONE);
                    }
                }
            }
        };
        BRApplication.runOnUiThread(runable);

    }

    //特殊处理 WebTabActivity 刷新按钮
    private void handleParentRefreshButton(boolean show){
        if(mParentActivity instanceof WebTabActivity){
            ((WebTabActivity)mParentActivity).showFreshButton(show);
        }
    }

}
