package com.zhanggui.uilayouttest.widget;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zhanggui.uilayouttest.R;

/**
 * Created by zhanggui on 2017/2/20.
 */

public class TitleLayout extends LinearLayout {
    public TitleLayout(Context context ,AttributeSet attrs) {
        super(context,attrs);

        LayoutInflater.from(context).inflate(R.layout.title,this);

        Button backButton = (Button) findViewById(R.id.back_button);
        Button editButton = (Button) findViewById(R.id.edit_button);
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)getContext()).finish();
            }
        });
        editButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"You click edit button",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
