package com.zhanggui.servicetest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Array;

public class ServiceMainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        Button startServiceButton = (Button) findViewById(R.id.start_service_button);
        Button stopServiceButton = (Button) findViewById(R.id.close_service_button);

        Button[] buttons = new Button[]{startServiceButton,stopServiceButton};
        for (int i=0;i<buttons.length;i++) {
            Button bu = buttons[i];
            bu.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_service_button:
                doStartService();
                break;
            case R.id.close_service_button:
               doCloseService();
                break;
            default:
        }
    }
    private void doStartService() {
b
        Intent startService = new Intent(this,MyService.class);
        startService(startService);
    }
    private void doCloseService() {
        Intent stopIntent = new Intent(this,MyService.class);
        stopService(stopIntent);
    }
}
