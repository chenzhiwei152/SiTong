package com.sevenstringedzithers.sitong.mvp.contract

import com.jyall.bbzf.base.IBaseView
import com.jyall.bbzf.base.IPresenter
import com.sevenstringedzithers.sitong.mvp.model.bean.QinHallBean

interface QinHallContract {

    interface View : IBaseView {
        fun toast_msg(msg: String)
        fun getDataSuccess(list: ArrayList<QinHallBean>)
        fun setBelongSuccess()
    }

    interface Presenter : IPresenter<View> {

        fun getHallList()
        fun setBelong(map: HashMap<String, String>)
    }
}