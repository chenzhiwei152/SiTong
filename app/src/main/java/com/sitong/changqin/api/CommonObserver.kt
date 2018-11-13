package com.jyall.bbzf.api.scheduler

import com.google.gson.Gson
import com.jyall.android.common.utils.LogUtils
import com.jyall.bbzf.base.BaseBean
import com.jyall.bbzf.base.BaseContext
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.Response

abstract class CommonObserver<T> : Observer<Response<T>> {
    var disposable: Disposable? = null

    constructor()

    override fun onComplete() {

    }

    override fun onSubscribe(disposable: Disposable?) {
        this.disposable = disposable
    }

    override fun onNext(response: Response<T>?) {
        when {
            ResponseCode.SUCCESS == 200 -> {
                try {

                    val baseBean = response?.body() as BaseBean<Any>

                    if (baseBean.isSuccess()) {
                        try {
                            onSuccess(response?.body())
                        } catch (e: Exception) {
                            LogUtils.e("Exception:", e.toString())
                        }

                    } else {
                        if (!onFail(response.body())) {
                            LogUtils.e(baseBean.message)
                        }

                    }
                } catch (e: Exception) {
                    onError(e)
                    LogUtils.e("Exception:", e.message)

                }


            }
            ResponseCode.BAD_REQUEST == response?.code() -> {
                var errorResponseBean: ErrorResponseBean
                try {
                    errorResponseBean = Gson().fromJson(response.errorBody().string(), ErrorResponseBean::class.java)
                    onError(errorResponseBean)
                } catch (e: Exception) {
                    onError(e)
                }


            }
            ResponseCode.SIGN_ERROR == response?.code() -> {
                var errorResponseBean: ErrorResponseBean
                try {
                    errorResponseBean = Gson().fromJson(response.errorBody().string(), ErrorResponseBean::class.java)
                    onError(errorResponseBean)
                } catch (e: Exception) {
                    onError(e)
                }

            }
            else -> run { onError(Exception("unknown error")) }
        }

    }

    abstract fun onError(errorResponseBean: ErrorResponseBean): Boolean

    //业务错误
    abstract fun onFail(errorResponseBean: T): Boolean

    abstract fun onSuccess(body: T)

    override fun onError(e: Throwable?) {
        LogUtils.e(e?.message)
        onError(ErrorResponseBean(-1, "网络连接断开", e?.message))
        if (e?.message!!.contains("token == null")){
            BaseContext.instance.logout()
        }
    }


    fun processCode(errorResponseBean: ErrorResponseBean): Boolean {
        when (errorResponseBean.code) {
            ResponseCode.ACCESS_TOKEN_INVALID -> {
                BaseContext.instance.logout()
            }
            ResponseCode.UPDATE_FORCE -> LogUtils.e(errorResponseBean.message)//TODO
            ResponseCode.TICKET_UNAVALIBLE -> LogUtils.e(errorResponseBean.message)//TODO
            ResponseCode.TICKET_UNAVALIBLE2 -> LogUtils.e(errorResponseBean.message)//TODO
            ResponseCode.TICKET_UNAVALIBLE3 -> LogUtils.e(errorResponseBean.message)//TODO
            ResponseCode.TOKEN_EMPTY -> LogUtils.e(errorResponseBean.message) //TODO
        }
        return false
    }
}