package com.shuqu.microcredit.NetWork;

/** 
 * @ClassName Header 
 * @Description Request的Header信息
 */
public class Header {

	/**
	 * @Fields MODEL 设备型号
	 */ 
	public static final String DEVICEMODE = "DEVICEMODE";
	
	/**
	 * 设备id
	 */
	public static final String CLIENTID = "CLIENTID";
 
	/** 
	 * @Fields SCREEN_SIZE 设备分辨率 
	 */ 
	public static final String DEVICESIZE = "DEVICESIZE";
	
	/** 
	 * @Fields VERSION 客户端版本
	 */ 
	public static final String APPVERSION = "APPVERSION";
	
	/*
	 * 客户端软件Name   com.daling.gouChuangYi
	 */
	public static final String APPNAME = "APPNAME";

	/** 
	 * @Fields SYSTEM_VERSION 系统版本 
	 */ 
	public static final String SYSTEM_VERSION = "SYSTEMVERSION";
	
	/** 
	 * @Fields AGENCY 渠道标识 
	 */ 
	public static final String CHANNELID = "CHANNELID";
	
	/** 
	 * @Fields AGENCY 渠道名称
	 */ 
	public static final String CHANNELNAME = "CHANNELNAME";
	
	/** 
	 * @Fields ACCESS_MODE 接入方式 
	 */ 
	public static final String ACCESS_MODE = "ham";
	
	/*
	 * 平台区分
	 */
	public static final String PLATFORM = "PLATFORM";
	
    /*
     * 客户机支持的编码方式
     */
	public static final String ACCEPT_ENCODING = "Accept-Encoding";
	
	/*
	 * 登陆之后token相关
	 */
	public static final String ACCESS_TOKEN = "ACCESSTOKEN";
	

	/*
	 * TOKEN
	 */
	public static final String BRTOKEN = "accessToken";
	public static final String BRFRESHTOKEN = "refreshToken";
	
	/*
	 * session
	 */
	public static final String BRSESSION = "sessionId";
	/*
	 * co
	 */
	public static final String BRCOOKIE = "Cookie";
	/**
	 * uid
	 */
	public static final String BRUID = "uid";
	
}
