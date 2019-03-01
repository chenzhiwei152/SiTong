package com.sevenstringedzithers.sitong.mvp.contract

import com.sevenstringedzithers.sitong.base.IBaseView
import com.sevenstringedzithers.sitong.base.IPresenter
import com.sevenstringedzithers.sitong.mvp.model.bean.QuestionDetailBean
import com.sevenstringedzithers.sitong.mvp.model.bean.QuestionListBean

interface QuestionContract {

    interface View : IBaseView {
        fun toast_msg(msg: String)
        fun getDetailSuccess(list: ArrayList<QuestionDetailBean>)
        fun getListSuccess(list: ArrayList<QuestionListBean>)

    }

    interface Presenter : IPresenter<View> {

        fun getQuestions()
        fun getQuestionDetails(map:HashMap<String,String>)
    }
}