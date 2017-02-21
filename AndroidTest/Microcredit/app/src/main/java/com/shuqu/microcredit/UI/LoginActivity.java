package com.shuqu.microcredit.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuqu.microcredit.Content.ParamKeys;
import com.shuqu.microcredit.Content.Urls;
import com.shuqu.microcredit.Encry.EncryUtil;
import com.shuqu.microcredit.Model.DeviceParams;
import com.shuqu.microcredit.Model.LoginRegistResult;
import com.shuqu.microcredit.Model.UserInfo;
import com.shuqu.microcredit.NetWork.NetUtil;
import com.shuqu.microcredit.R;
import com.shuqu.microcredit.Utils.CommonErrorUtils;
import com.shuqu.microcredit.Utils.ForwardActivityUtils;
import com.shuqu.microcredit.Utils.MaxLengthWatcher;
import com.shuqu.microcredit.Utils.NativeOnBacklistener;
import com.shuqu.microcredit.Utils.PrefsUtils;
import com.shuqu.microcredit.Utils.PwdUtils;
import com.shuqu.microcredit.Utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by wuxin on 16/6/3.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_CODE_REGIST = 10001;

    ImageView imgPwdShow;
    ImageView imgHeader;
    ImageView leftBack;
    TextView tvForgetPwd;
    TextView tvTitle;
    TextView btnLogin;
    TextView tvRegist;
    EditText etPwd;
    EditText etPhone;

    private boolean isPwdShow;

    private String mPwd;
    private String loginPhone;

    @Override protected void initView() {
        setContentView(R.layout.activity_login);
        imgPwdShow = (ImageView) findViewById(R.id.img_pwd_show);
        tvForgetPwd = (TextView) findViewById(R.id.tv_forget_pwd);
        btnLogin = (TextView) findViewById(R.id.tv_login);
        tvRegist = (TextView) findViewById(R.id.tv_regist);
        etPwd = (EditText) findViewById(R.id.et_pwd);
        etPhone = (EditText) findViewById(R.id.et_phone);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        leftBack = (ImageView) findViewById(R.id.iv_left);
        imgHeader = (ImageView) findViewById(R.id.iv_user_head);
        activityRootView = findViewById(R.id.root_layout);

        etPwd.addTextChangedListener(new MaxLengthWatcher(this, 16, etPwd));

        leftBack.setOnClickListener(new NativeOnBacklistener(this));
        imgPwdShow.setOnClickListener(this);
        tvForgetPwd.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        tvRegist.setOnClickListener(this);
        etPwd.setOnClickListener(this);
    }


    @Override protected void initData() {
        tvTitle.setText(R.string.login_login);
    }


    /**
     * 注册
     */
    private void toRegistAc() {
        Intent intent = new Intent(this, RegistActivity.class);
        startActivityForResult(intent, REQUEST_CODE_REGIST);
    }

    /**
     * 忘记密码
     */
    private void toForgetPwd() {
        ForwardActivityUtils.forwardActivity(this, ForgetPwdActivity.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_pwd_show:
                isPwdShow = PwdUtils.showPassword(isPwdShow, etPwd, imgPwdShow);
                break;
            case R.id.tv_forget_pwd:
                toForgetPwd();
                break;
            case R.id.tv_login:
                toLogin();
                break;
            case R.id.tv_regist:
                toRegistAc();
                break;
        }
    }

    /**
     * 登陆
     */
    private void toLogin() {

        loginPhone = etPhone.getText().toString();
        if(TextUtils.isEmpty(loginPhone)) {
            toastMsg("请输入手机号码");
            return;
        }

        if(!StringUtil.isMobileNO(loginPhone)) {
            toastMsg("请输入正确的手机号码");
            return;
        }

        String pwd = etPwd.getText().toString();
        if(TextUtils.isEmpty(pwd)) {
            toastMsg("请输入您的密码");
            return;
        }

        new LoginAsyncTask(loginPhone, EncryUtil.encry(pwd)).execute();
    }

    protected class LoginAsyncTask extends AsyncTask<Void, Void, LoginRegistResult> {

        public LoginAsyncTask(String phone, String pwd) {
            loginPhone = phone;
            mPwd = pwd;
            showLoading("");
        }

        @Override
        protected LoginRegistResult doInBackground(Void... params) {

            HashMap<String, String> paramsMap = new HashMap<>();
            paramsMap.put("appId", ParamKeys.APPID);
            paramsMap.put("deviceId", DeviceParams.deviceId);
            paramsMap.put("unionId", loginPhone);
            paramsMap.put("password", mPwd);
            paramsMap.put("osType", "1");

            mNetData = NetUtil.requestData(LoginActivity.this, "POST", "", paramsMap, false, Urls.getUrl(Urls.METHOD_LOGIN));
            LoginRegistResult loginResult = null;
            if(StringUtil.checkStr(mNetData)) {
                try {
                    loginResult = new LoginRegistResult();
                    JSONObject jsonObject = new JSONObject(mNetData);
                    loginResult.setAccess_token(jsonObject.optString("accessToken"));
                    loginResult.setCode(jsonObject.optInt("code"));
                    loginResult.setMessage(jsonObject.optString("message"));
                    loginResult.setRefresh_token(jsonObject.optString("refreshToken"));
                    loginResult.setUid(jsonObject.optString("uid"));
                    loginResult.setUserPhone(loginPhone);
                    loginResult.setUserPwd(mPwd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return loginResult;
        }

        @Override
        protected void onPostExecute(LoginRegistResult result) {
            super.onPostExecute(result);
            hideLoading();
            handleResult(result);
        }
    }

    /**
     * 处理登陆返回数据
     * @param result
     */
    private void handleResult(LoginRegistResult result) {

        if(result == null) {
            setResult(CommonErrorUtils.LOGIN_FAILURE);
        } else {
            int code = result.getCode();
            if (code == ParamKeys.LOGIN_SUCESS) {
                PrefsUtils.putValue(LoginActivity.this, ParamKeys.USER_NAME, loginPhone);
                UserInfo.setUserPhone(result.getUserPhone());
                PrefsUtils.putValue(LoginActivity.this, ParamKeys.USER_PHONE, UserInfo.getUserPhone());
                updateUserData(result.getAccess_token(), result.getRefresh_token(), result.getUid());

                String intentFrom = getIntent().getStringExtra(ParamKeys.INTENT_FROM);

                if(StringUtil.checkStr(intentFrom) && ParamKeys.INTENT_FROM_JS.equals(intentFrom)) {
                    Intent data = new Intent();
                    data.putExtra(ParamKeys.NET_DATA, mNetData);
                    setResult(Activity.RESULT_OK, data);
                }
                this.finish();
            } else if (code == CommonErrorUtils.LOGIN_FAILURE) {
                toastMsg("账号或密码错误");
            } else if (code == CommonErrorUtils.LOGIN_FROZEN){
                toastMsg("连续5次登录失败,账号被冻结,请30分钟后重试");
                ForwardActivityUtils.forwardActivity(LoginActivity.this, ForgetPwdActivity.class);
            } else {
                String message = result.getMessage();
                toastMsg(StringUtil.checkStr(message) ? message : CommonErrorUtils
                        .getErrorMessage(code));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == ParamKeys.REGIST_SUCESS) {
            setResult(resultCode, data);
            this.finish();
        }
    }


    Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onsoftKeyboardUp() {
        super.onsoftKeyboardUp();
        mHandler.post(new Runnable() {
            @Override public void run() {
                imgHeader.setVisibility(View.GONE);
                tvRegist.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onsoftKeyboardDown() {
        super.onsoftKeyboardDown();
        mHandler.post(new Runnable() {
            @Override public void run() {
                imgHeader.setVisibility(View.VISIBLE);
                tvRegist.setVisibility(View.VISIBLE);
            }
        });
    }
}
