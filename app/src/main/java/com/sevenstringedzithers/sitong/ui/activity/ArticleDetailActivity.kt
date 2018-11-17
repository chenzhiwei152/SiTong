package com.sevenstringedzithers.sitong.ui.activity

import android.view.View
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.base.IBaseView
import com.sevenstringedzithers.sitong.R

class ArticleDetailActivity : BaseActivity<IBaseView, BasePresenter<IBaseView>>(), IBaseView {
    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()

    override fun getRootView(): IBaseView = this

    override fun getLayoutId(): Int = R.layout.activity_article_detail

    override fun initViewsAndEvents() {
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null
}