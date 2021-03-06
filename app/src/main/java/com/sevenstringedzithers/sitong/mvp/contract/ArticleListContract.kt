package com.sevenstringedzithers.sitong.mvp.contract

import com.sevenstringedzithers.sitong.base.IBaseView
import com.sevenstringedzithers.sitong.base.IPresenter
import com.sevenstringedzithers.sitong.mvp.model.bean.QinguanDetailBean
import com.sevenstringedzithers.sitong.mvp.model.bean.VideoListBean

interface ArticleListContract {

    interface View : IBaseView {
        fun toast_msg(msg: String)
        fun getDataSuccess(list: ArrayList<VideoListBean>)
        fun getArticleDetailSuccess(bean: QinguanDetailBean)
    }

    interface Presenter : IPresenter<View> {

        fun getList()

        fun getDetail(map: HashMap<String, String>)

    }
}