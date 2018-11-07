package com.sitong.changqin.ui.activity

import android.view.View
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.base.BaseContext
import com.jyall.bbzf.extension.jump
import com.jyall.bbzf.extension.toast
import com.sitong.changqin.MainActivity
import com.sitong.changqin.mvp.contract.LoginContract
import com.sitong.changqin.mvp.model.bean.UserInfo
import com.sitong.changqin.mvp.persenter.LoginPresenter
import com.stringedzithers.sitong.R
import kotlinx.android.synthetic.main.activity_login.*

/**
 * create by chen.zhiwei on 2018-8-13
 */
class LoginActivity : BaseActivity<LoginContract.View, LoginPresenter>(), LoginContract.View {
    override fun loginSuccess(user: UserInfo) {
        BaseContext.instance.setUserInfo(user)
        jump<MainActivity>()
        finish()
    }

    override fun getPresenter(): LoginPresenter = LoginPresenter()

    override fun getRootView(): LoginContract.View = this


    override fun getLayoutId(): Int = R.layout.activity_login

    override fun initViewsAndEvents() {
        tv_login.setOnClickListener{
            var map= hashMapOf<String,String>()
            map.put("phone",et_phone.text.toString())
            map.put("passwd",et_pw.text.toString())
            map.put("type","0")
            mPresenter?.login(map)
        }
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null

    override fun toast_msg(msg: String) {
        toast(msg)
    }
}