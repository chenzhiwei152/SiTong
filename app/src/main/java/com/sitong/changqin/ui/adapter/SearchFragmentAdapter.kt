package com.jyall.bbzf.ui.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.View
import com.sitong.changqin.R
import com.sitong.changqin.ui.fragment.ArticleFragment
import com.sitong.changqin.ui.fragment.QinHallFragment
import com.sitong.changqin.ui.fragment.VideoFragment
import kotlinx.android.synthetic.main.item_tablayout.view.*

/**
 * 寻 fragment适配器
 * create by chen.zhiwei on 2018-7-11
 */
class SearchFragmentAdapter(mContext: Context, fm: FragmentManager?, tabs: ArrayList<String>?, mRolder: String) : FragmentStatePagerAdapter(fm) {
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
                fragment = QinHallFragment.newInstance()
            }
            1 -> {
                fragment = VideoFragment.newInstance()
            }
            2 -> {
                fragment = ArticleFragment.newInstance()
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
    fun getTabItemView(position: Int): View {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_tablayout, null)
        view.tv_title.text = tabs?.get(position)
        return view
    }
}