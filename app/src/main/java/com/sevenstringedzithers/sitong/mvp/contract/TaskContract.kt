package com.sevenstringedzithers.sitong.mvp.contract

import com.jyall.bbzf.base.IBaseView
import com.jyall.bbzf.base.IPresenter
import com.sevenstringedzithers.sitong.mvp.model.bean.TaskBean

interface TaskContract {

    interface View : IBaseView {
        fun toast_msg(msg: String)
        fun getDataSuccess(list: ArrayList<TaskBean>)

    }

    interface Presenter : IPresenter<View> {

        fun getTasks(type:String)

    }
}