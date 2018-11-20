package com.jyall.bbzf.ui.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.View
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.ui.fragment.FileFragment
import com.sevenstringedzithers.sitong.ui.fragment.InformationFragment
import com.sevenstringedzithers.sitong.ui.fragment.MemberFragment
import com.sevenstringedzithers.sitong.ui.fragment.TaskFragment
import kotlinx.android.synthetic.main.item_tablayout.view.*

/**
 * 寻 fragment适配器
 * create by chen.zhiwei on 2018-7-11
 */
class MineInfoFragmentAdapter(mContext: Context, fm: FragmentManager?, tabs: ArrayList<String>?, mRolder: String) : FragmentStatePagerAdapter(fm) {
    private var tabs: ArrayList<String>? = null
    private var fm: FragmentManager? = null
    private var mContext: Context? = null

    init {
        this.tabs = tabs
        this.fm = fm
        this.mContext = mContext
    }


    override fun getCount(): Int = if (tabs == null) 0 else tabs!!.size

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = InformationFragment.newInstance()
            }
            1 -> {
                fragment = TaskFragment.newInstance()
            }
            2 -> {
                fragment = FileFragment.newInstance()
            }
            3 -> {
                fragment = MemberFragment.newInstance()
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
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_tablayout, null)
        if (isCurrent){
            view.tv_title.text = "【  "+tabs?.get(position)+"  】"
        }else{
            view.tv_title.text =tabs?.get(position)
        }
        return view
    }
}