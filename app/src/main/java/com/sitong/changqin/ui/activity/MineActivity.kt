package com.sitong.changqin.ui.activity

import android.view.View
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.base.IBaseView
import com.sitong.changqin.R

/**
 * create by chen.zhiwei on 2018-8-15
 */
class MineActivity : BaseActivity<IBaseView, BasePresenter<IBaseView>>(), IBaseView {
    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()

    override fun getRootView(): IBaseView = this

    override fun getLayoutId(): Int = R.layout.activity_mine

    override fun initViewsAndEvents() {
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null
}