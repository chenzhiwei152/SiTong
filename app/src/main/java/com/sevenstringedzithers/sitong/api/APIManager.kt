package com.jyall.bbzf.api.scheduler

import com.jyall.android.common.utils.LogUtils
import com.jyall.android.common.utils.MyDeviceInfo
import com.jyall.android.common.utils.SysUtils
import com.jyall.bbzf.base.BaseContext
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object APIManager {
    val jyApi: Jyapi by lazy { getRetofit()!!.create(Jyapi::class.java!!) }
    private var retrofit: Retrofit? = null
    private val okHttpClient by lazy { initOkHttp() }

    private fun getRetofit(): Retrofit? {
        if (null == retrofit) {
            synchronized(APIManager::class.java) {
                if (retrofit == null) {
                    retrofit = Retrofit.Builder()
                            .baseUrl(APIAddressConstants.APP_HOST)
                            .client(okHttpClient)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                }
            }

        }
        return retrofit
    }

    private fun initOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(20000L, TimeUnit.MILLISECONDS)//超时
                .readTimeout(20000L, TimeUnit.MILLISECONDS)
                .addInterceptor(getCommonHeaderInterceptor())//配置通用header
                .addInterceptor(getLogInterceptor())//打印http整个请求的Log
                .build()
    }

    /**
     * 打印retrofit日志
     */
    private fun getLogInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> LogUtils.e("RetrofitLog", "retrofitBack = $message") }).setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    /**
     * 增加通用的请求头
     * 签名元素：gid  timestamp  tokenId（有则放没有不放），请求参数，requestPathUrl，requestBody（post、put请求有）
     *
     *m
     */
    private fun getCommonHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                    .header("token", getToken())
                    .header("platform", "Android")// 平台
                    .header("version", SysUtils.getVersionName(BaseContext.instance))// 版本号
//                    .header("channel", getChannel())
//                    .header("Authorization", getAuthorization(original))
                    .header("uuid", MyDeviceInfo.getDeviceId(BaseContext.instance))//设备id
//                    .header("devicebrand", MyDeviceInfo.getDeviceName())//设备型号
//                    .header("systembrand",MyDeviceInfo.getOsVersion())//操作系统版本
            val request = requestBuilder.build()
            chain.proceed(request)

        }
    }


    fun getToken(): String {
        return if (BaseContext.instance.getUserInfo() != null) {
            BaseContext.instance.getUserInfo()!!.token
        } else ""

    }



}