package com.shuqu.microcredit.UI;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shuqu.microcredit.Content.ParamKeys;
import com.shuqu.microcredit.Content.Urls;
import com.shuqu.microcredit.Encry.EncryUtil;
import com.shuqu.microcredit.Model.LoginRegistResult;
import com.shuqu.microcredit.Model.UserInfo;
import com.shuqu.microcredit.NetWork.NetUtil;
import com.shuqu.microcredit.R;
import com.shuqu.microcredit.Utils.CommonErrorUtils;
import com.shuqu.microcredit.Utils.MaxLengthWatcher;
import com.shuqu.microcredit.Utils.NativeOnBacklistener;
import com.shuqu.microcredit.Utils.PwdUtils;
import com.shuqu.microcredit.Utils.StringUtil;
import com.shuqu.microcredit.Utils.TimeCount;
import com.shuqu.microcredit.Utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Class Info ：change password
 * Created by Lyndon on 16/5/31.
 */
public class ChangePwdActivity extends BaseActivity implements View.OnClickListener {

    EditText etPhone;
    EditText etCode;
    EditText newPwd;
    EditText etNewPwdConfirm;
    TextView tvGetCode;
    //    TextView tvProtocol;
    TextView tvTitle;
    ImageView imgShowPwd;
    ImageView imgShowPwdConfrim;
    ImageView leftBack;
    TextView btnOk;
    //    CheckBox cbAgree;
    //    FrameLayout flCheckBox;

    private boolean isPwdShow;
    private boolean isPwdShowConfirm;
    private TimeCount mTimer; //验证码倒计时
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override protected void initView() {
        setContentView(R.layout.activity_change_pwd);

        etPhone = (EditText) findViewById(R.id.et_phone);
        etCode = (EditText) findViewById(R.id.et_code);
        newPwd = (EditText) findViewById(R.id.et_pwd);
                etNewPwdConfirm = (EditText) findViewById(R.id.et_pwd_confirm);
        tvGetCode = (TextView) findViewById(R.id.tv_get_code);
        imgShowPwd = (ImageView) findViewById(R.id.img_pwd_show);
                imgShowPwdConfrim = (ImageView) findViewById(R.id.img_pwd_show_confirm);
        btnOk = (TextView) findViewById(R.id.tv_finish);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        leftBack = (ImageView) findViewById(R.id.iv_left);

        etPhone.setOnClickListener(this);
        etCode.setOnClickListener(this);
        newPwd.setOnClickListener(this);
        etNewPwdConfirm.setOnClickListener(this);
        tvGetCode.setOnClickListener(this);
        imgShowPwd.setOnClickListener(this);
        imgShowPwdConfrim.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        leftBack.setOnClickListener(new NativeOnBacklistener(this));
        newPwd.addTextChangedListener(new MaxLengthWatcher(this, 12, newPwd));
    }

    @Override protected void initData() {

        tvTitle.setText(R.string.motify_title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_pwd_show:
                isPwdShow = PwdUtils.showPassword(isPwdShow, newPwd, imgShowPwd);
                break;
            case R.id.img_pwd_show_confirm:
                isPwdShowConfirm = PwdUtils.showPassword(isPwdShowConfirm, etNewPwdConfirm, imgShowPwdConfrim);
                break;
            //获取验证码
            case R.id.tv_get_code:
                getCode();
                break;
            case R.id.tv_finish:
                toOk();
                break;
        }
    }

    /**
     * 点击确定按钮
     */
    private void toOk() {

        String phone = etPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.show(this, "请输入手机号码");
            return;
        }

        if (!StringUtil.isMobileNO(phone)) {
            toastMsg("请输入正确的手机号码");
            return;
        }

        String code = etCode.getText().toString();
        if (TextUtils.isEmpty(code)) {
            ToastUtils.show(this, "请输入验证码");
            return;
        }

        String password = newPwd.getText().toString();
        if (TextUtils.isEmpty(password)) {
            ToastUtils.show(this, "请输入密码");
            return;
        }

        String pwdConfirm = etNewPwdConfirm.getText().toString();
        if (!StringUtil.checkStr(pwdConfirm)) {
            ToastUtils.show(this, "请确认您的密码");
            return;
        }
        if (!password.equals(pwdConfirm)) {
            ToastUtils.show(this, "两次密码输入不一致");
            return;
        }

        if (password.length() < 6) {
            toastMsg("请输入6-12位英文或数字");
            return;
        }

        //        if(!isProtocolCheck()) {
        //            toastMsg("您还没有同意服务协议哦");
        //            return;
        //        }

        new ForgetPwdAysncTask(phone, code, password).execute();
    }


    /**
     * 获取验证码
     */
    private void getCode() {

        String phone = etPhone.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!StringUtil.isMobileNO(phone)) {
            toastMsg("请输入正确的手机号码");
            return;
        }

        if (null == mTimer) {
            mTimer = new TimeCount(60000, 1000, tvGetCode, this);
        }
        mTimer.start();

        tvGetCode.setEnabled(false);
        new CodeAysncTask(phone).execute();
    }

    /**
     * 处理忘记密码返回数据
     * @param result
     */
    private void handleResult(LoginRegistResult result) {

        if(result == null) {
            toastMsg("修改密码失败");
        } else {
            int code = result.getCode();
            if (code == ParamKeys.FORGET_PWD_SUCESS) {
                UserInfo.setAccess_token(result.getAccess_token());
                UserInfo.setRefresh_token(result.getRefresh_token());
                UserInfo.setUid(result.getUid());
                updateUserData(UserInfo.getAccess_token(), UserInfo.getRefresh_token(), UserInfo.getUid());
                toastMsg("修改密码成功");
                this.finish();
            } else if (code > 0) {
                String message = result.getMessage();
                toastMsg(StringUtil.checkStr(message) ? message : CommonErrorUtils
                        .getErrorMessage(code));
                return;
            }  else {
                toastMsg("修改密码失败");
            }
        }
    }

    class CodeAysncTask extends AsyncTask<Void, Void, String> {

        private String mPhone;


        public CodeAysncTask(String phone) {
            showLoading("");
            mPhone = phone;
        }


        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, String> codeParams = new HashMap<>();
            codeParams.put("flag", "2");
            codeParams.put("appId", ParamKeys.APPID);
            codeParams.put("unionId", mPhone);
            String requestData = "";
            requestData = NetUtil.requestData(ChangePwdActivity.this, "POST", "", codeParams, false, Urls.getUrl(Urls.METHOD_GET_CODE_URL));
            return requestData;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            hideLoading();
            tvGetCode.setEnabled(true);
            if (TextUtils.isEmpty(s)) {
                setCodeResult(ParamKeys.NET_ERROR_CODE, null);
                return;
            }

            try {
                JSONObject data = new JSONObject(s);
                if (data != null) {
                    int code = data.optInt("code");
                    String message = data.optString("message");
                    setCodeResult(code, message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理返回验证码
     */
    private void setCodeResult(int code, String message) {
        if (ParamKeys.REGIST_GET_CODE_SUCESS == code) {
            toastMsg("验证码发送成功");
            if (null == mTimer) {
                mTimer = new TimeCount(60000, 1000, tvGetCode, this);
            }
            mTimer.start();
            return;
        }
        else if (code > 0) {
            toastMsg(TextUtils.isEmpty(message) ? CommonErrorUtils.getErrorMessage(code) : message);
            return;
        }
        else {
            toastMsg("验证码发送失败,请重新发送");
        }
    }

    class ForgetPwdAysncTask extends AsyncTask<Void, Integer, LoginRegistResult> {

        private String mPhone;
        private String mCode;
        private String mPwd;


        public ForgetPwdAysncTask(String phone, String code, String pwd) {
            mPhone = phone;
            mCode = code;
            mPwd = pwd;
            showLoading("");
        }


        @Override
        protected LoginRegistResult doInBackground(Void... params) {

            HashMap<String, String> forgetParams = new HashMap<>();
            forgetParams.put("appId", ParamKeys.APPID);
            forgetParams.put("captcha", mCode);
            forgetParams.put("unionId", mPhone);
            forgetParams.put("password", EncryUtil.encry(mPwd));

            String requestData = "";
            requestData = NetUtil.requestData(ChangePwdActivity.this, "POST", "", forgetParams, Urls.getUrl(Urls.METHOD_FORGET_PWD));

            if (StringUtil.checkStr(requestData)) {
                try {
                    LoginRegistResult loginResult = new LoginRegistResult();
                    JSONObject jsonObject = new JSONObject(requestData);
                    loginResult.setAccess_token(jsonObject.optString("accessToken"));
                    loginResult.setCode(jsonObject.optInt("code"));
                    loginResult.setMessage(jsonObject.optString("message"));
                    loginResult.setRefresh_token(jsonObject.optString("refreshToken"));
                    loginResult.setUid(jsonObject.optString("uid"));
                    return loginResult;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(LoginRegistResult registResult) {
            super.onPostExecute(registResult);
            hideLoading();
            handleResult(registResult);
        }
    }

}
