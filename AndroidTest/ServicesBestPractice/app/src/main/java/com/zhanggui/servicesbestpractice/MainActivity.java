package com.zhanggui.servicesbestpractice;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zhanggui.servicesbestpractice.Services.DownloadServices;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private DownloadServices.DownloadBinder downloadBinder;



    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (DownloadServices.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        Button startButton =(Button) findViewById(R.id.start_download);
        Button pauseButton = (Button) findViewById(R.id.pause_download);
        Button cancelButton = (Button) findViewById(R.id.cancel_downlaod);
        Button[] buttons = new Button[]{startButton,pauseButton,cancelButton};
        for (int i=0;i<buttons.length;i++) {
            Button button = buttons[i];
            button.setOnClickListener(this);
        }
        Intent intent = new Intent(this,DownloadServices.class);
        startService(intent);
        bindService(intent,connection,BIND_AUTO_CREATE);  //绑定服务
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest
                .permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length>0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    public void onClick(View v) {
        if (downloadBinder == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.start_download:
                startDownload();
                break;
            case R.id.pause_download:
                pauseDownload();
                break;
            case R.id.cancel_downlaod:
                cancelDownload();
                break;
            default:
                break;
        }

    }

    public void startDownload() {
        String url = "http://111.202.99.12/sqdd.myapp.com/myapp/qqteam/Androidlite/qqlite_3.5.0.660_android_r108360_GuanWang_537047121_release_10000484.apk";
        downloadBinder.startDownload(url);
    }
    public void pauseDownload() {
        downloadBinder.pauseDownload();

    }
    public void cancelDownload() {
        downloadBinder.cancelDownload();
    }
}
