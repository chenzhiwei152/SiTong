package com.sitong.changqin.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jyall.bbzf.base.BaseFragment
import com.jyall.bbzf.extension.toast
import com.sitong.changqin.mvp.contract.ArticleListContract
import com.sitong.changqin.mvp.model.bean.VideoListBean
import com.sitong.changqin.mvp.persenter.ArticleListPresenter
import com.sitong.changqin.ui.adapter.VideoListAdapter
import com.stringedzithers.sitong.R
import kotlinx.android.synthetic.main.fragment_qin_hall.*

/**
 * 文章列表
 * create by chen.zhiwei on 2018-8-15
 */
class ArticleFragment : BaseFragment<ArticleListContract.View, ArticleListPresenter>(), ArticleListContract.View {
    private var mAdapter: VideoListAdapter? = null
    override fun getPresenter(): ArticleListPresenter =ArticleListPresenter()

    override fun getRootView(): ArticleListContract.View =this

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
        rv_list.adapter = mAdapter
        mPresenter?.getList()
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null
    companion object {
        fun newInstance(): ArticleFragment {
            return  ArticleFragment()
        }
    }
}