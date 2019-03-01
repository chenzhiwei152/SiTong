package com.sevenstringedzithers.sitong.mvp.persenter

import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.jyall.bbzf.api.scheduler.APIManager
import com.jyall.bbzf.api.scheduler.CommonObserver
import com.jyall.bbzf.api.scheduler.ErrorResponseBean
import com.sevenstringedzithers.sitong.base.BaseBean
import com.sevenstringedzithers.sitong.base.BasePresenter
import com.sevenstringedzithers.sitong.mvp.contract.FeedbackContract
import com.sevenstringedzithers.sitong.mvp.model.bean.ResultBean
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 *
 * 登陆
 *
 */

class FeedbackPresenter : BasePresenter<FeedbackContract.View>(), FeedbackContract.Presenter {
    override fun upload(map: HashMap<String, String>) {
        if (checkViewAttached()){
            mRootView?.showLoading(true)
            var observer = object : CommonObserver<BaseBean<ResultBean>>() {

                override fun onError(errorResponseBean: ErrorResponseBean): Boolean {
                    mRootView?.dismissLoading()
                    mRootView?.toast_msg(errorResponseBean.message!!)
                    return false
                }

                override fun onFail(errorResponseBean: BaseBean<ResultBean>): Boolean {
                    mRootView?.dismissLoading()
                    return true
                }

                override fun onSuccess(body: BaseBean<ResultBean>) {
                    mRootView?.dismissLoading()
                    if (body.data?.result==1){
                        mRootView?.upLoadSuccess(body.data?.msg)
                    }else{
                        mRootView?.toast_msg(body.data?.msg)
                    }
                }
            }

            APIManager.jyApi.uploadFeedBack(map).subscribeOn(AndroidSchedulers.mainThread()).compose(SchedulerUtils.ioToMain()).subscribe(observer)
            addSubscription(observer.disposable!!)
        }
    }




}