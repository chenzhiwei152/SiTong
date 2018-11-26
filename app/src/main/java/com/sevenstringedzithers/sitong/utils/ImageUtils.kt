package com.sevenstringedzithers.sitong.utils

import android.content.Context
import android.widget.ImageView
import com.sevenstringedzithers.sitong.R

class ImageUtils {


    companion object {
        fun getJiePai(type: String): Int? {
            var image: Int? = jiepaiMap[type] ?: return 0
            return image
        }

        fun getNumber(type: String,isSmall:Boolean, isPressed: Boolean = false): Int? {
            var image: Int? = null
            if (isPressed) {
                if (isSmall){
                    image = number_small_pressed[type] ?: return 0
                }else{
                    image = number_pressed[type] ?: return 0
                }

            } else {
                if (isSmall){
                    image = number_small_black[type] ?: return 0
                }else{
                    image = number_black[type] ?: return 0
                }
            }
            return image
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
            put("8", R.mipmap.ic_8)

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
            put("8", R.mipmap.ic_8_p)

        }
        var number_small_black = hashMapOf<String, Int>().apply {
            put("0", R.mipmap.ic_0_s)
            put("1", R.mipmap.ic_1_s)
            put("2", R.mipmap.ic_2_s)
            put("3", R.mipmap.ic_3_s)
            put("4", R.mipmap.ic_4_s)
            put("5", R.mipmap.ic_5_s)
            put("6", R.mipmap.ic_6_s)
            put("7", R.mipmap.ic_7_s)
            put("8", R.mipmap.ic_8_s)

        }
        var number_small_pressed = hashMapOf<String, Int>().apply {
            put("0", R.mipmap.ic_0_p)
            put("1", R.mipmap.ic_1_p)
            put("2", R.mipmap.ic_2_p)
            put("3", R.mipmap.ic_3_p)
            put("4", R.mipmap.ic_4_p)
            put("5", R.mipmap.ic_5_p)
            put("6", R.mipmap.ic_6_p)
            put("7", R.mipmap.ic_7_p)
            put("8", R.mipmap.ic_8_p)

        }
    }
}