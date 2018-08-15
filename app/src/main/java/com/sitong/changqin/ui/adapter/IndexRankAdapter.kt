package com.sitong.changqin.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sitong.changqin.R
import com.sitong.changqin.mvp.model.bean.MusicBean
import com.sitong.changqin.ui.listerner.RVAdapterItemOnClick
import kotlinx.android.synthetic.main.item_index_third.view.*

/**
 * create by chen.zhiwei on 2018-8-14
 */
class IndexRankAdapter(var context: Context) : RecyclerView.Adapter<IndexRankAdapter.ViewHolder>() {
    var list = arrayListOf<MusicBean>()
    var posi = 1
    fun setData(all: ArrayList<MusicBean>) {
        list?.addAll(all)
        notifyDataSetChanged()
    }

    fun clearData() {
        list?.clear()
        notifyDataSetChanged()
    }

    fun setPosition(po: Int) {
        this.posi = po
        notifyDataSetChanged()
    }

    private var onItemClick: RVAdapterItemOnClick? = null

    fun setListerner(onItemClick: RVAdapterItemOnClick) {
        this.onItemClick = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_index_third, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.viewLayout.tv_rank.text = list[position].levelName
        if (position <= posi) {
            holder.viewLayout.tv_rank.visibility = View.GONE
        } else {
            holder.viewLayout.tv_rank.visibility = View.VISIBLE
        }
    }


    class ViewHolder(val viewLayout: View) : RecyclerView.ViewHolder(viewLayout)
}
