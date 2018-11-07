package com.sitong.changqin.base;

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

    public static class Tag {
        //请自增往下写
        public static final int REGIST_SUCCESS = 0x21;//注册成功
        public static final int CHANGE_PASSWORD_SUCCESS = 0x22;//修改密码成功
        public static final int UPDATE_USER_INFO = 0x23;//获取用户信息成功
        public static final int RELOAD_USERINFO = 0x24;//重新获取用户信息
    }

}