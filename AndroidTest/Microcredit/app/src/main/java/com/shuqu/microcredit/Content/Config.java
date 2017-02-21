package com.shuqu.microcredit.Content;

/*
 * 程序的配置信息
 */
public class Config {
    public static final int MIN_HEAP_SIZE = 8 * 1024 * 1024;// 最小堆内存
    public static final float TARGET_HEAP_UTILIZATION = 0.75f;// 加强法度堆内存的处理惩罚效力
    public static final int SDCARD_SPACE_UNVALIABLE = 100;// sdcard_space_unvailable
    public static final int SD_AVAIABLE_SIZE = 100;// SD卡大小100Mb

    public static final boolean IS_DEBUG = true;// log日志的开关,true为输出，false为不输出

    public static final int CONNECT_TIME_OUT = 15 * 1000;// 连接超时时间设置

    public static final boolean USE_HTTPS = false;

    public static final long TOKEN_MAX_VALID = 7;//token时效,单位 天

    public static final long SUPPORT_MAX_VALID = 1; //刷新supporthost间隔时间
    public static final String SUPPORT_HOST_FILE_NAME = "/supportHostFileName";

    //版本检查 
    public static final String CHECK_VERSION_ID = "5773850fe75e2d54ab000002";
    public static final String CHECK_VERSION_API_TOKEN = "70d69c0fdd766f72c072fbe9f1011816";
}
