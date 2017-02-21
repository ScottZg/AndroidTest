package com.wuxin.facerecognitionlib.entity;

import android.graphics.Rect;

public class SizeSettiong {
	private SizeSettiong(){}
	private static SizeSettiong instance = new SizeSettiong();
	public static SizeSettiong getInstance() {
		return instance;
	}
	
	private int jiequKuandu = 40;
	private int jiequGaodu = 120;
	
	private int featrueKuandu = 10;
	private int featrueGaodu = 30;
	
	public int getJiequKuandu() {
		return jiequKuandu;
	}
	public void setJiequKuandu(int jiequKuandu) {
		this.jiequKuandu = jiequKuandu;
	}
	public int getJiequGaodu() {
		return jiequGaodu;
	}
	public void setJiequGaodu(int jiequGaodu) {
		this.jiequGaodu = jiequGaodu;
	}
	public int getFeatrueKuandu() {
		return featrueKuandu;
	}
	public void setFeatrueKuandu(int featrueKuandu) {
		this.featrueKuandu = featrueKuandu;
	}
	public int getFeatrueGaodu() {
		return featrueGaodu;
	}
	public void setFeatrueGaodu(int featrueGaodu) {
		this.featrueGaodu = featrueGaodu;
	}
	
	public static Rect faceRect = new Rect(60, 95, 400, 435);
}
