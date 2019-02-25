package com.sevenstringedzithers.sitong.mvp.persenter

import com.jyall.bbzf.api.scheduler.APIManager
import com.jyall.bbzf.api.scheduler.CommonObserver
import com.jyall.bbzf.api.scheduler.ErrorResponseBean
import com.jyall.bbzf.base.BaseBean
import com.jyall.bbzf.base.BasePresenter
import com.sevenstringedzithers.sitong.mvp.contract.ExerciseRecordContract
import com.sevenstringedzithers.sitong.mvp.model.bean.ExerciseRecordBean

/**
 *
 * 练习记录
 *
 */

class ExerciseRecordPresenter : BasePresenter<ExerciseRecordContract.View>(), ExerciseRecordContract.Presenter {
    override fun getList() {

        if (checkViewAttached()){
            mRootView?.showLoading(false)
            var observer = object : CommonObserver<BaseBean<ExerciseRecordBean>>() {

                override fun onError(errorResponseBean: ErrorResponseBean): Boolean {
                    mRootView?.dismissLoading()
                    mRootView?.toast_msg(errorResponseBean.message!!)
                    return false
                }

                override fun onFail(errorResponseBean: BaseBean<ExerciseRecordBean>): Boolean {
                    mRootView?.dismissLoading()
                    return true
                }

                override fun onSuccess(body: BaseBean<ExerciseRecordBean>) {
                    mRootView?.dismissLoading()
                    mRootView?.loginSuccess(body.data)
                }
            }

            APIManager.jyApi.get_exe_record().subscribe(observer)
            addSubscription(observer.disposable!!)
        }

    }





}