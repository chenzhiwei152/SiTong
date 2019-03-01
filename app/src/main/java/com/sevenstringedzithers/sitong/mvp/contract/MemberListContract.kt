package com.sevenstringedzithers.sitong.mvp.contract

import com.sevenstringedzithers.sitong.base.IBaseView
import com.sevenstringedzithers.sitong.base.IPresenter
import com.sevenstringedzithers.sitong.mvp.model.bean.MemberBean

interface MemberListContract {

    interface View : IBaseView {
        fun toast_msg(msg: String)
        fun getDataSuccess(list:ArrayList<MemberBean>)

    }

    interface Presenter : IPresenter<View> {

        fun getList(map:HashMap<String,String>)

    }
}