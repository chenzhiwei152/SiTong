package com.sitong.changqin.mvp.contract

import com.jyall.bbzf.base.IBaseView
import com.jyall.bbzf.base.IPresenter
import com.sitong.changqin.mvp.model.bean.UserInfo

interface RegisterContract {

    interface View : IBaseView {
        fun toast_msg(msg: String)
        fun registerSuccess(userInfo: UserInfo)
    }

    interface Presenter : IPresenter<View> {
        /**
         * 发送验证码
         */
        fun sendCode(phone: String)

        fun register(map: HashMap<String,String>)
    }
}