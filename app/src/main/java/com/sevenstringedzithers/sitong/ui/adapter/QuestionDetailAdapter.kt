package com.sevenstringedzithers.sitong.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.mvp.model.bean.QuestionDetailBean
import com.sevenstringedzithers.sitong.ui.listerner.RVAdapterItemOnClick
import kotlinx.android.synthetic.main.item_task_layout.view.*

/**
 * create by chen.zhiwei on 2018-8-14
 */
class QuestionDetailAdapter(var context: Context) : RecyclerView.Adapter<QuestionDetailAdapter.ViewHolder>() {
    var list = arrayListOf<QuestionDetailBean>()
    fun setData(all: ArrayList<QuestionDetailBean>) {
        list?.addAll(all)
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
                .inflate(R.layout.item_task_layout, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.viewLayout.tv_num.text = ""+list[position].sort
        holder.viewLayout.tv_content.text = list[position].content
    }


    class ViewHolder(val viewLayout: View) : RecyclerView.ViewHolder(viewLayout)
}
