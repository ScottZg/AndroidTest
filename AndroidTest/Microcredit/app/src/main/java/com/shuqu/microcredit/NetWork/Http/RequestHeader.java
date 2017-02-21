package com.shuqu.microcredit.NetWork.Http;

import android.content.Context;

import com.shuqu.microcredit.Content.Config;
import com.shuqu.microcredit.Model.UserInfo;
import com.shuqu.microcredit.NetWork.Header;
import com.shuqu.microcredit.Utils.StringUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

/*
 * 构建http请求头,返回一个HttpURLConnection对象
 */
public class RequestHeader {

	private static final String TAG = "RequestHeader";


	public static HttpURLConnection constructHeader(Context context,
													String params, String requestUrl) throws MalformedURLException,
																							 IOException {
		return creatHttpConnection(params, requestUrl);
	}

	private static HttpURLConnection creatHttpConnection(String params,
														 String requestUrl) throws MalformedURLException, IOException {
		HttpURLConnection httpConnection = LCHttpUrlConnection
				.getHttpConnection(requestUrl, params);
		if (null == httpConnection)
			return null;

		if(StringUtil.checkStr(UserInfo.getAccess_token())) {
			httpConnection.setRequestProperty(Header.BRTOKEN, UserInfo.getAccess_token());
		}
		if(StringUtil.checkStr(UserInfo.getUid())) {
			httpConnection.setRequestProperty(Header.BRUID, UserInfo.getUid());
		}

		httpConnection.setRequestProperty("charset", "utf-8");
		httpConnection.setRequestProperty("Connection", "close");
		httpConnection.setConnectTimeout(Config.CONNECT_TIME_OUT);
		httpConnection.setReadTimeout(Config.CONNECT_TIME_OUT);
		return httpConnection;
	}



}
