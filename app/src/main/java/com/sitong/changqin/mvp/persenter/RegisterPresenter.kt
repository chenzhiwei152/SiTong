package com.sitong.changqin.mvp.persenter

import com.jyall.bbzf.api.scheduler.CommonObserver
import com.jyall.bbzf.api.scheduler.ErrorResponseBean
import com.jyall.bbzf.base.BaseBean
import com.jyall.bbzf.base.BasePresenter
import com.sitong.changqin.mvp.contract.RegisterContract
import com.sitong.changqin.mvp.model.LoginModel
import com.sitong.changqin.mvp.model.bean.UserInfo

/**
 *
 * 登陆
 *
 */

class RegisterPresenter : BasePresenter<RegisterContract.View>(), RegisterContract.Presenter {
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
                    mRootView?.toast_msg(errorResponseBean.message!!)
                    return false
                }

                override fun onFail(errorResponseBean: BaseBean<String>): Boolean {
                    mRootView?.dismissLoading()
                    mRootView?.toast_msg(errorResponseBean.message!!)
                    return true
                }

                override fun onSuccess(body: BaseBean<String>) {
                    mRootView?.toast_msg(body.message)
                    mRootView?.dismissLoading()
                }
            }

            loginModel.sendCode(phone).subscribe(observer)
            addSubscription(observer.disposable!!)
        }
    }

}