package com.sitong.changqin.ui.fragment

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.github.huajianjiang.expandablerecyclerview.util.Logger
import com.github.huajianjiang.expandablerecyclerview.widget.ExpandableAdapter
import com.github.huajianjiang.expandablerecyclerview.widget.ExpandableRecyclerView
import com.github.huajianjiang.expandablerecyclerview.widget.ParentViewHolder
import com.jyall.bbzf.base.BaseFragment
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.base.IBaseView
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
class DescribeGestureFragment : BaseFragment<IBaseView, BasePresenter<IBaseView>>(), IBaseView {

    private val TAG = "SingleRvFragment"
    val KEY_DATA = "data"
    //    private var mRv: ExpandableRecyclerView? = null
    private var mAdapter: MyAdapter? = null
    private var mItemAnimator: RecyclerView.ItemAnimator? = null
    private var mIPresenter: PresenterImpl? = null
    private var mData: ArrayList<MyParent>? = arrayListOf()
    override fun getLayoutId(): Int = R.layout.fragment_describe_gesture
    override fun lazyLoad() {
    }

    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()
    override fun getRootView(): IBaseView = this

    override fun initViewsAndEvents() {
        mData = AppUtil.getListData()
        mAdapter = MyAdapter(requireActivity(), mData)
        mAdapter?.setExpandCollapseMode(ExpandableAdapter.ExpandCollapseMode.MODE_DEFAULT)

        mAdapter?.addParentExpandableStateChangeListener(ParentExpandableStateChangeListener())
        mAdapter?.addParentExpandCollapseListener(ParentExpandCollapseListener())
        rv_list.setAdapter(mAdapter)
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

    private inner class ParentExpandCollapseListener : ExpandableAdapter.OnParentExpandCollapseListener {
        override fun onParentExpanded(rv: RecyclerView, pvh: ParentViewHolder<*>?, position: Int,
                                      pendingCause: Boolean, byUser: Boolean) {
            Logger.e(TAG, "onParentExpanded=" + position + "," + rv.tag + ",byUser=" + byUser)
            if (pvh == null) return
            val arrow = pvh.getView<ImageView>(R.id.arrow)
            if (arrow.getVisibility() != View.VISIBLE) return
            val currRotate = arrow.getRotation()
            //重置为从0开始旋转
            if (currRotate == 360f) {
                arrow.setRotation(0f)
            }
            if (pendingCause) {
                arrow.setRotation(180f)
            } else {
                arrow.animate()
                        .rotation(180f)
                        .setDuration(mItemAnimator?.getAddDuration()!!.plus(180))
                        .start()
            }

            //            if (byUser) {
            //                int scrollToPos =
            //                        pvh.getAdapterPosition() + ((MyParent) pvh.getParent()).getChildCount();
            //                rv.scrollToPosition(scrollToPos);
            //            }
        }

        override fun onParentCollapsed(rv: RecyclerView, pvh: ParentViewHolder<*>?, position: Int,
                                       pendingCause: Boolean, byUser: Boolean) {
            Logger.e(TAG,
                    "onParentCollapsed=" + position + ",tag=" + rv.tag + ",byUser=" + byUser)

            if (pvh == null) return
            val arrow = pvh.getView<ImageView>(R.id.arrow)
            if (arrow.getVisibility() != View.VISIBLE) return
            val currRotate = arrow.getRotation()
            var rotate = 360f
            //未展开完全并且当前旋转角度小于180，逆转回去
            if (currRotate < 180) {
                rotate = 0f
            }
            if (pendingCause) {
                arrow.setRotation(rotate)
            } else {
                arrow.animate()
                        .rotation(rotate)
                        .setDuration(mItemAnimator?.getRemoveDuration()!!.plus(180))
                        .start()
            }
        }
    }
}


