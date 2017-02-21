package com.shuqu.microcredit.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Class Info ：
 * Created by Lyndon on 16/7/7.
 */
public class CustomViewPager extends ViewPager {

    private boolean mSrollble = true;
    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.mSrollble && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.mSrollble && super.onInterceptTouchEvent(event);
    }


    public void setScrollble(boolean scrollble) {
        mSrollble = scrollble;
    }
}
