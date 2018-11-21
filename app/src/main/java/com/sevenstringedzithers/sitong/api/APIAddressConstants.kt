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
    const val CHECK_VERSION = "/system/isforceupdate/"

    /*---------------------------------------------登录注册相关start-------------------------*/
    /**
     *  发送短信验证码
     */
    const val SEND_SHORT_MESSAGE = "$USER_API/sendcode/{phone}"
    /**
     *  注册
     */
    const val CHECK_SHORT_MESSAGE = "$USER_API/register/"
    /*
    * 找回密码
    * */
    const val FIND_PASSWORD = "$USER_API/resetpassword/"
    /*---------------------------------------------登录注册相关end-------------------------*/
/*
* 获取曲目列表
* 1：首页列表；2：收藏列表
* */
    const val GET_MUSIC_LIST = "/music/getmusiclist/{listtype}"

    /*
    * 获取曲目详情
    * */
    const val GET_MUSIC_DETAIL = "/music/getmusicinfo/"

    /*
    * 获取琴馆列表
    * */
    const val GET_HALL_LIST = "/carillon/getcarillonlist/"

    /*
    * 获取视频列表
    * */
    const val GET_VIDEO_LIST = "/infomation/videolist/"

    /*
    * 获取文章列表
    * */
    const val GET_ARTICLE_LIST = "/infomation/articlelist/"

    /*
    * 获取个人信息
    * */
    const val GET_USER_INFO = "/user/getuserinfo/"

    /*
    * 更新用户信息
    * */
    const val UPDATE_USER_INFOs = "/user/saveuserinfo/"


    /*
    * 获取任务
    * */
    const val GET_TASK = "/task/gettasks/{type}"


    /*获取练习记录
    * */
    const val GET_EXERCISE_RECORD = "/music/getpracticelist/"

    /*上传意见反馈图片
    * */
    const val upload_feedback_image = "/system/uploadimage/"


    /*提交意见反馈
   * */
    const val UPLOAD_FEEDBACK = "/system/feedback/"

    /*
    * 获取，下载oos权限
    * */
    const val GET_OOS_PERMESSITION = "/music/downloadmusic/"

    /*上传头像图片
       * */
    const val UPLOAD_HEAD = "/user/uploaduheader/"

    /*获取消息列表
      * */
    const val GET_MESSAGE = "/message/getmessages/"


    /*设置已读消息
      * */
    const val SET_MESSAGE_READ = "/message/readmessage/"

    /*领奖
      * */
    const val GET_REWARD = "/user/getaward/"

    /*收藏
      * */
    const val COLLECTION = "/music/setcollectionstate/"

    /*琴馆详情
      * */
    const val QINGUAN_DETAIL = "/carillon/getcarillondetail/"
    /*文章详情
      * */
    const val ARTICLE_DETAIL = "/infomation/articledetail/"
    /*
    * 获取会员列表*/
    const val GET_MESSBER_LIST = "/system/getmemberinfo/{type}"
    /*
    * 获取会员列表*/
    const val GET_MESSBER_LIST2 = "/user/pricelist/"

    /*
    * 下单
    * */
    const val GET_ORDER = "/music/placeorder/"

    /*获取支付宝鉴权*/
    const val GET_ali_premissition = "/music/getalipaykey/{orderid}"

    /*获取微信鉴权*/
    const val GET_wx_premissition = "/music/getweixinpaykey/{orderid}"

    /*打卡*/
    const val DAILY_PUNCH = "/task/punchtheclock/"

    /*
    获取练琴记录
    * */
    const val EXERCISE_RECORD_LIST = "/music/getpracticelist/"
    /*
    获取常见问题记录
    * */
    const val NOMAL_PROBLEM_LIST = "/system/getquestionslist/"
    /*
    获取常见问题详情
    * */
    const val NOMAL_PROBLEM_DETAIL = "/system/getsolutions/"
}
