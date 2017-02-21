package com.shuqu.microcredit.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Class Info ：≤
 * Created by Lyndon on 16/6/7.
 */
public class CustomWebView extends WebView {

    private WebScrollTopCallBack mWebScrollTop;
    public CustomWebView(Context context) {
        super(context);
    }


    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
    }


    @Override protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(mWebScrollTop != null){
            mWebScrollTop.onScrollTop(t == 0);
        }
    }

    public void setWebScrollTopListener(WebScrollTopCallBack callBack){
        mWebScrollTop = callBack;
    }

    public interface WebScrollTopCallBack{
        void onScrollTop(boolean top);
    }
}
