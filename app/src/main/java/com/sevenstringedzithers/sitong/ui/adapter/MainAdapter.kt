package com.sevenstringedzithers.sitong.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.flexbox.FlexboxLayoutManager
import com.jyall.android.common.utils.ImageLoadedrManager
import com.jyall.android.common.utils.LogUtils
import com.jyall.android.common.utils.UIUtil
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.R.attr.maxSize
import com.sevenstringedzithers.sitong.mvp.model.bean.MusicDetailBean
import com.sevenstringedzithers.sitong.utils.ImageUtils
import com.sevenstringedzithers.sitong.view.dragselectrecyclerview.DragSelectRecyclerView
import com.sevenstringedzithers.sitong.view.dragselectrecyclerview.IDragSelectAdapter
import com.sevenstringedzithers.sitong.view.dragselectrecyclerview.RectangleView
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Aidan Follestad (afollestad)
 */
class MainAdapter(private val mContext: Context, private val callback: Listener?) : RecyclerView.Adapter<MainAdapter.MainViewHolder>(), IDragSelectAdapter {
    val selectedIndices: ArrayList<Int>//选中的items
    internal var selectedIndicesCopy: ArrayList<Int> = arrayListOf()
    private var mPlayPosition = -1
    private var cachePositoin = -1
    private var isSelected = false
    private val yanyinSet = TreeMap<Int, Int>()
    private val yanyinSetWithNum = TreeMap<Int, Int>()
    private var list: ArrayList<MusicDetailBean.Score>? = null
    private var isScrolling = false
    private var value = 12f

    private var mLinesMap = hashMapOf<Int, Array<Int>>()

    fun setLinesMap(map: HashMap<Int, Array<Int>>) {
        this.mLinesMap = map
    }

    private var rv_list: DragSelectRecyclerView? = null

    fun setScrolling(scrolling: Boolean) {
        isScrolling = scrolling
    }

    fun setmRecyclerView(mRecyclerView: DragSelectRecyclerView) {
        this.rv_list = mRecyclerView
    }

    fun setList(list: ArrayList<MusicDetailBean.Score>) {
        this.list = list
        notifyDataSetChanged()
    }


    interface Listener {
        fun onClick(index: Int)

        fun onLongClick(index: Int)

        fun onSelectionChanged(count: Int)
    }

    init {
        this.selectedIndices = ArrayList(16)
    }


    fun getSelectedIndices(): List<Int> {
        return selectedIndices
    }

    fun toggleSelected(index: Int) {
        if (selectedIndices.size > 0) {
            if (selectedIndices.contains(index) && selectedIndices.size == 1) {
                selectedIndices.remove(index)
                rv_list?.findViewHolderForLayoutPosition(index)?.itemView?.findViewById<FrameLayout>(R.id.fl_foreground)
                        ?.foreground = mContext.resources.getDrawable(R.drawable.bg_transparent)
            } else {
                clearSelected()
                selectedIndices.add(index)
                rv_list?.findViewHolderForLayoutPosition(index)?.itemView?.findViewById<FrameLayout>(R.id.fl_foreground)
                        ?.foreground = mContext.resources.getDrawable(R.drawable.bg_99d0a670)
            }
        } else {
            rv_list?.findViewHolderForLayoutPosition(index)?.itemView?.findViewById<FrameLayout>(R.id.fl_foreground)
                    ?.foreground = mContext.resources.getDrawable(R.drawable.bg_99d0a670)
            selectedIndices.add(index)
        }
        setTags()
        callback?.onSelectionChanged(selectedIndices.size)
    }

    fun clearSelected() {

        if (selectedIndices.isEmpty()) {
            return
        }
        selectedIndicesCopy.addAll(selectedIndices)
        selectedIndices.clear()
        selectedIndicesCopy.forEach {
            rv_list?.findViewHolderForLayoutPosition(it)?.itemView?.findViewById<FrameLayout>(R.id.fl_foreground)
                    ?.foreground = mContext.resources.getDrawable(R.drawable.bg_transparent)
            clearAb(it)
        }
        selectedIndicesCopy.clear()
        callback?.onSelectionChanged(0)
    }

    fun setPlayPosition(pre: Int, curent: Int) {
        this.cachePositoin = pre
        this.mPlayPosition = curent
        LogUtils.e("currentPo:" + mPlayPosition + "---cachePositoin:" + cachePositoin)


        if (cachePositoin != -1) {

//            notifyItemChanged(cachePositoin)


//            if (rv_list?.findViewHolderForLayoutPosition(cachePositoin)?.itemView==null||rv_list?.findViewHolderForLayoutPosition(cachePositoin)?.itemView?.parent==null){
//                return
//            }
            try {
//                if (cachePositoin >= (rv_list?.layoutManager as FlexboxLayoutManager).findFirstCompletelyVisibleItemPosition() && cachePositoin < (rv_list?.layoutManager as FlexboxLayoutManager).findLastVisibleItemPosition()){
//                    notifyItemChanged(cachePositoin)
//                }

//            notifyItemChanged(mPlayPosition)
//            return
                var iv_image = rv_list?.findViewHolderForLayoutPosition(cachePositoin)?.itemView?.findViewById<LinearLayout>(R.id.iv_image)
                var iv_shoushi = rv_list?.findViewHolderForLayoutPosition(cachePositoin)?.itemView?.findViewById<ImageView>(R.id.iv_shoushi)
                var ll_num = rv_list?.findViewHolderForLayoutPosition(cachePositoin)?.itemView?.findViewById<LinearLayout>(R.id.ll_center)

//                    最下面的图片
//                iv_image?.removeAllViews()
                if (!isScrolling && !TextUtils.isEmpty(list!![cachePositoin].jianzipu)) {

//                var url = list!![cachePositoin].jianzipu
//                if (list!![cachePositoin].jianziwidth > 0) {
//                    if (jianzipu?.layoutParams!=null){
//                        var params =  LinearLayout.LayoutParams((list!![mPlayPosition].jianziwidth * 2.2).toInt(),(list!![mPlayPosition].jianziheight * 2.2).toInt())

//                        val params = jianzipu?.layoutParams as LinearLayout.LayoutParams
//                        params.width = (list!![mPlayPosition].jianziwidth * 2.2).toInt()
//                        params.height = (list!![mPlayPosition].jianziheight * 2.2).toInt()
//                        jianzipu?.layoutParams = params
//                    }
//                }
//                    var imge = ImageView(mContext)
//                    ImageLoadedrManager.getInstance().displayNoDefult(mContext, list!![cachePositoin].jianzipu, imge)
                    ImageLoadedrManager.getInstance().displayNoDefult(mContext, list!![cachePositoin].jianzipu, iv_shoushi)
//                    iv_image?.addView(imge)
                } else {
//                iv_image?.setImageResource(R.drawable.bg_transparent)
                }


//            ll_num?.removeAllViews()

                if (list!![cachePositoin].numbered_music == "-1" || list!![cachePositoin].numbered_music == "8") {
                    val textview = TextView(mContext)
                    textview.text = ""
                    ll_num?.removeAllViews()
                    ll_num?.addView(textview)
                } else {
                    ll_num?.removeAllViews()
                    if (list!![cachePositoin].sound_type == 0) {
                        //        中间的数字
//                    LogUtils.e("开始获取数字："+list!![cachePositoin].numbered_music)
                        ImageUtils.getNumber(ll_num, mContext, list!![cachePositoin].numbered_music, 1, false)
//                    LogUtils.e("获取到的数字："+list!![cachePositoin].numbered_music)
                    } else if (list!![cachePositoin].sound_type == 1) {
                        ImageUtils.getNumber(ll_num, mContext, list!![cachePositoin].numbered_music_up, 2, false)
                        ImageUtils.getNumber(ll_num, mContext, list!![cachePositoin].numbered_music, 2, false)
                    } else {
                        ImageUtils.getNumber(ll_num, mContext, list!![cachePositoin].numbered_music_up, 3, false)
                        ImageUtils.getNumber(ll_num, mContext, list!![cachePositoin].numbered_music_middle, 3, false)
                        ImageUtils.getNumber(ll_num, mContext, list!![cachePositoin].numbered_music, 3, false)
                    }
                }
            } catch (e: java.lang.Exception) {
            }

        }
//        notifyItemChanged(mPlayPosition)
//        return
//        if (rv_list?.findViewHolderForLayoutPosition(mPlayPosition)?.itemView==null||rv_list?.findViewHolderForLayoutPosition(mPlayPosition)?.itemView?.parent==null){
//            return
//        }
        var iv_image = rv_list?.findViewHolderForLayoutPosition(mPlayPosition)?.itemView?.findViewById<LinearLayout>(R.id.iv_image)
        var iv_shoushi = rv_list?.findViewHolderForLayoutPosition(mPlayPosition)?.itemView?.findViewById<ImageView>(R.id.iv_shoushi)
        var ll_num = rv_list?.findViewHolderForLayoutPosition(mPlayPosition)?.itemView?.findViewById<LinearLayout>(R.id.ll_center)

        //        最下面的图片
//        iv_image?.removeAllViews()
        if (!isScrolling && !TextUtils.isEmpty(list!![mPlayPosition].jianzipu)) {

            var url = list!![mPlayPosition].jianzipu
            url = url.replace("normal", "highlight")
//            if (list!![mPlayPosition].jianziwidth > 0) {
//                if (jianzipu?.layoutParams!=null){
//                    var params =  LinearLayout.LayoutParams((list!![mPlayPosition].jianziwidth * 2.2).toInt(),(list!![mPlayPosition].jianziheight * 2.2).toInt())
//                    params.width = (list!![mPlayPosition].jianziwidth * 2.2).toInt()
//                    params.height = (list!![mPlayPosition].jianziheight * 2.2).toInt()
//                    jianzipu?.layoutParams = params
//                }
//            }
//            var imge = ImageView(mContext)
            ImageLoadedrManager.getInstance().displayNoDefult(mContext, url, iv_shoushi)
//            iv_image?.addView(imge)
        } else {
//            jianzipu?.setImageResource(R.drawable.bg_transparent)
        }



        if (list!![mPlayPosition].numbered_music == "-1" || list!![mPlayPosition].numbered_music == "8") {
            val textview = TextView(mContext)
            textview.text = ""
            ll_num?.removeAllViews()
            ll_num?.addView(textview)
        } else {
            ll_num?.removeAllViews()
            if (list!![mPlayPosition].sound_type == 0) {
                //        中间的数字
                ImageUtils.getNumber(ll_num, mContext, list!![mPlayPosition].numbered_music, 1, true)
            } else if (list!![mPlayPosition].sound_type == 1) {
                ImageUtils.getNumber(ll_num, mContext, list!![mPlayPosition].numbered_music_up, 2, true)
                ImageUtils.getNumber(ll_num, mContext, list!![mPlayPosition].numbered_music, 2, true)
            } else {
                ImageUtils.getNumber(ll_num, mContext, list!![mPlayPosition].numbered_music_up, 3, true)
                ImageUtils.getNumber(ll_num, mContext, list!![mPlayPosition].numbered_music_middle, 3, true)
                ImageUtils.getNumber(ll_num, mContext, list!![mPlayPosition].numbered_music, 3, true)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.griditem_main, parent, false)
        return MainViewHolder(v, callback)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
//        LogUtils.e("CurrentPosition:$position")
        isSelected = selectedIndices.contains(position)

        holder.ll_center.removeAllViews()
        holder.ll_right.removeAllViews()
        holder.ll_left_center.removeAllViews()
        holder.ll_center_dowm.removeAllViews()
        holder.ll_center_top.removeAllViews()
        holder.ll_right_down.removeAllViews()
        holder.ll_left_down.removeAllViews()
        holder.ll_limit_top.removeAllViews()
        holder.ll_limit_right.removeAllViews()
        holder.ll_limit_left.removeAllViews()
        holder.ll_left_top.removeAllViews()
//        holder.iv_image.removeAllViews()
        //        最下面的图片
        if (!isScrolling && !TextUtils.isEmpty(list!![position].jianzipu)) {

            var url = list!![position].jianzipu
            if (mPlayPosition == position) {
                url = url.replace("normal", "highlight")
            }
//            var imge = ImageView(mContext)
//            ImageLoadedrManager.getInstance().displayNoDefult(mContext, url, imge)
            ImageLoadedrManager.getInstance().displayNoDefult(mContext, url, holder.iv_shoushi)
//            holder.iv_image.addView(imge)

        }

        if (list!![position].numbered_music == "-1" || list!![position].numbered_music == "8") {
            val textview = TextView(mContext)
            textview.text = ""
            holder.ll_center.addView(textview)
            val textview1 = TextView(mContext)
            holder.ll_left_center.addView(textview1)
        }
        if (list!![position].numbered_music == "8") {
            holder.ll_right.addView(ImageUtils.getImageView(mContext, R.drawable.line_black_5, 0, 0))
        }
        if (list!![position].sound_type == 0) {
            //        中间的数字
            ImageUtils.getNumber(holder.ll_center, mContext, list!![position].numbered_music, 1, mPlayPosition == position)
        } else if (list!![position].sound_type == 1) {
            ImageUtils.getNumber(holder.ll_center, mContext, list!![position].numbered_music_up, 2, mPlayPosition == position)
            ImageUtils.getNumber(holder.ll_center, mContext, list!![position].numbered_music, 2, mPlayPosition == position)
        } else {
            ImageUtils.getNumber(holder.ll_center, mContext, list!![position].numbered_music_up, 3, mPlayPosition == position)
            ImageUtils.getNumber(holder.ll_center, mContext, list!![position].numbered_music_middle, 3, mPlayPosition == position)
            ImageUtils.getNumber(holder.ll_center, mContext, list!![position].numbered_music, 3, mPlayPosition == position)
        }

        //下方的
        for (i in 0 until list!![position].symbol.size) {
            val symbol = list!![position].symbol[i]
            when (symbol.positioncode) {
                //符号位置
                0 ->
                    //                        小结顶部
                    when (symbol.namecode) {
                        17 -> try {
                            var ss = arrayOfNulls<String>(2)
                            if (!TextUtils.isEmpty(symbol.param))
                                ss = symbol.param.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            if (ss.size > 0) {
                                yanyinSet[position] = Integer.parseInt(ss[0])
                            }
                        } catch (e: Exception) {
                            LogUtils.e("解析异常", e.message)
                        }

                        18 -> try {
                            var ss = arrayOfNulls<String>(2)
                            if (!TextUtils.isEmpty(symbol.param))
                                ss = symbol.param.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            if (ss.size > 0) {
                                yanyinSetWithNum[position] = Integer.parseInt(ss[0])
                            }
                        } catch (e: Exception) {
                            LogUtils.e("解析异常", e.message)
                        }

                    }
                1 ->
                    //                    左上
                    when (symbol.namecode) {
                        0 -> holder.ll_left_top.addView(ImageUtils.getImageView(mContext, R.mipmap.ic_one_double, 0, 0))
                    }
                2 ->
                    //                    上
                    when (symbol.namecode) {
                        13 -> {
                            val v3 = ImageView(mContext)
                            v3.setImageResource(R.mipmap.ic_point_black)
                            holder.ll_center_top.addView(v3)
                            val params1 = v3.layoutParams as LinearLayout.LayoutParams
                            params1.topMargin = UIUtil.dip2px(mContext, 2f)
                            v3.layoutParams = params1
                        }
                        14 -> {
                            //泛音
                            val v2 = ImageView(mContext)
                            v2.setImageResource(R.mipmap.ic_point_virtual)
                            holder.ll_center_top.addView(v2)
                        }
                        15 -> {
                            //延音
                            //                                ImageView v1 = new ImageView(mContext);
                            //                                v1.setImageResource(R.drawable.bg_transparent);
                            val v5 = ImageView(mContext)
                            v5.setImageResource(R.mipmap.ic_yanyin)
                            //                                holder.ll_center_top.addView(v1);
                            holder.ll_center_top.addView(v5)
                            val params2 = v5.layoutParams as LinearLayout.LayoutParams
                            params2.topMargin = UIUtil.dip2px(mContext, 2f)
                            v5.layoutParams = params2
                        }
                        23 -> {
                            val v = ImageView(mContext)
                            v.setImageResource(R.drawable.line_black_vertical_5)
                            holder.ll_center_top.addView(v)
                            val params3 = v.layoutParams as LinearLayout.LayoutParams
                            params3.topMargin = UIUtil.dip2px(mContext, 2f)
                            v.layoutParams = params3
                        }
                    }
                3 -> {
                }
                4 ->
                    //                    右
                    when (symbol.namecode) {
                        6 -> {
                            val v3 = ImageView(mContext)
                            v3.setImageResource(R.mipmap.ic_point_black)
                            holder.ll_right.addView(v3)
                            val params1 = v3.layoutParams as LinearLayout.LayoutParams
                            params1.leftMargin = UIUtil.dip2px(mContext, 2f)
                            v3.layoutParams = params1
                        }
                        9 -> {
                            val image = ImageUtils.getJiePai(symbol.param)!!
                            if (image != 0) {
                                val v1 = ImageView(mContext)
                                v1.setImageResource(image)
                                holder.ll_right.addView(v1)
                                val params2 = v1.layoutParams as LinearLayout.LayoutParams
                                params2.leftMargin = UIUtil.dip2px(mContext, 2f)
                                v1.layoutParams = params2
                            }
                        }

                        23 -> {
                            val v = ImageView(mContext)
                            v.setImageResource(R.drawable.line_black_vertical_20)
                            holder.ll_right.addView(v)
                            val params2 = v.layoutParams as LinearLayout.LayoutParams
                            params2.leftMargin = UIUtil.dip2px(mContext, 4f)
                            params2.rightMargin = UIUtil.dip2px(mContext, 4f)
                            v.layoutParams = params2
                        }
                        24 -> {
                            val v4 = ImageView(mContext)
                            v4.setImageResource(R.mipmap.ic_line_double)
                            holder.ll_right.addView(v4)
                            val params3 = v4.layoutParams as LinearLayout.LayoutParams
                            params3.leftMargin = UIUtil.dip2px(mContext, 4f)
                            v4.layoutParams = params3
                        }
                        26 -> {
                            val v8 = ImageView(mContext)
                            v8.setImageResource(R.mipmap.ic_repeat_end)
                            holder.ll_right.addView(v8)
                            val params4 = v8.layoutParams as LinearLayout.LayoutParams
                            params4.leftMargin = UIUtil.dip2px(mContext, 4f)
                            v8.layoutParams = params4
                        }
                    }
                5 -> {
                }
                6 ->
                    //                    下
                    when (symbol.namecode) {
                        7 -> {
                            var hasLineNext: Boolean? = false
                            val v1: ImageView
                            if (position < list!!.size - 1) {
                                if (list!![position + 1].symbol.size > 0) {
                                    //判断下一个音符
                                    for (j in 0 until list!![position + 1].symbol.size) {
                                        if (list!![position + 1].symbol[j].namecode == 7) {
                                            hasLineNext = true
                                            break
                                        }
                                    }
                                }
                            }

                            if (hasLineNext!!) {
                                v1 = ImageView(mContext)
                                v1.setImageResource(R.drawable.line_black_10)
                                holder.ll_right_down.addView(v1)
                                val params1 = v1.layoutParams as LinearLayout.LayoutParams
                                params1.topMargin = UIUtil.dip2px(mContext, 2f)
                                params1.width = UIUtil.dip2px(mContext, value)
                                v1.layoutParams = params1
                            }


                            var hasLinePre: Boolean? = false
                            val v2: ImageView
                            if (position > 0) {
                                if (list!![position - 1].symbol.size > 0) {
                                    //                                            判断上一个音符
                                    for (j in 0 until list!![position - 1].symbol.size) {
                                        if (list!![position - 1].symbol[j].namecode == 7) {
                                            hasLinePre = true
                                            break
                                        }
                                    }
                                }
                            }

                            if (hasLinePre!!) {
                                v2 = ImageView(mContext)
                                v2.setImageResource(R.drawable.line_black_10)
                                holder.ll_left_down.addView(v2)
                                val params1 = v2.layoutParams as LinearLayout.LayoutParams
                                params1.topMargin = UIUtil.dip2px(mContext, 2f)
                                params1.width = UIUtil.dip2px(mContext, value)
                                v2.layoutParams = params1
                            }

                            val v = ImageView(mContext)
                            v.setImageResource(R.drawable.line_black_10)
                            holder.ll_center_dowm.addView(v)
                            val params = v.layoutParams as LinearLayout.LayoutParams
                            params.topMargin = UIUtil.dip2px(mContext, 2f)
                            params.width = UIUtil.dip2px(mContext, value)
                            v.layoutParams = params
                        }
                        8 -> {
                            val v3 = ImageView(mContext)
                            v3.setImageResource(R.mipmap.ic_point_black)
                            holder.ll_center_dowm.addView(v3)
                            val params1 = v3.layoutParams as LinearLayout.LayoutParams
                            params1.topMargin = UIUtil.dip2px(mContext, 2f)
                            v3.layoutParams = params1
                        }
                    }
                7 -> {
                }
                8 ->
                    //                    左
                    when (symbol.namecode) {
                        9 -> {
                            val image = ImageUtils.getJiePai(symbol.param)!!
                            if (image != 0) {
                                val v = ImageView(mContext)
                                v.setImageResource(image)
                                holder.ll_left_center.addView(v)
                                val params = v.layoutParams as LinearLayout.LayoutParams
                                params.rightMargin = UIUtil.dip2px(mContext, 2f)
                                v.layoutParams = params
                            }
                        }
                        11 -> {
                            val v1 = ImageView(mContext)
                            v1.setImageResource(R.mipmap.ic_shanghuayin)
                            holder.ll_left_center.addView(v1)
                            val params = v1.layoutParams as LinearLayout.LayoutParams
                            params.rightMargin = UIUtil.dip2px(mContext, 2f)
                            v1.layoutParams = params
                        }
                        12 -> {
                            val v2 = ImageView(mContext)
                            v2.setImageResource(R.mipmap.ic_xiahuaxin)
                            holder.ll_left_center.addView(v2)
                            val params1 = v2.layoutParams as LinearLayout.LayoutParams
                            params1.rightMargin = UIUtil.dip2px(mContext, 2f)
                            v2.layoutParams = params1
                        }
                        25 -> {
                            val v8 = ImageView(mContext)
                            v8.setImageResource(R.mipmap.ic_repeat_start)
                            holder.ll_left_center.addView(v8)
                            val params2 = v8.layoutParams as LinearLayout.LayoutParams
                            params2.rightMargin = UIUtil.dip2px(mContext, 4f)
                            v8.layoutParams = params2
                        }
                    }
            }//                    右上
            //                    右下
            //                    左下
        }
        var isAdded = false//是否加载了，加载一次就跳出，减少循环次数
        for (key in yanyinSet.keys) {
            //            LogUtils.e("key:" + key + "----value:" + yanyinSet.get(key));
            //            System.out.println("Key = " + key);


            if (position == key) {

                val params = holder.ll_limit_left.layoutParams as LinearLayout.LayoutParams
                params.width = UIUtil.dip2px(mContext, value)
                holder.ll_limit_left.layoutParams = params
                val params1 = holder.ll_limit_top.layoutParams as LinearLayout.LayoutParams
                params1.width = UIUtil.dip2px(mContext, value)
                holder.ll_limit_top.layoutParams = params1
                val params2 = holder.ll_limit_right.layoutParams as LinearLayout.LayoutParams
                params2.width = UIUtil.dip2px(mContext, value)
                holder.ll_limit_right.layoutParams = params2

                val imageView2 = ImageView(mContext)
                imageView2.setImageResource(R.drawable.bg_transparent_5)
                holder.ll_limit_left.addView(imageView2)


                val imageView = ImageView(mContext)
                //                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setBackgroundResource(R.mipmap.ic_oval_left_small)
                holder.ll_limit_top.addView(imageView)


                val imageView1 = ImageView(mContext)
                //                imageView1.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView1.setBackgroundResource(R.mipmap.ic_oval_middle_small)
                holder.ll_limit_right.addView(imageView1)
                isAdded = true
                break
            } else if (position == key + yanyinSet[key]!! - 1) {
                val params = holder.ll_limit_left.layoutParams as LinearLayout.LayoutParams
                params.width = UIUtil.dip2px(mContext, value)
                holder.ll_limit_left.layoutParams = params
                val params1 = holder.ll_limit_top.layoutParams as LinearLayout.LayoutParams
                params1.width = UIUtil.dip2px(mContext, value)
                holder.ll_limit_top.layoutParams = params1
                val params2 = holder.ll_limit_right.layoutParams as LinearLayout.LayoutParams
                params2.width = UIUtil.dip2px(mContext, value)
                holder.ll_limit_right.layoutParams = params2

                val imageView1 = ImageView(mContext)
                imageView1.setBackgroundResource(R.mipmap.ic_oval_middle_small)
                holder.ll_limit_left.addView(imageView1)

                val imageView = ImageView(mContext)
                imageView.setBackgroundResource(R.mipmap.ic_oval_right_small)
                holder.ll_limit_top.addView(imageView)

                val imageView2 = ImageView(mContext)
                imageView2.setBackgroundResource(R.drawable.bg_transparent_5)
                holder.ll_limit_right.addView(imageView2)

                isAdded = true
                break
            } else if (position > key && position < key + yanyinSet[key]!! - 1) {

                //
                val params = holder.ll_limit_left.layoutParams as LinearLayout.LayoutParams
                params.width = UIUtil.dip2px(mContext, value)
                holder.ll_limit_left.layoutParams = params
                val params1 = holder.ll_limit_top.layoutParams as LinearLayout.LayoutParams
                params1.width = UIUtil.dip2px(mContext, value)
                holder.ll_limit_top.layoutParams = params1
                val params2 = holder.ll_limit_right.layoutParams as LinearLayout.LayoutParams
                params2.width = UIUtil.dip2px(mContext, value)
                holder.ll_limit_right.layoutParams = params2


                val imageView1 = ImageView(mContext)
                imageView1.setBackgroundResource(R.mipmap.ic_oval_middle_small)
                holder.ll_limit_left.addView(imageView1)

                val imageView = ImageView(mContext)
                imageView.setBackgroundResource(R.mipmap.ic_oval_middle_small)
                holder.ll_limit_top.addView(imageView)

                val imageView2 = ImageView(mContext)
                imageView2.setBackgroundResource(R.mipmap.ic_oval_middle_small)
                holder.ll_limit_right.addView(imageView2)
                isAdded = true
                break
            }
            if (isAdded) {
                break
            }
        }
        if (!isAdded) {
            for (key in yanyinSetWithNum.keys) {
                //            LogUtils.e("key:" + key + "----value:" + yanyinSet.get(key));
                //            System.out.println("Key = " + key);
                var isAdded1 = false//是否加载了，加载一次就跳出，减少循环次数

                if (position == key) {

                    val params = holder.ll_limit_left.layoutParams as LinearLayout.LayoutParams
                    params.width = UIUtil.dip2px(mContext, value)
                    holder.ll_limit_left.layoutParams = params
                    val params1 = holder.ll_limit_top.layoutParams as LinearLayout.LayoutParams
                    params1.width = UIUtil.dip2px(mContext, value)
                    holder.ll_limit_top.layoutParams = params1
                    val params2 = holder.ll_limit_right.layoutParams as LinearLayout.LayoutParams
                    params2.width = UIUtil.dip2px(mContext, value)
                    holder.ll_limit_right.layoutParams = params2

                    val imageView2 = ImageView(mContext)
                    imageView2.setImageResource(R.drawable.bg_transparent_5)
                    holder.ll_limit_left.addView(imageView2)


                    val imageView = ImageView(mContext)
                    //                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setBackgroundResource(R.mipmap.ic_oval_left_small)
                    holder.ll_limit_top.addView(imageView)


                    val imageView1 = ImageView(mContext)
                    //                imageView1.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView1.setBackgroundResource(R.mipmap.ic_oval_middle_small)
                    holder.ll_limit_right.addView(imageView1)
                    isAdded1 = true
                    break
                } else if (position == key + yanyinSetWithNum[key]!! - 1) {
                    val params = holder.ll_limit_left.layoutParams as LinearLayout.LayoutParams
                    params.width = UIUtil.dip2px(mContext, value)
                    holder.ll_limit_left.layoutParams = params
                    val params1 = holder.ll_limit_top.layoutParams as LinearLayout.LayoutParams
                    params1.width = UIUtil.dip2px(mContext, value)
                    holder.ll_limit_top.layoutParams = params1
                    val params2 = holder.ll_limit_right.layoutParams as LinearLayout.LayoutParams
                    params2.width = UIUtil.dip2px(mContext, value)
                    holder.ll_limit_right.layoutParams = params2

                    //需要显示数字
                    if (yanyinSetWithNum[key]!! % 2 == 0) {
                        //偶数
                        val needShowNum = key + yanyinSetWithNum[key]!! / 2
                        if (position == needShowNum) {
                            val v = TextView(mContext)
                            v.textSize = UIUtil.sp2px(mContext, 3f).toFloat()
                            v.text = yanyinSetWithNum[key].toString() + ""
                            holder.ll_limit_left.addView(v)
                            val params3 = v.layoutParams as LinearLayout.LayoutParams
                            params3.topMargin = -UIUtil.dip2px(mContext, 3.5f)
                            v.layoutParams = params3
                        }
                    } else {

                    }

                    val imageView1 = ImageView(mContext)
                    imageView1.setBackgroundResource(R.mipmap.ic_oval_middle_small)
                    holder.ll_limit_left.addView(imageView1)

                    val imageView = ImageView(mContext)
                    imageView.setBackgroundResource(R.mipmap.ic_oval_right_small)
                    holder.ll_limit_top.addView(imageView)

                    val imageView2 = ImageView(mContext)
                    imageView2.setBackgroundResource(R.drawable.bg_transparent_5)
                    holder.ll_limit_right.addView(imageView2)

                    isAdded1 = true
                    break
                } else if (position > key && position < key + yanyinSetWithNum[key]!! - 1) {

                    //
                    val params = holder.ll_limit_left.layoutParams as LinearLayout.LayoutParams
                    params.width = UIUtil.dip2px(mContext, value)
                    holder.ll_limit_left.layoutParams = params
                    val params1 = holder.ll_limit_top.layoutParams as LinearLayout.LayoutParams
                    params1.width = UIUtil.dip2px(mContext, value)
                    holder.ll_limit_top.layoutParams = params1
                    val params2 = holder.ll_limit_right.layoutParams as LinearLayout.LayoutParams
                    params2.width = UIUtil.dip2px(mContext, value)
                    holder.ll_limit_right.layoutParams = params2

                    var isCenterHasNum = false
                    var isLeftHasNum = false
                    //需要显示数字
                    if (yanyinSetWithNum[key]!! % 2 == 0) {
                        //偶数
                        val needShowNum = key + yanyinSetWithNum[key]!! / 2
                        if (position == needShowNum) {
                            val v = TextView(mContext)
                            v.textSize = UIUtil.sp2px(mContext, 3f).toFloat()
                            v.text = yanyinSetWithNum[key].toString() + ""
                            holder.ll_limit_left.addView(v)
                            holder.ll_limit_left.gravity = Gravity.RIGHT
                            val params3 = v.layoutParams as LinearLayout.LayoutParams
                            params3.topMargin = -UIUtil.dip2px(mContext, 3.5f)
                            v.layoutParams = params3
                            isLeftHasNum = true
                        }
                    } else {
                        val needShowNum = key + (yanyinSetWithNum[key]!! - 1) / 2
                        if (position == needShowNum) {

                            val v = TextView(mContext)
                            v.textSize = UIUtil.sp2px(mContext, 3f).toFloat()
                            v.text = yanyinSetWithNum[key].toString() + ""
                            holder.ll_limit_top.addView(v)
                            val params3 = v.layoutParams as LinearLayout.LayoutParams
                            params3.topMargin = -UIUtil.dip2px(mContext, 3.5f)
                            v.layoutParams = params3
                            isCenterHasNum = true
                        }
                    }

                    if (!isLeftHasNum) {
                        val imageView1 = ImageView(mContext)
                        imageView1.setBackgroundResource(R.mipmap.ic_oval_middle_small)
                        holder.ll_limit_left.addView(imageView1)
                    }

                    if (!isCenterHasNum) {
                        val imageView = ImageView(mContext)
                        imageView.setBackgroundResource(R.mipmap.ic_oval_middle_small)
                        holder.ll_limit_top.addView(imageView)
                    }

                    val imageView2 = ImageView(mContext)
                    imageView2.setBackgroundResource(R.mipmap.ic_oval_middle_small)
                    holder.ll_limit_right.addView(imageView2)
                    isAdded1 = true
                    break
                }
                if (isAdded1) {
                    break
                }
            }
        }
//


        var rightChildCount = 0
        rightChildCount = holder.ll_right.childCount
        if (holder.ll_right_down.childCount > rightChildCount) {
            rightChildCount = holder.ll_right_down.childCount
        }


        var leftChildCount = 0
        leftChildCount = holder.ll_left_center.childCount
        if (holder.ll_left_down.childCount > leftChildCount) {
            leftChildCount = holder.ll_left_down.childCount
        }
        if (holder.ll_left_top.childCount > maxSize) {
            leftChildCount = holder.ll_left_top.childCount
        }

        if (leftChildCount > rightChildCount) {
            for (i in 1..(leftChildCount - rightChildCount)) {
                var vv = ImageView(mContext)
                vv.setImageResource(R.drawable.bg_transparent_5)
                holder.ll_right.addView(vv)
            }
        } else if (rightChildCount > leftChildCount) {
            for (i in 1..(rightChildCount - leftChildCount)) {
                var vv = ImageView(mContext)
                vv.setImageResource(R.drawable.bg_transparent_5)
                holder.ll_left_center.addView(vv)
            }
        }


        if (isSelected) {
            holder.fl_foreground.foreground = mContext.resources.getDrawable(R.drawable.bg_99d0a670)
        } else {
            holder.fl_foreground.foreground = mContext.resources.getDrawable(R.drawable.bg_transparent)
        }
        if (selectedIndices.size > 0 && position >= selectedIndices[0] && position <= selectedIndices[selectedIndices.size - 1]) {
            if (selectedIndices.size == 1) {
                if (selectedIndices[0] == position) {

                    holder.left.visibility = View.VISIBLE
                    holder.right.visibility = View.VISIBLE
                } else {
                    holder.left.visibility = View.INVISIBLE
                    holder.right.visibility = View.GONE
                }
            } else if (position >= selectedIndices[0] && position <= selectedIndices[selectedIndices.size - 1]) {
                if (selectedIndices[0] == position) {
                    holder.left.visibility = View.VISIBLE
                    holder.right.visibility = View.GONE
                } else if (selectedIndices[selectedIndices.size - 1] == position) {
                    holder.left.visibility = View.INVISIBLE
                    holder.right.visibility = View.VISIBLE
                } else {
                    holder.left.visibility = View.INVISIBLE
                    holder.right.visibility = View.GONE
                }

            } else {
                if (getCurrentEndLine(position) - getSelectEndLines() >= 1) {

                    holder.left.visibility = View.GONE
                } else {

                    holder.left.visibility = View.INVISIBLE
                }
                holder.right.visibility = View.GONE
            }

        } else {
            holder.left.visibility = View.INVISIBLE
            holder.right.visibility = View.GONE
        }

        //是否换行
        val lp = holder.itemView.layoutParams
        if (lp is FlexboxLayoutManager.LayoutParams) {
            val flexboxLp = holder.itemView.layoutParams as FlexboxLayoutManager.LayoutParams
            if (position > 0) {
                flexboxLp.isWrapBefore = list!![position - 1].islinefeed == 1
            }

        }
    }

    override fun setSelected(index: Int, selected: Boolean) {
//        Log.d("MainAdapter", "setSelected($index, $selected)")
        if (!selected) {
            selectedIndices.remove(index)
            selectedIndices.sort()
            clearAb(index)
            rv_list?.findViewHolderForLayoutPosition(index)?.itemView?.findViewById<FrameLayout>(R.id.fl_foreground)
                    ?.foreground = mContext.resources.getDrawable(R.drawable.bg_transparent)
        } else if (!selectedIndices.contains(index)) {
            selectedIndices.add(index)
            selectedIndices.sort()
            rv_list?.findViewHolderForLayoutPosition(index)?.itemView?.findViewById<FrameLayout>(R.id.fl_foreground)
                    ?.foreground = mContext.resources.getDrawable(R.drawable.bg_99d0a670)
        }
        setTags()
        callback?.onSelectionChanged(selectedIndices.size)
    }

    override fun isIndexSelectable(index: Int): Boolean {
        return true
    }

    override fun getItemCount(): Int {
        return if (list == null) {
            0
        } else {
            list!!.size
        }
    }

    //    private void getItemView(int position) {
    //    }

    class MainViewHolder(itemView: View, private val callback: Listener?) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {

        val colorSquare: RectangleView
        val left: TextView
        val right: TextView
        val ll_center: LinearLayout
        val ll_center_dowm: LinearLayout
        val ll_center_top: LinearLayout
        val ll_limit_top: LinearLayout
        val ll_left_center: LinearLayout
        val ll_left_down: LinearLayout
        val ll_right: LinearLayout
        val ll_right_down: LinearLayout
        val ll_limit_left: LinearLayout
        val ll_limit_right: LinearLayout
        val ll_right_content: LinearLayout
        val ll_left_content: LinearLayout
        val ll_center_content: LinearLayout
        val ll_left_top: LinearLayout
        //        val ll_all: LinearLayout
        val iv_image: LinearLayout
        val fl_foreground: FrameLayout
        val iv_shoushi: ImageView

        init {
            this.colorSquare = itemView.findViewById(R.id.colorSquare)
            this.right = itemView.findViewById(R.id.right)
            this.left = itemView.findViewById(R.id.left)
            this.ll_center = itemView.findViewById(R.id.ll_center)
            this.ll_center_dowm = itemView.findViewById(R.id.ll_center_dowm)
            this.iv_shoushi = itemView.findViewById(R.id.iv_shoushi)
            this.ll_left_center = itemView.findViewById(R.id.ll_left_center)
            this.ll_right = itemView.findViewById(R.id.ll_right)
            this.ll_center_top = itemView.findViewById(R.id.ll_center_top)
            this.ll_right_down = itemView.findViewById(R.id.ll_right_down)
            this.ll_left_down = itemView.findViewById(R.id.ll_left_down)
            this.ll_limit_top = itemView.findViewById(R.id.ll_limit_top)
            this.ll_limit_left = itemView.findViewById(R.id.ll_limit_left)
            this.ll_limit_right = itemView.findViewById(R.id.ll_limit_right)
            this.ll_right_content = itemView.findViewById(R.id.ll_right_content)
            this.ll_left_content = itemView.findViewById(R.id.ll_left_content)
            this.ll_center_content = itemView.findViewById(R.id.ll_center_content)
            this.ll_left_top = itemView.findViewById(R.id.ll_left_top)
//            this.ll_all = itemView.findViewById(R.id.ll_all)
            this.iv_image = itemView.findViewById(R.id.iv_image)
            this.fl_foreground = itemView.findViewById(R.id.fl_foreground)

            this.itemView.setOnClickListener(this)
            this.itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View) {
            callback?.onClick(adapterPosition)
        }

        override fun onLongClick(v: View): Boolean {
            callback?.onLongClick(adapterPosition)
            return true
        }
    }

    /**
     * AB句模式开始结束位置的a b 字母显示
     *
     * */
    private fun setTags() {
        selectedIndices.forEach {
            var pos = it
            var left = rv_list?.findViewHolderForLayoutPosition(pos)?.itemView?.findViewById<TextView>(R.id.left)
            var right = rv_list?.findViewHolderForLayoutPosition(pos)?.itemView?.findViewById<TextView>(R.id.right)
            if (selectedIndices.size > 0 && pos >= selectedIndices[0] && pos <= selectedIndices[selectedIndices.size - 1]) {
                if (selectedIndices.size == 1) {
                    if (selectedIndices[0] == pos) {
                        left?.visibility = View.VISIBLE
                        right?.visibility = View.VISIBLE
                    } else {
                        left?.visibility = View.INVISIBLE
                        right?.visibility = View.GONE
                    }
                } else if (pos >= selectedIndices[0] && pos <= selectedIndices[selectedIndices.size - 1]) {
                    if (selectedIndices[0] == pos) {
                        left?.visibility = View.VISIBLE
                        right?.visibility = View.GONE
                    } else if (selectedIndices[selectedIndices.size - 1] == pos) {
                        right?.visibility = View.VISIBLE
                        left?.visibility = View.INVISIBLE
                    } else {
                        left?.visibility = View.INVISIBLE
                        right?.visibility = View.GONE
                    }

                } else {
                    if (getCurrentEndLine(pos) - getSelectEndLines() >= 1) {

                        left?.visibility = View.GONE
                    } else {

                        left?.visibility = View.INVISIBLE
                    }
                    right?.visibility = View.GONE
//                    left?.visibility = View.INVISIBLE
//                    right?.visibility = View.INVISIBLE
                }

            } else {
                left?.visibility = View.INVISIBLE
                right?.visibility = View.GONE
            }
        }
    }

    private fun clearAb(pos: Int) {
        var left = rv_list?.findViewHolderForLayoutPosition(pos)?.itemView?.findViewById<TextView>(R.id.left)
        var right = rv_list?.findViewHolderForLayoutPosition(pos)?.itemView?.findViewById<TextView>(R.id.right)
        left?.visibility = View.INVISIBLE
        right?.visibility = View.GONE
    }

    fun getSelectEndLines(): Int {
        var endLine = -1
        mLinesMap.forEach {
            if (selectedIndices.get(selectedIndices.size - 1) <= it.value[1] && selectedIndices.get(selectedIndices.size - 1) >= it.value[0]) {
                endLine = it.key
                return@forEach
            }
        }
        LogUtils.e("getSelectEndLines:" + endLine)
        return endLine
    }

    fun getCurrentEndLine(index: Int): Int {
        var endLine = -1
        mLinesMap.forEach {
            if (index <= it.value[1] && index >= it.value[0]) {
                endLine = it.key
                return@forEach
            }
        }
        LogUtils.e("getCurrentEndLine:" + endLine)
        return endLine
    }
}
