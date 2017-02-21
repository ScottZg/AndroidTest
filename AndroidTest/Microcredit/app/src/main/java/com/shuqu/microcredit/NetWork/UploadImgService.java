package com.shuqu.microcredit.NetWork;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.ArrayMap;
import com.shuqu.microcredit.Content.Urls;
import com.shuqu.microcredit.Interface.IUploadImage;
import java.io.File;

/**
 * Class Info ：
 * Created by Lyndon on 16/9/6.
 */
public class UploadImgService {

    Context mContext;
    ArrayMap<String, String> mParams;
    ArrayMap<String, File> mFileDatas;
    IUploadImage mUploadImageListener;

    public UploadImgService(Context context, ArrayMap<String, String> params, ArrayMap<String, File> fileDatas, IUploadImage listener){
        mContext = context;
        mParams = params;
        mFileDatas = fileDatas;
        mUploadImageListener = listener;
        try {
            new UploadImageAsyn().execute();
        }catch ( Exception e){
        }
    }

    //上传图片
    protected class UploadImageAsyn extends AsyncTask<Void, Void, String> {

        @Override protected String doInBackground(Void... voids) {
            return NetUtil.requestData(mContext, "POST", "", mParams, mFileDatas, false, Urls.getUrl(Urls.METHOD_UPLOAD_IMG));
        }

        @Override protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(mUploadImageListener != null){
                mUploadImageListener.onResult(s);
            }
        }
    }
}
