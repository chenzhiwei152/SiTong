package com.jyall.bbzf.api.scheduler

import com.jyall.android.common.utils.LogUtils
import com.sevenstringedzithers.sitong.base.BaseBean
import com.sevenstringedzithers.sitong.base.BaseContext
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

                    } else if (baseBean.statuscode == ResponseCode.ACCESS_TOKEN_INVALID) {
                        BaseContext.instance.logout()
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
//            ResponseCode.ACCESS_TOKEN_INVALID == response?.code() -> {
//                try {
//                    BaseContext.instance.logout()
//                } catch (e: Exception) {
//                    onError(e)
//                }
//
//            }
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
    }


    fun processCode(errorResponseBean: ErrorResponseBean): Boolean {
        when (errorResponseBean.code) {
            ResponseCode.ACCESS_TOKEN_INVALID -> {
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