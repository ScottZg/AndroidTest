package com.shuqu.microcredit.Encry;

public class EncryUtil {
	public static String encry(String data){
		String data1 = MD5.md5Encrypt(data);
		String data2 = SHA256.SHA256Encrypt(data);
		String data3 = data1.substring(0, 8) +
								data2.substring(24, 32) +
								data2.substring(0, 8) +
								data1.substring(16, 24) +
								data1.substring(8, 16) +
								data2.substring(8, 16) +
								data2.substring(16, 24) +
								data1.substring(24, 32);
		String data4 = SHA256.SHA256Encrypt(data3);
		return data4.substring(0,32);
	}
}
