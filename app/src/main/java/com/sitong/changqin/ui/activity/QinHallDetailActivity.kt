package com.sitong.changqin.ui.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.MotionEvent
import android.view.View
import com.jyall.android.common.utils.UIUtil
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.extension.loadImage
import com.jyall.bbzf.extension.toast
import com.sitong.changqin.mvp.contract.QinHallDetailContract
import com.sitong.changqin.mvp.model.bean.QinguanDetailBean
import com.sitong.changqin.mvp.persenter.qinHalDetailPresenter
import com.sitong.changqin.ui.adapter.QinHallDetilListAdapter
import com.stringedzithers.sitong.R
import com.yinglan.scrolllayout.ScrollLayout
import kotlinx.android.synthetic.main.activity_qin_hall_detail.*
import kotlinx.android.synthetic.main.layout_common_title.*
import android.R.attr.startX
import android.R.attr.startY
import android.os.Build
import android.support.annotation.RequiresApi


class QinHallDetailActivity : BaseActivity<QinHallDetailContract.View, qinHalDetailPresenter>(), QinHallDetailContract.View, ScrollLayout.OnScrollChangedListener {
    override fun onScrollProgressChanged(currentProgress: Float) {
        if (currentProgress >= 0) {
            var precent = 255 * currentProgress
            if (precent > 255) {
                precent = 255f
            } else if (precent < 0) {
                precent = 0f
            }
            scrollLayout.getBackground().setAlpha(255 - precent.toInt())
        }
//        if (text_foot.getVisibility() == View.VISIBLE)
//            text_foot.setVisibility(View.GONE)
    }

    override fun onScrollFinished(currentStatus: ScrollLayout.Status?) {
        if (currentStatus == ScrollLayout.Status.EXIT) {
            title2.setBackgroundColor(resources.getColor(R.color.color_ffffff))
            tv_title.visibility = View.VISIBLE
            vv_divider.visibility = View.VISIBLE
            iv_menu.visibility = View.VISIBLE
        } else if (currentStatus == ScrollLayout.Status.CLOSED) {
            title2.setBackgroundColor(resources.getColor(R.color.color_ffffff))
            tv_title.visibility = View.VISIBLE
            vv_divider.visibility = View.VISIBLE
            iv_menu.visibility = View.VISIBLE
        } else {
            title2.setBackgroundColor(resources.getColor(R.color.albumTransparent))
            tv_title.visibility = View.GONE
            vv_divider.visibility = View.GONE
            iv_menu.visibility = View.GONE
        }
    }

    override fun onChildScroll(top: Int) {
    }

    override fun getPresenter(): qinHalDetailPresenter = qinHalDetailPresenter()

    override fun getRootView(): QinHallDetailContract.View = this

    override fun toast_msg(msg: String) {
        toast(msg)
    }

    override fun getDataSuccess(bean: QinguanDetailBean) {
        iv_fengmian.loadImage(this@QinHallDetailActivity, bean.img)
        tv_title.setText(bean.name)
        tv_titles.setText(bean.name)
        mAdapter?.setData(bean.content)
    }

    private var id: String? = null
    private var mAdapter: QinHallDetilListAdapter? = null

    override fun getLayoutId(): Int = R.layout.activity_qin_hall_detail

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initViewsAndEvents() {
        initTitle()
        if (intent.extras != null) {
            id = intent.extras.getString("id")
        }

        mAdapter = QinHallDetilListAdapter(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter


//        scrollLayout.setMinOffset(UIUtil.dip2px(this,title2.measuredHeight.toFloat()))
        scrollLayout.setMinOffset(270)
        scrollLayout.setMaxOffset(-70)
        scrollLayout.setExitOffset(0)
        scrollLayout.setToOpen()
        scrollLayout.setIsSupportExit(true)
        scrollLayout.setAllowHorizontalScroll(true)
        scrollLayout.setOnScrollChangedListener(this)

        iv_more.setOnClickListener { scrollLayout.setToClosed() }


        var mp = hashMapOf<String, String>()
        mp.put("id", id!!)
        mPresenter?.getList(mp)
        var startX = 0F
        var startY = 0F
        rl_content.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, ev: MotionEvent?): Boolean {

                when (ev?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        startX = ev.getRawX()
                        startY = ev.getRawY()

                    }
                    MotionEvent.ACTION_MOVE -> {
                        val endX = ev?.getRawX()
                        val endY = ev?.getRawY()

                        if (Math.abs(endX!!.minus(startX)) > Math.abs(endY!!.minus(startY))) {// 左右滑动
                            if (endX > startX) {// 右划
//                        if (getCurrentItem() === 0) {// 第一个页面, 需要父控件拦截
//                            parent.requestDisallowInterceptTouchEvent(false)
//                        }
                            } else {// 左划
//                        if (getCurrentItem() === getAdapter().getCount() - 1) {// 最后一个页面,
//                            // 需要拦截
//                            parent.requestDisallowInterceptTouchEvent(false)
//                        }
                            }
                        } else {// 上下滑动
//                    parent.requestDisallowInterceptTouchEvent(false)
                            if (startY.minus(endY) > 10) {
                                scrollLayout.setToClosed()
                                return true
                            }

                        }
                    }
                    MotionEvent.ACTION_UP -> {

                    }
                }

                return false
            }

        })
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null

    private fun initTitle() {
        iv_back.setOnClickListener { finish() }
        tv_title.setText("")
        tv_title.visibility = View.GONE
        vv_divider.visibility = View.GONE
        iv_menu.visibility = View.GONE
    }
}