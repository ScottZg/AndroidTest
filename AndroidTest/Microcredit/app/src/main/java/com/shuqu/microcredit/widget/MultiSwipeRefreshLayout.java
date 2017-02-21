package com.shuqu.microcredit.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Class Info ：
 * Created by Lyndon on 16/6/7.
 */
public class MultiSwipeRefreshLayout extends SwipeRefreshLayout {
    private CanChildScrollUpCallback mCanChildScrollUpCallback;

    public MultiSwipeRefreshLayout(Context context) {
        super(context);
    }

    public MultiSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCanChildScrollUpCallback(CanChildScrollUpCallback canChildScrollUpCallback) {
        mCanChildScrollUpCallback = canChildScrollUpCallback;
    }

    public interface CanChildScrollUpCallback {
        boolean canSwipeRefreshChildScrollUp();
    }

    @Override
    public boolean canChildScrollUp() {
        if (mCanChildScrollUpCallback != null) {
            return mCanChildScrollUpCallback.canSwipeRefreshChildScrollUp();
        }
        return super.canChildScrollUp();
    }
}
