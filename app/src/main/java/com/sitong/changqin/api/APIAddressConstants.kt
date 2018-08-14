package com.jyall.bbzf.api.scheduler


/**
 * 接口地址
 */
object APIAddressConstants {


    /**
     * 获取基础地址，此地址是最主要的接口地址
     */
//    const val baseUrl: String = "server.7stringedzithers.com"
    const val baseUrl: String = "test.server.7stringedzithers.com"
    val APP_HOST = "http://$baseUrl"
    const val USER_API: String = "/user"

    //    登陆
    const val USER_LOGIN: String = "$USER_API/login/"

    /*
        * 上传文件
        * */
    const val UPLOAD_FILES = "$USER_API/base64Upload.do"
    /*
        * 版本升级
        * */
    const val CHECK_VERSION = "$USER_API/basic/getAppVersion.do"

    /*---------------------------------------------登录注册相关start-------------------------*/
    /**
     *  发送短信验证码
     */
    const val SEND_SHORT_MESSAGE = "$USER_API/sendcode/{phone}"
    /**
     *  校验短信验证码
     */
    const val CHECK_SHORT_MESSAGE = "$USER_API/register/"


    /*---------------------------------------------登录注册相关end-------------------------*/
/*
* 获取曲目列表
* 1：首页列表；2：收藏列表
* */
    const val GET_MUSIC_LIST = "/music/getmusiclist/{listtype}"
}
