package com.sevenstringedzithers.sitong.ui.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.extension.toast
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.mvp.contract.MessageContract
import com.sevenstringedzithers.sitong.mvp.model.bean.GetRewardBean
import com.sevenstringedzithers.sitong.mvp.model.bean.MessageListBean
import com.sevenstringedzithers.sitong.mvp.model.bean.ResultBean
import com.sevenstringedzithers.sitong.mvp.persenter.MessagePresenter
import com.sevenstringedzithers.sitong.ui.adapter.MessageListAdapter
import com.sevenstringedzithers.sitong.ui.listerner.RVAdapterItemOnClick
import com.sevenstringedzithers.sitong.view.NormalRewardDialog
import kotlinx.android.synthetic.main.activity_message_list.*
import kotlinx.android.synthetic.main.layout_common_title.*

/**
 * 消息列表
 * create by chen.zhiwei on 2018-8-13
 */
class MessageActivity : BaseActivity<MessageContract.View, MessagePresenter>(), MessageContract.View {
    override fun setMessageRead(bean: ResultBean) {
//        mPresenter?.getList(true)

    }

    override fun getRewardResult(bean: GetRewardBean) {
        mPresenter?.getList(true)
        var mDialog = NormalRewardDialog(this@MessageActivity, bean?.reason, bean?.award)
        mDialog.show()

    }

    private var messageAdapter: MessageListAdapter? = null
    override fun getListSuccess(list: ArrayList<MessageListBean>, isFresh: Boolean, isLoadMore: Boolean) {
        if (isFresh) {
            sm_layout.finishRefresh()
            messageAdapter?.clearData()
        } else {
            sm_layout.finishLoadmore()
        }
        sm_layout.isEnableAutoLoadmore = isLoadMore
        messageAdapter?.setData(list)
    }

    override fun getPresenter(): MessagePresenter = MessagePresenter()

    override fun getRootView(): MessageContract.View = this


    override fun getLayoutId(): Int = R.layout.activity_message_list

    override fun initViewsAndEvents() {
        initTitle()
        sm_layout.setOnRefreshLoadmoreListener(object : OnRefreshLoadmoreListener {
            override fun onLoadmore(refreshlayout: RefreshLayout?) {
                mPresenter?.getList(false)
            }

            override fun onRefresh(refreshlayout: RefreshLayout?) {
                mPresenter?.getList(true)
            }

        })
        rv_list.layoutManager = LinearLayoutManager(this)
        rv_list.isNestedScrollingEnabled = false

        messageAdapter = MessageListAdapter(this)
        rv_list.adapter = messageAdapter

        sm_layout.autoRefresh()

        messageAdapter?.setListerner(object : RVAdapterItemOnClick {
            override fun onItemClicked(data: Any) {
//item点击
                var bean = data as MessageListBean
                MessageDetailActivity.newIntentce(this@MessageActivity, bean?.title, bean?.content)
                var map = hashMapOf<String, String>()
                map.put("messageid", bean.id)
                mPresenter?.setRead(map)
            }

        })
        messageAdapter?.setGetRewardListerner(object : RVAdapterItemOnClick {
            override fun onItemClicked(data: Any) {
//                领奖
                var bean = data as MessageListBean
                var map = hashMapOf<String, String>()
                map.put("messageid", bean.id)
//                map.put("taskid",bean.id)
                mPresenter?.getReward(map)

            }

        })
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null

    override fun toast_msg(msg: String) {
        toast(msg)
    }

    override fun showErrorView() {
        super.showErrorView()
        sm_layout?.finishRefresh()
        sm_layout?.finishLoadmore()
    }

    private fun initTitle() {
        iv_back.setOnClickListener { finish() }
        tv_title.setText("消息")
        iv_menu.visibility = View.GONE
    }
}