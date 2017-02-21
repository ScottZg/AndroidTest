package com.wuxin.facerecognition;

/**
 * Created by wuxin on 16/9/9.
 */
public class YUV2RGB {
    static {
        System.loadLibrary("faceRecognition");
    }

    public native void YUVtoRBG(int[] rgb, byte[] yuv, int width, int height);
}
