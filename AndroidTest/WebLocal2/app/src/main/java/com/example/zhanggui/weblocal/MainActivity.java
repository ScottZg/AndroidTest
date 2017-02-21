package com.example.zhanggui.weblocal;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView webView = new WebView(this);
        webView.setWebViewClient(new CustomerClient());
        webView.setWebChromeClient(new ChromeCilent());
        webView.setBackgroundColor(Color.YELLOW);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
       try {
           webView.loadUrl("file:///android_asset/testH5/test.html");
       }catch (Exception e) {
           e.printStackTrace();
       }
        setContentView(webView);

        Log.d("1111","11111");
        Log.i("2222","11111");
        Log.v("3333","11111");
        Log.e("4444","11111");

    }

    class CustomerClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return true;
        }
    }

    class ChromeCilent extends WebChromeClient{
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }
    }
}
