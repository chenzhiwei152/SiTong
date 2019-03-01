package com.sevenstringedzithers.sitong.mvp.contract

import com.sevenstringedzithers.sitong.base.IBaseView
import com.sevenstringedzithers.sitong.base.IPresenter

interface FeedbackContract {

    interface View : IBaseView {
        fun toast_msg(msg: String)
        fun upLoadSuccess(msg:String)
    }

    interface Presenter : IPresenter<View> {

        fun upload(map:HashMap<String,String>)

    }
}