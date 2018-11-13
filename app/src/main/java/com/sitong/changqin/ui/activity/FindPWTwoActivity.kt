package com.sitong.changqin.ui.activity

import android.view.View
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.base.EventBusCenter
import com.jyall.bbzf.extension.toast
import com.sevenstringedzithers.sitong.R
import com.sitong.changqin.base.Constants
import com.sitong.changqin.mvp.contract.RegisterContract
import com.sitong.changqin.mvp.model.bean.UserInfo
import com.sitong.changqin.mvp.persenter.RegisterPresenter
import kotlinx.android.synthetic.main.activity_register_two.*
import org.greenrobot.eventbus.EventBus

/**
 * create by chen.zhiwei on 2018-8-14
 */
class FindPWTwoActivity : BaseActivity<RegisterContract.View, RegisterPresenter>(), RegisterContract.View {
    override fun sendCodeSuccess() {
        EventBus.getDefault().post(EventBusCenter<Int>(Constants.Tag.REGISTER_FINISH))
        finish()
    }

    private var code: String? = null
    private var phone: String? = null
    override fun registerSuccess(userInfo: UserInfo) {
//        BaseContext.instance.setUserInfo(userInfo)
//        jump<MainActivity>()
//        EventBus.getDefault().post(EventBusCenter<Int>(Constants.Tag.REGISTER_SUCCESS))
    }


    override fun getPresenter(): RegisterPresenter = RegisterPresenter()

    override fun getRootView(): RegisterContract.View = this

    override fun getLayoutId(): Int = R.layout.activity_find_pw_two
    override fun initViewsAndEvents() {
        var bundle = intent.extras
        if (bundle != null) {
            code = bundle.getString("code")
            phone = bundle.getString("phone")
        }
//        et_phone.setText(phone)
//        et_phone.isEnabled = false
//        tv_send_code.setOnClickListener {
//            if (ExtraUtils.isMobile(et_phone.text.toString())) {
//                mPresenter?.sendCode(et_phone.text.toString())
//            }
//        }
        tv_login.setOnClickListener {
            finish()
            EventBus.getDefault().post(EventBusCenter<Int>(Constants.Tag.REGISTER_FINISH))
        }
        go_register.setOnClickListener {
            if (et_pw.text.toString().isNullOrEmpty()){
                toast_msg("密码不能为空")
                return@setOnClickListener
            }
            if (et_pw.text.toString().length<8){
                toast_msg("密码长度至少8位")
                return@setOnClickListener
            }
            if (et_pw.text.toString() != et_phone.text.toString()){
                toast_msg("密码不一致")
                return@setOnClickListener
            }

            var map = HashMap<String, String>()
            map.put("phone", phone!!)
            map.put("passwd", et_pw.text.toString())
            map.put("code", code!!)
            mPresenter?.findPW(map)
        }
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null

    override fun toast_msg(msg: String) {
        toast(msg)
    }
}