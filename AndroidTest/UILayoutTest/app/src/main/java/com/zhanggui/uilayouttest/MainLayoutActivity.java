package com.zhanggui.uilayouttest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainLayoutActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        Button frame_button = (Button)findViewById(R.id.frame_button);
        frame_button.setOnClickListener(this);

        Button liner_button = (Button)findViewById(R.id.liner_button);
        liner_button.setOnClickListener(this);

        Button relative_button = (Button)findViewById(R.id.relative_button);
        relative_button.setOnClickListener(this);

        Button percent_button = (Button)findViewById(R.id.percent_button);
        percent_button.setOnClickListener(this);

        Button custom_button = (Button) findViewById(R.id.cus_button);
        custom_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.liner_button:
                showLinerLayoutActivity();
                break;
            case R.id.frame_button:
                showFrameLayoutActivity();
                break;
            case R.id.relative_button:
                showRelativeLayoutActivity();
                break;
            case R.id.percent_button:
                showPercentLayoutActivity();
                break;
            case R.id.cus_button:
                showCustomViewActivity();
                break;
            default:
                break;
        }
    }

    private  void  showCustomViewActivity() {
        Intent intent = new Intent(this,CustomActivity.class);
        startActivity(intent);
    }
    private  void  showPercentLayoutActivity() {
        Intent intent = new Intent(this,PercentActivity.class);
        startActivity(intent);
    }
    private  void  showRelativeLayoutActivity() {
        Intent intent = new Intent(this,RelativeActivity.class);
        startActivity(intent);
    }
    private  void  showFrameLayoutActivity() {
        Intent intent = new Intent(this,FrameActivity.class);
        startActivity(intent);

    }
    private  void  showLinerLayoutActivity() {
        Intent intent = new Intent(this,LayoutActivity.class);
        startActivity(intent);

    }
}
