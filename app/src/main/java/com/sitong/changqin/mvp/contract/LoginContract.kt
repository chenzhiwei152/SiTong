package com.sitong.changqin.mvp.contract

import com.jyall.bbzf.base.IBaseView
import com.jyall.bbzf.base.IPresenter

interface LoginContract {

    interface View : IBaseView {
        fun toast_msg(msg: String)
    }

    interface Presenter : IPresenter<View> {
        /**
         * 发送验证码
         */
        fun sendCode(phone: String)

    }
}