package com.sevenstringedzithers.sitong.mvp.persenter

import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.jyall.bbzf.api.scheduler.APIManager
import com.jyall.bbzf.api.scheduler.CommonObserver
import com.jyall.bbzf.api.scheduler.ErrorResponseBean
import com.sevenstringedzithers.sitong.base.BaseBean
import com.sevenstringedzithers.sitong.base.BasePresenter
import com.sevenstringedzithers.sitong.mvp.contract.EditContract
import com.sevenstringedzithers.sitong.mvp.model.bean.UserInfo

/**
 * create by chen.zhiwei on 2018-8-14
 */
class EditPresenter : BasePresenter<EditContract.View>(), EditContract.Presenter {
    override fun updateUserInfo(map: HashMap<String, String>) {
        if (checkViewAttached()) {
            mRootView?.showLoading(false)
            var observer = object : CommonObserver<BaseBean<UserInfo>>() {

                override fun onError(errorResponseBean: ErrorResponseBean): Boolean {
                    mRootView?.dismissLoading()
                    mRootView?.toast_msg(errorResponseBean.message!!)
                    return false
                }

                override fun onFail(errorResponseBean: BaseBean<UserInfo>): Boolean {
                    mRootView?.dismissLoading()
                    return true
                }

                override fun onSuccess(body: BaseBean<UserInfo>) {
                    mRootView?.dismissLoading()
                    mRootView?.getDataSuccess(body.data)
                }
            }

            APIManager.jyApi.updateUserInfo(map).compose(SchedulerUtils.ioToMain()).subscribe(observer)
            addSubscription(observer.disposable!!)
        }
    }

}