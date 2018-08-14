package com.sitong.changqin.mvp.contract

import com.jyall.bbzf.base.IBaseView
import com.jyall.bbzf.base.IPresenter
import com.sitong.changqin.mvp.model.bean.MusicBean

interface IndexContract {

    interface View : IBaseView {
        fun toast_msg(msg: String)
        fun getDataSuccess(musicList:ArrayList<MusicBean>)

    }

    interface Presenter : IPresenter<View> {

        fun getMusicList(type: String)

    }
}