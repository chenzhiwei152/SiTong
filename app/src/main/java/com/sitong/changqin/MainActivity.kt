package com.sitong.changqin

import android.support.design.widget.AppBarLayout
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
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

    private var mRVAdapter: IndexAdapter? = null
    private var mRankRVAdapter: IndexRankAdapter? = null
    private var lists = arrayListOf<MusicBean>()
    private var mKeys = arrayListOf<Int>()
    private var mValues = arrayListOf<Int>()
    override fun getDataSuccess(musicList: ArrayList<MusicBean>) {
//        toast_msg("" + musicList?.size)
        mRVAdapter?.setData(musicList)
        mRankRVAdapter?.setData(musicList)
        lists = musicList
        rv_rank.postDelayed({
            getValue()
//            controlLetters(true)
        }, 3000)


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
        rv_list.layoutManager = LinearLayoutManager(this)
        rv_list.adapter = mRVAdapter

        mRankRVAdapter = IndexRankAdapter(this)
        rv_rank.layoutManager = LinearLayoutManager(this)
        rv_rank.adapter = mRankRVAdapter

        mPresenter?.getMusicList("1")



        stick_scroll.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
                controlLetters(scrollY > oldScrollY)
            }

        })

    }

    var mPosition = 1
    fun controlLetters(flag: Boolean) {
        try {
            //        var mKey = IntArray(2)
//        rv_rank.findViewHolderForLayoutPosition(mPosition)?.itemView?.getLocationOnScreen(mKey)


            if (mKeys.size <= 0 || mKeys[0] <= 0) {
                getValue()
                return
            }
            if (flag) {
                //上滑
                var mValue = IntArray(2)
                rv_list.findViewHolderForLayoutPosition(mPosition)?.itemView?.getLocationOnScreen(mValue)
                if (mKeys[mPosition] >= mValue[1]) {
//                mRVAdapter!!.setPosition(mPosition)
                    var hol = rv_list.findViewHolderForLayoutPosition(mPosition)?.itemView?.findViewById<TextView>(R.id.tv_rank)
                    hol?.visibility = View.VISIBLE
                    mRankRVAdapter!!.setPosition(mPosition)
                    if (mPosition < lists.size - 1) {
                        mPosition++
//                    LogUtils.e("++--mPosition:" + mPosition + "-----key:" + mKeys[mPosition] + "-----value:" + mValue[1])
                    }
                }

            } else {
//下滑
                var cache = 0
                if (mPosition > 0) {
                    cache = mPosition - 1
                } else {
                    cache = mPosition
                }
                var mValue = IntArray(2)
                rv_list.findViewHolderForLayoutPosition(cache)?.itemView?.getLocationOnScreen(mValue)
                if (mKeys[cache] <= mValue[1]) {
                    mRankRVAdapter!!.setPosition(cache - 1)
//                mRVAdapter!!.setPosition(cache - 1)
                    var hol = rv_list.findViewHolderForLayoutPosition(cache)?.itemView?.findViewById<TextView>(R.id.tv_rank)
                    hol?.visibility = View.INVISIBLE
                    mPosition = cache
//                LogUtils.e("----mPosition:" + mPosition + "-----key:" + mKeys[mPosition] + "-----value:" + mValue[1])
                }

            }
        } catch (ex: Exception) {

        }
    }

    fun getValue() {
//        mValues.clear()
        mKeys.clear()
        mRankRVAdapter?.list?.forEachIndexed { index, value ->
            var startLocation = IntArray(2)
            rv_rank.findViewHolderForLayoutPosition(index)?.itemView?.getLocationOnScreen(startLocation)
            mKeys.add(startLocation[1])
//            LogUtils.e("Stickysssssss:" + startLocation[0].toString() + "----" + startLocation[1].toString())
        }
//        mRVAdapter?.list?.forEachIndexed { index, value ->
//            var startLocation = IntArray(2)
//            rv_list.findViewHolderForLayoutPosition(index)?.itemView?.getLocationOnScreen(startLocation)
//            mValues.add(startLocation[1])
////            LogUtils.e("Stickysssssss:" + startLocation[0].toString() + "----" + startLocation[1].toString())
//        }
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null

    override fun toast_msg(msg: String) {
        toast(msg)
    }
}
