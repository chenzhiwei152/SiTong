package com.sevenstringedzithers.sitong.ui.fragment

import android.view.View
import com.jyall.bbzf.extension.jump
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.base.BaseFragment
import com.sevenstringedzithers.sitong.base.BasePresenter
import com.sevenstringedzithers.sitong.base.IBaseView
import com.sevenstringedzithers.sitong.ui.activity.MemberListActivity
import com.sevenstringedzithers.sitong.ui.activity.MusicMemberListActivity
import kotlinx.android.synthetic.main.fragment_member.*

/**
 * create by chen.zhiwei on 2018-8-15
 */
class MemberFragment : BaseFragment<IBaseView, BasePresenter<IBaseView>>(), IBaseView {
    override fun getLayoutId(): Int = R.layout.fragment_member

    override fun lazyLoad() {
    }

    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()
    override fun getRootView(): IBaseView = this

    override fun initViewsAndEvents() {
        rl_music_pay.setOnClickListener { activity?.jump<MusicMemberListActivity>() }
        rl_member.setOnClickListener { activity?.jump<MemberListActivity>() }
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null
    companion object {
        fun newInstance(): MemberFragment {
            return MemberFragment()
        }
    }
}