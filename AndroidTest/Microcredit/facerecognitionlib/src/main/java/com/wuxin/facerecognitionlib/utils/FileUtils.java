package com.wuxin.facerecognitionlib.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtils {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);

	private static File filePath = Environment.getExternalStorageDirectory();
	private static String yanjinFilePath = Environment.getExternalStorageDirectory() + "/zzyanjingWenJian";
	private static String fileImagePath = Environment.getExternalStorageDirectory() + "/faceImage";
	private static String zhayanImagePath = Environment.getExternalStorageDirectory() + "/yanjing1111111111";
	private static String buzhayanImagePath = Environment.getExternalStorageDirectory() + "/yanjing2222222222";

	private static boolean isWrite = false;

	public static String getNowFileName(long longTime) {
		return "FaceData" + dateFormat.format(new Date(longTime)) + ".txt";
	}

	public static String getNowFileNameJpg(long longTime) {
		return "FaceData" + dateFormat.format(new Date(longTime)) + ".jpeg";
	}
	
	public static String getNowFileNameData(long longTime) {
		return "FaceData" + dateFormat.format(new Date(longTime));
	}

	public static void setStart(boolean b) {
		isWrite = b;
	}

	public static void writeStringToFile(String fileName, String str) {
		try {

			// if (isWrite) {

			File dir = new File(yanjinFilePath);
			if(!dir.exists()) {
				dir.mkdir();
			}

			File file = new File(dir, fileName);
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(str);
			bw.flush();
			bw.close();
			fw.close();

			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void newLine(String fileName) {
		try {

			// if (isWrite) {
			
			File dir = new File(yanjinFilePath);
			if(!dir.exists()) {
				dir.mkdir();
			}

			File file = new File(dir, fileName);
			if (!file.exists()) {
				return;
			}
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.newLine();
			bw.flush();
			bw.close();
			fw.close();

			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void saveYuImageToFile(YuvImage yuvImage, String fileName, int weith, int height) {

		File dir = new File(fileImagePath);
		if (!dir.exists()) {
			dir.mkdir();
		}

		File file = new File(fileImagePath, fileName);
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);

			yuvImage.compressToJpeg(new Rect(0, 0, weith, height), 100, fileOutputStream);

			fileOutputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void saveBitmap(String time, Bitmap bitmap) {

		if (null == bitmap) {
			return;
		}

		File dir = new File(zhayanImagePath);

		if (!dir.exists()) {
			dir.mkdir();
		}

		File file = new File(dir, time + "yuvimage.jpg");
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void saveBitmap(String time, int type, Bitmap bitmap) {

		if (null == bitmap) {
			return;
		}

		File file = null;

		if(type == 1) {
			File zhayan = new File(zhayanImagePath);

			if (!zhayan.exists()) {
				zhayan.mkdir();
			}
			file = new File(zhayan, time + "_1.jpg");
		} else {
			File buzhayan = new File(buzhayanImagePath);

			if (!buzhayan.exists()) {
				buzhayan.mkdir();
			}
			file = new File(buzhayan, time + "_2.jpg");
		}
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将彩色图转换为灰度图
	 *
	 *            位图
	 * @return 返回转换好的位图
	 */
	public static void convertGreyImg(int[] pixels, int width, int height) {
		// int width = img.getWidth(); //获取位图的宽
		// int height = img.getHeight(); //获取位图的高

		// int []pixels = new int[width * height]; //通过位图的大小创建像素点数组

		// img.getPixels(pixels, 0, width, 0, 0, width, height);
		int alpha = 0xFF << 24;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int grey = pixels[width * i + j];

				int red = ((grey & 0x00FF0000) >> 16);
				int green = ((grey & 0x0000FF00) >> 8);
				int blue = (grey & 0x000000FF);

				grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
				// grey = alpha | (grey << 16) | (grey << 8) | grey;
				pixels[width * i + j] = grey;
			}
		}
		// Bitmap result = Bitmap.createBitmap(width, height, Config.RGB_565);
		// result.setPixels(pixels, 0, width, 0, 0, width, height);
		// return result;
	}

	/**
	 * 将彩色图转换为灰度图
	 *
	 * @param img
	 *            位图
	 * @return 返回转换好的位图
	 */
	public static Bitmap convertGreyImg(Bitmap img) {
		int width = img.getWidth(); // 获取位图的宽
		int height = img.getHeight(); // 获取位图的高

		int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组

		img.getPixels(pixels, 0, width, 0, 0, width, height);
		int alpha = 0xFF << 24;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int grey = pixels[width * i + j];

				int red = ((grey & 0x00FF0000) >> 16);
				int green = ((grey & 0x0000FF00) >> 8);
				int blue = (grey & 0x000000FF);

				grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
				// grey = alpha | (grey << 16) | (grey << 8) | grey;
				pixels[width * i + j] = grey;
			}
		}
		Bitmap result = Bitmap.createBitmap(width, height, Config.RGB_565);
		result.setPixels(pixels, 0, width, 0, 0, width, height);
		return result;
	}

	/**
	 * 将彩色图转换为灰度图
	 *
	 * @param img
	 *            位图
	 * @return 返回转换好的位图
	 */
	public static int[] convertGreyImgForInt(Bitmap img) {
		int width = img.getWidth(); // 获取位图的宽
		int height = img.getHeight(); // 获取位图的高

		int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组

		img.getPixels(pixels, 0, width, 0, 0, width, height);
		int alpha = 0xFF << 24;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int grey = pixels[width * i + j];

				int red = ((grey & 0x00FF0000) >> 16);
				int green = ((grey & 0x0000FF00) >> 8);
				int blue = (grey & 0x000000FF);

				grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
				// grey = alpha | (grey << 16) | (grey << 8) | grey;
				pixels[width * i + j] = grey;
			}
		}
		// Bitmap result = Bitmap.createBitmap(width, height, Config.RGB_565);
		// result.setPixels(pixels, 0, width, 0, 0, width, height);
		return pixels;
	}

	//直方图
	public static int[] eachEqualize(int[] temp, int width, int height) {
		// 灰度映射表
		int bMap[] = new int[256];
		// 灰度映射表
		int lCount[] = new int[256];
		// 重置计数为0
		int i, j;
		// 计算各个灰度值的计数 - 参考灰度直方图的绘制代码 (对话框类中)
		for (i = 0; i < height; i++) {
			for (j = 0; j < width; j++) {
				lCount[temp[i * width + j]]++; // 计数加1
			}
		}
		// 计算灰度映射表
		for (i = 0; i < 256; i++) {
			// 初始为0
			int Temp = 0;
			for (j = 0; j <= i; j++) {
				Temp += lCount[j];
			}
			// 计算对应的新灰度值
			bMap[i] = Temp * 255 / height / width;
		}
		// 每行
		for (i = 0; i < height; i++) {
			// 每列
			for (j = 0; j < width; j++) {
				temp[i * width + j] = bMap[temp[i * width + j]];
			}
		}
		return temp;
	}

	public static float[] feature(int[] temp, int bins) {
		int size = 256 / bins;
		float[] result = new float[bins];
		for (int i : temp) {
			result[i / size]++;
		}
		for (int i = 0; i < result.length; i++) {
			result[i] /= temp.length;
		}

		return result;
	}

	public static float diff(float[] feature1, float[] feature2) {
		float result = 0;
		for (int i = 0; i < feature1.length; i++) {
			result += Math.abs(feature1[i] - feature2[i]);
		}

		return result;
	}

	/**
	 * 
	 * @param bitmap 原bitmap
	 * @return
	 */
	public static Bitmap compressBitmap(Bitmap bitmap, int dstWidth, int disHeight) {
		int srcWdith = bitmap.getWidth();
		int srcHeight = bitmap.getHeight();
		float scale = (float)dstWidth / (float)srcWdith;
		Matrix matrix = new Matrix();
		matrix.setScale(scale, scale);
		
		return Bitmap.createBitmap(bitmap, 0, 0, srcWdith, srcHeight, matrix, false);
 	}
	
	public static int[][] genFeature(int[][] temp, int widthR, int heightR) {
        // 灰度映射表
        int height = temp.length;
        int width = temp[0].length;
        int[][] result = new int[heightR][widthR];
        int widthStep = width / widthR;
        int heightStep = height / heightR;
        for (int i = 0; i < heightR; i++) {

            for (int j = 0; j < widthR; j++) {
                int sum = 0;
                for (int k = i * heightStep; k < (i + 1) * heightStep; k++) {
                    for (int l = j * widthStep; l < (j + 1) * widthStep; l++) {
                        sum += temp[k][l];
                    }
                }
                result[i][j] = sum / widthStep / heightStep;
            }

        }
        return result;
    }
	
	public static void yiwei2erwei(int[] src, int[][] dst, int width, int height) {
 		for (int i = 0; i < height; i++) {
 			System.arraycopy(src, width * i, dst[i], 0, width);
 		}
 		
 		
	}
}





















