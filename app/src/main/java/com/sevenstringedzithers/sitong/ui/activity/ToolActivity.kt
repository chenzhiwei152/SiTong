package com.sevenstringedzithers.sitong.ui.activity

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RadioGroup
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.base.BaseActivity
import com.sevenstringedzithers.sitong.base.BasePresenter
import com.sevenstringedzithers.sitong.base.IBaseView
import com.sevenstringedzithers.sitong.ui.fragment.DelayFragment
import com.sevenstringedzithers.sitong.ui.fragment.MetronomeFragment
import com.sevenstringedzithers.sitong.ui.fragment.TunerFragment
import kotlinx.android.synthetic.main.activity_tool.*

/**
 * create by chen.zhiwei on 2018-8-15
 */
class ToolActivity : BaseActivity<IBaseView, BasePresenter<IBaseView>>(), IBaseView, View.OnClickListener {
    var fragment1: TunerFragment? = null
    var fragment2: MetronomeFragment? = null
    var fragment3: DelayFragment? = null
    override fun onClick(p0: View?) {
        when (p0?.id) {
        }
    }

    override fun getRootView(): IBaseView = this

    override fun getLayoutId(): Int = R.layout.activity_tool

    override fun initViewsAndEvents() {
        val left_in = AnimationUtils.loadAnimation(this@ToolActivity, R.anim.animation_menu_up_in)
        val right_in = AnimationUtils.loadAnimation(this@ToolActivity, R.anim.animation_menu_bottom_in)
        ll_left.animation = left_in
        ll_left.startAnimation(left_in)
        ll_right.animation = right_in
        ll_right.startAnimation(right_in)
        fragment1 = TunerFragment()
        fragment2 = MetronomeFragment()
        fragment3 = DelayFragment()
        rg_group.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                when (checkedId) {
                    R.id.rb_tuner -> {
                        val fm = supportFragmentManager
                        val transaction = fm.beginTransaction()
                        transaction.replace(R.id.fl_container, fragment1)
                        transaction.commit()
                    }
                    R.id.rb_metronome -> {
                        val fm = supportFragmentManager
                        val transaction = fm.beginTransaction()
                        transaction.replace(R.id.fl_container,fragment2)
                        transaction.commit()
                    }
                    R.id.rb_delay -> {
                        val fm = supportFragmentManager
                        val transaction = fm.beginTransaction()
                        transaction.replace(R.id.fl_container, fragment3)
                        transaction.commit()
                    }

                }

            }

        })

        rb_delay.isChecked = true
        tv_close.setOnClickListener {
            back()
        }

    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null

    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()
    override fun onBackPressed() {
        back()
    }

    fun back() {
        val left_out = AnimationUtils.loadAnimation(this@ToolActivity, R.anim.animation_menu_up_out)
        val right_out = AnimationUtils.loadAnimation(this@ToolActivity, R.anim.animation_menu_bottom_out)
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