package com.sevenstringedzithers.sitong.mvp.persenter

import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.jyall.bbzf.api.scheduler.APIManager
import com.jyall.bbzf.api.scheduler.CommonObserver
import com.jyall.bbzf.api.scheduler.ErrorResponseBean
import com.jyall.bbzf.base.BaseBean
import com.jyall.bbzf.base.BasePresenter
import com.sevenstringedzithers.sitong.mvp.contract.VideoListContract
import com.sevenstringedzithers.sitong.mvp.model.bean.VideoListBean

/**
 * create by chen.zhiwei on 2018-8-14
 */
class VideoListPresenter : BasePresenter<VideoListContract.View>(), VideoListContract.Presenter {
    override fun getList() {
        if (checkViewAttached()) {
//            mRootView?.showLoading(false)
            var observer = object : CommonObserver<BaseBean<ArrayList<VideoListBean>>>() {

                override fun onError(errorResponseBean: ErrorResponseBean): Boolean {
                    mRootView?.dismissLoading()
                    mRootView?.toast_msg(errorResponseBean.message!!)
                    return false
                }

                override fun onFail(errorResponseBean: BaseBean<ArrayList<VideoListBean>>): Boolean {
                    mRootView?.dismissLoading()
                    return true
                }

                override fun onSuccess(body: BaseBean<ArrayList<VideoListBean>>) {
//                    mRootView?.dismissLoading()
                    mRootView?.getDataSuccess(body.data)
                }
            }

            APIManager.jyApi.getVideoList().compose(SchedulerUtils.ioToMain()).subscribe(observer)
            addSubscription(observer.disposable!!)
        }
    }
}