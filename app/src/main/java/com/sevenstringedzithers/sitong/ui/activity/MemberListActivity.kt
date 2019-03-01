package com.sevenstringedzithers.sitong.ui.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jyall.bbzf.extension.toast
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.base.BaseActivity
import com.sevenstringedzithers.sitong.mvp.contract.MemberListContract
import com.sevenstringedzithers.sitong.mvp.model.bean.MemberBean
import com.sevenstringedzithers.sitong.mvp.persenter.MenberListPresenter
import com.sevenstringedzithers.sitong.ui.adapter.MemberListAdapter
import com.sevenstringedzithers.sitong.ui.listerner.RVAdapterItemOnClick
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.layout_common_title.*

class MemberListActivity : BaseActivity<MemberListContract.View, MenberListPresenter>(), MemberListContract.View {
    private var musicId: String? = null
    override fun getPresenter(): MenberListPresenter = MenberListPresenter()
    private var mAdapter: MemberListAdapter? = null
    override fun getRootView(): MemberListContract.View = this

    override fun toast_msg(msg: String) {
        toast(msg)
    }

    override fun getDataSuccess(list: ArrayList<MemberBean>) {
        mAdapter?.setData(list)
    }

    override fun getLayoutId(): Int = R.layout.activity_task

    override fun initViewsAndEvents() {
        var bund = intent.extras
        if (bund != null) {
            musicId = bund.getString("id")
        }
        initTitle()
        mAdapter = MemberListAdapter(this)
        rv_list.layoutManager = LinearLayoutManager(this)
        rv_list.adapter = mAdapter
        mAdapter?.setListerner(object : RVAdapterItemOnClick {
            override fun onItemClicked(data: Any) {
                if (musicId.isNullOrEmpty()) {
                    PayActivity.newIntentce(this@MemberListActivity, data as MemberBean)
                } else {
                    PayActivity.newIntentce(this@MemberListActivity, data as MemberBean, musicId!!)
                }
            }

        })


        var map = hashMapOf<String, String>()
        if (musicId.isNullOrEmpty()) {
            map.put("type", "1")
        } else {
            map.put("type", "0")
            map.put("musicid", musicId!!)
        }
        mPresenter?.getList(map)
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null
    private fun initTitle() {
        iv_back.setOnClickListener { finish() }
        tv_title.setText("会员")
        iv_menu.visibility = View.GONE
    }
}