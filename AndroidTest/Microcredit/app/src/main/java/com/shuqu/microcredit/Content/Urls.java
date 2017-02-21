package com.shuqu.microcredit.Content;

/**
 * Class Info ：
 * Created by Lyndon on 16/6/1.
 */
public class Urls {
    public static final String FirstTab = "/src/p/my_index/index.html";

    public static final String TEST = "http://192.168.2.128:3333";
    public static final String DAILY_TEST = "http://dymapi.100credit.com";
    public static final String ONLINE = "http://dymapi.100credit.com";

//    public static String BASE_API_URL = "http://dymapi.100credit.com/eagleeye_app";
    public static String BASE_API_URL = "http://192.168.2.150:8080";
    public static String BASE_WEB_URL = "http://dym.100credit.com/credit-loan";
    //日常环境
    public static String DAILY_API_URL = BASE_API_URL;//192.168.180.10:8084
    public static String DAILY_WEB_URL = "http://dym.100credit.com/credit-loan";
    //预发环境
    public static String PRE_API_URL = "http://preapi.100credit.com";
    public static String PRE_WEB_URL = "http://pre.100credit.com/credit-loan";

    //服务协议
    public static String SERVICE_INFO_URL = BASE_WEB_URL + "/src/p/protocol/index.html";

    //刷新token
    public static final String FRESH_TOKEN = "http://192.168.180.10:8082/spore/userCenter/refresh.do";
    //登陆
//    public static final String METHOD_LOGIN = "http://dymapi.100credit.com/spore/userCenter/login.do";
    public static final String METHOD_LOGIN = "http://192.168.180.10:8082/spore/userCenter/login.do";
    //登出
    public static final String METHOD_LOGOUT = "http://192.168.180.10:8082/user/logout";
    //验证码
    public static final String METHOD_GET_CODE_URL = "http://192.168.180.10:8082/spore/userCenter/sendMsg.do";
    //注册
    public static final String METHON_REGIST_URL = "http://192.168.180.10:8082/spore/userCenter/register.do";
    //忘记密码
    public static final String METHOD_FORGET_PWD = "http://192.168.180.10:8082/spore/userCenter/forget.do";
    //getHost接口
    public static final String METHOD_GET_HOSTS = "http://192.168.180.10:8082/spore/userCenter/getHost.do";
    //获取热门城市
    public static final String METHOD_GET_HOTS = "/eagleeye_app/loan/getHotCity";
    //上传图片
    public static final String METHOD_UPLOAD_IMG = "http://192.168.180.10:8082/demeter/file/upload.do";
    //数据统计
    public static final String METHOD_STATISTICS = "http://192.168.180.10:8083/orion/stat/logging.do";
    //消息列表
    public static final String METHOD_MSG_CENTER = "/eagleeye_app/msg.do";
    //版本检查 
    public static final String METHOS_CHECK_VERSION = "http://api.fir.im/apps/latest/%s?api_token=%s";
    //获取我的界面配置
    public static final String METHOS_GET_CUSTOMDATA = "http://192.168.180.10:8082/demeter/my/getCustomData.do";

    /**
     * 因部署多个服务器
     *  登录 注册 验证码 需要处理处理
     * @param url
     * @return
     */
    public static String getUrl(String url) {
        if(METHOD_LOGIN.equals(url)){
            return METHOD_LOGIN;
        } else if(METHON_REGIST_URL.equals(url)){
            return METHON_REGIST_URL;
        } else if(METHOD_GET_CODE_URL.equals(url)){
            return METHOD_GET_CODE_URL;
        } else if(METHOD_FORGET_PWD.equals(url)){
            return METHOD_FORGET_PWD;
        } else if(METHOD_UPLOAD_IMG.equals(url)){
            return METHOD_UPLOAD_IMG;
        } else if(METHOD_STATISTICS.equals(url)){
            return METHOD_STATISTICS;
        } else if(METHOS_GET_CUSTOMDATA.equals(url)){
            return METHOS_GET_CUSTOMDATA;
        } else if(FRESH_TOKEN.equals(url)){
            return FRESH_TOKEN;
        } else if(METHOD_GET_HOTS.equals(url)){
            return METHOD_GET_HOTS;
        }
        return BASE_API_URL + url;
    }

    public static int getHttpsType(String url){
        if(url.contains("userCenter")){
            //用户中心
            return 1;
        }
        //业务
        return 2;
    }
}
