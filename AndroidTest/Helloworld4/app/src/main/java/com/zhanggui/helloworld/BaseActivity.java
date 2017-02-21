package com.zhanggui.helloworld;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

/**
 * Created by zhanggui on 2017/2/16.
 */

public class BaseActivity extends AppCompatActivity {


    private void initFontScale() {
        Configuration configuration = getResources().getConfiguration();
        configuration.fontScale = (float) 1;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;

        getBaseContext().getResources().updateConfiguration(configuration,metrics);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFontScale();
    }
}
