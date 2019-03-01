package com.sevenstringedzithers.sitong.mvp.contract

import com.sevenstringedzithers.sitong.base.IBaseView
import com.sevenstringedzithers.sitong.base.IPresenter
import com.sevenstringedzithers.sitong.mvp.model.bean.MusicBean

interface IndexContract {

    interface View : IBaseView {
        fun toast_msg(msg: String)
        fun getDataSuccess(musicList:ArrayList<MusicBean>)

    }

    interface Presenter : IPresenter<View> {

        fun getMusicList(type: String)

    }
}