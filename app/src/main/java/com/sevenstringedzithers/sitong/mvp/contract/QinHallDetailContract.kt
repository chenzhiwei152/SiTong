package com.sevenstringedzithers.sitong.mvp.contract

import com.sevenstringedzithers.sitong.base.IBaseView
import com.sevenstringedzithers.sitong.base.IPresenter
import com.sevenstringedzithers.sitong.mvp.model.bean.QinguanDetailBean

interface QinHallDetailContract {

    interface View : IBaseView {
        fun toast_msg(msg: String)
        fun getDataSuccess(bean: QinguanDetailBean)

    }

    interface Presenter : IPresenter<View> {

        fun getList(map: HashMap<String, String>)

    }
}