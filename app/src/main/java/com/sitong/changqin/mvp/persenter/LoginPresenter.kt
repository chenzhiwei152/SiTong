package com.sitong.changqin.mvp.persenter

import com.jyall.bbzf.api.scheduler.CommonObserver
import com.jyall.bbzf.api.scheduler.ErrorResponseBean
import com.jyall.bbzf.api.scheduler.ResponseCode
import com.jyall.bbzf.base.BaseBean
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.mvp.model.bean.UserInfo
import com.sitong.changqin.mvp.contract.LoginContract
import com.sitong.changqin.mvp.model.LoginModel

/**
 *
 * 登陆
 *
 */

class LoginPresenter : BasePresenter<LoginContract.View>(), LoginContract.Presenter {

    private val loginModel: LoginModel by lazy {
        LoginModel()
    }

    override fun sendCode(phone: String) {
        checkViewAttached()
        mRootView?.showLoading(false)
        var observer = object : CommonObserver<BaseBean<String>>() {

            override fun onError(errorResponseBean: ErrorResponseBean): Boolean {
                mRootView?.dismissLoading()
                mRootView?.showTaost(errorResponseBean.message!!)
                return false
            }

            override fun onFail(errorResponseBean: BaseBean<String>): Boolean {
                mRootView?.dismissLoading()
                when (errorResponseBean.error_code) {
                    ResponseCode.SEND_CODE_FAIL -> {
                        mRootView?.showErrorDialog("验证码发送失败", errorResponseBean.message)
                    }
                    else -> {
                        mRootView?.showTaost(errorResponseBean.message)
                    }
                }
                return true
            }

            override fun onSuccess(body: BaseBean<String>) {
                mRootView?.dismissLoading()
                mRootView?.sendCodeSuccess()
            }
        }

        loginModel.sendCode(phone).subscribe(observer)
        addSubscription(observer.disposable!!)
    }

    override fun checkCode(cityId: String, phone: String, shortCode: String) {
        checkViewAttached()
        mRootView?.showLoading(false)
        var observer = object : CommonObserver<BaseBean<UserInfo>>() {

            override fun onError(errorResponseBean: ErrorResponseBean): Boolean {
                mRootView?.dismissLoading()
                mRootView?.showTaost(errorResponseBean.message!!)
                return false
            }

            override fun onFail(errorResponseBean: BaseBean<UserInfo>): Boolean {
                mRootView?.dismissLoading()
                when (errorResponseBean.error_code) {
                    ResponseCode.CHECK_CODE_ERROR -> {
                        mRootView?.showErrorDialog("", errorResponseBean.message)
                    }
                    ResponseCode.USER_WHITE_LIST -> {
                    }
                    ResponseCode.USER_UNREGIST -> {
                        mRootView?.checkCodeSuccess()
                    }
                    ResponseCode.LOGIN_SUCCESS -> {
                    }
                    else -> {
                        mRootView?.showTaost(errorResponseBean.message)
                    }
                }
                return true
            }

            override fun onSuccess(body: BaseBean<UserInfo>) {
                mRootView?.dismissLoading()
                mRootView?.checkCodeSuccess()
            }
        }
        loginModel.checkCode(cityId, phone, shortCode).subscribe(observer)
        addSubscription(observer.disposable!!)
    }


    override fun login(cityId: String, phone: String, userRole: String) {
        checkViewAttached()
        mRootView?.showLoading(false)
        var observer = object : CommonObserver<BaseBean<UserInfo>>() {

            override fun onError(errorResponseBean: ErrorResponseBean): Boolean {
                mRootView?.dismissLoading()
                mRootView?.showTaost(errorResponseBean.message.toString()!!)
                return false
            }

            override fun onFail(errorResponseBean: BaseBean<UserInfo>): Boolean {
                when (errorResponseBean.error_code) {
                    ResponseCode.USER_WHITE_LIST -> {
//                        agentDirectLogin(errorResponseBean)
                    }
                    else -> {
                        mRootView?.showTaost(errorResponseBean.message)
                    }
                }
                return true
            }

            override fun onSuccess(body: BaseBean<UserInfo>) {
                mRootView?.dismissLoading()
            }
        }
        loginModel.login(cityId, phone, userRole).subscribe(observer)
        addSubscription(observer.disposable!!)
    }
}