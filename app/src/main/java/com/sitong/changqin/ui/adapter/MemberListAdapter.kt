package com.sitong.changqin.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sitong.changqin.mvp.model.bean.MemberBean
import com.sitong.changqin.ui.activity.PayActivity
import com.sitong.changqin.ui.listerner.RVAdapterItemOnClick
import com.stringedzithers.sitong.R
import kotlinx.android.synthetic.main.item_question.view.*

/**
 * create by chen.zhiwei on 2018-8-14
 */
class MemberListAdapter(var context: Context) : RecyclerView.Adapter<MemberListAdapter.ViewHolder>() {
    var list = arrayListOf<MemberBean>()
    var posi = 1
    fun setData(all: ArrayList<MemberBean>) {
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
                .inflate(R.layout.item_question, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.viewLayout.tv_name.text = "" + list[position].name + "   " + list[position].price
        holder.viewLayout.setOnClickListener {
            onItemClick?.onItemClicked(list[position])
        }
    }


    class ViewHolder(val viewLayout: View) : RecyclerView.ViewHolder(viewLayout)
}
