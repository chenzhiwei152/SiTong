package com.sitong.changqin.mvp.persenter

import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.jyall.bbzf.api.scheduler.APIManager
import com.jyall.bbzf.api.scheduler.CommonObserver
import com.jyall.bbzf.api.scheduler.ErrorResponseBean
import com.jyall.bbzf.base.BaseBean
import com.jyall.bbzf.base.BasePresenter
import com.sitong.changqin.mvp.contract.TaskContract
import com.sitong.changqin.mvp.model.bean.TaskBean

/**
 * create by chen.zhiwei on 2018-8-14
 */
class TaskPresenter : BasePresenter<TaskContract.View>(), TaskContract.Presenter {
    override fun getTasks(type: String) {
        if (checkViewAttached()) {
//            mRootView?.showLoading(false)
            var observer = object : CommonObserver<BaseBean<ArrayList<TaskBean>>>() {

                override fun onError(errorResponseBean: ErrorResponseBean): Boolean {
                    mRootView?.dismissLoading()
                    mRootView?.toast_msg(errorResponseBean.message!!)
                    return false
                }

                override fun onFail(errorResponseBean: BaseBean<ArrayList<TaskBean>>): Boolean {
                    mRootView?.dismissLoading()
                    return true
                }

                override fun onSuccess(body: BaseBean<ArrayList<TaskBean>>) {
//                    mRootView?.dismissLoading()
                    mRootView?.getDataSuccess(body.data)
                }
            }

            APIManager.jyApi.getTasks(type).compose(SchedulerUtils.ioToMain()).subscribe(observer)
            addSubscription(observer.disposable!!)
        }
    }

}