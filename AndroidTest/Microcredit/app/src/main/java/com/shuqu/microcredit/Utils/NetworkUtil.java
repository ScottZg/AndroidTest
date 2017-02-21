package com.shuqu.microcredit.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @ClassName NetworkUtil
 * @Description 网络连接工具类
 *
 */
public class NetworkUtil {

	// private static final String TAG = "NetworkUtil";

	private static final String TYPE_NAME = "mobile";

	/**
	 * @Description 得到当前网络连接类型
	 * @param context
	 * @return String 当前网络连接类型名称
	 */
	public static String getCurrentNetworkTypeName(Context context) {

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		if (networkInfo == null) {
			return null;
		} else {// 当前存在网络接入方式，获取具体的网络接入类型值
			String typeName = networkInfo.getTypeName();
			if (typeName.contains(TYPE_NAME)) {
				String extraInfo = networkInfo.getExtraInfo();
				if (extraInfo == null) {
					extraInfo = "ChinaTelecom";
				}
				return extraInfo;
			}
			return typeName;
		}
	}

	/**
	 * @Description 判断网络连接是否存在并可用
	 * @param context
	 * @return boolean true可用，false不可用
	 */
	public static boolean isConnected(Context context) {

		if (context == null)
			return false;

		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectivityManager
					.getActiveNetworkInfo();
			if (networkInfo == null) {
				return false;
			} else {
				if (networkInfo.isConnected()) {
					return true;
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}

	}
}
