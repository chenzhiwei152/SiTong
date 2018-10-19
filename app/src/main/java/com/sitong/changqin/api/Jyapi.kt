package com.jyall.bbzf.api.scheduler

import com.jyall.bbzf.api.scheduler.APIAddressConstants.CHECK_SHORT_MESSAGE
import com.jyall.bbzf.api.scheduler.APIAddressConstants.GET_ARTICLE_LIST
import com.jyall.bbzf.api.scheduler.APIAddressConstants.GET_HALL_LIST
import com.jyall.bbzf.api.scheduler.APIAddressConstants.GET_MUSIC_DETAIL
import com.jyall.bbzf.api.scheduler.APIAddressConstants.GET_MUSIC_LIST
import com.jyall.bbzf.api.scheduler.APIAddressConstants.GET_VIDEO_LIST
import com.jyall.bbzf.api.scheduler.APIAddressConstants.SEND_SHORT_MESSAGE
import com.jyall.bbzf.api.scheduler.APIAddressConstants.USER_LOGIN
import com.jyall.bbzf.base.BaseBean
import com.sitong.changqin.mvp.model.bean.*
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
//    @GET(APIAddressConstants.CHECK_VERSION)
//    fun checkVersion(@Query("updateType") updateType: String): Observable<Response<BaseBean<AppVersionBean>>>

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
    * 视频列表
    * */
    @GET(GET_VIDEO_LIST)
    fun getVideoList(): Observable<Response<BaseBean<ArrayList<VideoListBean>>>>

    /*
    * 文章列表
    * */
    @GET(GET_ARTICLE_LIST)
    fun getArticleList(): Observable<Response<BaseBean<ArrayList<VideoListBean>>>>
}