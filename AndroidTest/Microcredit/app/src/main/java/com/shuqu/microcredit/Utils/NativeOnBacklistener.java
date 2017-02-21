package com.shuqu.microcredit.Utils;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;

/**
 * Created by wuxin on 16/6/7.
 */
public class NativeOnBacklistener implements View.OnClickListener {

    private Activity mActivity;

    public NativeOnBacklistener(Object obj){
        if(obj instanceof Activity) {
            mActivity = (Activity) obj;
        } else if (obj instanceof Fragment) {
            mActivity = ((Fragment) obj).getActivity();
        } else if (obj instanceof android.support.v4.app.Fragment) {
            mActivity = ((android.support.v4.app.Fragment) obj).getActivity();
        }
    }

    @Override
    public void onClick(View v) {
        mActivity.onBackPressed();
    }
}
