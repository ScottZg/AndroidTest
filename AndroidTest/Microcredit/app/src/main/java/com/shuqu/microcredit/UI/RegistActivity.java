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
import android.widget.Toast;

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
import com.shuqu.microcredit.Utils.SearchWather;
import com.shuqu.microcredit.Utils.StringUtil;
import com.shuqu.microcredit.Utils.TimeCount;
import com.shuqu.microcredit.Utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 *
 * Created by wuxin on 16/6/3.
 */
public class RegistActivity extends BaseActivity implements View.OnClickListener {

    EditText etPwd;
    EditText etConfirmPwd;
    EditText etPhone;
    EditText etCode;
    ImageView imgShowPwd;
    ImageView imgShowPwdConfirm;
    ImageView imgLogo;
    ImageView leftBack;
    TextView tvGetCode;
    TextView tvRegist;
    TextView tvTitle;
    TextView tvProtocol;
    String mNetData;
    String mUserPhone;

    private boolean isPwdShow; //是否显示密码
    private boolean isPwdShowConfirm;
    private TimeCount mTimer; //验证码倒计时

    protected void initView() {
        setContentView(R.layout.activity_regist);
        etPwd = (EditText) findViewById(R.id.et_pwd);
//        etConfirmPwd = (EditText) findViewById(R.id.et_confirm_pwd);
        imgShowPwd = (ImageView) findViewById(R.id.img_pwd_show);
//        imgShowPwdConfirm = (ImageView) findViewById(R.id.img_confirm_pwd_show);
        tvGetCode = (TextView) findViewById(R.id.tv_get_code);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etCode = (EditText) findViewById(R.id.et_code);
        tvRegist = (TextView) findViewById(R.id.tv_regist);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        leftBack = (ImageView) findViewById(R.id.iv_left);
//        imgLogo = (ImageView) findViewById(R.id.img_logo);
        activityRootView = findViewById(R.id.root_layout);
//        tvProtocol = (TextView) findViewById(R.id.tv_protocol);

        leftBack.setOnClickListener(new NativeOnBacklistener(this));
        etPwd.setOnClickListener(this);
        imgShowPwd.setOnClickListener(this);
//        imgShowPwdConfirm.setOnClickListener(this);
        tvGetCode.setOnClickListener(this);
        tvRegist.setOnClickListener(this);
//        tvProtocol.setOnClickListener(this);

        etPwd.addTextChangedListener(new SearchWather(etPwd));
        etPwd.addTextChangedListener(new MaxLengthWatcher(this, 16, etPwd));
//        tvProtocol.setText(Html.fromHtml("<u>"+"服务协议"+"</u>"));
    }


    @Override protected void initData() {
        tvTitle.setText(R.string.regist_);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //控制密码显示
            case R.id.img_pwd_show:
                isPwdShow = PwdUtils.showPassword(isPwdShow, etPwd, imgShowPwd);
                break;
//            //控制密码显示
//            case R.id.img_confirm_pwd_show:
//                isPwdShowConfirm = PwdUtils.showPassword(isPwdShowConfirm, etConfirmPwd, imgShowPwdConfirm);
//                break;
            //获取验证码
            case R.id.tv_get_code:
                getCode();
                break;
            //注册
            case R.id.tv_regist:
                toRegist();
                break;
//            case R.id.tv_protocol:
//                Bundle data = new Bundle();
//                data.putString(ParamKeys.WEBURL, Urls.SERVICE_INFO_URL);
//                ForwardActivityUtils.forwardActivity(RegistActivity.this, WebActivity.class, data, false);
//                break;

        }
    }

    /**
     * 注册
     */
    private void toRegist() {

        mUserPhone = etPhone.getText().toString();
        if(TextUtils.isEmpty(mUserPhone)) {
            ToastUtils.show(this, "请输入手机号码");
            return;
        }

        if(!StringUtil.isMobileNO(mUserPhone)) {
            toastMsg("请输入正确的手机号码");
            return;
        }

        String code = etCode.getText().toString();
        if(TextUtils.isEmpty(code)) {
            ToastUtils.show(this, "请输入验证码");
            return;
        }

        String password = etPwd.getText().toString();
        if(TextUtils.isEmpty(password)) {
            ToastUtils.show(this, "请输入密码");
            return;
        }
        //
        //String pwdConfirm = etConfirmPwd.getText().toString();
        //if(!StringUtil.checkStr(pwdConfirm)) {
        //    ToastUtils.show(this, "请确认您的密码");
        //    return;
        //}
        //if(!password.equals(pwdConfirm)) {
        //    ToastUtils.show(this, "两次密码输入不一致");
        //    return;
        //}

        if(password.length() < 6) {
            toastMsg("请输入6-12位英文或数字");
            return;
        }

        new RegistAysncTask(mUserPhone, code, EncryUtil.encry(password)).execute();
    }

    /**
     * 获取验证码
     */
    private void getCode() {

        String phone = etPhone.getText().toString();
        mUserPhone = etPhone.getText().toString();
        if(!StringUtil.isMobileNO(phone)) {
            Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        tvGetCode.setEnabled(false);
        new CodeAysncTask(phone).execute();
    }

    /**
     * 处理返回验证码
     * @param code
     * @param message
     */
    private void setCodeResult(int code, String message) {

        if(ParamKeys.REGIST_GET_CODE_SUCESS == code) {
            toastMsg("验证码发送成功");
            if (null == mTimer) {
                mTimer = new TimeCount(60000, 1000, tvGetCode, this);
            }
            mTimer.start();
            return;
        } else if(code > 0) {
            toastMsg(TextUtils.isEmpty(message) ? CommonErrorUtils.getErrorMessage(code) : message);
            return;
        } else {
            toastMsg("验证码发送失败,请重新发送");
        }
    }

    /**
     * 处理注册返回数据
     * @param result
     */
    private void handleResult(LoginRegistResult result) {

        if(result == null) {
            setResult(CommonErrorUtils.REGIST_GET_CODE_FAILURE);
        } else {
            int code = result.getCode();
            if (code == ParamKeys.REGIST_SUCESS) {
                PrefsUtils.putValue(RegistActivity.this, ParamKeys.USER_NAME, mUserPhone);
                UserInfo.setUserPhone(mUserPhone);
                PrefsUtils.putValue(RegistActivity.this, ParamKeys.USER_PHONE, UserInfo.getUserPhone());
                updateUserData(result.getAccess_token(), result.getRefresh_token(), result.getUid());

                String intentFrom = getIntent().getStringExtra(ParamKeys.INTENT_FROM);
                if(StringUtil.checkStr(intentFrom) && ParamKeys.INTENT_FROM_JS.equals(intentFrom)) {
                    Intent data = new Intent();
                    data.putExtra(ParamKeys.NET_DATA, mNetData);
                    setResult(Activity.RESULT_OK, data);
                }else{
                    ForwardActivityUtils.forwardActivity(RegistActivity.this, MainActivity.class, false);
                }
                this.finish();
            } else if (code >0 ) {
                String message = result.getMessage();
                toastMsg(TextUtils.isEmpty(message) ? CommonErrorUtils.getErrorMessage(code) : message);
            } else {
                toastMsg("注册失败");
            }
        }
    }

    class CodeAysncTask extends AsyncTask<Void, Void, String> {

        private String mPhone;

        public CodeAysncTask(String phone) {
            mPhone = phone;
            showLoading("");
        }

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, String> codeParams = new HashMap<>();
            codeParams.put("flag", "1");
            codeParams.put("appId", ParamKeys.APPID);
            codeParams.put("unionId", mPhone);
            String requestData;

            requestData = NetUtil.requestData(RegistActivity.this, "POST", "", codeParams, false, Urls.getUrl(Urls.METHOD_GET_CODE_URL));
            return requestData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            hideLoading();
            tvGetCode.setEnabled(true);

            if(TextUtils.isEmpty(s)) {
                setCodeResult(ParamKeys.NET_ERROR_CODE, null);
                return;
            }

            try {
                JSONObject data = new JSONObject(s);
                if(data != null) {
                    int code = data.optInt("code");
                    String message = data.optString("message");
                    setCodeResult(code, message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class RegistAysncTask extends AsyncTask<Void, Integer, LoginRegistResult> {

        private String mPhone;
        private String mCode;
        private String mPwd;


        public RegistAysncTask(String phone, String code, String pwd) {
            mPhone = phone;
            mCode = code;
            mPwd = pwd;
            showLoading("");
        }

        @Override
        protected LoginRegistResult doInBackground(Void... params) {

            HashMap<String, String> registParams = new HashMap<>();
            registParams.put("appId", ParamKeys.APPID);
            registParams.put("captcha", mCode);
            registParams.put("unionId", mUserPhone);
            registParams.put("password", mPwd);
            registParams.put("deviceId", DeviceParams.deviceId);

            mNetData = NetUtil.requestData(RegistActivity.this, "POST", "", registParams, false, Urls.getUrl(Urls.METHON_REGIST_URL));

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
                    loginResult.setUserPhone(mPhone);
                    loginResult.setUserPwd(mPwd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return loginResult;
        }

        @Override
        protected void onPostExecute(LoginRegistResult registResult) {
            super.onPostExecute(registResult);
            hideLoading();
            handleResult(registResult);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(ParamKeys.REGIST_CANCEL);
        this.finish();
    }

    Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onsoftKeyboardUp() {
        super.onsoftKeyboardUp();
        mHandler.post(new Runnable() {
            @Override public void run() {
                imgLogo.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onsoftKeyboardDown() {
        super.onsoftKeyboardDown();
        mHandler.post(new Runnable() {
            @Override public void run() {
                imgLogo.setVisibility(View.VISIBLE);
            }
        });
    }
}















