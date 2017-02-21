package com.shuqu.microcredit.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import com.shuqu.microcredit.Content.ParamKeys;
import com.shuqu.microcredit.Interface.UserApi;
import com.shuqu.microcredit.R;
import com.shuqu.microcredit.UI.fragment.WebFragment;

/**
 * Class Info ：
 * Created by Lyndon on 16/6/1.
 */
public class WebActivity extends BaseActivity {

    private WebFragment mWebFragment;
    //tag of current activity
    private String mTag = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override protected void initView(){
        setContentView(R.layout.activity_web);
    }


    @Override protected String currentClassName() {
        return super.currentClassName() + "-" +mTag;
    }


    @Override protected void initData(){
        mWebFragment = (WebFragment) getSupportFragmentManager().findFragmentByTag("WebFragment");
        Intent intent = getIntent();
        if(intent != null){
            String webUrl = intent.getStringExtra(ParamKeys.WEBURL);
            if(!TextUtils.isEmpty(webUrl) && mWebFragment != null){
                try {
                    int startIndex = webUrl.indexOf("/src", 0);
                    int endIndex = webUrl.lastIndexOf("/");
                    if(endIndex > startIndex && endIndex > 0) {
                        mTag = webUrl.substring(startIndex+1, endIndex).replace("/",  ".");
                    }
                }catch (Exception e){}
                mWebFragment.loadData(webUrl);
                mWebFragment.showLoading();
            }
        }
        mWebFragment.setLeftButton(R.mipmap.left_back_icon, new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        registLoginCallback();
    }

    @Override public void onBackPressed() {
        if(mWebFragment != null && !mWebFragment.webGoBack()){
            WebActivity.this.finish();
        }
    }

    @Override
    public void onUserLogin() {
        mWebFragment.startFresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserApi.unRegistLoginCallback(this);
    }
}
