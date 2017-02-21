package com.zhanggui.broadcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by zhanggui on 2017/2/21.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "----onee");
//        Toast.makeText(context,"received in MyBroadCastReceiver",Toast.LENGTH_SHORT).show();
    }
}
