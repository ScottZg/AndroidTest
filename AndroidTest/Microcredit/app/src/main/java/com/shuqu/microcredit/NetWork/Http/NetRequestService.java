package com.shuqu.microcredit.NetWork.Http;

import android.content.Context;
import android.graphics.Bitmap;

import com.shuqu.microcredit.Content.ParamKeys;
import com.shuqu.microcredit.Model.UserInfo;
import com.shuqu.microcredit.NetWork.FreshTokenService;
import com.shuqu.microcredit.NetWork.Header;
import com.shuqu.microcredit.R;
import com.shuqu.microcredit.UI.LoginActivity;
import com.shuqu.microcredit.Utils.CommonErrorUtils;
import com.shuqu.microcredit.Utils.ForwardActivityUtils;
import com.shuqu.microcredit.Utils.LogUtils;
import com.shuqu.microcredit.Utils.NetworkUtil;
import com.shuqu.microcredit.Utils.PrefsUtils;
import com.shuqu.microcredit.Utils.StreamUtil;
import com.shuqu.microcredit.Utils.StringUtil;
import com.shuqu.microcredit.Utils.ToastUtils;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.Map;

/*
 * 根据Http请求得到数据，String类型
 * 
 */
public class NetRequestService{

    private static final String TAG = "HttpsNetRequestService";
    private Context mContext;
    private boolean isToastOpen = true;
    private boolean mBusy = false;


    public NetRequestService(Context context) {
        this.mContext = context;
    }

    public boolean isBusy(){
        return mBusy;
    }

    private HttpURLConnection getConnection(String params, String url) throws MalformedURLException, IOException {
        return LCHttpUrlConnection.getHttpConnectionWithHeader(mContext, params, url);
    }


    /**
     * 设置是否打开异常提示
     */
    public void setIsToastOpen(boolean isToastOpen) {
        this.isToastOpen = isToastOpen;
    }


    public String requestData(String method, String params, Map<String, String> paramMaps, boolean needShowCach, String url) {
        return this.requestData(method, params, paramMaps, null, needShowCach, url);
    }


    public String requestData(String method, String params, Map<String, String> paramMaps, Map<String, File> filesParams, boolean needShowCach, String url) {
        // 生成缓存文件内容的key值
        /*
         * if(StringUtil.checkStr(params)) params =
		 * URLEncodUtil.getEncodeStr(params);
		 */
        /*
         * 如果接口为GET则把参数拼接到URL里
		 */
        setBusy(true);
        if ("GET".equals(method) && null != paramMaps && !paramMaps.isEmpty()) {
            if (filesParams != null && !filesParams.isEmpty()) {
                throw new IllegalArgumentException("GET can't upload file");
            }
            int i = 0;
            StringBuilder data = new StringBuilder();
            for (Map.Entry<String, String> map : paramMaps.entrySet()) {// Map.Entry<String,String>
                // map:paramsMap.entrySet()

                if (i == 0) {
                    data.append("?");
                }
                else {
                    data.append("&");
                }
                data.append(map.getKey()).append("=");
                String value = map.getValue();
                if (StringUtil.checkStr(value)) {
                    try {
                        value = URLEncoder.encode(value, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    value = "";
                }
                data.append(value);
                i++;
            }
            params += data;
        }

        //String cachContentKey = generateKey(params, paramMaps);
        if (!NetworkUtil.isConnected(mContext)) {
            if (isToastOpen) {
                ToastUtils.show(
                        mContext,
                        mContext.getResources().getString(R.string.network_exception)
                );
            }
            setBusy(false);
            return null;
        }
        try {

            HttpURLConnection httpConn = getConnection(params, url);
            //trustNetServer(mContext, httpConn);

            if (null == httpConn) {
                setBusy(false);
                return null;
            }
            httpConn.setRequestMethod(method);
            // 请求参数放在body里时的post请求
            if ("POST".equals(method)) {
                DataOutputStream ds;
                if(filesParams == null || filesParams.isEmpty()) {
                    httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    if(paramMaps != null && !paramMaps.isEmpty()) {
                        StringBuilder data = new StringBuilder();
                        for (Map.Entry<String, String> map : paramMaps.entrySet()) {// Map.Entry<String,String>
                            // map:paramsMap.entrySet()
                            data.append(map.getKey()).append("=");
                            String value = map.getValue();
                            if (StringUtil.checkStr(value)) {
                                value = URLEncoder.encode(value, "utf-8");
                            } else {
                                value = "";
                            }
                            data.append(value);
                            data.append("&");
                        }
                        data.deleteCharAt(data.length() - 1);
                        byte[] paramsdata = data.toString().getBytes();// Content-Type:
                        ds = new DataOutputStream(httpConn.getOutputStream());
                        ds.write(paramsdata);

                        ds.flush();
                        ds.close();
                    }
                } else {
                    httpConn.setRequestProperty("Content-Type",
                            "multipart/form-data; boundary=" + StreamUtil.boundary);
                    ds = new DataOutputStream(httpConn.getOutputStream());
                    //StreamUtil.writeStringParams(ds, paramMaps);
                    if(filesParams != null && !filesParams.isEmpty()) {
                        StreamUtil.writeFileToOutputStream(ds, filesParams);
                    }
                    ds.writeBytes("--" + StreamUtil.boundary + "--" + "\r\n");
                    ds.writeBytes("\r\n");

                    ds.flush();
                    ds.close();
                }
            }
            int code = httpConn.getResponseCode();

            if (code == 200) {
                UserInfo.setSession(httpConn.getHeaderField("sessionid"));
                String result = LCHttpUrlConnection.decodeConnectionToString(httpConn);
                LogUtils.e(result);
                JSONObject jsonObject = new JSONObject(result);
                int errCode = jsonObject.optInt("code");
                if (CommonErrorUtils.isAccountException(errCode)) {
                    if (CommonErrorUtils.isTokenExpired(errCode)) {
                        FreshTokenService.freshToken(mContext, null);
                    }
                    else {
                        ForwardActivityUtils.forwardActivity(mContext, LoginActivity.class, false);
                        PrefsUtils.putValue(mContext, Header.BRFRESHTOKEN, "");
                        PrefsUtils.putValue(mContext, ParamKeys.TOKEN_FRESH_TIME, "");
                    }
                    ToastUtils.show(mContext, jsonObject.optString("message"), true);
                    setBusy(false);
                    return null;
                }

                // 将字符串写入文件
                //if (StringUtil.checkStr(cachContentKey) && StringUtil
                //		.checkStr(result)) {
                //	dbAdmin.setNetCache(cachContentKey, result);
                //}

                setBusy(false);
                return result;
            }
            else {
                ToastUtils.show(
                        mContext,
                        mContext.getResources().getString(R.string.service_exception)
                );
                setBusy(false);
                return null;
            }
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            setBusy(false);
            return null;
        } catch (SocketTimeoutException ste) {
            ste.printStackTrace();
            ToastUtils.show(
                    mContext,
                    mContext.getResources().getString(R.string.time_out_exception)
            );
            setBusy(false);
            return null;
        } catch (IOException e1) {
            e1.printStackTrace();
            e1.printStackTrace();
            setBusy(false);
            return null;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            setBusy(false);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            setBusy(false);
            return null;
        }
    }

    private void setBusy(boolean busy){
        mBusy = busy;
    }


    private String generateKey(String params, Map<?, ?> paramMaps) {
        String key = null;
        if (null == paramMaps) {
            key = params;
        }
        else {
            key = params + paramMaps.toString();
        }
        return key;
    }


    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
