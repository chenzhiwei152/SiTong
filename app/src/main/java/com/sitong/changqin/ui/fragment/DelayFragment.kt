package com.sitong.changqin.ui.fragment

import android.view.View
import com.jyall.bbzf.base.BaseFragment
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.base.IBaseView
import com.sevenstringedzithers.sitong.R
import com.sitong.changqin.ui.listerner.RVAdapterItemOnClick
import kotlinx.android.synthetic.main.fragment_delay.*

/**
 * 延音
 * create by chen.zhiwei on 2018-8-15
 */
class DelayFragment : BaseFragment<IBaseView, BasePresenter<IBaseView>>(), IBaseView {
    override fun getLayoutId(): Int = R.layout.fragment_delay
    private var list_1 = arrayListOf<String>("紧五弦", "紧二五弦", "正调")
    private var list_2 = arrayListOf<String>("1", "2", "3", "4", "5", "6", "7")
    override fun lazyLoad() {
    }

    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()
    override fun getRootView(): IBaseView = this

    override fun initViewsAndEvents() {
        ver_line.onclick = object : RVAdapterItemOnClick {
            override fun onItemClicked(data: Any) {
                var rate = data as String
                tv_rate.text = rate
            }

        }

    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null

    companion object {
        fun newInstance(): DelayFragment {
            return DelayFragment()
        }
    }
}