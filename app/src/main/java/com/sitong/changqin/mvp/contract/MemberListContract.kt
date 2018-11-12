package com.sitong.changqin.mvp.contract

import com.jyall.bbzf.base.IBaseView
import com.jyall.bbzf.base.IPresenter
import com.sitong.changqin.mvp.model.bean.MemberBean
import com.sitong.changqin.mvp.model.bean.VideoListBean

interface MemberListContract {

    interface View : IBaseView {
        fun toast_msg(msg: String)
        fun getDataSuccess(list:ArrayList<MemberBean>)

    }

    interface Presenter : IPresenter<View> {

        fun getList(map:HashMap<String,String>)

    }
}