package com.shuqu.microcredit.UI;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.shuqu.microcredit.Admin.StatisticsMgr;
import com.shuqu.microcredit.BRApplication;
import com.shuqu.microcredit.Content.ParamKeys;
import com.shuqu.microcredit.Content.Urls;
import com.shuqu.microcredit.Interface.IUserLoginCallback;
import com.shuqu.microcredit.Interface.UserApi;
import com.shuqu.microcredit.Model.UserInfo;
import com.shuqu.microcredit.NetWork.Header;
import com.shuqu.microcredit.NetWork.NetUtil;
import com.shuqu.microcredit.Utils.ForwardActivityUtils;
import com.shuqu.microcredit.Utils.KeyBoardUtils;
import com.shuqu.microcredit.Utils.PrefsUtils;
import com.shuqu.microcredit.Utils.StringUtil;
import com.shuqu.microcredit.Utils.TimeUtils;
import com.shuqu.microcredit.Utils.ToastUtils;
import com.shuqu.microcredit.widget.LoadingDialog;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Class Info ：base activity
 * Created by Lyndon on 16/6/1.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnLayoutChangeListener, IUserLoginCallback {

    LoadingDialog mLoadingDialog;

    //Activity最外层的Layout视图
    protected View activityRootView;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;

    private BRApplication mApplication;
    private boolean mPressTwoExit = false;
    private long mPressTime = 0;

    String mNetData;

    protected View mCurrentFoucsView; //当前焦点view
    protected boolean isKeybordOpen; //当前软键盘是否打开

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        PushAgent.getInstance(this).onAppStart();

        if (getApplication() instanceof BRApplication) {
            mApplication = (BRApplication) getApplication();
            if(mApplication != null){
                mApplication.addActivity(this);
            }
        }
        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight/3;
        initView();
        initData();
        StatisticsMgr.onPageStart(currentClassName());
    }

    protected abstract void initView();

    protected abstract void initData();

    protected String currentClassName(){
        return getClass().getSimpleName();
    }


    @Override public void finish() {
        if (mApplication != null) {
            mApplication.removeActivity(this);
        }
        super.finish();
    }

    public void registLoginCallback() {
        UserApi.registLoginCallback(this);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        UserApi.unRegistLoginCallback(this);
        StatisticsMgr.onPageEnd(currentClassName());
//        移除layout大小发生改变监听器
        if(activityRootView != null) {
            activityRootView.removeOnLayoutChangeListener(this);
        }
    }

    @Override protected void onPause() {
        super.onPause();
        StatisticsMgr.onPause(this);
        mCurrentFoucsView = getCurrentFocus();
        if(mCurrentFoucsView != null && mCurrentFoucsView instanceof EditText)
            isKeybordOpen = KeyBoardUtils.isKeybordOpen((EditText) mCurrentFoucsView, this);


    }

    public void showLoading(String message){
        hideLoading();
        if(StringUtil.checkStr(message)){
            mLoadingDialog = LoadingDialog.show(this, message);
        }else {
            mLoadingDialog = LoadingDialog.show(this, "加载中...");
        }
    }

    public void hideLoading(){
        if(mLoadingDialog != null){
            mLoadingDialog.dismiss();
        }
    }

    protected void setPressTwoExitEnable(boolean  exit) {
        mPressTwoExit = exit;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (mPressTwoExit) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                    && event.getAction() == KeyEvent.ACTION_UP) {
                if (System.currentTimeMillis() - mPressTime > 2000) {
                    ToastUtils.show(this, "再按一次退出" + getPackageManager().getApplicationLabel(
                            getApplicationInfo()));
                    mPressTime = System.currentTimeMillis();
                } else {
                    mApplication.exit();
                }
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    protected void toastMsg(String msg) {
        ToastUtils.show(this, msg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatisticsMgr.onResume(this);
        if(mCurrentFoucsView != null && isKeybordOpen && mCurrentFoucsView instanceof EditText) {
            KeyBoardUtils.openKeybord((EditText) mCurrentFoucsView, this);
        }

        //添加layout大小发生改变监听器
        if(activityRootView != null) {
            activityRootView.addOnLayoutChangeListener(this);
        }
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right,
                               int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

        //old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值

//      System.out.println(oldLeft + " " + oldTop +" " + oldRight + " " + oldBottom);
//      System.out.println(left + " " + top +" " + right + " " + bottom);

        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){
            //弹起
            this.onsoftKeyboardUp();
        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){
            this.onsoftKeyboardDown();
        }
    }

    protected void onsoftKeyboardUp(){}
    protected void onsoftKeyboardDown(){}

    // logout
    public void logout(){
        new LogoutAsyncTask().execute();
       //new AlertDialog.Builder(BaseActivity.this)
       //        .setCancelable(false)
       //        .setTitle("退出提示")
       //        .setMessage("您将退出登录,是否继续?")
       //        .setIcon(R.mipmap.icon)
       //        .setPositiveButton("退出", new DialogInterface.OnClickListener() {
       //            @Override
       //            public void onClick(DialogInterface dialog, int which) {
       //                try {
       //                    dialog.dismiss();
       //                    new LogoutAsyncTask().execute();
       //                }catch (Exception e){
       //                    e.printStackTrace();
       //                }
       //            }
       //        })
       //        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
       //            @Override
       //            public void onClick(DialogInterface dialog, int which) {
       //                dialog.dismiss();
       //            }
       //        }).create().show();

    }

    //
    private class LogoutAsyncTask extends AsyncTask<Void, Void, String> {

        public LogoutAsyncTask() {
            showLoading("");
        }

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, String> paramsMap = new HashMap<>();
            paramsMap.put("appId", ParamKeys.APPID);
            paramsMap.put(Header.BRUID, UserInfo.getUid());
            paramsMap.put(Header.BRTOKEN, UserInfo.getAccess_token());
            mNetData = NetUtil.requestData(BaseActivity.this, "POST", "", paramsMap, false, Urls.getUrl(Urls.METHOD_LOGOUT));
            Log.e("BaseActivity", "NetData: " + mNetData);
            return mNetData;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            hideLoading();
            try {
                JSONObject data = new JSONObject(result);
                if(data.optInt("code") == 0){
                    UserInfo.setAccess_token("");
                    UserInfo.setRefresh_token("");
                    UserInfo.setUid("");
                    UserInfo.setUserPhone("");
                    PrefsUtils.putValue(BaseActivity.this, Header.BRFRESHTOKEN, "");
                    PrefsUtils.putValue(BaseActivity.this, ParamKeys.TOKEN_FRESH_TIME, "");
                    PrefsUtils.putValue(BaseActivity.this, ParamKeys.USER_PHONE, "");
                    ForwardActivityUtils.forwardActivity(BaseActivity.this, LoginActivity.class, true);
                    toastMsg("退出成功");
                }else{
                    toastMsg(data.optString("message"));
                }
            } catch (Exception e) {
                toastMsg("退出失败,请重试!");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUserLogin() {
    }

    @Override
    public void onUserLogout() {
    }

    /**
     *
     * @param accessToken access token
     * @param freshToken fresh token
     * @param uid user id
     */
    protected void updateUserData(String accessToken, String freshToken, String uid){

        if(StringUtil.checkStr(accessToken)){
            UserInfo.setAccess_token(accessToken);
        }
        if(StringUtil.checkStr(freshToken)){
            UserInfo.setRefresh_token(freshToken);
            PrefsUtils.putValue(BaseActivity.this, Header.BRFRESHTOKEN, UserInfo.getRefresh_token());
            PrefsUtils.putValue(BaseActivity.this, ParamKeys.TOKEN_FRESH_TIME, TimeUtils.getCurrentTime());

        }
        if(StringUtil.checkStr(uid)){
            UserInfo.setUid(uid);
            PrefsUtils.putValue(BaseActivity.this, Header.BRUID, UserInfo.getUid());
        }
    }
}
