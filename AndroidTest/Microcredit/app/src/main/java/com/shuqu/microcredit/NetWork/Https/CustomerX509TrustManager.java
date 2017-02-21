package com.shuqu.microcredit.NetWork.Https;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

/**
 * Class Info ：
 * Created by Lyndon on 16/6/6.
 */
public class CustomerX509TrustManager implements X509TrustManager {
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {

    }


    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {

    }


    @Override public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
