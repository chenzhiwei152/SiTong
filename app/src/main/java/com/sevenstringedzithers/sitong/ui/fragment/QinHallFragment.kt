package com.sevenstringedzithers.sitong.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jyall.bbzf.base.BaseFragment
import com.jyall.bbzf.extension.toast
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.mvp.contract.QinHallContract
import com.sevenstringedzithers.sitong.mvp.model.bean.QinHallBean
import com.sevenstringedzithers.sitong.mvp.persenter.QinHallPresenter
import com.sevenstringedzithers.sitong.ui.adapter.QinHallListAdapter
import kotlinx.android.synthetic.main.fragment_qin_hall.*

/**
 * 琴馆
 * create by chen.zhiwei on 2018-8-15
 */
class QinHallFragment : BaseFragment<QinHallContract.View, QinHallPresenter>(), QinHallContract.View {
    override fun getPresenter(): QinHallPresenter = QinHallPresenter()

    override fun getRootView(): QinHallContract.View = this

    override fun toast_msg(msg: String) {
        activity!!.toast(msg)
    }

    override fun getDataSuccess(list: ArrayList<QinHallBean>) {
        qinAdapter!!.setData(list)
    }

    private var qinAdapter: QinHallListAdapter? = null
    override fun getLayoutId(): Int = R.layout.fragment_qin_hall

    override fun lazyLoad() {
    }


    override fun initViewsAndEvents() {

        rv_list.layoutManager = LinearLayoutManager(activity)
        qinAdapter = QinHallListAdapter(context!!)
        rv_list.adapter = qinAdapter
        mPresenter?.getHallList()
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null

    companion object {
        fun newInstance(): QinHallFragment {
            return QinHallFragment()
        }
    }
}