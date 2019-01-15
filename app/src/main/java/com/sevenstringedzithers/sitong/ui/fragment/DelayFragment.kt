package com.sevenstringedzithers.sitong.ui.fragment

import android.view.View
import com.jyall.bbzf.base.BaseFragment
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.base.EventBusCenter
import com.jyall.bbzf.base.IBaseView
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.base.Constants
import com.sevenstringedzithers.sitong.ui.listerner.RVAdapterItemOnClick
import kotlinx.android.synthetic.main.fragment_delay.*
import org.greenrobot.eventbus.EventBus

/**
 * 延音
 * create by chen.zhiwei on 2018-8-15
 */
class DelayFragment : BaseFragment<IBaseView, BasePresenter<IBaseView>>(), IBaseView {
    override fun getLayoutId(): Int = R.layout.fragment_delay
    override fun lazyLoad() {
    }

    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()
    override fun getRootView(): IBaseView = this

    override fun initViewsAndEvents() {
        ver_line.onclick = object : RVAdapterItemOnClick {
            override fun onItemClicked(data: Any) {
                var rate = data as String
                tv_rate.text = rate
                EventBus.getDefault().post(EventBusCenter<Float>(Constants.Tag.SETTING_DELAY,data.toFloat()))
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