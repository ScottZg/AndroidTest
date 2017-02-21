package com.shuqu.microcredit.Utils;

import android.support.v4.util.ArrayMap;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

public class StringUtil {

	/**
	 * 字符串非空 return true
	 * @param str
	 * @return
	 */
	public static boolean checkStr(CharSequence str) {
		if (null == str) {
			return false;
		}
		if ("".equals(str)) {
			return false;
		}
		if ("".equals(str.toString().trim())) {
			return false;
		}
		if ("null".equals(str)) {
			return false;
		}
		return true;
	}

	public static String trimLast(String str) {
		return str.replaceAll("\\s*$", "");
	}

	// 正则判断一个字符串是否全是数字
	public static boolean isNumeric(String str) {
		if (!checkStr(str))
			return false;
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	// 判断电话号码格式是否正确
	public static boolean isMobileNO(String mobiles) {

		/*
		 * Pattern p = Pattern
		 * 
		 * .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		 */

		if (!checkStr(mobiles))
			return false;
		Pattern p = Pattern.compile("[0-9]*");
		Matcher m = p.matcher(mobiles);

		return m.matches() & mobiles.trim().length() == 11;

	}

	// 判断密码格式是否正确
	public static boolean isPassword(String password) {
		if (password.length() >= 6) {
			return true;
		}
		if (password.length() <= 13) {
			return true;
		}
		return false;

	}

	// 判断IMEI的合法性
	public static boolean checkIMEI(String str) {
		if (null == str) {
			return false;
		}
		if ("".equals(str)) {
			return false;
		}
		if ("".equals(str.trim())) {
			return false;
		}
		if ("null".equals(str)) {
			return false;
		}
		if (str.length() < 14) {
			return false;
		}
		boolean isNum = str.matches("[0]+");
		return !isNum;
	}

	/**
	 * 判断二个字符串是否相等
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean equals(String str1, String str2) {
		if (str1 == null) {
			if (str2 == null) {
				return true;
			}
			return false;
		} else if (str1.equals(str2)) {
			return true;
		}
		return false;
	}

	public static HashMap<String, String> string2Map(String jsonData)
	{
		HashMap<String, String> data = new HashMap<>();
		// 将json字符串转换成jsonObject
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(jsonData);
			Iterator it = jsonObject.keys();
			// 遍历jsonObject数据，添加到Map对象
			while (it.hasNext())
			{
				String key = String.valueOf(it.next());
				String value = jsonObject.optString(key);
				data.put(key, value);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}

	public static ArrayMap<String, String> string2ArrayMap(String jsonData)
	{
		ArrayMap<String, String> data = new ArrayMap<String, String>();
		// 将json字符串转换成jsonObject
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(jsonData);
			Iterator it = jsonObject.keys();
			// 遍历jsonObject数据，添加到Map对象
			while (it.hasNext())
			{
				String key = String.valueOf(it.next());
				String value = jsonObject.optString(key);
				data.put(key, value);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}
	/**
	 * 将String转换成InputStream
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static InputStream String2InputStream(String in) throws Exception{

		ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes("ISO-8859-1"));
		return is;
	}

}