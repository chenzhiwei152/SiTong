package com.sitong.changqin.mvp.contract

import com.jyall.bbzf.base.IBaseView
import com.jyall.bbzf.base.IPresenter
import com.sitong.changqin.mvp.model.bean.VideoListBean

interface ArticleListContract {

    interface View : IBaseView {
        fun toast_msg(msg: String)
        fun getDataSuccess(list:ArrayList<VideoListBean>)

    }

    interface Presenter : IPresenter<View> {

        fun getList()

    }
}