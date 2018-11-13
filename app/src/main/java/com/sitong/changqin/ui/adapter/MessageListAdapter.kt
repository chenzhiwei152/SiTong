package com.sitong.changqin.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sevenstringedzithers.sitong.R
import com.sitong.changqin.mvp.model.bean.MessageListBean
import com.sitong.changqin.ui.listerner.RVAdapterItemOnClick
import kotlinx.android.synthetic.main.item_message_list.view.*

/**
 * create by chen.zhiwei on 2018-8-14
 */
class MessageListAdapter(var context: Context) : RecyclerView.Adapter<MessageListAdapter.ViewHolder>() {
    var list = arrayListOf<MessageListBean>()
    private var onItemClick: RVAdapterItemOnClick? = null
    private var onGetRewardLister: RVAdapterItemOnClick? = null

    fun setListerner(onItemClick: RVAdapterItemOnClick) {
        this.onItemClick = onItemClick
    }
    fun setGetRewardListerner(onGetRewardLister: RVAdapterItemOnClick) {
        this.onGetRewardLister = onGetRewardLister
    }

    fun setData(all: ArrayList<MessageListBean>) {
        list?.addAll(all)
        notifyDataSetChanged()
    }

    fun clearData() {
        list?.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_message_list, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.viewLayout.setOnClickListener{
            onItemClick?.onItemClicked(list[position])
        }
        holder.viewLayout.tv_title.text = list[position].title
        holder.viewLayout.tv_time.text = list[position].sendtime
        if (list[position].isreceivereward) {
            holder.viewLayout.tv_get_reward.visibility = View.GONE
        } else {
            holder.viewLayout.tv_get_reward.visibility = View.VISIBLE
            holder.viewLayout.tv_get_reward.setOnClickListener { onGetRewardLister?.onItemClicked(list[position]) }
        }
    }


    class ViewHolder(val viewLayout: View) : RecyclerView.ViewHolder(viewLayout)
}
