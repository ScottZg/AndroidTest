package com.zhanggui.helloworld;


import android.os.Build;
import android.os.Bundle;
import android.util.Log;


public class HelloWorldActivity extends BaseActivity {

    private static final String TAG = "HelloWorldActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello_world_layout);
        Log.i(TAG, "onCreate: " + R.string.app_name);
    }


}
