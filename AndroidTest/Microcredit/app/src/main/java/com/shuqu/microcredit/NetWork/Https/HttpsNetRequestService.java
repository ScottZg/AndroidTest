package com.shuqu.microcredit.NetWork.Https;

import android.content.Context;
import android.graphics.Bitmap;
import com.lyndon.nativedata.JniBridge;
import com.shuqu.microcredit.Content.ParamKeys;
import com.shuqu.microcredit.Content.Urls;
import com.shuqu.microcredit.Model.UserInfo;
import com.shuqu.microcredit.NetWork.FreshTokenService;
import com.shuqu.microcredit.NetWork.Header;
import com.shuqu.microcredit.R;
import com.shuqu.microcredit.UI.LoginActivity;
import com.shuqu.microcredit.Utils.CommonErrorUtils;
import com.shuqu.microcredit.Utils.ForwardActivityUtils;
import com.shuqu.microcredit.Utils.NetworkUtil;
import com.shuqu.microcredit.Utils.PrefsUtils;
import com.shuqu.microcredit.Utils.StreamUtil;
import com.shuqu.microcredit.Utils.StringUtil;
import com.shuqu.microcredit.Utils.ToastUtils;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import org.json.JSONObject;

/*
 * 根据Http请求得到数据，String类型
 * 
 */
public class HttpsNetRequestService {

    private static final String TAG = "HttpsNetRequestService";
    private Context mContext;
    private boolean isToastOpen = true;
    private static final CustomerHostnameVerifier mHostnameVerfier = new CustomerHostnameVerifier();
    private static final CustomerX509TrustManager mX509Manager = new CustomerX509TrustManager();
    private boolean mBusy = false;


    public HttpsNetRequestService(Context context) {
        this.mContext = context;
    }


    private HttpsURLConnection getConnection(String params,
                                             String url) throws MalformedURLException, IOException {
        return LCHttpsUrlConnection.getHttpConnectionWithHeader(mContext, params, url);
    }

    public boolean isBusy(){
        return mBusy;
    }

    /**
     * 设置是否打开异常提示
     */
    public void setIsToastOpen(boolean isToastOpen) {
        this.isToastOpen = isToastOpen;
    }


    public String requestData(String method, String params, Map<String, String> paramMaps,
                              boolean needShowCach, String url) {
        return this.requestData(method, params, paramMaps, null, needShowCach, url);
    }

    public String requestData(String method, String params, Map<String, String> paramMaps, Map<String, File> filesParams,
                              boolean needShowCach, String url) {
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
            if(filesParams != null && !filesParams.isEmpty()) {
                throw new IllegalArgumentException("GET can't upload file");
            }
            int i = 0;
            StringBuilder data = new StringBuilder();
            for (Map.Entry<String, String> map : paramMaps.entrySet()) {//
                // Map.Entry<String,String>
                // map:paramsMap.entrySet()

                if (i == 0) {
                    data.append("?");
                } else {
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
                } else {
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
                ToastUtils.show(mContext,
                                mContext.getResources().getString(R.string.network_exception)
                );
            }
            setBusy(false);
            return null;
        }
        try {

            HttpsURLConnection httpConn = getConnection(params, url);
            int certType = Urls.getHttpsType(url);
            String certData = JniBridge.getPrivateData(certType);
            trustNetServer(httpConn, certData);

            if (null == httpConn) {
                setBusy(false);
                return null;
            }
            httpConn.setRequestMethod(method);
            // 请求参数放在body里时的post请求
            if ("POST".equals(method) && null != paramMaps &&
                    !paramMaps.isEmpty()) {
                DataOutputStream ds;
                if(filesParams == null || filesParams.isEmpty()) {
                    httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    StringBuilder data = new StringBuilder();
                    for (Map.Entry<String, String> map : paramMaps.entrySet()) {// Map.Entry<String,String>
                        // map:paramsMap.entrySet()
                        data.append(map.getKey()).append("=");
                        String value = map.getValue();
                        if (StringUtil.checkStr(value)) {
                            value = URLEncoder.encode(value, "utf-8");
                        }
                        else {
                            value = "";
                        }
                        data.append(value);
                        data.append("&");
                    }
                    data.deleteCharAt(data.length() - 1);
                    byte[] paramsdata = data.toString().getBytes();// Content-Type:
                    ds = new DataOutputStream(httpConn.getOutputStream());
                    ds.write(paramsdata);
                } else {
                    httpConn.setRequestProperty("Content-Type",
                            "multipart/form-data; boundary=" + StreamUtil.boundary);
                    ds = new DataOutputStream(httpConn.getOutputStream());
                    StreamUtil.writeStringParams(ds, paramMaps);
                    if(filesParams != null && !filesParams.isEmpty()) {
                        StreamUtil.writeFileToOutputStream(ds, filesParams);
                    }
                    ds.writeBytes("--" + StreamUtil.boundary + "--" + "\r\n");
                    ds.writeBytes("\r\n");
                }

                ds.flush();
                ds.close();
            }
            int code = httpConn.getResponseCode();
            if (code == 200) {
                UserInfo.setSession(httpConn.getHeaderField("sessionid"));
                String result = LCHttpsUrlConnection.decodeConnectionToString(httpConn);
                JSONObject jsonObject = new JSONObject(result);
                int errCode = jsonObject.optInt("code");
                if (CommonErrorUtils.isAccountException(errCode)) {
                    if(CommonErrorUtils.isTokenExpired(errCode)){
                        FreshTokenService.freshToken(mContext, null);
                    }else {
                        ForwardActivityUtils.forwardActivity(mContext, LoginActivity.class, true);
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
            } else {
                ToastUtils.show(mContext,
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
            ToastUtils.show(mContext,
                            mContext.getResources().getString(R.string.time_out_exception)
            );
            setBusy(false);
            return null;
        } catch (IOException e1) {
            e1.printStackTrace();
            setBusy(false);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            setBusy(false);
            return null;
        } catch (OutOfMemoryError e) {
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
        } else {
            key = params + paramMaps.toString();
        }
        return key;
    }


    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


    //实现ssl 双向验证
    private static void trustNetServer(Context context, HttpsURLConnection urlConnection) {
        SSLContext sSLContext = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            // 证书打包在 asset 中
            InputStream caInput = new BufferedInputStream(context.getAssets()
                                                                 .open(ParamKeys.CERT_NAME));
            Certificate ca = null;
            try {
                ca = (Certificate) cf.generateCertificate(caInput);
            } catch (CertificateException e) {
                e.printStackTrace();
            } finally {
                caInput.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            //create keystore
            //KeyStore clientKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            //clientKeyStore.load(context.getAssets().open(ParamKeys.KEYSTORE_NAME),
            //                    ParamKeys.KEYSTORE_PWD.toCharArray()
            //);
            //
            //KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            //keyManagerFactory.init(clientKeyStore, ParamKeys.KEYSTORE_PWD.toCharArray());

            // Create an SSLContext that uses our TrustManager and KeyManager
            sSLContext = SSLContext.getInstance("TLS");
            sSLContext.init(null, tmf.getTrustManagers(), null);
            urlConnection.setSSLSocketFactory(sSLContext.getSocketFactory());
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpsURLConnection.setDefaultHostnameVerifier(mHostnameVerfier);
        if (sSLContext != null) {
            HttpsURLConnection.setDefaultSSLSocketFactory(sSLContext.getSocketFactory());
        }
    }

    //
    //实现ssl 双向验证
    private static void trustNetServer(HttpsURLConnection urlConnection, String cert) {
        SSLContext sSLContext = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = StringUtil.String2InputStream(cert);
            Certificate ca = null;
            try {
                ca = (Certificate) cf.generateCertificate(caInput);
            } catch (CertificateException e) {
                e.printStackTrace();
            } finally {
                caInput.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            ////create keystore
            //KeyStore clientKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            //clientKeyStore.load(StringUtil.String2InputStream(keystore),
            //        ParamKeys.KEYSTORE_PWD.toCharArray()
            //);
            //
            //KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            //keyManagerFactory.init(clientKeyStore, ParamKeys.KEYSTORE_PWD.toCharArray());

            // Create an SSLContext that uses our TrustManager and KeyManager
            sSLContext = SSLContext.getInstance("TLS");
            sSLContext.init(null, tmf.getTrustManagers(), null);
            urlConnection.setSSLSocketFactory(sSLContext.getSocketFactory());
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (Exception e){

        }

        HttpsURLConnection.setDefaultHostnameVerifier(mHostnameVerfier);
        if (sSLContext != null) {
            HttpsURLConnection.setDefaultSSLSocketFactory(sSLContext.getSocketFactory());
        }
    }
}
