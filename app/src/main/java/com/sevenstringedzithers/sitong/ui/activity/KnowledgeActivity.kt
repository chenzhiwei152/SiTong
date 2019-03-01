package com.sevenstringedzithers.sitong.ui.activity

import android.support.design.widget.TabLayout
import android.view.View
import android.view.ViewGroup
import com.jyall.bbzf.ui.adapter.KnowledgeFragmentAdapter
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.base.BaseActivity
import com.sevenstringedzithers.sitong.base.BasePresenter
import com.sevenstringedzithers.sitong.base.IBaseView
import kotlinx.android.synthetic.main.activity_knowledge.*

class KnowledgeActivity : BaseActivity<IBaseView, BasePresenter<IBaseView>>(), IBaseView {
    private var tabsTitle: ArrayList<String> = arrayListOf()
    private var pageAdapter: KnowledgeFragmentAdapter? = null
    private var tabIcon: ArrayList<Int>? =arrayListOf()
    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()

    override fun getRootView(): IBaseView = this

    override fun getLayoutId(): Int = R.layout.activity_knowledge

    override fun initViewsAndEvents() {

        tabsTitle.add(resources.getString(R.string.describe_qin))
        tabsTitle.add(resources.getString(R.string.describe_gesture))
        tabsTitle.add(resources.getString(R.string.describe_jianzi))

        tabIcon?.add(R.drawable.selector_describe_qin)
        tabIcon?.add(R.drawable.selector_describe_gesture)
        tabIcon?.add(R.drawable.selector_describe_jianzi)


        pageAdapter = KnowledgeFragmentAdapter(this, supportFragmentManager, tabsTitle, tabIcon)
        view_pager.adapter = pageAdapter
        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tablayout))
        tablayout.setupWithViewPager(view_pager)
        setUpTabBadge(tabsTitle)

//        tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//            }
//
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                setUpTabBadge(tabsTitle)
//            }
//
//        })

    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null
    /**
     * 更新tablayout itemview
     *
     */
    private fun setUpTabBadge(tabName: ArrayList<String>) {
        for (i in tabName.indices) {
            val tab = tablayout.getTabAt(i)

            // 更新Badge前,先remove原来的customView,否则Badge无法更新
            val customView = tab?.customView
            if (customView != null) {
                val parent = customView!!.parent
                if (parent != null) {
                    (parent as ViewGroup).removeView(customView)
                }
            }
            // 更新CustomView
            tab?.customView = pageAdapter?.getTabItemView(i,i==tablayout.selectedTabPosition)
        }
        // 需加上以下代码,不然会出现更新Tab角标后,选中的Tab字体颜色不是选中状态的颜色
        tablayout.getTabAt(tablayout.selectedTabPosition)?.customView?.isSelected = true
    }
}