package com.sevenstringedzithers.sitong.mvp.model

import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.jyall.bbzf.api.scheduler.APIManager
import com.jyall.bbzf.base.BaseBean
import com.sevenstringedzithers.sitong.mvp.model.bean.UserInfo
import io.reactivex.Observable
import retrofit2.Response

class RegisterModel {
    /**
     * 登录
     */
    fun login(map: HashMap<String,String>): Observable<Response<BaseBean<UserInfo>>> {
        return APIManager.jyApi.logIn(map).compose(SchedulerUtils.ioToMain())
    }

    fun sendCode( phone: String): Observable<Response<BaseBean<String>>> {
        return APIManager.jyApi.sendCode(phone).compose(SchedulerUtils.ioToMain())
    }

}