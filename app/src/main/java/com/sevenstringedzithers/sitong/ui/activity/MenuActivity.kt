package com.sevenstringedzithers.sitong.ui.activity

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.jyall.bbzf.extension.jump
import com.sevenstringedzithers.sitong.MainActivity
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.base.BaseActivity
import com.sevenstringedzithers.sitong.base.BasePresenter
import com.sevenstringedzithers.sitong.base.IBaseView
import kotlinx.android.synthetic.main.activity_menu.*

/**
 * create by chen.zhiwei on 2018-8-15
 */
class MenuActivity : BaseActivity<IBaseView, BasePresenter<IBaseView>>(), IBaseView, View.OnClickListener {
    private var isAnimation: Boolean = false
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_close -> back()
            R.id.iv_qin -> {

                back(1)
            }
            R.id.iv_search -> {
                back(2)

            }
            R.id.iv_me -> {

                back(3)
//                ActivityStackManager.getInstance().finishActivity(MusicSearchActivity::class.java)
//                ActivityStackManager.getInstance().finishActivity(AboutActivity::class.java)
            }
            R.id.iv_story -> {

                back(4)
//                ActivityStackManager.getInstance().finishActivity(MineActivity::class.java)
//                ActivityStackManager.getInstance().finishActivity(MusicSearchActivity::class.java)
            }
        }
    }

    override fun getRootView(): IBaseView = this

    override fun getLayoutId(): Int = R.layout.activity_menu

    override fun initViewsAndEvents() {
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
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

    fun back(type: Int = 0) {
        if (isAnimation) {
            return
        }
        isAnimation = true
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
                when (type) {
                    1 -> {
                        jump<MainActivity>(isAnimation = false)
                    }
                    2 -> {
                        jump<MusicSearchActivity>(isAnimation = false)
                    }
                    3 -> {
                        jump<MineActivity>(isAnimation = false)
                    }
                    4 -> {
                        jump<AboutActivity>(isAnimation = false)
                    }
                }
                isAnimation = false
                finish()
            }

            override fun onAnimationStart(p0: Animation?) {
            }

        })
    }
}