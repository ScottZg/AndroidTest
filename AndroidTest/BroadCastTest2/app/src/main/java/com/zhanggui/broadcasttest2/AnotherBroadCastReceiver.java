package com.zhanggui.broadcasttest2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by zhanggui on 2017/2/2®1.
 */

public class AnotherBroadCastReceiver extends BroadcastReceiver {
    private static final String TAG = "AnotherBroadCastReceive";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "---another");
//        Toast.makeText(context,"received in anotherbroadcasetreceive",Toast.LENGTH_LONG).show();
    }
}
