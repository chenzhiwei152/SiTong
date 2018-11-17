package com.sevenstringedzithers.sitong.ui.activity

import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.extension.toast
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.mvp.contract.ExerciseRecordContract
import com.sevenstringedzithers.sitong.mvp.model.bean.ExerciseRecordBean
import com.sevenstringedzithers.sitong.mvp.persenter.ExerciseRecordPresenter
import com.sevenstringedzithers.sitong.ui.adapter.ExerciseRecordListAdapter
import com.yanzhenjie.recyclerview.swipe.SwipeMenu
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener
import kotlinx.android.synthetic.main.activity_exercise_record.*
import kotlinx.android.synthetic.main.layout_common_title.*


/**
 * 练习记录
 * create by chen.zhiwei on 2018-8-13
 */
class ExperienceRecordActivity : BaseActivity<ExerciseRecordContract.View, ExerciseRecordPresenter>(), ExerciseRecordContract.View {
    private var mAdapter:ExerciseRecordListAdapter?=null
    override fun getPresenter(): ExerciseRecordPresenter =ExerciseRecordPresenter()

    override fun getRootView(): ExerciseRecordContract.View=this

    override fun loginSuccess(bean: ExerciseRecordBean) {
        mAdapter?.setData(bean.music)
    }




    override fun getLayoutId(): Int = R.layout.activity_exercise_record

    override fun initViewsAndEvents() {
        initTitle()
//        rv_list.isItemViewSwipeEnabled=true
        rv_list.layoutManager = LinearLayoutManager(this)




// 创建菜单：
        var  mSwipeMenuCreator =  object :SwipeMenuCreator{
            override fun onCreateMenu(swipeLeftMenu: SwipeMenu?, swipeRightMenu: SwipeMenu?, viewType: Int) {
                val width = resources.getDimensionPixelSize(R.dimen.dp_90)

                // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
                // 2. 指定具体的高，比如80;
                // 3. WRAP_CONTENT，自身高度，不推荐;
                val height = ViewGroup.LayoutParams.MATCH_PARENT
                var  deleteItem =  SwipeMenuItem(this@ExperienceRecordActivity).setText("删除").setWidth(width).setHeight(height).setTextColor(Color.WHITE).setBackgroundColor(resources.getColor(R.color.color_d0a670))
             // 各种文字和图标属性设置。
                swipeRightMenu?.addMenuItem(deleteItem) // 在Item左侧添加一个菜单。

            }

        }

        // 设置监听器。
        rv_list.setSwipeMenuCreator(mSwipeMenuCreator)
        val mMenuItemClickListener = SwipeMenuItemClickListener { menuBridge ->
            // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
            menuBridge.closeMenu()

            val direction = menuBridge.direction // 左侧还是右侧菜单。
            val adapterPosition = menuBridge.adapterPosition // RecyclerView的Item的position。
            val menuPosition = menuBridge.position // 菜单在RecyclerView的Item中的Position。
        }
        // 菜单点击监听。
        rv_list.setSwipeMenuItemClickListener(mMenuItemClickListener)

        mAdapter= ExerciseRecordListAdapter(this)
        rv_list.adapter=mAdapter
        var ll= arrayListOf<ExerciseRecordBean.Music>()
        ll.add(ExerciseRecordBean.Music("测试",""))
        ll.add(ExerciseRecordBean.Music("测试",""))
        ll.add(ExerciseRecordBean.Music("测试",""))
        ll.add(ExerciseRecordBean.Music("测试",""))
        ll.add(ExerciseRecordBean.Music("测试",""))
        ll.add(ExerciseRecordBean.Music("测试",""))
        ll.add(ExerciseRecordBean.Music("测试",""))
//        mAdapter?.setData(ll)

        mPresenter?.getList()
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null

    override fun toast_msg(msg: String) {
        toast(msg)
    }
    private fun initTitle(){
        iv_back.setOnClickListener { finish() }
        tv_title.setText("练习记录")
        iv_menu.visibility=View.GONE
    }
}