package com.shuqu.microcredit.Utils;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import com.shuqu.microcredit.Content.ParamKeys;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @ClassName ReadAssetsUtil
 * @Description 读取assets目录下文件内容工具类
 */
public class ReadAssetsUtil {

	private static final String TAG = "ReadAssetsUtil";

	private static final String ENCODING = "UTF-8";
	private static final String AGENCY_ID = "agencyid";
	private static final String CUSTOMER_ID = "customerid";
	private static final String SEPARATOR_EQUALS = "=";

	/**
	 * 读取assets中单个的文件
	 * 
	 * @param fileName
	 * @return 读取的内容
	 * @author huangke@yoka.com
	 */
	public String readFile(Context context, String fileName) {
		String content = null;
		try {
			InputStream is = context.getAssets().open(fileName);
			// 获取文件的字节数
			int length = is.available();
			// 创建byte数组
			byte[] data = new byte[length];
			// 将文件中的数据读到byte数组中
			is.read(data);
			content = new String(data);
			is.close();
		} catch (Exception e) {
		}
		return content;
	}

	/**
	 * 读取agencyID
	 * 
	 * @param context
	 *            调用者上下文环境
	 * @param fileName
	 *            agency.txt
	 * @return agencyID
	 */
	public static String readAgencyID(Context context, String fileName) {
		String agencyID = null;
		try {
			InputStream is = context.getAssets().open(fileName);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String temp = null;
			// 循环读取
			while ((temp = br.readLine()) != null) {
				if (temp.indexOf(AGENCY_ID) > -1) {
					String[] temps = temp.split(SEPARATOR_EQUALS);
					agencyID = temps[1];
				}
			}
			br.close();
			isr.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return agencyID;
	}

	/**
	 * 获取<meta-data>的值
	 * @param context
	 * @param metaKey
	 * @return
	 */
	public static String getMetaValue(Context context, Class<?> cls, String metaKey) {
		Bundle metaData = null;
		String value = null;
		if (context == null || TextUtils.isEmpty(metaKey)) {
			return null;
		}
		try {
			PackageItemInfo pii = null;
			if (Application.class.isAssignableFrom(cls)) {
				pii = context.getPackageManager()
							 .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			} else if (Activity.class.isAssignableFrom(cls)) {
				pii = context.getPackageManager()
							 .getActivityInfo(new ComponentName(context, cls), PackageManager.GET_META_DATA);
			} else if (Service.class.isAssignableFrom(cls)) {
				pii = context.getPackageManager()
							 .getServiceInfo(new ComponentName(context, cls), PackageManager.GET_META_DATA);
			} else if (BroadcastReceiver.class.isAssignableFrom(cls)) {
				pii = context.getPackageManager()
							 .getReceiverInfo(new ComponentName(context, cls), PackageManager.GET_META_DATA);
			}
			if (pii != null) {
				metaData = pii.metaData;
			}
			if (metaData != null) {
				value = String.valueOf(metaData.get(metaKey));
			}
		} catch (Exception e) {
		}
		return value;
	}

	/**
	 * 读取customerID
	 * 
	 * @param context
	 *            调用者上下文环境
	 * @param fileName
	 *            customer.txt
	 * @return customerID
	 */
	public static String readCustomerID(Context context, String fileName) {
		String customerID = null;
		try {
			InputStream is = context.getAssets().open(fileName);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String temp = null;
			// 循环读取
			while ((temp = br.readLine()) != null) {
				if (temp.indexOf(CUSTOMER_ID) > -1) {
					String[] temps = temp.split(SEPARATOR_EQUALS);
					customerID = temps[1];
				}
			}
			br.close();
			isr.close();
			is.close();
		} catch (Exception e) {
		}
		return customerID;
	}

	/*
	 * 取得渠道名称
	 */
	public static String getChannelName(Context con) {
		String key = ParamKeys.CHANNEL_NAME_KEY;
		ApplicationInfo appInfo = con.getApplicationInfo();
		Bundle bundle = appInfo.metaData;
		if (null != bundle && bundle.containsKey(key))
			return bundle.getString(key);
		return "";
	}

	/*
	 * 取得渠道号
	 */
	public static String getChannelId(Context con) {
		String key = ParamKeys.CHANNEL_ID_KEY;
		try {
			ApplicationInfo appInfo = con.getPackageManager()
										 .getApplicationInfo(con.getPackageName(),
							PackageManager.GET_META_DATA);
			String msg = appInfo.metaData.getString(key);

			if (StringUtil.checkStr(msg)) {
				msg = msg.substring(1);
				return msg;
			} else {
				return "null";
			}
		} catch (Exception e) {
			return "error";
		}

	}

	public static String readAssetsCityJson(Context context, String fileName) {

		StringBuilder sb = null;
		String temp = null;
		InputStream open = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;

		try {
			open = context.getAssets().open(fileName);
			sb = new StringBuilder();
			inputStreamReader = new InputStreamReader(open);
			bufferedReader = new BufferedReader(inputStreamReader);

			while ((temp = bufferedReader.readLine()) != null) {
				sb.append(temp);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedReader.close();
				inputStreamReader.close();
				open.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return sb.toString();
	}
}
