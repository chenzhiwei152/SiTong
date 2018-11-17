package com.jyall.bbzf.api.scheduler

object ResponseCode {

    /**
     * 业务正确
     */
    val SUCCESS = 200
    /**
     * 业务错误
     */
    val BAD_REQUEST = 400
    /**
     * 签名校验错误
     */
    val SIGN_ERROR = 406



    val UPDATE_FORCE = 201
    val ACCESS_TOKEN_INVALID = -2001//token失效
    val TICKET_UNAVALIBLE = 400000023//ticket无效的请求参数
    val TICKET_UNAVALIBLE2 = 400000024//ticket无效的请求参数
    val TICKET_UNAVALIBLE3 = 400000002//ticket不存在或者已经失效
    val TOKEN_EMPTY = 400001008//TOKEN_EMPTY
    val LOGIN_DUPLICATE = 400001045//用户在其他设备，您已经被踢掉



    /**
     * 业务正确
     */
    val OPTION_SUCCESS = 0
    /**
     * 登录成功
     */
    val LOGIN_SUCCESS = 10000
    /**
     * 用户未注册
     */
    val USER_UNREGIST = 10001
    /**
     * 用户未注册，经纪人白名单存在
     */
    val USER_WHITE_LIST = 10002
    /**
     * 短信验证码发送成功
     */
    val SEND_CODE_SUCCESS = 10003
    /**
     * 短信验证码发送失败
     */
    val SEND_CODE_FAIL = 10004
    /**
     * 短信验证码错误
     */
    val CHECK_CODE_ERROR = 10005

}