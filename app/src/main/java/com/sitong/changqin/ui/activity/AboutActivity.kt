package com.sitong.changqin.ui.activity

import android.view.View
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.base.IBaseView
import com.jyall.bbzf.extension.jump
import com.sevenstringedzithers.sitong.R
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : BaseActivity<IBaseView, BasePresenter<IBaseView>>(), IBaseView {
    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()

    override fun getRootView(): IBaseView = this

    override fun getLayoutId(): Int = R.layout.activity_about

    override fun initViewsAndEvents() {
        iv_menu.setOnClickListener {
            jump<MenuActivity>(isAnimation = false)
        }
    }


    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null
}