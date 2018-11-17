package com.sevenstringedzithers.sitong.ui.fragment

import android.view.View
import com.jyall.bbzf.base.BaseFragment
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.base.IBaseView
import com.sevenstringedzithers.sitong.R

/**
 * 延音
 * create by chen.zhiwei on 2018-8-15
 */
class DescribejianziFragment : BaseFragment<IBaseView, BasePresenter<IBaseView>>(), IBaseView {
    override fun getLayoutId(): Int = R.layout.fragment_describe_jianzi
    override fun lazyLoad() {
    }

    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()
    override fun getRootView(): IBaseView = this

    override fun initViewsAndEvents() {

    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null

    companion object {
        fun newInstance(): DescribejianziFragment {
            return DescribejianziFragment()
        }
    }
}