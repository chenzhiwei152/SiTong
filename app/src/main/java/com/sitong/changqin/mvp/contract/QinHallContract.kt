package com.sitong.changqin.mvp.contract

import com.jyall.bbzf.base.IBaseView
import com.jyall.bbzf.base.IPresenter
import com.sitong.changqin.mvp.model.bean.QinHallBean

interface QinHallContract {

    interface View : IBaseView {
        fun toast_msg(msg: String)
        fun getDataSuccess(list:ArrayList<QinHallBean>)

    }

    interface Presenter : IPresenter<View> {

        fun getHallList()

    }
}