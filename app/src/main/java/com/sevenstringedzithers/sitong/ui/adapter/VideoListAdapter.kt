package com.sevenstringedzithers.sitong.ui.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jyall.bbzf.extension.loadRoundImage
import com.jyall.bbzf.extension.toast
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.base.Constants
import com.sevenstringedzithers.sitong.mvp.model.bean.VideoListBean
import com.sevenstringedzithers.sitong.ui.activity.ArticleDetailActivity
import com.sevenstringedzithers.sitong.ui.activity.VideoPlayActivity
import com.sevenstringedzithers.sitong.ui.listerner.RVAdapterItemOnClick
import kotlinx.android.synthetic.main.item_qin_hall_layout.view.*

/**
 * create by chen.zhiwei on 2018-8-14
 */
class VideoListAdapter(var context: Context) : RecyclerView.Adapter<VideoListAdapter.ViewHolder>() {
    var list = arrayListOf<VideoListBean>()
    var posi = 1
    private var type: Int = 0//0 文章，1，视频
    fun setData(all: ArrayList<VideoListBean>) {
        list?.addAll(all)
        notifyDataSetChanged()
    }

    fun clearData() {
        list?.clear()
        notifyDataSetChanged()
    }

    fun setType(type: Int) {
        this.type = type
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
        holder.viewLayout.iv_image.loadRoundImage(context, list[position].icon)
        holder.viewLayout.setOnClickListener {
            if (type == 0) {
                var intent = Intent(context, ArticleDetailActivity::class.java)
                intent.putExtra("id", list[position].id)
                context.startActivity(intent)
            } else {
                if (list[position].url != null) {
                    var url = (list[position].url)?.replace("https", "http")
                    var intent = Intent(context, VideoPlayActivity::class.java)
                    intent.putExtra(Constants.VIDOE_URL, url)
                    context.startActivity(intent)
                } else {
                    context.toast("url为空")
                }
            }
        }
    }


    class ViewHolder(val viewLayout: View) : RecyclerView.ViewHolder(viewLayout)
}
