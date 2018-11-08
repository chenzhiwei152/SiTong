package com.jyall.bbzf.ui.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.View
import com.sitong.changqin.ui.fragment.DescribeGestureFragment
import com.sitong.changqin.ui.fragment.DescribeQinFragment
import com.sitong.changqin.ui.fragment.DescribejianziFragment
import com.stringedzithers.sitong.R
import kotlinx.android.synthetic.main.item_tablayout_image.view.*

/**
 * 寻 fragment适配器
 * create by chen.zhiwei on 2018-7-11
 */
class KnowledgeFragmentAdapter(mContext: Context, fm: FragmentManager?, tabs: ArrayList<String>?, tabIcon : ArrayList<Int>?) : FragmentStatePagerAdapter(fm) {
    private var tabs: ArrayList<String>? = null
    private var tabIcon: ArrayList<Int>? = null
    private var fm: FragmentManager? = null
    private var mContext: Context? = null

    init {
        this.tabs = tabs
        this.fm = fm
        this.mContext = mContext
        this.tabIcon=tabIcon
    }


    override fun getCount(): Int = if (tabs == null) 0 else tabs!!.size

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = DescribeQinFragment.newInstance()
            }
            1 -> {
                fragment = DescribeGestureFragment.newInstance()
            }
            2 -> {
                fragment = DescribejianziFragment.newInstance()
            }
        }
        return fragment!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (tabs != null) tabs!![position] else ""
    }

            /**
     * 自定义tablayout 布局
     *
     * @param position
     * @return
     */
    fun getTabItemView(position: Int,isCurrent:Boolean=false): View {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_tablayout_image, null)
            view.tv_title.setText(tabs?.get(position))
        view.iv_image.setImageResource(tabIcon?.get(position)!!)
        return view
    }
}