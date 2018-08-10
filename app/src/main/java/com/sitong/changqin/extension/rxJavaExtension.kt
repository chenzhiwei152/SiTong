package com.jyall.bbzf.extension

import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.jyall.bbzf.api.scheduler.CommonObserver
import com.jyall.bbzf.api.scheduler.ErrorResponseBean
import io.reactivex.Observable
import retrofit2.Response


/**
 * Created by cui.yan on 2018/7/11.
 * 简化联网请求回调 以lambda处理
 * 全部回调都处理版本
 */

fun <T> Observable<Response<T>>.callBack(success: (t: T)->(Unit), fail:(t:T) ->(Unit), error: (t:ErrorResponseBean)->(Unit)){

    val observer =  object : CommonObserver<T>(){
        override fun onError(errorResponseBean: ErrorResponseBean): Boolean {
            error(errorResponseBean)
            return false
        }

        override fun onFail(errorResponseBean: T): Boolean {
            fail(errorResponseBean)
            return false
        }

        override fun onSuccess(body: T) {
            success(body)
        }

    }
    compose(SchedulerUtils.ioToMain()).subscribe(observer)
}

/**
 * Created by cui.yan on 2018/7/11.
 * 简化联网请求回调 以lambda处理
 * 忽略erro版本
 */

fun <T> Observable<Response<T>>.callBack(success: (t: T)->(Unit), fail:(t:T) ->(Unit)){

    val observer =  object : CommonObserver<T>(){
        override fun onError(errorResponseBean: ErrorResponseBean): Boolean {
            return false
        }

        override fun onFail(errorResponseBean: T): Boolean {
            fail(errorResponseBean)
            return false
        }

        override fun onSuccess(body: T) {
            success(body)
        }

    }
    compose(SchedulerUtils.ioToMain()).subscribe(observer)
}

/**
 * Created by cui.yan on 2018/7/11.
 * 简化联网请求回调 以lambda处理
 * 只处理成功，用于不需要错误处理的情景
 */

fun <T> Observable<Response<T>>.callBack(success: (t: T)->(Unit)){

    val observer =  object : CommonObserver<T>(){
        override fun onError(errorResponseBean: ErrorResponseBean): Boolean {
            return false
        }

        override fun onFail(errorResponseBean: T): Boolean {
            return false
        }

        override fun onSuccess(body: T) {
            success(body)
        }

    }
    compose(SchedulerUtils.ioToMain()).subscribe(observer)
}