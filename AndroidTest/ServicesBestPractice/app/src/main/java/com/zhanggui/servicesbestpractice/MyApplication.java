package com.zhanggui.servicesbestpractice;

import android.app.Application;
import android.content.Context;

/**
 * Created by zhanggui on 2017/2/23.
 */

public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        context = getApplicationContext();
    }
    public  static Context getContext() {
        return context;
    }

}
