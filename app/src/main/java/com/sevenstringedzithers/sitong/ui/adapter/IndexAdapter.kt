package com.sevenstringedzithers.sitong.ui.adapter

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.mvp.model.bean.MusicBean
import com.sevenstringedzithers.sitong.ui.listerner.RVAdapterItemOnClick
import kotlinx.android.synthetic.main.item_index_first.view.*

/**
 * create by chen.zhiwei on 2018-8-14
 */
class IndexAdapter(var context: Context) : RecyclerView.Adapter<IndexAdapter.ViewHolder>() {
    var list = arrayListOf<MusicBean>()
    var posi = 1
    private var onItemClick: RVAdapterItemOnClick? = null
    private var letteOnclick: RVAdapterItemOnClick? = null

    fun setListerner(onItemClick: RVAdapterItemOnClick) {
        this.onItemClick = onItemClick
    }

    fun setLetterOnclick(letteOnclick: RVAdapterItemOnClick) {
        this.letteOnclick = letteOnclick
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
            if (list[position].type == 1) {
                holder.viewLayout.rv_music_list.layoutManager = LinearLayoutManager(context)
                holder.viewLayout.rv_music_list.isNestedScrollingEnabled = false
                var ada = IndexMusicAdapter(context, list[position].musics)
                holder.viewLayout.rv_music_list.adapter = ada
                ada.setListerner(object : RVAdapterItemOnClick {
                    override fun onItemClicked(data: Any) {
                        onItemClick?.onItemClicked(data)
                    }
                })
                holder.viewLayout.rv_list_letter.visibility = View.GONE

            } else {
                holder.viewLayout.rv_music_list.layoutManager = LinearLayoutManager(context)
                holder.viewLayout.rv_music_list.isNestedScrollingEnabled = false
                var ada = IndexMusicWithLetterAdapter(context, list[position].musics)
                holder.viewLayout.rv_music_list.adapter = ada
                ada.setListerner(object : RVAdapterItemOnClick {
                    override fun onItemClicked(data: Any) {
                        onItemClick?.onItemClicked(data)
                    }
                })

                holder.viewLayout.rv_list_letter.visibility = View.VISIBLE
                holder.viewLayout.rv_list_letter.layoutManager = LinearLayoutManager(context)
                holder.viewLayout.rv_list_letter.isNestedScrollingEnabled = false
                var letterAdapter = IndexLetterLinkdapter(context, list[position].musics)
//                if (letteOnclick!=null){
                letterAdapter.setLetterOnclick(object : RVAdapterItemOnClick {
                    override fun onItemClicked(data: Any) {
                        var pos = data as Int
//                        var ll = holder.viewLayout.rv_music_list.layoutManager as LinearLayoutManager
//                        ll.scrollToPositionWithOffset(pos, 0)
                        var mValue = IntArray(2)
                        holder.viewLayout.rv_music_list.getChildAt(pos).getLocationOnScreen(mValue)
                        letteOnclick?.onItemClicked(mValue[1])

                    }

                })
//                }
                holder.viewLayout.rv_list_letter.adapter = letterAdapter
            }

        }
    }


    class ViewHolder(val viewLayout: View) : RecyclerView.ViewHolder(viewLayout)
}
