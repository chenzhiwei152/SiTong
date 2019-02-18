package com.jyall.bbzf.api.scheduler

import com.jyall.bbzf.api.scheduler.APIAddressConstants.ARTICLE_DETAIL
import com.jyall.bbzf.api.scheduler.APIAddressConstants.CHECK_SHORT_MESSAGE
import com.jyall.bbzf.api.scheduler.APIAddressConstants.COLLECTION
import com.jyall.bbzf.api.scheduler.APIAddressConstants.DAILY_PUNCH
import com.jyall.bbzf.api.scheduler.APIAddressConstants.EXERCISE_RECORD_LIST
import com.jyall.bbzf.api.scheduler.APIAddressConstants.FIND_PASSWORD
import com.jyall.bbzf.api.scheduler.APIAddressConstants.GET_ARTICLE_LIST
import com.jyall.bbzf.api.scheduler.APIAddressConstants.GET_EXERCISE_RECORD
import com.jyall.bbzf.api.scheduler.APIAddressConstants.GET_HALL_LIST
import com.jyall.bbzf.api.scheduler.APIAddressConstants.GET_MESSAGE
import com.jyall.bbzf.api.scheduler.APIAddressConstants.GET_MESSBER_LIST
import com.jyall.bbzf.api.scheduler.APIAddressConstants.GET_MESSBER_LIST2
import com.jyall.bbzf.api.scheduler.APIAddressConstants.GET_MUSIC_DETAIL
import com.jyall.bbzf.api.scheduler.APIAddressConstants.GET_MUSIC_LIST
import com.jyall.bbzf.api.scheduler.APIAddressConstants.GET_OOS_PERMESSITION
import com.jyall.bbzf.api.scheduler.APIAddressConstants.GET_ORDER
import com.jyall.bbzf.api.scheduler.APIAddressConstants.GET_REWARD
import com.jyall.bbzf.api.scheduler.APIAddressConstants.GET_TASK
import com.jyall.bbzf.api.scheduler.APIAddressConstants.GET_USER_INFO
import com.jyall.bbzf.api.scheduler.APIAddressConstants.GET_VIDEO_LIST
import com.jyall.bbzf.api.scheduler.APIAddressConstants.GET_ali_premissition
import com.jyall.bbzf.api.scheduler.APIAddressConstants.GET_wx_premissition
import com.jyall.bbzf.api.scheduler.APIAddressConstants.NOMAL_PROBLEM_DETAIL
import com.jyall.bbzf.api.scheduler.APIAddressConstants.NOMAL_PROBLEM_LIST
import com.jyall.bbzf.api.scheduler.APIAddressConstants.QINGUAN_DETAIL
import com.jyall.bbzf.api.scheduler.APIAddressConstants.SEND_SHORT_MESSAGE
import com.jyall.bbzf.api.scheduler.APIAddressConstants.SET_BELONG
import com.jyall.bbzf.api.scheduler.APIAddressConstants.SET_MESSAGE_READ
import com.jyall.bbzf.api.scheduler.APIAddressConstants.UPDATE_USER_INFOs
import com.jyall.bbzf.api.scheduler.APIAddressConstants.UPLOAD_EXERCISE_RECRD
import com.jyall.bbzf.api.scheduler.APIAddressConstants.UPLOAD_FEEDBACK
import com.jyall.bbzf.api.scheduler.APIAddressConstants.UPLOAD_HEAD
import com.jyall.bbzf.api.scheduler.APIAddressConstants.UPLOAD_MESSAGE_TOKEN
import com.jyall.bbzf.api.scheduler.APIAddressConstants.UPLOAD_SHARE_RECORD
import com.jyall.bbzf.api.scheduler.APIAddressConstants.USER_LOGIN
import com.jyall.bbzf.api.scheduler.APIAddressConstants.upload_feedback_image
import com.jyall.bbzf.base.BaseBean
import com.sevenstringedzithers.sitong.base.OSSPermissionBean
import com.sevenstringedzithers.sitong.mvp.model.bean.*
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Jyapi {
    /**
     * get secret
     */
//    @POST(APIAddressConstants.GET_SECRET)
//    fun getSecret(@Body param: SecretParam): Observable<Response<BaseBean<SecretKey>>>
    /**
     *  版本升级
     */
    @GET(APIAddressConstants.CHECK_VERSION)
    fun checkVersion(): Observable<Response<BaseBean<AppVersionBean>>>

    //    上传图片
//    @Multipart
//    @POST(APIAddressConstants.UPLOAD_FILES)
//    fun uploadFiles(@Part file: List<MultipartBody.Part>): Observable<Response<BaseBean<UploadImageResult>>>

    /*---------------------------------------------登录注册相关end-------------------------*/
    /**
     *  登录
     */
    @POST(USER_LOGIN)
    fun logIn(@Body queryMap: HashMap<String, String>): Observable<Response<BaseBean<UserInfo>>>

    /**
     *  发送验证码
     */
    @GET(SEND_SHORT_MESSAGE)
    fun sendCode(@Path("phone") phone: String): Observable<Response<BaseBean<String>>>

    /**
     *  注册
     */
    @POST(CHECK_SHORT_MESSAGE)
    fun register(@Body map: HashMap<String, String>): Observable<Response<BaseBean<UserInfo>>>


    /**
     *  找回密码
     */
    @POST(FIND_PASSWORD)
    fun findPW(@Body map: HashMap<String, String>): Observable<Response<BaseBean<String>>>

    /*---------------------------------------------登录注册相关end-------------------------*/
/*
* 音乐列表
* */
    @GET(GET_MUSIC_LIST)
    fun getMusicList(@Path("listtype") listtype: String): Observable<Response<BaseBean<ArrayList<MusicBean>>>>

    /*
    * 音乐详情
    * */
    @POST(GET_MUSIC_DETAIL)
    fun getMusicDetail(@Body map: HashMap<String, String>): Observable<Response<BaseBean<MusicDetailBean>>>

    /*
    * 琴馆列表
    * */
    @GET(GET_HALL_LIST)
    fun getHallList(): Observable<Response<BaseBean<ArrayList<QinHallBean>>>>

    /*
    * 设置琴管归属
    * */
    @POST(SET_BELONG)
    fun setBelong(@Body map: HashMap<String, String>): Observable<Response<BaseBean<Boolean>>>

    /*
    * 视频列表
    * */
    @GET(GET_VIDEO_LIST)
    fun getVideoList(): Observable<Response<BaseBean<ArrayList<VideoListBean>>>>

    /*
    * 文章列表
    * */
    @GET(GET_ARTICLE_LIST)
    fun getArticleList(): Observable<Response<BaseBean<ArrayList<VideoListBean>>>>

    /*
    * 个人信息
    * */
    @GET(GET_USER_INFO)
    fun getUserInfo(): Observable<Response<BaseBean<UserInfo>>>

    /*
    * 更新用户信息
    * */
    @POST(UPDATE_USER_INFOs)
    fun updateUserInfo(@Body map: HashMap<String, String>): Observable<Response<BaseBean<UserInfo>>>

    /*
    * 获取任务
    * */
    @GET(GET_TASK)
    fun getTasks(@Path("type") type: String): Observable<Response<BaseBean<ArrayList<TaskBean>>>>


    /*
    * 获取练习记录
    * */
    @GET(GET_EXERCISE_RECORD)
    fun getExerciseRecord(): Observable<Response<BaseBean<ExerciseRecordBean>>>

    /*
    * 上传意见反馈图片
    * */
    @POST(upload_feedback_image)
    fun upLoadFeedbackImage(@Body map: HashMap<String, String>): Observable<Response<BaseBean<OSSPermissionBean>>>

    /*
    * 上传头像图片
    * */
    @POST(UPLOAD_HEAD)
    fun uploadHeadImage(@Body map: HashMap<String, String>): Observable<Response<BaseBean<OSSPermissionBean>>>

    /*
    * 上传意见反馈
    * */
    @POST(UPLOAD_FEEDBACK)
    fun uploadFeedBack(@Body map: HashMap<String, String>): Observable<Response<BaseBean<ResultBean>>>


    /*
    * 获取，下载  oos权限
    * */
    @POST(GET_OOS_PERMESSITION)
    fun getOOSPremession(): Observable<Response<BaseBean<OSSPermissionBean>>>

    /*
    * 获取消息列表*/
    @POST(GET_MESSAGE)
    fun getMessageList(@Body map: HashMap<String, String>): Observable<Response<BaseBean<ArrayList<MessageListBean>>>>


    /*
    * 设置消息已读
    * */
    @POST(SET_MESSAGE_READ)
    fun setMessageRead(@Body map: HashMap<String, String>): Observable<Response<BaseBean<ResultBean>>>

    /*
    * 领奖*/
    @POST(GET_REWARD)
    fun getReward(@Body map: HashMap<String, String>): Observable<Response<BaseBean<GetRewardBean>>>


    /*
    * 收藏*/
    @POST(COLLECTION)
    fun collection(@Body map: HashMap<String, String>): Observable<Response<BaseBean<ResultBean>>>

    /*
    * 琴馆详情*/
    @POST(QINGUAN_DETAIL)
    fun get_qinguan_detail(@Body map: HashMap<String, String>): Observable<Response<BaseBean<QinguanDetailBean>>>

    /* 文章详情
    */
    @POST(ARTICLE_DETAIL)
    fun get_article_detail(@Body map: HashMap<String, String>): Observable<Response<BaseBean<QinguanDetailBean>>>

    /*
    * 获取会员列表*/
    @GET(GET_MESSBER_LIST)
    fun get_member_list(@Path("type") type: String): Observable<Response<BaseBean<ArrayList<MemberBean>>>>

    /*
    * 获取会员列表*/
    @POST(GET_MESSBER_LIST2)
    fun get_member_list2(@Body map: HashMap<String, String>): Observable<Response<BaseBean<ArrayList<MemberBean>>>>

    /*
    * 下单*/
    @POST(GET_ORDER)
    fun get_order(@Body map: HashMap<String, String>): Observable<Response<BaseBean<OrderBean>>>

    /*
   * 获取支付宝鉴权*/
    @GET(GET_ali_premissition)
    fun get_ali_premission(@Path("orderid") orderid: String): Observable<Response<BaseBean<AliPremissionBean>>>

    /*
   * 获取微信鉴权*/
    @GET(GET_wx_premissition)
    fun get_wx_premission(@Path("orderid") orderid: String): Observable<Response<BaseBean<WXPremissionBean>>>

    /*打卡
    * */
    @POST(DAILY_PUNCH)
    fun daily_punch(): Observable<Response<BaseBean<ResultBean>>>

    /*打卡
    * */
    @GET(EXERCISE_RECORD_LIST)
    fun get_exe_record(): Observable<Response<BaseBean<ExerciseRecordBean>>>

    /*
    * 获取问题列表
    * */
    @POST(NOMAL_PROBLEM_LIST)
    fun get_problem_list(): Observable<Response<BaseBean<ArrayList<QuestionListBean>>>>

    /*
    * 获取问题列表
    * */
    @POST(NOMAL_PROBLEM_DETAIL)
    fun get_problem_detail(@Body map: HashMap<String, String>): Observable<Response<BaseBean<ArrayList<QuestionDetailBean>>>>

    /*
    * 上传练琴记录
    * */
    @POST(UPLOAD_EXERCISE_RECRD)
    fun upload_exercise_record(@Body map: HashMap<String, String>): Observable<Response<BaseBean<ResultBean>>>

    /*
    * 上传分享录音文件
    * */
    @POST(UPLOAD_SHARE_RECORD)
    fun upload_share_record(): Observable<Response<BaseBean<ResultBean>>>
   /*
    * 上传分享录音文件
    * */
    @POST(UPLOAD_MESSAGE_TOKEN)
    fun upload_message_token(@Body map: HashMap<String, String>): Observable<Response<BaseBean<String>>>
}