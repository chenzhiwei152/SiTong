package com.sitong.changqin

import android.support.design.widget.AppBarLayout
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.extension.toast
import com.sitong.changqin.mvp.contract.IndexContract
import com.sitong.changqin.mvp.model.bean.MusicBean
import com.sitong.changqin.mvp.persenter.IndexPresenter
import com.sitong.changqin.ui.adapter.IndexAdapter
import com.sitong.changqin.ui.adapter.IndexRankAdapter
import com.sitong.changqin.ui.listerner.AppBarStateChangeListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity<IndexContract.View, IndexPresenter>(), IndexContract.View {

    var mRVAdapter: IndexAdapter? = null
    var mRankRVAdapter: IndexRankAdapter? = null
    var indexLayoutManaget: LinearLayoutManager? = null

    override fun getDataSuccess(musicList: ArrayList<MusicBean>) {
        toast_msg("" + musicList?.size)
        mRVAdapter?.setData(musicList)
        mRankRVAdapter?.setData(musicList)
    }

    override fun getPresenter(): IndexPresenter = IndexPresenter()

    override fun getRootView(): IndexContract.View = this

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initViewsAndEvents() {
        setSupportActionBar(toolbar)
        appBar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: AppBarStateChangeListener.State) {
                if (state === AppBarStateChangeListener.State.EXPANDED) {

                    //展开状态
                    tv_subtitle.visibility = View.VISIBLE
                } else if (state === AppBarStateChangeListener.State.COLLAPSED) {

                    //折叠状态
                    tv_subtitle.visibility = View.GONE
                } else {

                    //中间状态
                    tv_subtitle.visibility = View.GONE
                }
            }
        })
        mRVAdapter = IndexAdapter(this)
        rv_list.isNestedScrollingEnabled = false
        indexLayoutManaget = LinearLayoutManager(this)
        rv_list.layoutManager = indexLayoutManaget
        rv_list.adapter = mRVAdapter

        mRankRVAdapter = IndexRankAdapter(this)
        rv_rank.layoutManager = LinearLayoutManager(this)
        rv_rank.adapter = mRankRVAdapter

        mPresenter?.getMusicList("1")



        stick_scroll.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
                controlLetters()
            }

        })


    }

    fun controlLetters() {
        if (rv_list == null || rv_rank == null) {
            return
        }
        mRankRVAdapter?.list?.forEachIndexed loop@{ index, value ->
            mRVAdapter?.list?.forEachIndexed { index, value ->
                var startLocation = IntArray(2)
                rv_list.findViewHolderForLayoutPosition(index)?.itemView?.getLocationOnScreen(startLocation)


                var startLocation1 = IntArray(2)
                rv_rank.findViewHolderForLayoutPosition(index)?.itemView?.getLocationOnScreen(startLocation1)

                if (startLocation[0] <= startLocation1[0]) {
//                    LogUtils.e("index_position:" + index + "rv_rank" + rv_rank.getChildAt(index).x + "-----rv_list" + rv_list.getChildAt(index).x)
                    mRankRVAdapter!!.setPosition(index)
                } else {
                    return@loop
                }
            }

        }

//        rv_list.setOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//            }
//
//            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                val layoutManager = recyclerView!!.layoutManager
//                //判断是当前layoutManager是否为LinearLayoutManager
//                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
//                if (layoutManager is LinearLayoutManager) {
////获取最后一个可见view的位置
//                    val lastItemPosition = layoutManager.findLastVisibleItemPosition()
//                    //获取第一个可见view的位置
//                    val firstItemPosition = layoutManager.findFirstVisibleItemPosition()
//                    LogUtils.e(lastItemPosition.toString() + "   " + firstItemPosition)
//                }
//            }
//        })
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null

    override fun toast_msg(msg: String) {
        toast(msg)
    }
}
