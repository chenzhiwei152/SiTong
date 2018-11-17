package com.sevenstringedzithers.sitong.ui.fragment

import android.view.View
import com.jyall.bbzf.base.BaseFragment
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.base.IBaseView
import com.sevenstringedzithers.sitong.R
import kotlinx.android.synthetic.main.fragment_metronome.*

/**
 * 调音器
 * create by chen.zhiwei on 2018-8-15
 */
class MetronomeFragment : BaseFragment<IBaseView, BasePresenter<IBaseView>>(), IBaseView {
    override fun getLayoutId(): Int = R.layout.fragment_metronome
    private var list_1 = arrayListOf<String>("7/8", "6/8", "5/8", "4/8", "3/8", "2/8", "1/8")
    private var list_2 = arrayListOf<String>("80", "70", "60", "50", "40", "30")
    override fun lazyLoad() {
    }

    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()
    override fun getRootView(): IBaseView = this

    override fun initViewsAndEvents() {

        picker_01.data = list_1 as List<CharSequence>?
        picker_02.data = list_2 as List<CharSequence>?
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null

    companion object {
        fun newInstance(): MetronomeFragment {
            return MetronomeFragment()
        }
    }
}