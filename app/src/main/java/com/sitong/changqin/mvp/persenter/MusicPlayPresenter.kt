package com.sitong.changqin.mvp.persenter

import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.jyall.bbzf.api.scheduler.APIManager
import com.jyall.bbzf.api.scheduler.CommonObserver
import com.jyall.bbzf.api.scheduler.ErrorResponseBean
import com.jyall.bbzf.base.BaseBean
import com.jyall.bbzf.base.BasePresenter
import com.sitong.changqin.mvp.contract.MusicPlayContract
import com.sitong.changqin.mvp.model.bean.MusicDetailBean

/**
 * create by chen.zhiwei on 2018-8-14
 */
class MusicPlayPresenter : BasePresenter<MusicPlayContract.View>(), MusicPlayContract.Presenter {
    override fun getMusicDetail( map: HashMap<String, String>) {
        if (checkViewAttached()){
//            mRootView?.showLoading(false)
            var observer = object : CommonObserver<BaseBean<MusicDetailBean>>() {

                override fun onError(errorResponseBean: ErrorResponseBean): Boolean {
                    mRootView?.dismissLoading()
                    mRootView?.toast_msg(errorResponseBean.message!!)
                    return false
                }

                override fun onFail(errorResponseBean: BaseBean<MusicDetailBean>): Boolean {
                    mRootView?.dismissLoading()
                    return true
                }

                override fun onSuccess(body: BaseBean<MusicDetailBean>) {
                    mRootView?.dismissLoading()
                    mRootView?.getDataSuccess(body.data)
                }
            }

            APIManager.jyApi.getMusicDetail(map).compose(SchedulerUtils.ioToMain()).subscribe(observer)
            addSubscription(observer.disposable!!)
        }
    }

}