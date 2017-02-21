package com.shuqu.microcredit.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.lyndon.easycamera.DefaultCallback;
import com.lyndon.easycamera.EasyImage;
import com.shuqu.microcredit.Content.ParamKeys;
import com.shuqu.microcredit.Interface.IUploadImage;
import com.shuqu.microcredit.NetWork.UploadImgService;
import com.shuqu.microcredit.R;
import com.shuqu.microcredit.Utils.StringUtil;
import com.shuqu.microcredit.filter.fragment.SingleSelectorFragment;
import com.shuqu.microcredit.filter.interfac.SelectorCallback;
import java.io.File;
import org.json.JSONException;
import org.json.JSONObject;

public class UploadBankCardActivity extends BaseActivity implements View.OnClickListener, IUploadImage{

    private ImageView mIvCardFront;
    private Button mBtnSubmit;
    TextView tvTitle;
    ImageView leftBack;

    private static int S_TYPE = 1000;

    private int type_front = ++S_TYPE;

    private static final String CHOOSE_FROM_CAPTURE = "拍照";
    private static final String CHOOSE_FROM_GALLERY = "相册";

    private String BANKCARD = "bankCard";

    ArrayMap<String, File> mFileMapDatas = new ArrayMap<String, File>();
    String mResult = "";

    @Override protected void initView() {
        setContentView(R.layout.activity_card_upload);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        leftBack = (ImageView) findViewById(R.id.iv_left);

        mIvCardFront = (ImageView) findViewById(R.id.iv_front);
        mBtnSubmit = (Button) findViewById(R.id.btn_submit);

        mIvCardFront.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
        leftBack.setOnClickListener(this);
    }


    @Override protected void initData() {
        tvTitle.setText(getString(R.string.identity_title));
        leftBack.setVisibility(View.VISIBLE);
        EasyImage.configuration(this)
                 .setMaxImageSize(500)
                 .setImagesFolderName("imgDatas")
                 .saveInAppExternalFilesDir();
        updateBtnState();
    }


    @Override public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_front:
                getImageFile(type_front);
                break;
            case R.id.btn_submit:
                submitData();
                break;
            case R.id.iv_left:
                this.finish();
                break;
        }
    }

    private void getImageFile(final int type) {
        String json = "{'show':'false', 'type':'','option':[{'name':'"+ CHOOSE_FROM_CAPTURE + "'},{'name':'" + CHOOSE_FROM_GALLERY + "'}] }";
        Bundle bundle = new Bundle();
        bundle.putString(ParamKeys.DIALOG_SINGLE_FILTER_KEY, json);
        SingleSelectorFragment singleSelectorFragment = SingleSelectorFragment.newInstance(bundle);
        singleSelectorFragment.show(getSupportFragmentManager(), ParamKeys.SINGLE_SELECTOR_TAG);
        singleSelectorFragment.addSingleCallback(new SelectorCallback() {
            @Override
            public void onSingleSelecte(JSONObject jsonObject) {
                if(jsonObject != null) {
                    String option = jsonObject.optString("option");
                    try {
                        JSONObject nameJson = new JSONObject(option);
                        String name = nameJson.optString("name");
                        realGetImage(type, name);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void realGetImage(int type, String from) {
        if(CHOOSE_FROM_CAPTURE.equals(from)) {
            EasyImage.openCamera(this, type);
        } else if(CHOOSE_FROM_GALLERY.equals(from)) {
            EasyImage.openGallery(this, type);
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                //Handle the image
                onPhotoReturned(imageFile, type);
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(UploadBankCardActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    private void onPhotoReturned(File imgFile, int type){
        String typeInfo = null;
        if(type == type_front) {
            Glide.with(this)
                 .load(imgFile)
                 .centerCrop()
                 .into(mIvCardFront);
            typeInfo = BANKCARD;
        }
        mFileMapDatas.put(BANKCARD, imgFile);
        updateBtnState();
    }

    private void updateBtnState(){
        if(mFileMapDatas.size() == 1){
            mBtnSubmit.setEnabled(true);
        }else {
            mBtnSubmit.setEnabled(false);
        }
    }


    @Override protected void onDestroy() {
        EasyImage.clearConfiguration(this);
        super.onDestroy();
    }


    @Override public void finish() {
        hideLoading();
        Intent resultData = new Intent();
        if(mFileMapDatas.size() == 1 && StringUtil.checkStr(mResult)){
            resultData.putExtra(ParamKeys.USER_UPLOAD_BANK_CARD_STATE, ParamKeys.JS_SUCCESS);
            resultData.putExtra(ParamKeys.UPLOAD_IMG_RESULT, mResult);
        }else {
            resultData.putExtra(ParamKeys.USER_UPLOAD_BANK_CARD_STATE, ParamKeys.JS_FAILURE);
        }
        setResult(Activity.RESULT_OK, resultData);
        super.finish();
    }

    private void submitData(){
        showLoading("");
        new UploadImgService(this, null, mFileMapDatas, this);
    }


    @Override public void onResult(String result) {
        if(result != null){
            try {
                JSONObject tempData = new JSONObject(result);
                int code = tempData.optInt("code", -1);
                String message = tempData.optString("message");
                String pathMap = tempData.optString("filePath");
                if(code == ParamKeys.NET_SUCCESS_CODE){
                    mResult = pathMap;
                } else {
                    toastMsg(message);
                    mResult = "";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.finish();
    }

}
