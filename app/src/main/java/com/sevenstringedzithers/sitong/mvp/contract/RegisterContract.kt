package com.sevenstringedzithers.sitong.mvp.contract

import com.sevenstringedzithers.sitong.base.IBaseView
import com.sevenstringedzithers.sitong.base.IPresenter
import com.sevenstringedzithers.sitong.mvp.model.bean.UserInfo

interface RegisterContract {

    interface View : IBaseView {
        fun toast_msg(msg: String)
        fun registerSuccess(userInfo: UserInfo)
        fun sendCodeSuccess()
    }

    interface Presenter : IPresenter<View> {
        /**
         * 发送验证码
         */
        fun sendCode(phone: String)

        fun register(map: HashMap<String,String>)

        fun findPW(map: HashMap<String,String>)
    }
}