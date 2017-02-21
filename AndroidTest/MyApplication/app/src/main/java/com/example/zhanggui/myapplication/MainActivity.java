package com.example.zhanggui.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("sdfsdf");
        list.add("ccc");
        list.add(1);
        for (int i = 0; i < list.size(); i++) {
            String name = list.get(i); // 2
            System.out.println("name:" + name);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
