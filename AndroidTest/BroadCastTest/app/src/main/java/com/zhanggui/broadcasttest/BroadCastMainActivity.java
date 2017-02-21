package com.zhanggui.broadcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class BroadCastMainActivity extends AppCompatActivity {

//    private IntentFilter intentFilter;
//    private NetworkChangeReceiver networkChangeReceiver;


    private IntentFilter intentFilter;

    private LocalReceiver localReceiver;
    private LocalBroadcastManager localBroadcastManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        intentFilter = new IntentFilter();

        intentFilter.addAction("com.zhanggui.broadcasttest.LOCAL_BROADCAST");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver,intentFilter);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.zhanggui.broadcasttest.LOCAL_BROADCAST");
                localBroadcastManager.sendBroadcast(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(localReceiver);
//        unregisterReceiver(networkChangeReceiver);
    }

    class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"receive local broadcast",Toast.LENGTH_SHORT).show();
        }
    }
//    class NetworkChangeReceiver extends BroadcastReceiver{
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            ConnectivityManager connectivityManager =  (ConnectivityManager)
//                    getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//            if (networkInfo != null && networkInfo.isAvailable()) {
//                Toast.makeText(context,"network available",Toast.LENGTH_SHORT).show();
//            }else  {
//                Toast.makeText(context,"network unavailable",Toast.LENGTH_SHORT).show();
//            }
//
//        }
//    }
}
