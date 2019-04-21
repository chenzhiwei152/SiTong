package com.sevenstringedzithers.sitong.ui.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jyall.bbzf.extension.loadImage
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.base.Constants
import com.sevenstringedzithers.sitong.mvp.model.bean.QinguanDetailBean
import com.sevenstringedzithers.sitong.ui.activity.VideoPlayActivity
import com.sevenstringedzithers.sitong.ui.listerner.RVAdapterItemOnClick
import kotlinx.android.synthetic.main.item_qin_hanll_detail.view.*

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
        var ctn = list[position].ctn.replace("\\n", "\n")
        ctn = ctn.replace("\\r", "\r")
        holder.viewLayout.tv_name.text = ctn
        if (!list[position]?.img.isNullOrEmpty()) {
            holder.viewLayout.iv_image.loadImage(context, list[position]?.img)
            holder.viewLayout.iv_image.visibility = View.VISIBLE
        } else {
            holder.viewLayout.iv_image.visibility = View.GONE
        }
        holder.viewLayout.tv_video.text = "" + list[position].video
        holder.viewLayout.tv_video.setOnClickListener {
            var intent = Intent(context, VideoPlayActivity::class.java)
            intent.putExtra(Constants.VIDOE_URL, list[position].video)
            context.startActivity(intent)
        }
    }


    class ViewHolder(val viewLayout: View) : RecyclerView.ViewHolder(viewLayout)
}
