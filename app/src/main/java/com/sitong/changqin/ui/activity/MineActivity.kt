package com.sitong.changqin.ui.activity

import android.support.design.widget.TabLayout
import android.view.View
import android.view.ViewGroup
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.base.IBaseView
import com.jyall.bbzf.ui.adapter.MineInfoFragmentAdapter
import com.sitong.changqin.R
import com.sitong.changqin.utils.ExtraUtils
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * create by chen.zhiwei on 2018-8-15
 */
class MineActivity : BaseActivity<IBaseView, BasePresenter<IBaseView>>(), IBaseView {
    private var tabsTitle: ArrayList<String> = arrayListOf()
    private var pageAdapter: MineInfoFragmentAdapter? = null
    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()

    override fun getRootView(): IBaseView = this

    override fun getLayoutId(): Int = R.layout.fragment_mine

    override fun initViewsAndEvents() {
        tv_rank.setText(ExtraUtils.ToSBC("6"))

        tabsTitle.add(resources.getString(R.string.information))
        tabsTitle.add(resources.getString(R.string.task))
        tabsTitle.add(resources.getString(R.string.file))
        tabsTitle.add(resources.getString(R.string.member))


        pageAdapter = MineInfoFragmentAdapter(this, supportFragmentManager, tabsTitle, "1")
        view_pager.adapter = pageAdapter
        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tablayout))
        tablayout.setupWithViewPager(view_pager)
        setUpTabBadge(tabsTitle)


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
            tab?.customView = pageAdapter?.getTabItemView(i)
        }
        // 需加上以下代码,不然会出现更新Tab角标后,选中的Tab字体颜色不是选中状态的颜色
        tablayout.getTabAt(tablayout.selectedTabPosition)?.customView?.isSelected = true
    }
}