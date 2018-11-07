package com.sitong.changqin.mvp.contract

import com.jyall.bbzf.base.IBaseView
import com.jyall.bbzf.base.IPresenter
import com.sitong.changqin.mvp.model.bean.ExerciseRecordBean

interface ExerciseRecordContract {

    interface View : IBaseView {
        fun toast_msg(msg: String)
        fun loginSuccess(bean: ExerciseRecordBean)
    }

    interface Presenter : IPresenter<View> {

        fun getList()

    }
}