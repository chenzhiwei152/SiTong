package com.jyall.bbzf.api.scheduler

import com.jyall.bbzf.api.scheduler.APIAddressConstants.CHECK_SHORT_MESSAGE
import com.jyall.bbzf.api.scheduler.APIAddressConstants.SEND_SHORT_MESSAGE
import com.jyall.bbzf.api.scheduler.APIAddressConstants.USER_LOGIN
import com.jyall.bbzf.base.BaseBean
import com.jyall.bbzf.mvp.model.bean.UserInfo
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

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
    @GET(USER_LOGIN)
    fun logIn(@Query("cityId") cityId: String, @Query("userPhone") phone: String,
              @Query("userRole") shortCode: String): Observable<Response<BaseBean<UserInfo>>>

    /**
     *  发送验证码
     */
    @GET(SEND_SHORT_MESSAGE)
    fun sendCode(@Query("phone") phone: String): Observable<Response<BaseBean<String>>>

    /**
     *  校验验证码
     */
    @GET(CHECK_SHORT_MESSAGE)
    fun checkCode(@Query("cityId") cityId: String,
                  @Query("phone") phone: String,
                  @Query("shortCode") shortCode: String): Observable<Response<BaseBean<UserInfo>>>

    /*---------------------------------------------登录注册相关end-------------------------*/


}