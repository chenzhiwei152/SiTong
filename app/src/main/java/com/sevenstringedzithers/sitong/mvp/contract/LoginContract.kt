package com.sevenstringedzithers.sitong.mvp.contract

import com.sevenstringedzithers.sitong.base.IBaseView
import com.sevenstringedzithers.sitong.base.IPresenter
import com.sevenstringedzithers.sitong.mvp.model.bean.UserInfo

interface LoginContract {

    interface View : IBaseView {
        fun toast_msg(msg: String)
        fun loginSuccess(user: UserInfo)
    }

    interface Presenter : IPresenter<View> {

        fun login(map:HashMap<String,String>)

    }
}