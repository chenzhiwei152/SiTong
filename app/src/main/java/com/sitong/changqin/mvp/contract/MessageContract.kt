package com.sitong.changqin.mvp.contract

import com.jyall.bbzf.base.IBaseView
import com.jyall.bbzf.base.IPresenter
import com.sitong.changqin.mvp.model.bean.GetRewardBean
import com.sitong.changqin.mvp.model.bean.MessageListBean
import com.sitong.changqin.mvp.model.bean.ResultBean

interface MessageContract {

    interface View : IBaseView {
        fun toast_msg(msg: String)
        fun getListSuccess(list: ArrayList<MessageListBean>,isFresh:Boolean,isLoadMore:Boolean)
        fun setMessageRead(bean: ResultBean)
        fun getRewardResult(bean: GetRewardBean)
    }

    interface Presenter : IPresenter<View> {

        fun getList(isFresh:Boolean)
        fun setRead(map:HashMap<String,String>)
        fun getReward(map:HashMap<String,String>)

    }
}