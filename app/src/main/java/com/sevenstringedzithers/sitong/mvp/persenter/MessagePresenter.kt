package com.sevenstringedzithers.sitong.mvp.persenter

import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.jyall.bbzf.api.scheduler.APIManager
import com.jyall.bbzf.api.scheduler.CommonObserver
import com.jyall.bbzf.api.scheduler.ErrorResponseBean
import com.sevenstringedzithers.sitong.base.BaseBean
import com.sevenstringedzithers.sitong.base.BasePresenter
import com.sevenstringedzithers.sitong.mvp.contract.MessageContract
import com.sevenstringedzithers.sitong.mvp.model.bean.GetRewardBean
import com.sevenstringedzithers.sitong.mvp.model.bean.MessageListBean
import com.sevenstringedzithers.sitong.mvp.model.bean.ResultBean

/**
 *
 * 消息
 *
 */

class MessagePresenter : BasePresenter<MessageContract.View>(), MessageContract.Presenter {
    override fun getReward(map: HashMap<String, String>) {
        if (checkViewAttached()) {
            mRootView?.showLoading(false)
            var observer = object : CommonObserver<BaseBean<GetRewardBean>>() {

                override fun onError(errorResponseBean: ErrorResponseBean): Boolean {
                    mRootView?.dismissLoading()
                    mRootView?.toast_msg(errorResponseBean.message!!)
                    return false
                }

                override fun onFail(errorResponseBean: BaseBean<GetRewardBean>): Boolean {
                    mRootView?.dismissLoading()
                    return true
                }

                override fun onSuccess(body: BaseBean<GetRewardBean>) {
                    mRootView?.dismissLoading()
                    mRootView?.getRewardResult(body.data)
                }
            }

            APIManager.jyApi.getReward(map).compose(SchedulerUtils.ioToMain()).subscribe(observer)
            addSubscription(observer.disposable!!)
        }
    }

    var page = 0
    var range = 20

    override fun getList(isFresh: Boolean) {
        if (checkViewAttached()) {
            var map = hashMapOf<String, String>()
            if (isFresh) {
                page = 0
            }
            map.put("page", "" + page)
            map.put("range", "" + range)
            mRootView?.showLoading(true)
            var observer = object : CommonObserver<BaseBean<ArrayList<MessageListBean>>>() {

                override fun onError(errorResponseBean: ErrorResponseBean): Boolean {
                    mRootView?.dismissLoading()
                    mRootView?.showErrorView()
                    mRootView?.toast_msg(errorResponseBean.message!!)
                    return false
                }

                override fun onFail(errorResponseBean: BaseBean<ArrayList<MessageListBean>>): Boolean {
                    mRootView?.dismissLoading()
                    mRootView?.showErrorView()
                    return true
                }

                override fun onSuccess(body: BaseBean<ArrayList<MessageListBean>>) {
                    mRootView?.dismissLoading()

                    if (body.data.size <= 0) {
                        mRootView?.getListSuccess(body.data, page == 0, false)
                    } else {
                        mRootView?.getListSuccess(body.data, page == 0, ((page + 1) * range < body.data[0].total))
                        page++
                    }
                }
            }

            APIManager.jyApi.getMessageList(map).compose(SchedulerUtils.ioToMain()).subscribe(observer)
            addSubscription(observer.disposable!!)
        }
    }

    override fun setRead(map: HashMap<String, String>) {
        if (checkViewAttached()) {
            mRootView?.showLoading(false)
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
                    mRootView?.setMessageRead(body.data)
                }
            }

            APIManager.jyApi.setMessageRead(map).compose(SchedulerUtils.ioToMain()).subscribe(observer)
            addSubscription(observer.disposable!!)
        }
    }


}