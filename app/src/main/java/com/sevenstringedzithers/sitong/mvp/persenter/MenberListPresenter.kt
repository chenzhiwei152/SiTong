package com.sevenstringedzithers.sitong.mvp.persenter

import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.jyall.bbzf.api.scheduler.APIManager
import com.jyall.bbzf.api.scheduler.CommonObserver
import com.jyall.bbzf.api.scheduler.ErrorResponseBean
import com.jyall.bbzf.base.BaseBean
import com.jyall.bbzf.base.BasePresenter
import com.sevenstringedzithers.sitong.mvp.contract.MemberListContract
import com.sevenstringedzithers.sitong.mvp.model.bean.MemberBean

/**
 * create by chen.zhiwei on 2018-8-14
 */
class MenberListPresenter : BasePresenter<MemberListContract.View>(), MemberListContract.Presenter {
    override fun getList(map:HashMap<String,String>) {
        if (checkViewAttached()) {
            mRootView?.showLoading()
            var observer = object : CommonObserver<BaseBean<ArrayList<MemberBean>>>() {

                override fun onError(errorResponseBean: ErrorResponseBean): Boolean {
                    mRootView?.dismissLoading()
                    mRootView?.toast_msg(errorResponseBean.message!!)
                    return false
                }

                override fun onFail(errorResponseBean: BaseBean<ArrayList<MemberBean>>): Boolean {
                    mRootView?.dismissLoading()
                    return true
                }

                override fun onSuccess(body: BaseBean<ArrayList<MemberBean>>) {
                    mRootView?.dismissLoading()
                    mRootView?.getDataSuccess(body.data)
                }
            }

            APIManager.jyApi.get_member_list2(map).compose(SchedulerUtils.ioToMain()).subscribe(observer)
            addSubscription(observer.disposable!!)
        }
    }
}