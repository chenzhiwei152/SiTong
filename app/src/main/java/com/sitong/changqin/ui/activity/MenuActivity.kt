package com.sitong.changqin.ui.activity

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.base.IBaseView
import com.jyall.bbzf.extension.jump
import com.sitong.changqin.MainActivity
import com.stringedzithers.sitong.R
import kotlinx.android.synthetic.main.activity_menu.*

/**
 * create by chen.zhiwei on 2018-8-15
 */
class MenuActivity : BaseActivity<IBaseView, BasePresenter<IBaseView>>(), IBaseView, View.OnClickListener {
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_close -> back()
            R.id.iv_qin -> {
                jump<MainActivity>(isAnimation = false)
                back()
            }
            R.id.iv_search -> {
                jump<MusicSearchActivity>(isAnimation = false)
                back()
            }
            R.id.iv_me -> {
                jump<MineActivity>(isAnimation = false)
                back()
            }
            R.id.iv_story -> {
                jump<AboutActivity>(isAnimation = false)
                back()
            }
        }
    }

    override fun getRootView(): IBaseView = this

    override fun getLayoutId(): Int = R.layout.activity_menu

    override fun initViewsAndEvents() {
        val left_in = AnimationUtils.loadAnimation(this@MenuActivity, R.anim.animation_menu_left_in)
        val right_in = AnimationUtils.loadAnimation(this@MenuActivity, R.anim.animation_menu_right_in)
        ll_left.animation = left_in
        ll_left.startAnimation(left_in)
        ll_right.animation = right_in
        ll_right.startAnimation(right_in)

        iv_story.setOnClickListener(this)
        iv_me.setOnClickListener(this)
        iv_search.setOnClickListener(this)
        iv_qin.setOnClickListener(this)
        tv_close.setOnClickListener(this)

    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null

    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()
    override fun onBackPressed() {
        back()
    }

    fun back() {
        val left_out = AnimationUtils.loadAnimation(this@MenuActivity, R.anim.animation_menu_left_out)
        val right_out = AnimationUtils.loadAnimation(this@MenuActivity, R.anim.animation_menu_right_out)
        ll_left.animation = left_out
        ll_left.startAnimation(left_out)
        ll_right.animation = right_out
        ll_right.startAnimation(right_out)
        right_out.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                finish()
            }

            override fun onAnimationStart(p0: Animation?) {
            }

        })
    }
}