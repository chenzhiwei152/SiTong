package com.sitong.changqin.mvp.contract

import com.jyall.bbzf.base.IBaseView
import com.jyall.bbzf.base.IPresenter
import com.sitong.changqin.mvp.model.bean.UserInfo

interface MineContract {

    interface View : IBaseView {
        fun toast_msg(msg: String)
        fun getDataSuccess(bean: UserInfo)

    }

    interface Presenter : IPresenter<View> {

        fun getUserInfo()

        fun upLoadHeadImage(url:String)

    }
}