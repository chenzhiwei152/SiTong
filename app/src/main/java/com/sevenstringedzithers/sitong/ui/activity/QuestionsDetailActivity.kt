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
import com.sevenstringedzithers.sitong.ui.adapter.QuestionDetailAdapter
import kotlinx.android.synthetic.main.activity_exercise_record.*
import kotlinx.android.synthetic.main.layout_common_title.*

class QuestionsDetailActivity : BaseActivity<QuestionContract.View, QuestionPresenter>(), QuestionContract.View {
    override fun getPresenter(): QuestionPresenter = QuestionPresenter()

    override fun getRootView(): QuestionContract.View = this
    override fun toast_msg(msg: String) {
        toast(msg)
    }

    override fun getDetailSuccess(list: ArrayList<QuestionDetailBean>) {
        mAdapter?.setData(list)
    }

    override fun getListSuccess(list: ArrayList<QuestionListBean>) {

    }

    private var mAdapter: QuestionDetailAdapter? = null

    override fun getLayoutId(): Int = R.layout.activity_exercise_record

    override fun initViewsAndEvents() {
        initTitle()
        rv_list.layoutManager = LinearLayoutManager(this)
        mAdapter = QuestionDetailAdapter(this)
        rv_list.adapter = mAdapter
        if (intent.extras != null) {
            var id = intent.extras.getString("id")
            if (!id.isNullOrEmpty()) {
                var map= hashMapOf<String,String>()
                map.put("questionid",id)
                mPresenter?.getQuestionDetails(map)
            }
        }
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null
    private fun initTitle() {
        iv_back.setOnClickListener { finish() }
        tv_title.setText("常见问题")
        iv_menu.visibility = View.GONE
    }
}