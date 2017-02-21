package com.zhanggui.uiwidgettest;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class UWTMainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uwtmain_layout);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                showDialog();
//                EditText text = (EditText) findViewById(R.id.edit_text);
//                Toast.makeText(this,text.getText().toString(),Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
    public void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(UWTMainActivity.this);
        dialog.setTitle("温馨提示");
        dialog.setMessage("欢迎使用UWT");
        dialog.setCancelable(false);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();

    }
}
