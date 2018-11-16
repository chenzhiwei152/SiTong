package com.sitong.changqin.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sevenstringedzithers.sitong.R
import com.sitong.changqin.mvp.model.bean.MusicBean
import com.sitong.changqin.ui.listerner.RVAdapterItemOnClick
import kotlinx.android.synthetic.main.item_index_third.view.*

/**
 * create by chen.zhiwei on 2018-8-14
 */
class IndexLetterLinkdapter(var context: Context, var list: ArrayList<MusicBean.Music>) : RecyclerView.Adapter<IndexLetterLinkdapter.ViewHolder>() {

    private var letteOnclick: RVAdapterItemOnClick? = null


    fun setLetterOnclick(onItemClick: RVAdapterItemOnClick) {
        this.letteOnclick = onItemClick
    }


    fun setData(all: ArrayList<MusicBean.Music>) {
        list?.addAll(all)
        notifyDataSetChanged()
    }

    fun clearData() {
        list?.clear()
        notifyDataSetChanged()
    }


//    private var onItemClick: RVAdapterItemOnClick? = null
//
//    fun setListerner(onItemClick: RVAdapterItemOnClick) {
//        this.onItemClick = onItemClick
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_index_letter_link, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.viewLayout.tv_rank_tag.text = list[position].letter
        holder.viewLayout.setOnClickListener {
            letteOnclick?.onItemClicked(position)
        }
    }


    class ViewHolder(val viewLayout: View) : RecyclerView.ViewHolder(viewLayout)
}
