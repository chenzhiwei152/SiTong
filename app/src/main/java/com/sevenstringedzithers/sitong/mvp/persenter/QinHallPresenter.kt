package com.sevenstringedzithers.sitong.mvp.persenter

import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.jyall.bbzf.api.scheduler.APIManager
import com.jyall.bbzf.api.scheduler.CommonObserver
import com.jyall.bbzf.api.scheduler.ErrorResponseBean
import com.jyall.bbzf.base.BaseBean
import com.jyall.bbzf.base.BasePresenter
import com.sevenstringedzithers.sitong.mvp.contract.QinHallContract
import com.sevenstringedzithers.sitong.mvp.model.bean.QinHallBean

/**
 * create by chen.zhiwei on 2018-8-14
 */
class QinHallPresenter : BasePresenter<QinHallContract.View>(), QinHallContract.Presenter {
    override fun getHallList() {
        if (checkViewAttached()) {
//            mRootView?.showLoading(false)
            var observer = object : CommonObserver<BaseBean<ArrayList<QinHallBean>>>() {

                override fun onError(errorResponseBean: ErrorResponseBean): Boolean {
                    mRootView?.dismissLoading()
                    mRootView?.toast_msg(errorResponseBean.message!!)
                    return false
                }

                override fun onFail(errorResponseBean: BaseBean<ArrayList<QinHallBean>>): Boolean {
                    mRootView?.dismissLoading()
                    return true
                }

                override fun onSuccess(body: BaseBean<ArrayList<QinHallBean>>) {
//                    mRootView?.dismissLoading()
                    mRootView?.getDataSuccess(body.data)
                }
            }

            APIManager.jyApi.getHallList().compose(SchedulerUtils.ioToMain()).subscribe(observer)
            addSubscription(observer.disposable!!)
        }
    }
}