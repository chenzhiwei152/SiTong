package com.sitong.changqin.mvp.persenter

import com.jyall.bbzf.api.scheduler.CommonObserver
import com.jyall.bbzf.api.scheduler.ErrorResponseBean
import com.jyall.bbzf.api.scheduler.ResponseCode
import com.jyall.bbzf.base.BaseBean
import com.jyall.bbzf.base.BasePresenter
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
                mRootView?.toast_msg(errorResponseBean.message!!)
                return false
            }

            override fun onFail(errorResponseBean: BaseBean<String>): Boolean {
                mRootView?.dismissLoading()
                when (errorResponseBean.error_code) {
                    ResponseCode.SEND_CODE_FAIL -> {
//                        mRootView?.toast_msg("验证码发送失败", errorResponseBean.message)
                    }
                    else -> {
                        mRootView?.toast_msg(errorResponseBean.message)
                    }
                }
                return true
            }

            override fun onSuccess(body: BaseBean<String>) {
                mRootView?.dismissLoading()
            }
        }

        loginModel.sendCode(phone).subscribe(observer)
        addSubscription(observer.disposable!!)
    }

}