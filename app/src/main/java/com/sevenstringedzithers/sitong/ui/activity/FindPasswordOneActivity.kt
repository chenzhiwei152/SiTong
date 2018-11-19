package com.sevenstringedzithers.sitong.ui.activity

import android.os.Bundle
import android.view.View
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.base.BaseContext
import com.jyall.bbzf.base.EventBusCenter
import com.jyall.bbzf.extension.jump
import com.jyall.bbzf.extension.toast
import com.sevenstringedzithers.sitong.MainActivity
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.base.Constants
import com.sevenstringedzithers.sitong.mvp.contract.RegisterContract
import com.sevenstringedzithers.sitong.mvp.model.bean.UserInfo
import com.sevenstringedzithers.sitong.mvp.persenter.RegisterPresenter
import com.sevenstringedzithers.sitong.utils.CountDownTimerUtils
import com.sevenstringedzithers.sitong.utils.ExtraUtils
import kotlinx.android.synthetic.main.activity_register.*

/**
 * create by chen.zhiwei on 2018-8-14
 */
class FindPasswordOneActivity : BaseActivity<RegisterContract.View, RegisterPresenter>(), RegisterContract.View {
    private var mTimer = CountDownTimerUtils.getCountDownTimer()
    private var isCountDown = false
    override fun sendCodeSuccess() {

    }

    override fun registerSuccess(userInfo: UserInfo) {
        BaseContext.instance.setUserInfo(userInfo)
        jump<MainActivity>()
    }


    override fun getPresenter(): RegisterPresenter = RegisterPresenter()

    override fun getRootView(): RegisterContract.View = this

    override fun getLayoutId(): Int = R.layout.activity_find_password_one
    override fun initViewsAndEvents() {
        tv_send_code.setOnClickListener {
            if (ExtraUtils.isMobile(et_phone.text.toString())) {
                mPresenter?.sendCode(et_phone.text.toString())
            }else{
                toast_msg("手机号码不正确")
                return@setOnClickListener
            }
            mTimer.setMillisInFuture(60 * 1000)
                    .setCountDownInterval(1000)
                    .setTickDelegate {
                        tv_send_code.text = "(${it / 1000})"
                        tv_send_code.isEnabled = false
                    }
                    .setFinishDelegate {
                        tv_send_code.text = getString(R.string.send_message_code)
                        if (ExtraUtils.isMobile(et_phone.text.toString()))
                            tv_send_code.isEnabled = true
                        isCountDown = false
                    }
                    .start()
            isCountDown = true
        }
        tv_login.setOnClickListener {
            finish()
        }
        go_register.setOnClickListener {
            //            var map = HashMap<String, String>()
//            map.put("phone", et_phone.text.toString())
//            map.put("passwd", et_pw.text.toString())
//            map.put("code", et_code.text.toString())
//            map.put("type", "0")
//            mPresenter?.register(map)
            if (et_phone.text.toString().isNullOrEmpty()) {
                toast_msg("手机号码不能为空")
                return@setOnClickListener
            }
            if (et_phone.text.toString().length != 11) {
                toast_msg("手机号码格式不对")
                return@setOnClickListener
            }
            if (et_code.text.toString().isNullOrEmpty()) {
                toast_msg("验证码不能为空")
                return@setOnClickListener
            }
            var bundle = Bundle()
            bundle.putString("phone", et_phone.text.toString())
            bundle.putString("code", et_code.text.toString())
            jump<FindPWTwoActivity>(dataBundle = bundle)
        }
    }

    override fun isRegistEventBus(): Boolean = true
    override fun onMessageEvent(eventBusCenter: EventBusCenter<Object>) {
        super.onMessageEvent(eventBusCenter)
        if (eventBusCenter != null) {
            if (eventBusCenter.evenCode == Constants.Tag.REGISTER_SUCCESS||eventBusCenter.evenCode == Constants.Tag.REGISTER_FINISH)
                finish()
        }
    }

    override fun isNeedLec(): View? = null

    override fun toast_msg(msg: String) {
        toast(msg)
    }
}