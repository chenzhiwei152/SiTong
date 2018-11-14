package com.sitong.changqin.ui.activity

import android.support.design.widget.TabLayout
import android.view.View
import android.view.ViewGroup
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.base.IBaseView
import com.jyall.bbzf.extension.jump
import com.jyall.bbzf.ui.adapter.SearchFragmentAdapter
import com.sevenstringedzithers.sitong.R
import kotlinx.android.synthetic.main.activity_search.*

/**
 * 寻
 * create by chen.zhiwei on 2018-8-15
 */
class MusicSearchActivity : BaseActivity<IBaseView, BasePresenter<IBaseView>>(), IBaseView {

    private var tabsTitle: ArrayList<String> = arrayListOf()
    private var pageAdapter: SearchFragmentAdapter? = null
    override fun getRootView(): IBaseView = this

    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()

    private var mThread: Thread? = null
    var id: String? = null


    override fun getLayoutId(): Int = R.layout.activity_search

    override fun initViewsAndEvents() {

        tabsTitle.add(resources.getString(R.string.qin_hall))
        tabsTitle.add(resources.getString(R.string.video))
        tabsTitle.add(resources.getString(R.string.article))


        pageAdapter = SearchFragmentAdapter(this, supportFragmentManager, tabsTitle, "1")
        view_pager.adapter = pageAdapter
        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tablayout))
        tablayout.setupWithViewPager(view_pager)
        setUpTabBadge(tabsTitle)

        tablayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                setUpTabBadge(tabsTitle)
            }

        })
        iv_menu.setOnClickListener {
            jump<MenuActivity>(isAnimation = false)
        }

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