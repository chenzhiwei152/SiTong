package com.sevenstringedzithers.sitong.utils

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import com.jyall.android.common.utils.UIUtil
import com.sevenstringedzithers.sitong.R

class ImageUtils {


    companion object {
        fun getJiePai(type: String): Int? {
            var image: Int? = jiepaiMap[type] ?: return 0
            return image
        }

        fun getNumber(ll_center: LinearLayout?, mContext: Context, type: String, isSmall: Int = 1, isPressed: Boolean = false) {
            if (ll_center==null){
                return
            }
            var image: Int? = null
            var imageView = ImageView(mContext)
            if (isPressed) {
                image = number_pressed[type]
            } else {
                image = number_black[type]
            }
            if (image != null) {
                imageView.setImageResource(image!!)
                ll_center?.addView(imageView)

                var para = imageView.layoutParams as LinearLayout.LayoutParams

                if (isSmall == 1) {
                } else if (isSmall == 2) {
                    para.width = UIUtil.dip2px(mContext, 8f)
                    para.height = UIUtil.dip2px(mContext, 8f)
                    para.topMargin= UIUtil.dip2px(mContext, 2f)
                } else {
                    para.width = UIUtil.dip2px(mContext, 4f)
                    para.height =UIUtil.dip2px(mContext, 4f)
                    para.topMargin= UIUtil.dip2px(mContext, 2f)
                }
                imageView.layoutParams = para
            }


        }

        fun getImageView(mContext: Context, id: Int, widths: Int = -1, height: Int = -1): ImageView? {
            var imageView = ImageView(mContext)
            imageView.setImageResource(id)
            if (widths > 0) {
                var param = imageView.layoutParams
                param.width = widths
                param.height = height
                imageView.layoutParams = param
            }
            return imageView
        }

        var jiepaiMap = hashMapOf<String, Int>().apply {
            put("1/4", R.mipmap.ic_jiepai_1_4)
            put("2/4", R.mipmap.ic_jiepai_2_4)
            put("2/2", R.mipmap.ic_jiepai_2_2)
            put("3/2", R.mipmap.ic_jiepai_3_2)
            put("3/4", R.mipmap.ic_jiepai_3_4)
            put("4/2", R.mipmap.ic_jiepai_4_2)
            put("4/4", R.mipmap.ic_jiepai_4_4)
            put("5/4", R.mipmap.ic_jiepai_5_4)
            put("6/8", R.mipmap.ic_jiepai_6_8)
            put("3/8", R.mipmap.ic_jiepai_3_8)
            put("9/8", R.mipmap.ic_jiepai_9_8)
            put("12/8", R.mipmap.ic_jiepai_12_8)
        }
        var number_black = hashMapOf<String, Int>().apply {
            put("0", R.mipmap.ic_0)
            put("1", R.mipmap.ic_1)
            put("2", R.mipmap.ic_2)
            put("3", R.mipmap.ic_3)
            put("4", R.mipmap.ic_4)
            put("5", R.mipmap.ic_5)
            put("6", R.mipmap.ic_6)
            put("7", R.mipmap.ic_7)

        }
        var number_pressed = hashMapOf<String, Int>().apply {
            put("0", R.mipmap.ic_0_p)
            put("1", R.mipmap.ic_1_p)
            put("2", R.mipmap.ic_2_p)
            put("3", R.mipmap.ic_3_p)
            put("4", R.mipmap.ic_4_p)
            put("5", R.mipmap.ic_5_p)
            put("6", R.mipmap.ic_6_p)
            put("7", R.mipmap.ic_7_p)
        }
    }
}