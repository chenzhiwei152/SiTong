package com.sevenstringedzithers.sitong.mvp.model

import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.jyall.bbzf.api.scheduler.APIManager
import com.sevenstringedzithers.sitong.base.BaseBean
import com.sevenstringedzithers.sitong.mvp.model.bean.FindPawSuccessBean
import com.sevenstringedzithers.sitong.mvp.model.bean.UserInfo
import io.reactivex.Observable
import retrofit2.Response

class LoginModel {
    /**
     * 登录
     */
    fun login(map: HashMap<String,String>): Observable<Response<BaseBean<UserInfo>>> {
        return APIManager.jyApi.logIn(map).compose(SchedulerUtils.ioToMain())
    }

    fun sendCode(phone: String): Observable<Response<BaseBean<String>>> {
        return APIManager.jyApi.sendCode(phone).compose(SchedulerUtils.ioToMain())
    }

    fun register(map: HashMap<String,String>): Observable<Response<BaseBean<UserInfo>>> {
        return APIManager.jyApi.register(map).compose(SchedulerUtils.ioToMain())
    }
    fun findPW(map: HashMap<String,String>): Observable<Response<BaseBean<FindPawSuccessBean>>> {
        return APIManager.jyApi.findPW(map).compose(SchedulerUtils.ioToMain())
    }

}