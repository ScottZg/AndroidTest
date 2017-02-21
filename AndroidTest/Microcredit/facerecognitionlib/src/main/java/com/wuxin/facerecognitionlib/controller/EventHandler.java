package com.wuxin.facerecognitionlib.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.wuxin.facerecognitionlib.activity.VideoDemo;

/**
 * Created by wuxin on 16/9/8.
 */
public class EventHandler extends Handler {

    public static final int ENTER_FRAME = 101;
    public static final int OUT_FRAME = 102;
    public static final int GET_PHOTO_SUCESS = 103;
    public static final int TIME_COUNT = 104;

    private int mTimeCount = 3;
    private TextView mTvHint;
    private VideoDemo mVideoDemo;
    private boolean isEnter;

    public EventHandler(IHandlerBridge bridge) {
        super();
        this.mVideoDemo = bridge.getActivity();
        this.mTvHint = mVideoDemo.mTvHint;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case ENTER_FRAME :
                if(!isEnter) {
                    isEnter = true;
                    this.sendEmptyMessage(TIME_COUNT);
                }
                break;
            case OUT_FRAME :
                isEnter = false;
                mTimeCount = 3;
                mTvHint.setText("出去了");
                break;
            case GET_PHOTO_SUCESS:
                mTvHint.setText("眨眼通过");
                Bitmap bitmap = (Bitmap) msg.obj;
                mVideoDemo.mPreviewSurface.setVisibility(View.INVISIBLE);
                mVideoDemo.mFaceSurface.setVisibility(View.INVISIBLE);
                mVideoDemo.mIvPhoto.setVisibility(View.VISIBLE);
                mVideoDemo.mIvPhoto.setImageBitmap(bitmap);
                break;
            case TIME_COUNT:
                if(isEnter) {
                    if (mTimeCount == 0) {
                        mVideoDemo.takePhoto(true);
                        this.removeMessages(TIME_COUNT);
                    } else {
                        mTvHint.setText(mTimeCount-- + "秒后拍照");
                        this.sendEmptyMessageDelayed(TIME_COUNT, 1000);
                    }
                }
        }
    }
}
