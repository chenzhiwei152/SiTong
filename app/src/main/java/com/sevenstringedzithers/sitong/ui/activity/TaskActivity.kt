package com.sevenstringedzithers.sitong.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.extension.toast
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.mvp.contract.TaskContract
import com.sevenstringedzithers.sitong.mvp.model.bean.TaskBean
import com.sevenstringedzithers.sitong.mvp.persenter.TaskPresenter
import com.sevenstringedzithers.sitong.ui.adapter.TaskListAdapter
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.layout_common_title.*

class TaskActivity : BaseActivity<TaskContract.View, TaskPresenter>(), TaskContract.View {
    private var mAdapter: TaskListAdapter? = null
    override fun getPresenter(): TaskPresenter = TaskPresenter()

    override fun getRootView(): TaskContract.View = this
    override fun getLayoutId(): Int = R.layout.activity_task

    override fun initViewsAndEvents() {
        initTitle()
        mAdapter = TaskListAdapter(this)
        rv_list.layoutManager = LinearLayoutManager(this)
        rv_list.adapter = mAdapter
        if (intent?.extras != null) {
            mPresenter?.getTasks(intent.extras.getString("type"))
            when(intent.extras.getString("type")){
                "0"->tv_title.text = "日常任务"
                "1"->tv_title.text = "临时任务"
                "2"->tv_title.text = "长期任务"
                "3"->tv_title.text = "特殊任务"
            }
        }
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null
    override fun toast_msg(msg: String) {
        toast(msg)
    }

    override fun getDataSuccess(list: ArrayList<TaskBean>) {
        mAdapter?.setData(list)
    }

    private fun initTitle() {
        iv_back.setOnClickListener { finish() }
        tv_title.text = "任务"
        iv_menu.visibility=View.GONE
    }

    companion object {
        fun newIntentce(mContext: Context, type: String) {
            var intent = Intent(mContext, TaskActivity::class.java)
            intent.putExtra("type", type)
            mContext.startActivity(intent)
        }
    }
}
