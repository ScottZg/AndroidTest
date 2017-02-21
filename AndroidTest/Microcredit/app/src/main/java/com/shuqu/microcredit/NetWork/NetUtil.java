package com.shuqu.microcredit.NetWork;

import android.content.Context;
import com.shuqu.microcredit.NetWork.Http.NetRequestService;
import com.shuqu.microcredit.NetWork.Https.HttpsNetRequestService;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 * Class Info ：
 * Created by lyndon on 16/8/23.
 */
public class NetUtil {
    public static final String HTTP_METHOD_GET = "GET";
    public static final String HTTP_METHOD_POST = "POST";
    private static String https_prefix = "https";
    private static ArrayList<NetRequestService> mHttpServicePool = new ArrayList<>();
    private static ArrayList<HttpsNetRequestService> mHttpsServicePool = new ArrayList<>();

    public static String requestData(Context context, String method, String params, Map<String, String> paramMaps, String url){
        if(url.startsWith(https_prefix)){
            return getHttpsService(context).requestData(method, params, paramMaps, false, url);
        }
        return getHttpService(context).requestData(method, params, paramMaps, false, url);
    }

    public static String requestData(Context context, String method, String params, Map<String, String> paramMaps, Map<String, File> filesParams, String url){
        if(url.startsWith(https_prefix)){
            return getHttpsService(context).requestData(method, params, paramMaps, filesParams, false, url);
        }
        return getHttpService(context).requestData(method, params, paramMaps, filesParams, false, url);
    }

    public static String requestData(Context context, String method, String params, Map<String, String> paramMaps, boolean needShowCach, String url){
        if(url.startsWith(https_prefix)){
            return getHttpsService(context).requestData(method, params, paramMaps, needShowCach, url);
        }
        return getHttpService(context).requestData(method, params, paramMaps, needShowCach, url);
    }

    public static String requestData(Context context, String method, String params, Map<String, String> paramMaps, Map<String, File> filesParams, boolean needShowCach, String url){
        if(url.startsWith(https_prefix)){
            return getHttpsService(context).requestData(method, params, paramMaps, filesParams, needShowCach, url);
        }
        return getHttpService(context).requestData(method, params, paramMaps, filesParams, needShowCach, url);
    }

    private static NetRequestService getHttpService(Context context){
        int size = mHttpServicePool.size();
        NetRequestService service;
        for (int i=0; i<size; ++i){
            service = mHttpServicePool.get(i);
            if(!service.isBusy()){
                return service;
            }
        }
        service = new NetRequestService(context);
        mHttpServicePool.add(service);
        return service;
    }

    private static HttpsNetRequestService getHttpsService(Context context){
        int size = mHttpsServicePool.size();
        HttpsNetRequestService service;
        for (int i=0; i<size; ++i){
            service = mHttpsServicePool.get(i);
            if(!service.isBusy()){
                return service;
            }
        }
        service = new HttpsNetRequestService(context);
        mHttpsServicePool.add(service);
        return service;
    }

    private NetUtil(){}
}
