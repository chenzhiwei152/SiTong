package com.sitong.changqin.ui.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.base.IBaseView
import com.sitong.changqin.mvp.model.bean.ExerciseRecordBean
import com.sitong.changqin.ui.adapter.QuestionListAdapter
import com.stringedzithers.sitong.R
import kotlinx.android.synthetic.main.activity_exercise_record.*
import kotlinx.android.synthetic.main.layout_common_title.*

class QuestionsActivity : BaseActivity<IBaseView, BasePresenter<IBaseView>>(), IBaseView {
    private var mAdapter: QuestionListAdapter?=null
    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()

    override fun getRootView(): IBaseView = this

    override fun getLayoutId(): Int = R.layout.activity_exercise_record

    override fun initViewsAndEvents() {
        initTitle()

        rv_list.layoutManager = LinearLayoutManager(this)
        mAdapter= QuestionListAdapter(this)
        rv_list.adapter=mAdapter
        var ll= arrayListOf<ExerciseRecordBean.Music>()
        ll.add(ExerciseRecordBean.Music("如何进入练琴模式",""))
        ll.add(ExerciseRecordBean.Music("如何下载喜欢的琴谱",""))
        ll.add(ExerciseRecordBean.Music("如何浏览更多的视频",""))
        mAdapter?.setData(ll)
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null
    private fun initTitle() {
        iv_back.setOnClickListener { finish() }
        tv_title.setText("常见问题")
        iv_menu.visibility = View.GONE
    }
}