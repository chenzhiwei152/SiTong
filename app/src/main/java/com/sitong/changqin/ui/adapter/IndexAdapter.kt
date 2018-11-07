package com.sitong.changqin.ui.adapter

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sitong.changqin.mvp.model.bean.MusicBean
import com.sitong.changqin.ui.listerner.RVAdapterItemOnClick
import com.stringedzithers.sitong.R
import kotlinx.android.synthetic.main.item_index_first.view.*

/**
 * create by chen.zhiwei on 2018-8-14
 */
class IndexAdapter(var context: Context) : RecyclerView.Adapter<IndexAdapter.ViewHolder>() {
    var list = arrayListOf<MusicBean>()
    var posi = 1
    private var onItemClick: RVAdapterItemOnClick? = null

    fun setListerner(onItemClick: RVAdapterItemOnClick) {
        this.onItemClick = onItemClick
    }

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
        updatePosition(posi)
        if (posi < list.size - 1) {
            updatePosition(posi + 1)
        }
    }

    fun updatePosition(po: Int) {
        notifyItemChanged(po)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_index_first, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.viewLayout.tag = position
        holder.viewLayout.tv_rank.text = list[position].levelName
        holder.viewLayout.ll_tag.tag = "sticky"
        if (position <= posi) {
            holder.viewLayout.tv_rank.visibility = View.VISIBLE
        } else {
            holder.viewLayout.tv_rank.visibility = View.INVISIBLE
        }
        if (list[position].musics.isNotEmpty()) {
            holder.viewLayout.rv_music_list.layoutManager = LinearLayoutManager(context)
            holder.viewLayout.rv_music_list.isNestedScrollingEnabled = false
            var ada = IndexMusicAdapter(context, list[position].musics as ArrayList<MusicBean.Music>)
            holder.viewLayout.rv_music_list.adapter = ada
            ada.setListerner(object : RVAdapterItemOnClick {
                override fun onItemClicked(data: Any) {
                    onItemClick?.onItemClicked(data)
                }
            })
        }
    }


    class ViewHolder(val viewLayout: View) : RecyclerView.ViewHolder(viewLayout)
}
