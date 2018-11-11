package com.sitong.changqin.ui.fragment

import android.content.ContentValues.TAG
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.RadioGroup
import com.github.huajianjiang.expandablerecyclerview.util.Logger
import com.github.huajianjiang.expandablerecyclerview.widget.ExpandableAdapter
import com.github.huajianjiang.expandablerecyclerview.widget.ExpandableRecyclerView
import com.github.huajianjiang.expandablerecyclerview.widget.Parent
import com.github.huajianjiang.expandablerecyclerview.widget.ParentViewHolder
import com.jyall.bbzf.base.BaseFragment
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.base.IBaseView
import com.jyall.bbzf.extension.toast
import com.sitong.changqin.ui.adapter.MyAdapter
import com.sitong.changqin.utils.AppUtil
import com.sitong.changqin.view.expandable.PresenterImpl
import com.sitong.changqin.view.expandable.model.MyParent
import com.stringedzithers.sitong.R
import kotlinx.android.synthetic.main.fragment_describe_gesture.*
import java.util.ArrayList

/**
 * 延音
 * create by chen.zhiwei on 2018-8-15
 */
class DescribeGestureFragment : BaseFragment<IBaseView, BasePresenter<IBaseView>>(), IBaseView, ExpandableAdapter.OnParentExpandCollapseListener {
    override fun onParentExpanded(rv: RecyclerView?, pvh: ParentViewHolder<out Parent<*>>?, position: Int, pendingCause: Boolean, byUser: Boolean) {
        var iv = pvh?.getView<ImageView>(R.id.android)
        iv?.setImageResource(mData?.get(position)!!.image[1])
    }

    override fun onParentCollapsed(rv: RecyclerView?, pvh: ParentViewHolder<out Parent<*>>?, position: Int, pendingCause: Boolean, byUser: Boolean) {
        var iv = pvh?.getView<ImageView>(R.id.android)
        iv?.setImageResource(mData?.get(position)!!.image[0])

    }

    private val TAG = "SingleRvFragment"
    val KEY_DATA = "data"
    //    private var mRv: ExpandableRecyclerView? = null
    private var mAdapter: MyAdapter? = null
    private var mItemAnimator: RecyclerView.ItemAnimator? = null
    private var mIPresenter: PresenterImpl? = null
    private var mData: ArrayList<MyParent>? = arrayListOf()
    private var currentType = 1//0 右手势  1 左手势
    override fun getLayoutId(): Int = R.layout.fragment_describe_gesture
    override fun lazyLoad() {
    }

    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()
    override fun getRootView(): IBaseView = this

    override fun initViewsAndEvents() {
        mData = AppUtil.getListData(currentType)
        mAdapter = MyAdapter(requireActivity(), mData)
        mAdapter?.setExpandCollapseMode(ExpandableAdapter.ExpandCollapseMode.MODE_DEFAULT)

        mAdapter?.addParentExpandableStateChangeListener(ParentExpandableStateChangeListener())
        mAdapter?.addParentExpandCollapseListener(this)
        rv_list.setAdapter(mAdapter)
        rg_group.setOnCheckedChangeListener { p0, p1 ->
            when (p1) {
                R.id.rb_left -> {
                    if (currentType != 1) {
                        currentType = 1
                        setChangeState()
                        mData = AppUtil.getListData(currentType)
                        mAdapter?.updateList(mData)
                    }

                }
                R.id.rb_right -> {
                    if (currentType != 0) {
                        currentType = 0
                        setChangeState()
                        mData = AppUtil.getListData(currentType)
                        mAdapter?.updateList(mData)
                    }
                }
            }
        }
        setChangeState()
    }

    private fun setChangeState() {
        if (currentType == 0) {
            rb_left.text = "左"
            rb_right.text = "【" + "右" + "】"
        } else {
            rb_left.text = "【" + "左" + "】"
            rb_right.text = "右"
        }
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null

    companion object {
        fun newInstance(): DescribeGestureFragment {
            return DescribeGestureFragment()
        }
    }

    private inner class ParentExpandableStateChangeListener : ExpandableAdapter.OnParentExpandableStateChangeListener {
        override fun onParentExpandableStateChanged(rv: RecyclerView, pvh: ParentViewHolder<*>?,
                                                    position: Int, expandable: Boolean) {
            Logger.e(TAG, "onParentExpandableStateChanged=" + position + "," + rv.tag)
            if (pvh == null) return
            val arrow = pvh.getView<ImageView>(R.id.arrow)
            if (expandable && arrow.getVisibility() != View.VISIBLE) {
                arrow.setVisibility(View.VISIBLE)
                arrow.setRotation((if (pvh.isExpanded) 180 else 0).toFloat())
            } else if (!expandable && arrow.getVisibility() == View.VISIBLE) {
                arrow.setVisibility(View.GONE)
            }
        }
    }

}



