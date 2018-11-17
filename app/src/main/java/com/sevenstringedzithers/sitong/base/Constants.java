package com.sevenstringedzithers.sitong.base;

/**
 * Created by sun.luwei on 2016/12/20.
 * jyallapp://m.jyall.com/openapp？type=****param=**
 */

public class Constants {
    public final static int SPLASHTIME = 3000;// 启动页时间

    // 密码最少位数
    public static final int PWD_MIN = 6;
    // 密码最多位数
    public static final int PWD_MAX = 20;

    public static final String STRING = "STRING";

    public static final String RESERVATION_JUMP_POSITION = "reservation_jump_position";//预约跳转

    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";


    public static final String TAG_TABLE = "tag_table";
    public static String WECHAT_APPID="wx00afa9dfda94b424";
    public static String WECHAT_SECRET="be9eb7e0e2f4f6211722054ebb6e0aa5";
    public static String SINA_APP_ID="2302616735";
    public static String WBAPP_KEY="aaf68edab767612aea24863b04db4074";
    public static String REDIRECT_URL="https://api.weibo.com/oauth2/default.html";
    public static String SCOPE="";
    public static String musicList="musicList";


    public static class Tag {
        //请自增往下写
        public static final int LOGIN_SUCCESS = 0x21;//登录成功
        public static final int CHANGE_PASSWORD_SUCCESS = 0x22;//修改密码成功
        public static final int UPDATE_USER_INFO = 0x23;//获取用户信息成功
        public static final int RELOAD_USERINFO = 0x24;//重新获取用户信息
        public static final int WX_SHARE = 0x25;//微信分享
        public static final int WX_LOGIN = 0x26;//微信登录
        public static final int WX_PAY = 0x27;//微信支付
        public static final int REGISTER_SUCCESS = 0x28;//注册成功
        public static final int REGISTER_FINISH = 0x29;//finish注册页面
    }

}