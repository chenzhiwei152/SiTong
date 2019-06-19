package com.sevenstringedzithers.sitong.mvp.persenter

import com.jyall.bbzf.api.scheduler.CommonObserver
import com.jyall.bbzf.api.scheduler.ErrorResponseBean
import com.sevenstringedzithers.sitong.base.BaseBean
import com.sevenstringedzithers.sitong.base.BasePresenter
import com.sevenstringedzithers.sitong.mvp.contract.RegisterContract
import com.sevenstringedzithers.sitong.mvp.model.LoginModel
import com.sevenstringedzithers.sitong.mvp.model.bean.FindPawSuccessBean
import com.sevenstringedzithers.sitong.mvp.model.bean.UserInfo

/**
 *
 * 登陆
 *
 */

class RegisterPresenter : BasePresenter<RegisterContract.View>(), RegisterContract.Presenter {
    override fun findPW(map: HashMap<String, String>) {
        if (checkViewAttached()){
            mRootView?.showLoading(false)
            var observer = object : CommonObserver<BaseBean<FindPawSuccessBean>>() {

                override fun onError(errorResponseBean: ErrorResponseBean): Boolean {
                    mRootView?.dismissLoading()
                    mRootView?.toast_msg(errorResponseBean.message!!)
                    return false
                }

                override fun onFail(errorResponseBean: BaseBean<FindPawSuccessBean>): Boolean {
                    mRootView?.dismissLoading()
                    mRootView?.toast_msg(errorResponseBean.message!!)
                    return true
                }

                override fun onSuccess(body: BaseBean<FindPawSuccessBean>) {
                    mRootView?.dismissLoading()
                    mRootView?.sendCodeSuccess()
                }
            }

            loginModel.findPW(map).subscribe(observer)
            addSubscription(observer.disposable!!)
        }
    }

    override fun register(map: HashMap<String,String>) {
        if (checkViewAttached()){
            mRootView?.showLoading(false)
            var observer = object : CommonObserver<BaseBean<UserInfo>>() {

                override fun onError(errorResponseBean: ErrorResponseBean): Boolean {
                    mRootView?.dismissLoading()
                    mRootView?.toast_msg(errorResponseBean.message!!)
                    return false
                }

                override fun onFail(errorResponseBean: BaseBean<UserInfo>): Boolean {
                    mRootView?.toast_msg(errorResponseBean.message)
                    mRootView?.dismissLoading()
                    return true
                }

                override fun onSuccess(body: BaseBean<UserInfo>) {
                    mRootView?.dismissLoading()
                    mRootView?.registerSuccess(body.data)
                }
            }

            loginModel.register(map).subscribe(observer)
            addSubscription(observer.disposable!!)
        }
    }

    private val loginModel: LoginModel by lazy {
        LoginModel()
    }

    override fun sendCode(phone: String) {
        if (checkViewAttached()){
            mRootView?.showLoading(false)
            var observer = object : CommonObserver<BaseBean<String>>() {

                override fun onError(errorResponseBean: ErrorResponseBean): Boolean {
                    mRootView?.dismissLoading()
//                    mRootView?.toast_msg(errorResponseBean.message!!)
                    return false
                }

                override fun onFail(errorResponseBean: BaseBean<String>): Boolean {
                    mRootView?.dismissLoading()
                    mRootView?.toast_msg(errorResponseBean.message!!)
                    return true
                }

                override fun onSuccess(body: BaseBean<String>) {
                    mRootView?.toast_msg("验证码发送成功")
                    mRootView?.dismissLoading()
                    mRootView?.sendCodeSuccess()
                }
            }

            loginModel.sendCode(phone).subscribe(observer)
            addSubscription(observer.disposable!!)
        }
    }

}