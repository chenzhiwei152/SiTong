package com.sitong.changqin.mvp.persenter

import com.jyall.bbzf.api.scheduler.CommonObserver
import com.jyall.bbzf.api.scheduler.ErrorResponseBean
import com.jyall.bbzf.base.BaseBean
import com.jyall.bbzf.base.BasePresenter
import com.sitong.changqin.mvp.contract.LoginContract
import com.sitong.changqin.mvp.model.LoginModel
import com.sitong.changqin.mvp.model.bean.UserInfo

/**
 *
 * 登陆
 *
 */

class LoginPresenter : BasePresenter<LoginContract.View>(), LoginContract.Presenter {
    override fun login(map: HashMap<String, String>) {
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
                    mRootView?.loginSuccess(body.data)
                }
            }

            loginModel.login(map).subscribe(observer)
            addSubscription(observer.disposable!!)
        }
    }

    private val loginModel: LoginModel by lazy {
        LoginModel()
    }



}