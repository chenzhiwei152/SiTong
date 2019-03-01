package com.sevenstringedzithers.sitong.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jyall.bbzf.extension.toast
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.base.BaseFragment
import com.sevenstringedzithers.sitong.mvp.contract.VideoListContract
import com.sevenstringedzithers.sitong.mvp.model.bean.VideoListBean
import com.sevenstringedzithers.sitong.mvp.persenter.VideoListPresenter
import com.sevenstringedzithers.sitong.ui.adapter.VideoListAdapter
import kotlinx.android.synthetic.main.fragment_qin_hall.*

/**
 * 视频
 * create by chen.zhiwei on 2018-8-15
 */
class VideoFragment : BaseFragment<VideoListContract.View, VideoListPresenter>(), VideoListContract.View {
    private var mAdapter: VideoListAdapter? = null
    override fun getPresenter(): VideoListPresenter =VideoListPresenter()

    override fun getRootView(): VideoListContract.View =this

    override fun toast_msg(msg: String) {
        activity?.toast(msg)
    }

    override fun getDataSuccess(list: ArrayList<VideoListBean>) {
        mAdapter!!.setData(list)
    }

    override fun getLayoutId(): Int = R.layout.fragment_qin_hall

    override fun lazyLoad() {
    }


    override fun initViewsAndEvents() {
        rv_list.layoutManager = LinearLayoutManager(activity)
        mAdapter = VideoListAdapter(context!!)
        mAdapter?.setType(1)
        rv_list.adapter = mAdapter
        mPresenter?.getList()
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null
    companion object {
        fun newInstance(): VideoFragment {
            return VideoFragment()
        }
    }
}