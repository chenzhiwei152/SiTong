package com.sitong.changqin.mvp.persenter

import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.jyall.bbzf.api.scheduler.APIManager
import com.jyall.bbzf.api.scheduler.CommonObserver
import com.jyall.bbzf.api.scheduler.ErrorResponseBean
import com.jyall.bbzf.base.BaseBean
import com.jyall.bbzf.base.BasePresenter
import com.sitong.changqin.mvp.contract.ArticleListContract
import com.sitong.changqin.mvp.contract.MemberListContract
import com.sitong.changqin.mvp.model.bean.MemberBean
import com.sitong.changqin.mvp.model.bean.VideoListBean

/**
 * create by chen.zhiwei on 2018-8-14
 */
class MenberListPresenter : BasePresenter<MemberListContract.View>(), MemberListContract.Presenter {
    override fun getList(type:String) {
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

            APIManager.jyApi.get_member_list(type).compose(SchedulerUtils.ioToMain()).subscribe(observer)
            addSubscription(observer.disposable!!)
        }
    }
}