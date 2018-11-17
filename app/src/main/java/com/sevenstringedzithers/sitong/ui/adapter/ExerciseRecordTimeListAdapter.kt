package com.sevenstringedzithers.sitong.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.mvp.model.bean.ExerciseRecordBean
import com.sevenstringedzithers.sitong.ui.listerner.RVAdapterItemOnClick
import kotlinx.android.synthetic.main.item_exercise_record.view.*

/**
 * create by chen.zhiwei on 2018-8-14
 */
class ExerciseRecordTimeListAdapter(var context: Context) : RecyclerView.Adapter<ExerciseRecordTimeListAdapter.ViewHolder>() {
    var list = arrayListOf<ExerciseRecordBean.Date>()
    var posi = 1
    fun setData(all: ArrayList<ExerciseRecordBean.Date>) {
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
                .inflate(R.layout.item_exercise_record, parent, false))
    }

    override fun getItemCount(): Int =list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.viewLayout.tv_name.text = ""+list[position].date
        holder.viewLayout.tv_name_content.text = ""+list[position].duration

    }


    class ViewHolder(val viewLayout: View) : RecyclerView.ViewHolder(viewLayout)
}
