package com.sevenstringedzithers.sitong.ui.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jyall.bbzf.extension.toast
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.base.BaseActivity
import com.sevenstringedzithers.sitong.mvp.contract.QuestionContract
import com.sevenstringedzithers.sitong.mvp.model.bean.QuestionDetailBean
import com.sevenstringedzithers.sitong.mvp.model.bean.QuestionListBean
import com.sevenstringedzithers.sitong.mvp.persenter.QuestionPresenter
import com.sevenstringedzithers.sitong.ui.adapter.QuestionListAdapter
import kotlinx.android.synthetic.main.activity_exercise_record.*
import kotlinx.android.synthetic.main.layout_common_title.*

class QuestionsActivity : BaseActivity<QuestionContract.View, QuestionPresenter>(), QuestionContract.View {
    override fun getPresenter(): QuestionPresenter =QuestionPresenter()

    override fun getRootView(): QuestionContract.View =this
    override fun toast_msg(msg: String) {
        toast(msg)
    }

    override fun getDetailSuccess(list: ArrayList<QuestionDetailBean>) {
    }

    override fun getListSuccess(list: ArrayList<QuestionListBean>) {
        mAdapter?.setData(list)
    }

    private var mAdapter: QuestionListAdapter?=null

    override fun getLayoutId(): Int = R.layout.activity_exercise_record

    override fun initViewsAndEvents() {
        initTitle()

        rv_list.layoutManager = LinearLayoutManager(this)
        mAdapter= QuestionListAdapter(this)
        rv_list.adapter=mAdapter
        mPresenter?.getQuestions()
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null
    private fun initTitle() {
        iv_back.setOnClickListener { finish() }
        tv_title.setText("常见问题")
        iv_menu.visibility = View.GONE
    }
}