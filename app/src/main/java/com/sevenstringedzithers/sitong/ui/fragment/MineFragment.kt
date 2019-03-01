package com.sevenstringedzithers.sitong.ui.fragment

import android.view.View
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.base.BaseFragment
import com.sevenstringedzithers.sitong.base.BasePresenter
import com.sevenstringedzithers.sitong.base.IBaseView

/**
 * create by chen.zhiwei on 2018-8-15
 */
class MineFragment : BaseFragment<IBaseView, BasePresenter<IBaseView>>(), IBaseView {
    override fun getLayoutId(): Int = R.layout.fragment_mine

    override fun lazyLoad() {
    }

    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()
    override fun getRootView(): IBaseView = this

    override fun initViewsAndEvents() {
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null
}