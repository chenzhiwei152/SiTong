package com.sitong.changqin.ui.activity

import android.view.View
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.extension.toast
import com.sitong.changqin.R
import com.sitong.changqin.mvp.contract.LoginContract
import com.sitong.changqin.mvp.persenter.LoginPresenter

/**
 * create by chen.zhiwei on 2018-8-13
 */
class LoginActivity : BaseActivity<LoginContract.View, LoginPresenter>(), LoginContract.View {
    override fun getPresenter(): LoginPresenter = LoginPresenter()

    override fun getRootView(): LoginContract.View = this


    override fun getLayoutId(): Int = R.layout.activity_login

    override fun initViewsAndEvents() {
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null

    override fun toast_msg(msg: String) {
        toast(msg)
    }
}