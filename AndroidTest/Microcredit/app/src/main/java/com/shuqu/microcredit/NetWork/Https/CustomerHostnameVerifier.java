package com.shuqu.microcredit.NetWork.Https;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Class Info ：
 * Created by Lyndon on 16/6/6.
 */
public class CustomerHostnameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession session) {
        //if(Urls.HOSTNAME.equals(hostname)){
        //    return true;
        //} else {
        //    HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
        //    return hv.verify(hostname, session);
        //}
        return true;
    }
}
