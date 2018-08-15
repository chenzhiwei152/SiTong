package com.sitong.changqin.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jyall.bbzf.extension.loadImage
import com.sitong.changqin.R
import com.sitong.changqin.mvp.model.bean.MusicBean
import com.sitong.changqin.ui.listerner.RVAdapterItemOnClick
import kotlinx.android.synthetic.main.item_index_second.view.*

/**
 * create by chen.zhiwei on 2018-8-14
 */
class IndexMusicAdapter(var context: Context, lists: ArrayList<MusicBean.Music>) : RecyclerView.Adapter<IndexMusicAdapter.ViewHolder>() {
    var list = arrayListOf<MusicBean.Music>()

    init {
        this.list = lists
    }

    private var onItemClick: RVAdapterItemOnClick? = null

    fun setListerner(onItemClick: RVAdapterItemOnClick) {
        this.onItemClick = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_index_second, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.viewLayout.iv_image.loadImage(context, list[position].icon)
        holder.viewLayout.setOnClickListener{
            onItemClick?.onItemClicked(list[position])
        }
    }


    class ViewHolder(val viewLayout: View) : RecyclerView.ViewHolder(viewLayout)
}
