package com.sevenstringedzithers.sitong.mvp.persenter

import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.jyall.bbzf.api.scheduler.APIManager
import com.jyall.bbzf.api.scheduler.CommonObserver
import com.jyall.bbzf.api.scheduler.ErrorResponseBean
import com.sevenstringedzithers.sitong.base.BaseBean
import com.sevenstringedzithers.sitong.base.BasePresenter
import com.sevenstringedzithers.sitong.mvp.contract.IndexContract
import com.sevenstringedzithers.sitong.mvp.model.bean.MusicBean

/**
 * create by chen.zhiwei on 2018-8-14
 */
class IndexPresenter : BasePresenter<IndexContract.View>(), IndexContract.Presenter {
    override fun getMusicList(type: String) {
        if (checkViewAttached()){
//            mRootView?.showLoading(false)
            var observer = object : CommonObserver<BaseBean<ArrayList<MusicBean>>>() {

                override fun onError(errorResponseBean: ErrorResponseBean): Boolean {
                    mRootView?.dismissLoading()
                    mRootView?.toast_msg(errorResponseBean.message!!)
                    return false
                }

                override fun onFail(errorResponseBean: BaseBean<ArrayList<MusicBean>>): Boolean {
                    mRootView?.dismissLoading()
                    return true
                }

                override fun onSuccess(body: BaseBean<ArrayList<MusicBean>>) {
//                    mRootView?.dismissLoading()
                    mRootView?.getDataSuccess(body.data)
                }
            }

            APIManager.jyApi.getMusicList(type).compose(SchedulerUtils.ioToMain()).subscribe(observer)
            addSubscription(observer.disposable!!)
        }
    }
}