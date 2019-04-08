package com.sevenstringedzithers.sitong.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.mvp.model.bean.FileInfo
import com.sevenstringedzithers.sitong.ui.listerner.RVAdapterItemOnClick
import com.sevenstringedzithers.sitong.utils.ExtraUtils
import com.sevenstringedzithers.sitong.utils.TypefaceUtil
import com.sevenstringedzithers.sitong.utils.files.RecordFilesUtils
import com.xw.repo.BubbleSeekBar
import kotlinx.android.synthetic.main.item_record.view.*

/**
 * create by chen.zhiwei on 2018-8-14
 */
class RecordListAdapter(var context: Context) : RecyclerView.Adapter<RecordListAdapter.ViewHolder>() {
    private var max: Float? = null
    private var maxTime: String? = null
    var list = arrayListOf<FileInfo>()
    var shareListerner: RVAdapterItemOnClick? = null
    private var typeface = TypefaceUtil.createagaTypeface(context)

    fun setShareListerne(shareListerner: RVAdapterItemOnClick) {
        this.shareListerner = shareListerner
    }

    fun setData(all: ArrayList<FileInfo>) {
        list = all
        notifyDataSetChanged()
    }

    fun clearData() {
        list?.clear()
        notifyDataSetChanged()
    }

    fun removeItem(poi: Int) {
        list.removeAt(poi)
        notifyDataSetChanged()
    }

    fun setMax(max: Float) {
        this.max = max
        notifyDataSetChanged()
    }

    private var onItemClick: RVAdapterItemOnClick? = null
    private var onPlayMusic: onPlayListerner? = null
    private var onProgress: BubbleSeekBar.OnProgressChangedListener? = null

    fun setListerner(onItemClick: RVAdapterItemOnClick) {
        this.onItemClick = onItemClick
    }

    fun setPlayMusic(onPlayMusic: onPlayListerner) {
        this.onPlayMusic = onPlayMusic
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_record, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        设置样式
        holder.viewLayout.tv_name.typeface = typeface
        holder.viewLayout.tv_date.typeface = typeface
        holder.viewLayout.tv_share.typeface = typeface
        holder.viewLayout.tv_delete.typeface = typeface
        var value = list[position].name.split(".")
        holder.viewLayout.tv_date.text = list[position].lastModified
        if (value.isNotEmpty()) {
            holder.viewLayout.tv_name.text = value[0]

            onProgress = object : BubbleSeekBar.OnProgressChangedListener {
                override fun onProgressChanged(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float, fromUser: Boolean) {
                }

                override fun getProgressOnActionUp(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float) {
                }

                override fun getProgressOnFinally(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float, fromUser: Boolean) {
                }
            }
            holder.viewLayout.seek_bar.onProgressChangedListener = onProgress
            holder.viewLayout.tv_delete.setOnClickListener {
                try {
                    RecordFilesUtils.getInstance()?.deleteFiles(list[position].name)
                    removeItem(position)
                } catch (e: Exception) {
                    ExtraUtils.toasts("" + e.message)
                }
//                notifyDataSetChanged()
            }
            holder.viewLayout.tv_share.setOnClickListener {
                shareListerner?.onItemClicked("")
            }

            holder.viewLayout.iv_more.setOnClickListener {
                onPlayMusic?.onItemClicked(position, holder.viewLayout.seek_bar, list[position].absolutePath)
            }
            holder.viewLayout.setOnClickListener {
                onItemClick?.onItemClicked(list[position])
                if (holder.viewLayout.seek_bar.visibility == View.GONE) {
                    holder.viewLayout.seek_bar.visibility = View.VISIBLE
//                    holder.viewLayout.seek_bar.configBuilder?.max(list[position].length.toFloat())?.min(0f)?.build()
                    holder.viewLayout.tv_end?.text = ExtraUtils.secToTime((list[position].length.toFloat()).toInt())
                    holder.viewLayout.ll_button.visibility = View.VISIBLE
                    holder.viewLayout.rl_process.visibility = View.VISIBLE
                    holder.viewLayout.ll_content.setBackgroundColor(context.resources.getColor(R.color.color_f4f3f2))
                } else {
                    holder.viewLayout.ll_content.setBackgroundColor(context.resources.getColor(R.color.color_ffffff))
                    holder.viewLayout.seek_bar.visibility = View.GONE
                    holder.viewLayout.ll_button.visibility = View.GONE
                    holder.viewLayout.rl_process.visibility = View.GONE
                }
            }
        }

    }


    class ViewHolder(val viewLayout: View) : RecyclerView.ViewHolder(viewLayout)

    interface onPlayListerner {
        fun onItemClicked(pos: Int, bubble: BubbleSeekBar, data: String)
    }
}
