package com.zhanggui.sharedpreferencestest;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SharePreferenceMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_preference_main_layout);

        Button button = (Button) findViewById(R.id.save_data);

        Button getDataButton = (Button) findViewById(R.id.get_data);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                editor.putString("name","Tom");
                editor.putString("age","28");
                editor.putBoolean("married",false);
                editor.apply();
            }
        });
        getDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
                String name = pref.getString("name","");
                String age = pref.getString("age","");
                boolean married = pref.getBoolean("married",false);
                Toast.makeText(SharePreferenceMainActivity.this,name+age+married,Toast.LENGTH_LONG)
                        .show();

            }
        });
    }
}
