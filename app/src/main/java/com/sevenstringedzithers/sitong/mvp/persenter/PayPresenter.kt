package com.sevenstringedzithers.sitong.mvp.persenter

import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.jyall.bbzf.api.scheduler.APIManager
import com.jyall.bbzf.api.scheduler.CommonObserver
import com.jyall.bbzf.api.scheduler.ErrorResponseBean
import com.sevenstringedzithers.sitong.base.BaseBean
import com.sevenstringedzithers.sitong.base.BasePresenter
import com.sevenstringedzithers.sitong.mvp.contract.PayContract
import com.sevenstringedzithers.sitong.mvp.model.bean.AliPremissionBean
import com.sevenstringedzithers.sitong.mvp.model.bean.OrderBean
import com.sevenstringedzithers.sitong.mvp.model.bean.WXPremissionBean

/**
 * create by chen.zhiwei on 2018-8-14
 */
class PayPresenter : BasePresenter<PayContract.View>(), PayContract.Presenter {
    override fun getOrder(map: HashMap<String, String>) {
        if (checkViewAttached()) {
            mRootView?.showLoading(false)
            var observer = object : CommonObserver<BaseBean<OrderBean>>() {

                override fun onError(errorResponseBean: ErrorResponseBean): Boolean {
                    mRootView?.dismissLoading()
                    mRootView?.toast_msg(errorResponseBean.message!!)
                    return false
                }

                override fun onFail(errorResponseBean: BaseBean<OrderBean>): Boolean {
                    mRootView?.dismissLoading()
                    return true
                }

                override fun onSuccess(body: BaseBean<OrderBean>) {
//                    mRootView?.dismissLoading()
                    mRootView?.getOrderSuccess(body.data)
                }
            }

            APIManager.jyApi.get_order(map).compose(SchedulerUtils.ioToMain()).subscribe(observer)
            addSubscription(observer.disposable!!)
        }
    }

    override fun getaliPermission(id: String) {
        if (checkViewAttached()) {
            mRootView?.showLoading(false)
            var observer = object : CommonObserver<BaseBean<AliPremissionBean>>() {

                override fun onError(errorResponseBean: ErrorResponseBean): Boolean {
                    mRootView?.dismissLoading()
                    mRootView?.toast_msg(errorResponseBean.message!!)
                    return false
                }

                override fun onFail(errorResponseBean: BaseBean<AliPremissionBean>): Boolean {
                    mRootView?.dismissLoading()
                    return true
                }

                override fun onSuccess(body: BaseBean<AliPremissionBean>) {
//                    mRootView?.dismissLoading()
                    mRootView?.getAliPermissionSuccess(body.data)
                }
            }

            APIManager.jyApi.get_ali_premission(id).compose(SchedulerUtils.ioToMain()).subscribe(observer)
            addSubscription(observer.disposable!!)
        }
    }

    override fun getWXPermission(id: String) {
        if (checkViewAttached()) {
            mRootView?.showLoading(false)
            var observer = object : CommonObserver<BaseBean<WXPremissionBean>>() {

                override fun onError(errorResponseBean: ErrorResponseBean): Boolean {
                    mRootView?.dismissLoading()
                    mRootView?.toast_msg(errorResponseBean.message!!)
                    return false
                }

                override fun onFail(errorResponseBean: BaseBean<WXPremissionBean>): Boolean {
                    mRootView?.dismissLoading()
                    return true
                }

                override fun onSuccess(body: BaseBean<WXPremissionBean>) {
//                    mRootView?.dismissLoading()
                    mRootView?.getWXpermissionSuccess(body.data)
                }
            }

            APIManager.jyApi.get_wx_premission(id).compose(SchedulerUtils.ioToMain()).subscribe(observer)
            addSubscription(observer.disposable!!)
        }
    }

}