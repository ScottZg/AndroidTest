package com.wuxin.facerecognitionlib;

import android.content.Context;

import com.iflytek.cloud.SpeechUtility;

/**
 * Created by wuxin on 16/8/24.
 */
public class BrFaceRecognition {

    public static void init(Context context) {
        SpeechUtility.createUtility(context, "appid=" + 5787200d);
    }
}
