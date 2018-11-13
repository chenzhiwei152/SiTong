package com.sitong.changqin.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sevenstringedzithers.sitong.R
import com.sitong.changqin.mvp.model.bean.QinguanDetailBean
import com.sitong.changqin.ui.listerner.RVAdapterItemOnClick

/**
 * create by chen.zhiwei on 2018-8-14
 */
class QinHallDetilListAdapter(var context: Context) : RecyclerView.Adapter<QinHallDetilListAdapter.ViewHolder>() {
    var list = arrayListOf<QinguanDetailBean.Content>()
    fun setData(all: ArrayList<QinguanDetailBean.Content>) {
        list = all
        notifyDataSetChanged()
    }

    fun clearData() {
        list?.clear()
        notifyDataSetChanged()
    }


    private var onItemClick: RVAdapterItemOnClick? = null

    fun setListerner(onItemClick: RVAdapterItemOnClick) {
        this.onItemClick = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_qin_hanll_detail, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.viewLayout.tv_name.text = "" + list[position].ctn
//        holder.viewLayout.iv_image.loadImage(context, list[position]?.img)
    }


    class ViewHolder(val viewLayout: View) : RecyclerView.ViewHolder(viewLayout)
}
