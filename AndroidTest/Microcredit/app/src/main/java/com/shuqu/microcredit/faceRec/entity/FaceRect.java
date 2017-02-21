package com.shuqu.microcredit.faceRec.entity;


import android.graphics.Point;
import android.graphics.Rect;

/**
 * @author MatrixCV
 *         FaceRect是用于表示人脸检测的结果，其中包括了 人脸的角度、得分、检测框位置、关键点
 */
public class FaceRect {
	public float score;

	public Rect bound = new Rect();
	public Point point[];

	public Rect raw_bound = new Rect();
	public Point raw_point[];
	
	public Point mouth_upper_lip_top;
	public Point mouth_lower_lip_bottom;
	
	public Point leftYan = new Point();
	public Point rightYan = new Point();

	@Override
	public String toString() {
		return bound.toString();
	}
}
