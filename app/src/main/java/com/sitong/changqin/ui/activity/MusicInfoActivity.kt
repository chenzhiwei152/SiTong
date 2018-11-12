package com.sitong.changqin.ui.activity

import android.view.View
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.base.IBaseView
import com.stringedzithers.sitong.R

class MusicInfoActivity : BaseActivity<IBaseView, BasePresenter<IBaseView>>(), IBaseView {
    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()

    override fun getRootView(): IBaseView = this

    override fun getLayoutId(): Int = R.layout.activity_music_info

    override fun initViewsAndEvents() {
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null
}