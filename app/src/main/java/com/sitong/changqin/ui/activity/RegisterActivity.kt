package com.sitong.changqin.ui.activity

import android.view.View
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.base.BaseContext
import com.jyall.bbzf.extension.jump
import com.jyall.bbzf.extension.toast
import com.sitong.changqin.MainActivity
import com.sitong.changqin.mvp.contract.RegisterContract
import com.sitong.changqin.mvp.model.bean.UserInfo
import com.sitong.changqin.mvp.persenter.RegisterPresenter
import com.sitong.changqin.utils.ExtraUtils
import com.stringedzithers.sitong.R
import kotlinx.android.synthetic.main.activity_register.*

/**
 * create by chen.zhiwei on 2018-8-14
 */
class RegisterActivity : BaseActivity<RegisterContract.View, RegisterPresenter>(), RegisterContract.View {
    override fun registerSuccess(userInfo: UserInfo) {
        BaseContext.instance.setUserInfo(userInfo)
        jump<MainActivity>()
    }


    override fun getPresenter(): RegisterPresenter = RegisterPresenter()

    override fun getRootView(): RegisterContract.View = this

    override fun getLayoutId(): Int = R.layout.activity_register
    override fun initViewsAndEvents() {
        tv_send_code.setOnClickListener {
            if (ExtraUtils.isMobile(et_phone.text.toString())) {
                mPresenter?.sendCode(et_phone.text.toString())
            }
        }
        go_register.setOnClickListener {
            var map = HashMap<String, String>()
            map.put("phone", et_phone.text.toString())
            map.put("passwd", et_pw.text.toString())
            map.put("code", et_code.text.toString())
            map.put("type", "0")
            mPresenter?.register(map)
        }
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null

    override fun toast_msg(msg: String) {
        toast(msg)
    }
}