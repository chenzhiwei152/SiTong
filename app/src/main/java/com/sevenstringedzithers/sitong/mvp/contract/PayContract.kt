package com.sevenstringedzithers.sitong.mvp.contract

import com.sevenstringedzithers.sitong.base.IBaseView
import com.sevenstringedzithers.sitong.base.IPresenter
import com.sevenstringedzithers.sitong.mvp.model.bean.AliPremissionBean
import com.sevenstringedzithers.sitong.mvp.model.bean.OrderBean
import com.sevenstringedzithers.sitong.mvp.model.bean.WXPremissionBean

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