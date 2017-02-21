package com.wuxin.facerecognitionlib.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.os.Message;
import android.os.Process;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.FaceDetector;
import com.iflytek.cloud.util.Accelerometer;
import com.wuxin.facerecognitionlib.R;
import com.wuxin.facerecognitionlib.controller.EventHandler;
import com.wuxin.facerecognitionlib.controller.IHandlerBridge;
import com.wuxin.facerecognitionlib.entity.FaceRect;
import com.wuxin.facerecognitionlib.entity.ParseResult;
import com.wuxin.facerecognitionlib.entity.SizeSettiong;
import com.wuxin.facerecognitionlib.utils.FaceUtil;

import java.io.IOException;

import in.manishpathak.videodemo.OverlayView;

/**
 * Created by wuxin on 16/8/4.
 */
public class FaceRecognitionAc extends Activity implements IHandlerBridge, View.OnClickListener {
    private final static String TAG = FaceRecognitionAc.class.getSimpleName();
    public SurfaceView mPreviewSurface;
    public SurfaceView mFaceSurface;
    public FrameLayout mFlBg;
    public TextView mTvHint;
    public TextView mTvConfirm;
    public TextView mTvReset;
    public ImageView mIvPhoto;

    private EventHandler mHandler;
    private Toast mToast;
    private boolean isTakePhoto = false;

    private Camera mCamera;
    private int mCameraId = CameraInfo.CAMERA_FACING_FRONT;
    // Camera nv21格式预览帧的尺寸，默认设置640*480
    private int PREVIEW_WIDTH = 640;
    private int PREVIEW_HEIGHT = 480;
    // 预览帧数据存储数组和缓存数组
    private byte[] nv21;
    private byte[] buffer;
    private int[] rgba;
    // 缩放矩阵
    private Matrix mScaleMatrix = new Matrix();
    // 加速度感应器，用于获取手机的朝向
    private Accelerometer mAcc;
    // FaceDetector对象，集成了离线人脸识别：人脸检测、视频流检测功能
    private FaceDetector mFaceDetector;
    public boolean mStopTrack;
    private long mLastClickTime;
    private int isAlign = 0;

    private Paint shitiPaint;
    OverlayView overlayView;
    public int mRotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_demo);
        initUI();

        overlayView = new OverlayView();
        nv21 = new byte[PREVIEW_WIDTH * PREVIEW_HEIGHT * 2];
        buffer = new byte[PREVIEW_WIDTH * PREVIEW_HEIGHT * 2];
        rgba = new int[PREVIEW_WIDTH * PREVIEW_HEIGHT +1];
        mAcc = new Accelerometer(FaceRecognitionAc.this);
        mFaceDetector = FaceDetector.createDetector(FaceRecognitionAc.this, null);
    }

    private SurfaceHolder.Callback mPreviewCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            closeCamera();
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            openCamera();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            mScaleMatrix.setScale(width / (float) PREVIEW_HEIGHT, height / (float) PREVIEW_WIDTH);
        }
    };

    private void setSurfaceSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int width = metrics.widthPixels;
        int height = (int) (width * PREVIEW_WIDTH / (float) PREVIEW_HEIGHT);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        mPreviewSurface.setLayoutParams(params);
        mFaceSurface.setLayoutParams(params);
        mFlBg.setLayoutParams(params);
    }

    @SuppressLint("ShowToast")
    @SuppressWarnings("deprecation")
    private void initUI() {
        mPreviewSurface = (SurfaceView) findViewById(R.id.sfv_preview);
        mFaceSurface = (SurfaceView) findViewById(R.id.sfv_face);
        mFlBg = (FrameLayout) findViewById(R.id.fl_bg);
        mTvHint = (TextView) findViewById(R.id.tv_hint);
        mTvConfirm = (TextView) findViewById(R.id.tv_confirm);
        mTvReset = (TextView) findViewById(R.id.tv_reset);
        mIvPhoto = (ImageView) findViewById(R.id.iv_photo);

        mPreviewSurface.getHolder().addCallback(mPreviewCallback);
        mPreviewSurface.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mFaceSurface.setZOrderOnTop(true);
        mFaceSurface.getHolder().setFormat(PixelFormat.TRANSLUCENT);

        shitiPaint = new Paint();
        shitiPaint.setAntiAlias(true);
        shitiPaint.setColor(Color.RED);
        shitiPaint.setStyle(Paint.Style.STROKE);
        shitiPaint.setStrokeWidth(10);

        mFaceSurface.setOnClickListener(this);
        mTvReset.setOnClickListener(this);

        mHandler = new EventHandler(this);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        setSurfaceSize();
    }

    @SuppressWarnings("deprecation")
    public void openCamera() {
        if (null != mCamera) {
            return;
        }
        if (!checkCameraPermission()) {
            showTip("摄像头权限未打开，请打开后再试");
            mStopTrack = true;
            return;
        }
        // 只有一个摄相头，打开后置
        if (Camera.getNumberOfCameras() == 1) {
            mCameraId = CameraInfo.CAMERA_FACING_BACK;
        }

        try {
            mCamera = Camera.open(mCameraId);
            if (CameraInfo.CAMERA_FACING_FRONT == mCameraId) {
                showTip("前置摄像头已开启，点击可切换");
            } else {
                showTip("后置摄像头已开启，点击可切换");
            }
        } catch (Exception e) {
            e.printStackTrace();
            closeCamera();
            return;
        }

        Parameters params = mCamera.getParameters();
        params.setPreviewFormat(ImageFormat.NV21);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        params.setPreviewSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);
        mCamera.setParameters(params);

        // 设置显示的偏转角度
        mRotate = FaceUtil.getCameraDisplayOrientation(FaceRecognitionAc.this, mCameraId, mCamera);
        mCamera.setDisplayOrientation(mRotate);
        mCamera.setPreviewCallback(new PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                if(isTakePhoto) {
                    overlayView.YUVtoRBG(rgba, data, PREVIEW_WIDTH, PREVIEW_HEIGHT);
                    takePhoto();
                }
                System.arraycopy(data, 0, nv21, 0, data.length);
            }
        });
        try {
            mCamera.setPreviewDisplay(mPreviewSurface.getHolder());
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeCamera() {
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private boolean checkCameraPermission() {
        int status = checkPermission(Manifest.permission.CAMERA, Process.myPid(), Process.myUid());
        if (PackageManager.PERMISSION_GRANTED == status) {
            return true;
        }

        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        openFace();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeFace();
    }

    public void closeFace() {
        closeCamera();
        if (null != mAcc) {
            mAcc.stop();
        }
        mStopTrack = true;
    }

    public void openFace() {
        if (null != mAcc) {
            mAcc.start();
        }

        mStopTrack = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!mStopTrack) {
                    openCamera();
                    if (null == nv21) {
                        continue;
                    }
                    synchronized (nv21) {
                        System.arraycopy(nv21, 0, buffer, 0, nv21.length);
                    }

                    // 获取手机朝向，返回值0,1,2,3分别表示0,90,180和270度
                    int direction = Accelerometer.getDirection();
                    boolean frontCamera = (CameraInfo.CAMERA_FACING_FRONT == mCameraId);
                    // 前置摄像头预览显示的是镜像，需要将手机朝向换算成摄相头视角下的朝向。
                    // 转换公式：a' = (360 - a)%360，a为人眼视角下的朝向（单位：角度）
                    if (frontCamera) {
                        // SDK中使用0,1,2,3,4分别表示0,90,180,270和360度
                        direction = (4 - direction)%4;
                    }

                    if(mFaceDetector == null) {
                        /**
                         * 离线视频流检测功能需要单独下载支持离线人脸的SDK
                         * 请开发者前往语音云官网下载对应SDK
                         */
                        showTip("本SDK不支持离线视频流检测");
                        break;
                    }

                    String result = mFaceDetector.trackNV21(buffer, PREVIEW_WIDTH, PREVIEW_HEIGHT, isAlign, direction);

                    FaceRect[] faces = ParseResult.parseResult(result);
                    Canvas canvas = mFaceSurface.getHolder().lockCanvas();

                    if (null == canvas) {
                        continue;
                    }

                    canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                    canvas.setMatrix(mScaleMatrix);
                    canvas.drawRect(SizeSettiong.faceRect, shitiPaint);

                    if( faces.length <=0 ) {
                        mFaceSurface.getHolder().unlockCanvasAndPost(canvas);
                        continue;
                    }

                    if (null != faces && frontCamera == (CameraInfo.CAMERA_FACING_FRONT == mCameraId)) {
                        for (FaceRect face: faces) {
                            face.bound = FaceUtil.RotateDeg90(face.bound, PREVIEW_WIDTH, PREVIEW_HEIGHT);
                            face.leftYan = FaceUtil.RotateDeg90(face.leftYan, PREVIEW_WIDTH, PREVIEW_HEIGHT);
                            face.rightYan = FaceUtil.RotateDeg90(face.rightYan, PREVIEW_WIDTH, PREVIEW_HEIGHT);
                            if (face.point != null) {
                                for (int i = 0; i < face.point.length; i++) {
                                    face.point[i] = FaceUtil.RotateDeg90(face.point[i], PREVIEW_WIDTH, PREVIEW_HEIGHT);
                                }
                            }
                            FaceUtil.drawFaceRect(mHandler, canvas, face, PREVIEW_WIDTH, PREVIEW_HEIGHT,
                                    frontCamera);
                        }
                    } else {
                        Log.d(TAG, "faces:0");
                    }

                    mFaceSurface.getHolder().unlockCanvasAndPost(canvas);
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFaceDetector.destroy();
    }

    public void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    private void takePhoto() {
        closeFace();
        takePhoto(false);
        Bitmap bitmap = Bitmap.createBitmap(rgba, PREVIEW_WIDTH, PREVIEW_HEIGHT, Bitmap.Config.ARGB_8888);
        Bitmap rotateBmp = FaceUtil.rotateBmp(-mRotate, bitmap);

        Message msg = Message.obtain(mHandler);
        msg.what = EventHandler.GET_PHOTO_SUCESS;
        msg.obj = rotateBmp;
        mHandler.sendMessage(msg);
    }

    public void takePhoto(boolean b) {
        this.isTakePhoto = b;
    }

    @Override
    public FaceRecognitionAc getActivity() {
        return this;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_reset) {
            mPreviewSurface.setVisibility(View.VISIBLE);
            mFaceSurface.setVisibility(View.VISIBLE);
            mIvPhoto.setVisibility(View.GONE);

            openFace();
        } else if (id == R.id.sfv_face) {
            if(mCamera != null) {
                mCamera.autoFocus(null);
            }
        }
    }
}

