package com.sevenstringedzithers.sitong.mvp.persenter

import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.jyall.bbzf.api.scheduler.APIManager
import com.jyall.bbzf.api.scheduler.CommonObserver
import com.jyall.bbzf.api.scheduler.ErrorResponseBean
import com.jyall.bbzf.base.BaseBean
import com.jyall.bbzf.base.BasePresenter
import com.sevenstringedzithers.sitong.mvp.contract.QuestionContract
import com.sevenstringedzithers.sitong.mvp.model.bean.QuestionDetailBean
import com.sevenstringedzithers.sitong.mvp.model.bean.QuestionListBean

/**
 * create by chen.zhiwei on 2018-8-14
 */
class QuestionPresenter : BasePresenter<QuestionContract.View>(), QuestionContract.Presenter {
    override fun getQuestions() {
        if (checkViewAttached()) {
//            mRootView?.showLoading(false)
            var observer = object : CommonObserver<BaseBean<ArrayList<QuestionListBean>>>() {

                override fun onError(errorResponseBean: ErrorResponseBean): Boolean {
                    mRootView?.dismissLoading()
                    mRootView?.toast_msg(errorResponseBean.message!!)
                    return false
                }

                override fun onFail(errorResponseBean: BaseBean<ArrayList<QuestionListBean>>): Boolean {
                    mRootView?.dismissLoading()
                    return true
                }

                override fun onSuccess(body: BaseBean<ArrayList<QuestionListBean>>) {
//                    mRootView?.dismissLoading()
                    mRootView?.getListSuccess(body.data)
                }
            }

            APIManager.jyApi.get_problem_list().compose(SchedulerUtils.ioToMain()).subscribe(observer)
            addSubscription(observer.disposable!!)
        }
    }

    override fun getQuestionDetails(map: HashMap<String, String>) {
        if (checkViewAttached()) {
//            mRootView?.showLoading(false)
            var observer = object : CommonObserver<BaseBean<ArrayList<QuestionDetailBean>>>() {

                override fun onError(errorResponseBean: ErrorResponseBean): Boolean {
                    mRootView?.dismissLoading()
                    mRootView?.toast_msg(errorResponseBean.message!!)
                    return false
                }

                override fun onFail(errorResponseBean: BaseBean<ArrayList<QuestionDetailBean>>): Boolean {
                    mRootView?.dismissLoading()
                    return true
                }

                override fun onSuccess(body: BaseBean<ArrayList<QuestionDetailBean>>) {
//                    mRootView?.dismissLoading()
                    mRootView?.getDetailSuccess(body.data)
                }
            }

            APIManager.jyApi.get_problem_detail(map).compose(SchedulerUtils.ioToMain()).subscribe(observer)
            addSubscription(observer.disposable!!)
        }

    }


}