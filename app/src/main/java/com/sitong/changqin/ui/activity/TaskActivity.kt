package com.sitong.changqin.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.extension.toast
import com.sitong.changqin.mvp.contract.TaskContract
import com.sitong.changqin.mvp.model.bean.TaskBean
import com.sitong.changqin.mvp.persenter.TaskPresenter
import com.sitong.changqin.ui.adapter.TaskListAdapter
import com.stringedzithers.sitong.R
import kotlinx.android.synthetic.main.activity_main.*

class TaskActivity : BaseActivity<TaskContract.View, TaskPresenter>(), TaskContract.View {
    private var mAdapter: TaskListAdapter? = null
    override fun getPresenter(): TaskPresenter = TaskPresenter()

    override fun getRootView(): TaskContract.View = this
    override fun getLayoutId(): Int = R.layout.activity_task

    override fun initViewsAndEvents() {
        mAdapter = TaskListAdapter(this)
        rv_list.layoutManager = LinearLayoutManager(this)
        rv_list.adapter = mAdapter
        if (intent?.extras != null) {
            mPresenter?.getTasks(intent.extras.getString("type"))
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

    companion object {
        fun newIntentce(mContext: Context, type: String) {
            var intent = Intent(mContext, TaskActivity::class.java)
            intent.putExtra("type", type)
            mContext.startActivity(intent)
        }
    }
}