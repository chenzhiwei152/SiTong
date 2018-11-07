package com.sitong.changqin.ui.fragment

import android.view.View
import com.jyall.bbzf.base.BaseFragment
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.base.IBaseView
import com.stringedzithers.sitong.R
import kotlinx.android.synthetic.main.fragment_tuner.*

/**
 * 调音器
 * create by chen.zhiwei on 2018-8-15
 */
class TunerFragment : BaseFragment<IBaseView, BasePresenter<IBaseView>>(), IBaseView {
    override fun getLayoutId(): Int = R.layout.fragment_tuner
    private var list_1 = arrayListOf<String>("紧五弦", "紧二五弦", "正调")
    private var list_2 = arrayListOf<String>("1", "2", "3", "4", "5", "6", "7")
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
        fun newInstance(): TunerFragment {
            return TunerFragment()
        }
    }
}