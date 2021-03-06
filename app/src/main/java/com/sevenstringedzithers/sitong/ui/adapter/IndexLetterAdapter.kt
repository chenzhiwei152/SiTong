package com.sevenstringedzithers.sitong.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.mvp.model.bean.MusicBean
import com.sevenstringedzithers.sitong.ui.listerner.RVAdapterItemOnClick
import kotlinx.android.synthetic.main.item_index_letter.view.*

/**
 * create by chen.zhiwei on 2018-8-14
 */
class IndexLetterAdapter(var context: Context, lists: ArrayList<MusicBean.Music.Music>) : RecyclerView.Adapter<IndexLetterAdapter.ViewHolder>() {
    var list = arrayListOf<MusicBean.Music.Music>()

    init {
        this.list = lists
    }

    private var onItemClick: RVAdapterItemOnClick? = null

    fun setListerner(onItemClick: RVAdapterItemOnClick) {
        this.onItemClick = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_index_letter, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.viewLayout.tv_name.text = list[position].name
        holder.viewLayout.setOnClickListener {
            onItemClick?.onItemClicked(list[position])
        }
    }


    class ViewHolder(val viewLayout: View) : RecyclerView.ViewHolder(viewLayout)
}
