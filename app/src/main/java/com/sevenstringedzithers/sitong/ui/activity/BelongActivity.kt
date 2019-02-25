package com.sevenstringedzithers.sitong.ui.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.base.BaseContext
import com.jyall.bbzf.base.EventBusCenter
import com.jyall.bbzf.extension.toast
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.base.Constants
import com.sevenstringedzithers.sitong.mvp.contract.QinHallContract
import com.sevenstringedzithers.sitong.mvp.model.bean.QinHallBean
import com.sevenstringedzithers.sitong.mvp.persenter.QinHallPresenter
import com.sevenstringedzithers.sitong.ui.adapter.QinHallListNameAdapter
import kotlinx.android.synthetic.main.activity_qinhall_selected.*
import kotlinx.android.synthetic.main.layout_common_title.*
import org.greenrobot.eventbus.EventBus

/*
* 归属*/
class BelongActivity : BaseActivity<QinHallContract.View, QinHallPresenter>(), QinHallContract.View {
    private var list: ArrayList<QinHallBean>? = null
    override fun getPresenter(): QinHallPresenter = QinHallPresenter()
    override fun getRootView(): QinHallContract.View = this

    override fun getDataSuccess(list: ArrayList<QinHallBean>) {
        this.list = list
        list?.forEachIndexed { index, qinHallBean ->
            if (qinHallBean.name==BaseContext.instance.getUserInfo()?.carillon){
                mAdapter?.setPosition(index)
                return@forEachIndexed
            }
        }
        mAdapter?.setData(list)
    }

    override fun setBelongSuccess() {
        toast_msg("设置成功")
        EventBus.getDefault().post(EventBusCenter<Int>(Constants.Tag.RELOAD_USERINFO))
        finish()
    }

    override fun toast_msg(msg: String) {
        toast(msg)
    }


    private var mAdapter: QinHallListNameAdapter? = null

    override fun getLayoutId(): Int = R.layout.activity_qinhall_selected

    override fun initViewsAndEvents() {
        initTitle()

        rv_list.layoutManager = LinearLayoutManager(this)
        mAdapter = QinHallListNameAdapter(this)
        rv_list.adapter = mAdapter
        BaseContext.instance.getUserInfo()?.carillon
        mPresenter?.getHallList()
        tv_confirm.setOnClickListener {
            if (list != null && list?.size!! > 0) {
                var map = hashMapOf<String, String>()
                map.put("carillonid", list?.get(mAdapter?.getPosition()!!)?.id!!)
                mPresenter?.setBelong(map)
            }
        }
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null
    private fun initTitle() {
        iv_back.setOnClickListener { finish() }
        tv_title.text = "师从"
        iv_menu.visibility = View.GONE
    }
}