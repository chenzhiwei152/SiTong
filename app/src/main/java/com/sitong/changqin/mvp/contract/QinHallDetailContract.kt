package com.sitong.changqin.mvp.contract

import com.jyall.bbzf.base.IBaseView
import com.jyall.bbzf.base.IPresenter
import com.sitong.changqin.mvp.model.bean.QinHallDetailBean
import com.sitong.changqin.mvp.model.bean.QinguanDetailBean
import com.sitong.changqin.mvp.model.bean.VideoListBean

interface QinHallDetailContract {

    interface View : IBaseView {
        fun toast_msg(msg: String)
        fun getDataSuccess(bean: QinguanDetailBean)

    }

    interface Presenter : IPresenter<View> {

        fun getList(map: HashMap<String, String>)

    }
}