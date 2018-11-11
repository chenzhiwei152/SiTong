package com.sitong.changqin.ui.activity

import android.support.v7.widget.RecyclerView
import android.view.View
import com.github.huajianjiang.expandablerecyclerview.widget.ExpandableAdapter
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.base.IBaseView
import com.jyall.bbzf.extension.toast
import com.sitong.changqin.mvp.contract.IndexContract
import com.sitong.changqin.mvp.model.bean.MusicBean
import com.sitong.changqin.mvp.persenter.IndexPresenter
import com.sitong.changqin.ui.adapter.MyAdapter
import com.sitong.changqin.utils.AppUtil
import com.sitong.changqin.view.expandable.model.MyParent
import com.stringedzithers.sitong.R
import kotlinx.android.synthetic.main.activity_music_member.*
import kotlinx.android.synthetic.main.layout_common_title.*

class MusicMemberListActivity : BaseActivity<IndexContract.View, IndexPresenter>(), IndexContract.View {
    override fun getPresenter(): IndexPresenter = IndexPresenter()
    private var mAdapter: MyAdapter? = null
    private var mData: java.util.ArrayList<MyParent>? = arrayListOf()
    override fun getRootView(): IndexContract.View = this

    override fun toast_msg(msg: String) {
        toast(msg)
    }

    override fun getDataSuccess(musicList: ArrayList<MusicBean>) {
    }

    override fun getLayoutId(): Int = R.layout.activity_music_member

    override fun initViewsAndEvents() {
        initTitle()

        mData = AppUtil.getListData(0)
        mAdapter = MyAdapter(this, mData)
        mAdapter?.setExpandCollapseMode(ExpandableAdapter.ExpandCollapseMode.MODE_DEFAULT)

        rv_list.setAdapter(mAdapter)


        mPresenter?.getMusicList("1")

    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null
    private fun initTitle() {
        iv_back.setOnClickListener { finish() }
        tv_title.setText("会员")
        iv_menu.visibility = View.GONE
    }
}