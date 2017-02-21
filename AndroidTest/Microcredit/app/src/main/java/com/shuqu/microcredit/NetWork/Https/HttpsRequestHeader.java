package com.shuqu.microcredit.NetWork.Https;

import android.content.Context;
import com.shuqu.microcredit.Content.Config;
import com.shuqu.microcredit.Model.DeviceParams;
import com.shuqu.microcredit.Model.UserInfo;
import com.shuqu.microcredit.NetWork.Header;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.net.ssl.HttpsURLConnection;

/*
 * 构建http请求头,返回一个HttpURLConnection对象
 */
public class HttpsRequestHeader {

	private static final String TAG = "HttpsRequestHeader";


	public static HttpsURLConnection constructHeader(Context context,
													 String params, String requestUrl) throws MalformedURLException,
																							  IOException {
		return creatHttpConnection(params, requestUrl);
	}

	private static HttpsURLConnection creatHttpConnection(String params,
														  String requestUrl) throws MalformedURLException, IOException {
		HttpsURLConnection httpConnection = LCHttpsUrlConnection
				.getHttpConnection(requestUrl, params);
		if (null == httpConnection)
			return null;

		httpConnection.setRequestProperty(Header.BRTOKEN, UserInfo.getAccess_token());
		httpConnection.setRequestProperty(Header.BRUID, UserInfo.getUid());

		httpConnection.setRequestProperty("charset", "utf-8");
		httpConnection.setRequestProperty("Connection", "close");
		httpConnection.setConnectTimeout(Config.CONNECT_TIME_OUT);
		httpConnection.setReadTimeout(Config.CONNECT_TIME_OUT);
		return httpConnection;
	}



}
