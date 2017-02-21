package com.shuqu.microcredit.Content;

/**
 * Class Info ：param key
 * Created by Lyndon on 16/5/31.
 */
public class ParamKeys {

    public static final String MAIN_TAG_KEY = "mainTagKey";
    public static final String MAIN_TAG_REFRESH = "mainTagRefresh";

    public static final String APPID = "4";
    public static final String METHODID = "MethodId";
    public static final String WEBURL = "webUrl";
    public static final String TABWEBPARAM = "tabWebParams";
    public static final String HIDEFRAGMENTHEAD = "hideFragmentHead";
    public static final String INTENT_FROM = "intent_from";
    public static final String INTENT_FROM_JS = "intent_from_js";
    public static final String NET_DATA = "net_data";
    public static final String USER_NAME = "user_name";
    public static final String USER_PHONE = "user_phone";
    /*
	 * push
	 */
    public static final String PUSH_TYPE = "push_type";
    public static final String PUSH_ID = "push_id";
    public static final String PUSH_UID = "push_uid";
    public static final String PUSH_MESSAGE = "push_message";
    public static final String PUSH_PARAM = "push_param";
    public static final String PUSH_TITLE = "push_title";
    public static final String PUSH_URL = "url";
    public static final String FROM_PUSH = "frompush";

    public static final String NOTIFICATION_OPEN_ACTION = "com.shuqu.microcredit.notification_open";


    public static final String USER_AUTHENTICATION_STATE = "user_authentication_state";
    public static final String USER_UPLOAD_BANK_CARD_STATE = "user_upload_bank_card_state";
    public static final String USER_LIVING_BODY_IDENFY = "user_living_body_idenfy";
    public static final String UPLOAD_IMG_RESULT = "upload_img_result";

    public static final String TOKEN_FRESH_TIME = "token_fresh_time";

    //umeng
    public static final String UMENG_PUSH_TOKEN = "umeng_push_token";

    //certificate file name
    public static final String CERT_NAME = "uwca.crt";
    public static final String KEYSTORE_NAME = "uwca.jks";
    public static final String KEYSTORE_PWD = "123456";



    //channel
    public static final String CHANNEL_NAME_KEY = "UMENG_CHANNEL";
    public static final String CHANNEL_ID_KEY = "CHANNEL_ID";

    //action
    public static final String ACTION_PAY_SUCCESS = "com.credit100.pay.success";
    public static final String ACTION_PAY_CANCEL = "com.credit100.pay.cancel";
    public static final String ACTION_PAY_FAIL = "com.credit100.pay.fail";

    //支付相关
    public static final String ALIPAY = "alipay";
    public static final String WECHAT = "wechat";
    public static final int TYPE_ALIPAY = 1;
    public static final int TYPE_WECHAT = 2;


    public static final int NET_ERROR_CODE = -1;
    public static final int NET_SUCCESS_CODE = 0;

    //状态码
    public static final int JS_SUCCESS = 200;
    public static final int JS_FAILURE = 201; //取消

    //登陆相关
    public static final int LOGIN_SUCESS = 0; //登录成功

    //验证码
    public static final int REGIST_GET_CODE_SUCESS = 0; //验证码发送成功

    //注册
    public static final int REGIST_SUCESS = 0; //注册成功
    public static final int REGIST_CANCEL = 10001; //注册界面点已有账号

    //忘记密码
    public static final int FORGET_PWD_SUCESS = 0; //修改密码成功
    //上次请求刷新supportHost文件日期
    public static final String LAST_NET_SUPPORT_HOST_TIME = "lastNetSupportHostTime";

    //筛选
    public static final String SINGLE_SELECTOR_TAG = "singleSelectorTag";
    public static final String MULTI_SELECTOR_KEY = "multiSelectorKey";
    public static final String DIALOG_SINGLE_FILTER_KEY = "dialogSingleFilterKey";
    public static final String DIALOG_MULTI_FILTER_KEY = "dialogMultiFilterKey";
    public static final String CITY_SELECTE_KEY = "citySelecteKey";

    public static final String CITY_DATA_NAME = "city.json";

    //城市选择,item类型
    public static final int CITY_DATA_TYPE_FOCUS = 101;
    public static final int CITY_DATA_TYPE_HOT = 102;
    public static final int CITY_DATA_TYPE_LIST = 103;

    public static final String ICON_SOUSE_BASE64 = "iconSourceBase64";
    public static final String ICON_SOUSE_URL = "iconSourceUrl";
    public static final String ICON_SOUSE_APP = "iconSourceApp";

    public static final String HISTORY_BACK_REFRESH = "historyBackRefresh";
    //首页抽屉lastData
    public static final String HOME_DRAWER_LAST_DATA = "homeDrawerLastData";
}
