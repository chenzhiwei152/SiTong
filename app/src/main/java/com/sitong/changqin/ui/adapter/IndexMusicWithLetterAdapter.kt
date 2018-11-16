package com.sitong.changqin.ui.adapter

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sevenstringedzithers.sitong.R
import com.sitong.changqin.mvp.model.bean.MusicBean
import com.sitong.changqin.ui.listerner.RVAdapterItemOnClick
import kotlinx.android.synthetic.main.item_index_with_letter.view.*

/**
 * create by chen.zhiwei on 2018-8-14
 */
class IndexMusicWithLetterAdapter(var context: Context, lists: ArrayList<MusicBean.Music>) : RecyclerView.Adapter<IndexMusicWithLetterAdapter.ViewHolder>() {
    var list = arrayListOf<MusicBean.Music>()

    init {
        this.list = lists
    }

    private var onItemClick: RVAdapterItemOnClick? = null

    fun setListerner(onItemClick: RVAdapterItemOnClick) {
        this.onItemClick = onItemClick
    }

    fun scrollToPosition(poi: Int) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_index_with_letter, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 0) {
            holder.viewLayout.vv_divider.visibility = View.GONE
        } else {
            holder.viewLayout.vv_divider.visibility = View.VISIBLE
        }
        holder.viewLayout.tv_rank.text = list[position].letter
        holder.viewLayout.rv_music_list.layoutManager = LinearLayoutManager(context)
        holder.viewLayout.rv_music_list.isNestedScrollingEnabled = false
        var ada = IndexLetterAdapter(context, list[position].musics)
        holder.viewLayout.rv_music_list.adapter = ada
        ada.setListerner(object : RVAdapterItemOnClick {
            override fun onItemClicked(data: Any) {
                onItemClick?.onItemClicked(data)
            }
        })
    }


    class ViewHolder(val viewLayout: View) : RecyclerView.ViewHolder(viewLayout)
}
