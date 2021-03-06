package com.sevenstringedzithers.sitong.mvp.contract

import com.sevenstringedzithers.sitong.base.IBaseView
import com.sevenstringedzithers.sitong.base.IPresenter
import com.sevenstringedzithers.sitong.mvp.model.bean.ExerciseRecordBean

interface ExerciseRecordContract {

    interface View : IBaseView {
        fun toast_msg(msg: String)
        fun loginSuccess(bean: ExerciseRecordBean)
    }

    interface Presenter : IPresenter<View> {

        fun getList()

    }
}