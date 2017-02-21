package com.zhanggui.uilayouttest;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CustomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_layout);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.hide();
        }
    }
}
