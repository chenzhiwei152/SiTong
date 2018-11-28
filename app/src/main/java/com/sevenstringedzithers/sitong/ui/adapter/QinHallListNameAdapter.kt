package com.sevenstringedzithers.sitong.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.mvp.model.bean.QinHallBean
import com.sevenstringedzithers.sitong.ui.listerner.RVAdapterItemOnClick
import kotlinx.android.synthetic.main.item_qinhanll_name.view.*

/**
 * create by chen.zhiwei on 2018-8-14
 */
class QinHallListNameAdapter(var context: Context) : RecyclerView.Adapter<QinHallListNameAdapter.ViewHolder>() {
    var list = arrayListOf<QinHallBean>()
    var posi = 0
    fun setData(all: ArrayList<QinHallBean>) {
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

    fun getPosition(): Int? {
        return posi
    }

    private var onItemClick: RVAdapterItemOnClick? = null

    fun setListerner(onItemClick: RVAdapterItemOnClick) {
        this.onItemClick = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_qinhanll_name, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (posi == position) {
            holder.viewLayout.rb_check.setImageResource(R.drawable.rb_selected)
            holder.viewLayout.tv_title.setTextColor(context.resources.getColor(R.color.color_d0a670))
        } else {
            holder.viewLayout.rb_check.setImageResource(R.drawable.rb_normal)
            holder.viewLayout.tv_title.setTextColor(context.resources.getColor(R.color.color_20232b))
        }
        holder.viewLayout.tv_title.text = list[position].name
        holder.viewLayout.setOnClickListener {
            posi=position
            notifyDataSetChanged()
        }
    }


    class ViewHolder(val viewLayout: View) : RecyclerView.ViewHolder(viewLayout)
}
