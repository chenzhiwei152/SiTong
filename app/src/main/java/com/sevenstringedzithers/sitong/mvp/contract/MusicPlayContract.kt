package com.sevenstringedzithers.sitong.mvp.contract

import com.sevenstringedzithers.sitong.base.IBaseView
import com.sevenstringedzithers.sitong.base.IPresenter
import com.sevenstringedzithers.sitong.mvp.model.bean.MusicDetailBean

interface MusicPlayContract {

    interface View : IBaseView {
        fun toast_msg(msg: String)
        fun getDataSuccess(musicBean: MusicDetailBean)

    }

    interface Presenter : IPresenter<View> {

        fun getMusicDetail( map: HashMap<String, String>)

    }
}