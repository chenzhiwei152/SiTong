package com.sevenstringedzithers.sitong.utils

import com.sevenstringedzithers.sitong.R

class ImageUtils {


    companion object {
        fun getJiePai(type: String): Int? {
            var image: Int? = jiepaiMap.get(type) ?: return 0
            return jiepaiMap.get(type)
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

    }
}