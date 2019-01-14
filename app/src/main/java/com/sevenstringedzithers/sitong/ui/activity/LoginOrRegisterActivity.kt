package com.sevenstringedzithers.sitong.ui.activity

import android.animation.ObjectAnimator
import android.view.View
import com.jyall.android.common.utils.UIUtil
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.base.EventBusCenter
import com.jyall.bbzf.base.IBaseView
import com.jyall.bbzf.extension.jump
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.base.Constants
import kotlinx.android.synthetic.main.activity_login_or_register.*




/**
 * create by chen.zhiwei on 2018-8-13
 */
class LoginOrRegisterActivity : BaseActivity<IBaseView, BasePresenter<IBaseView>>() {
    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()

    override fun getRootView(): IBaseView = this

    override fun getLayoutId(): Int = R.layout.activity_login_or_register

    override fun initViewsAndEvents() {
        translate()
        tv_login.setOnClickListener {
            this@LoginOrRegisterActivity.jump<LoginActivity>()
        }
        tv_register.setOnClickListener {
            this@LoginOrRegisterActivity.jump<RegisterActivity>()
        }
    }

    override fun isRegistEventBus(): Boolean = true
    override fun onMessageEvent(eventBusCenter: EventBusCenter<Object>) {
        super.onMessageEvent(eventBusCenter)
        if (eventBusCenter != null) {
            if (eventBusCenter.evenCode == Constants.Tag.LOGIN_SUCCESS) {
                finish()
            }
        }
    }

    override fun isNeedLec(): View? = null
    fun translate() {

        val animator = ObjectAnimator.ofFloat(ll_right, "translationX", 0f, -UIUtil.dip2px(this,80f).toFloat())
        animator.duration = 1000
        animator.start()
        val animator1 = ObjectAnimator.ofFloat(ll_left, "translationX", 0f, -UIUtil.dip2px(this,80f).toFloat())
        animator1.duration = 1000
        animator1.start()
        val animator2 = ObjectAnimator.ofFloat(iv_spl, "translationX", 0f, UIUtil.dip2px(this,40f).toFloat())
        animator2.duration = 1000
        animator2.start()

    }

}