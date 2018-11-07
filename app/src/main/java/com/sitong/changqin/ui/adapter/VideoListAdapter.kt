package com.sitong.changqin.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jyall.bbzf.extension.loadRoundImage
import com.sitong.changqin.mvp.model.bean.VideoListBean
import com.sitong.changqin.ui.listerner.RVAdapterItemOnClick
import com.stringedzithers.sitong.R
import kotlinx.android.synthetic.main.item_qin_hall_layout.view.*

/**
 * create by chen.zhiwei on 2018-8-14
 */
class VideoListAdapter(var context: Context) : RecyclerView.Adapter<VideoListAdapter.ViewHolder>() {
    var list = arrayListOf<VideoListBean>()
    var posi = 1
    fun setData(all: ArrayList<VideoListBean>) {
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
                .inflate(R.layout.item_qin_hall_layout, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.viewLayout.tv_title.setText(list[position].title)
        holder.viewLayout.tv_city.setText(list[position].author)
        holder.viewLayout.iv_image.loadRoundImage(context,list[position].icon)
    }


    class ViewHolder(val viewLayout: View) : RecyclerView.ViewHolder(viewLayout)
}
