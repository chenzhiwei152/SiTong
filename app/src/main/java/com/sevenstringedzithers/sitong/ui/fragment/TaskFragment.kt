package com.sevenstringedzithers.sitong.ui.fragment

import android.view.View
import com.jyall.bbzf.base.BaseFragment
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.base.IBaseView
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.ui.activity.TaskActivity
import kotlinx.android.synthetic.main.fragment_task.*

/**
 * create by chen.zhiwei on 2018-8-15
 */
class TaskFragment : BaseFragment<IBaseView, BasePresenter<IBaseView>>(), IBaseView, View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rl_daily_task -> {
                TaskActivity.newIntentce(requireActivity(), "0")
            }
            R.id.rl_task_long -> {
                TaskActivity.newIntentce(requireActivity(), "2")
            }
            R.id.rl_task_special -> {
                TaskActivity.newIntentce(requireActivity(), "3")
            }
            R.id.rl_task_tem -> {
                TaskActivity.newIntentce(requireActivity(), "1")
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_task

    override fun lazyLoad() {
    }

    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()
    override fun getRootView(): IBaseView = this

    override fun initViewsAndEvents() {
        rl_daily_task.setOnClickListener(this)
        rl_task_long.setOnClickListener(this)
        rl_task_special.setOnClickListener(this)
        rl_task_tem.setOnClickListener(this)
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null

    companion object {
        fun newInstance(): TaskFragment {
            return TaskFragment()
        }
    }
}