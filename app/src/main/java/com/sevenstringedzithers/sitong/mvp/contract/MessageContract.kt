package com.sevenstringedzithers.sitong.mvp.contract

import com.sevenstringedzithers.sitong.base.IBaseView
import com.sevenstringedzithers.sitong.base.IPresenter
import com.sevenstringedzithers.sitong.mvp.model.bean.GetRewardBean
import com.sevenstringedzithers.sitong.mvp.model.bean.MessageListBean
import com.sevenstringedzithers.sitong.mvp.model.bean.ResultBean

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