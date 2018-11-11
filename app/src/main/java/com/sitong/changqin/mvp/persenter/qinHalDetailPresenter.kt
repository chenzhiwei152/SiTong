package com.sitong.changqin.mvp.persenter

import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.jyall.bbzf.api.scheduler.APIManager
import com.jyall.bbzf.api.scheduler.CommonObserver
import com.jyall.bbzf.api.scheduler.ErrorResponseBean
import com.jyall.bbzf.base.BaseBean
import com.jyall.bbzf.base.BasePresenter
import com.sitong.changqin.mvp.contract.ArticleListContract
import com.sitong.changqin.mvp.contract.QinHallDetailContract
import com.sitong.changqin.mvp.model.bean.QinguanDetailBean
import com.sitong.changqin.mvp.model.bean.VideoListBean

/**
 * create by chen.zhiwei on 2018-8-14
 */
class qinHalDetailPresenter : BasePresenter<QinHallDetailContract.View>(), QinHallDetailContract.Presenter {
    override fun getList(map: HashMap<String, String>) {
        if (checkViewAttached()) {
//            mRootView?.showLoading(false)
            var observer = object : CommonObserver<BaseBean<QinguanDetailBean>>() {

                override fun onError(errorResponseBean: ErrorResponseBean): Boolean {
                    mRootView?.dismissLoading()
                    mRootView?.toast_msg(errorResponseBean.message!!)
                    return false
                }

                override fun onFail(errorResponseBean: BaseBean<QinguanDetailBean>): Boolean {
                    mRootView?.dismissLoading()
                    return true
                }

                override fun onSuccess(body: BaseBean<QinguanDetailBean>) {
//                    mRootView?.dismissLoading()
                    mRootView?.getDataSuccess(body.data)
                }
            }

            APIManager.jyApi.get_qinguan_detail(map).compose(SchedulerUtils.ioToMain()).subscribe(observer)
            addSubscription(observer.disposable!!)
        }
    }
}