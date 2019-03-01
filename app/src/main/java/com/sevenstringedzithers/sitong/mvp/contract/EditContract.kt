package com.sevenstringedzithers.sitong.mvp.contract

import com.sevenstringedzithers.sitong.base.IBaseView
import com.sevenstringedzithers.sitong.base.IPresenter
import com.sevenstringedzithers.sitong.mvp.model.bean.UserInfo

interface EditContract {

    interface View : IBaseView {
        fun toast_msg(msg: String)
        fun getDataSuccess(bean: UserInfo)

    }

    interface Presenter : IPresenter<View> {

        fun updateUserInfo(map:HashMap<String,String>)

    }
}