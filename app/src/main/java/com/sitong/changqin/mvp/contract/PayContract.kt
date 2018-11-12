package com.sitong.changqin.mvp.contract

import com.jyall.bbzf.base.IBaseView
import com.jyall.bbzf.base.IPresenter
import com.sitong.changqin.mvp.model.bean.AliPremissionBean
import com.sitong.changqin.mvp.model.bean.OrderBean
import com.sitong.changqin.mvp.model.bean.VideoListBean
import com.sitong.changqin.mvp.model.bean.WXPremissionBean

interface PayContract {

    interface View : IBaseView {
        fun toast_msg(msg: String)
        fun getOrderSuccess(order: OrderBean)
        fun getAliPermissionSuccess(ali: AliPremissionBean)
        fun getWXpermissionSuccess(wx: WXPremissionBean)

    }

    interface Presenter : IPresenter<View> {

        fun getOrder(map: HashMap<String, String>)
        fun getaliPermission(id: String)
        fun getWXPermission(id: String)
    }
}