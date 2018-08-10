package com.sitong.changqin.mvp.model

import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.jyall.bbzf.api.scheduler.APIManager
import com.jyall.bbzf.base.BaseBean
import com.jyall.bbzf.mvp.model.bean.UserInfo
import io.reactivex.Observable
import retrofit2.Response

class LoginModel {
    /**
     * 登录
     */
    fun login(cityId: String, phone: String,
              userRole: String): Observable<Response<BaseBean<UserInfo>>> {
        return APIManager.jyApi.logIn(cityId, phone, userRole).compose(SchedulerUtils.ioToMain())
    }

    fun sendCode( phone: String): Observable<Response<BaseBean<String>>> {
        return APIManager.jyApi.sendCode(phone).compose(SchedulerUtils.ioToMain())
    }

    fun checkCode(cityId: String, phone: String,
              shortCode: String): Observable<Response<BaseBean<UserInfo>>> {
        return APIManager.jyApi.checkCode(cityId, phone, shortCode).compose(SchedulerUtils.ioToMain())
    }
}