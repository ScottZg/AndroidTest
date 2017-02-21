package com.shuqu.microcredit.Utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by wuxin on 2015/12/4.
 */
public class TimeCount extends CountDownTimer {

    private TextView mTextView;
    private Context mContext;

    public TimeCount(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public TimeCount(long millisInFuture, long countDownInterval, TextView textView, Context context){

        this(millisInFuture, countDownInterval);
        this.mTextView = textView;
        this.mContext = context;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mTextView.setEnabled(false);
//        mTextView.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        mTextView.setText((millisUntilFinished / 1000) + "s");
    }

    @Override
    public void onFinish() {

//        mTextView.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        mTextView.setText("重新获取");
        mTextView.setEnabled(true);
    }
}
