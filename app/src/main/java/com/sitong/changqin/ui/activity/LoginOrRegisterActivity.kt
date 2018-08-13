package com.sitong.changqin.ui.activity

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import com.jyall.android.common.utils.SysUtils
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.base.IBaseView
import com.jyall.bbzf.extension.jump
import com.sitong.changqin.MainActivity
import com.sitong.changqin.R
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
            this@LoginOrRegisterActivity.jump<MainActivity>()
        }
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null
    fun translate() {
        val translateAnimation2 = AnimationUtils.loadAnimation(this@LoginOrRegisterActivity, R.anim.animation_left_out)
        val translateAnimation1 = AnimationUtils.loadAnimation(this@LoginOrRegisterActivity, R.anim.animation_right_in)
        ll_right.animation = translateAnimation2
        ll_right.startAnimation(translateAnimation2)
        ll_left.animation = translateAnimation2
        ll_left.startAnimation(translateAnimation2)
        iv_spl.animation=translateAnimation1
        iv_spl.startAnimation(translateAnimation1)
        translateAnimation2.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {

                var width = 0

                val params1 = ll_left.getLayoutParams() as LinearLayout.LayoutParams
                width = ((-SysUtils.getDefaultScreenWidth(this@LoginOrRegisterActivity)*0.23).toInt())
                params1.rightMargin = -width
                ll_left.layoutParams = params1
                ll_left.clearAnimation()

                val params = ll_right.getLayoutParams() as LinearLayout.LayoutParams
                params.leftMargin = width
                ll_right.layoutParams = params
                ll_right.clearAnimation()
            }

            override fun onAnimationStart(p0: Animation?) {
            }

        })
        translateAnimation1.setAnimationListener(object :Animation.AnimationListener{
            override fun onAnimationRepeat(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                iv_spl.clearAnimation()
            }

            override fun onAnimationStart(p0: Animation?) {
            }
        })
    }

}