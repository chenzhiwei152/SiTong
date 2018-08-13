package com.sitong.changqin.utils

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.jyall.bbzf.base.BaseContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


/**
 * 扩展工具类，common里面没有的
 */
class ExtraUtils {
    companion object {
        /**
         * @param v 动态设置view margin
         * @param l 左
         * @param t 上
         * @param r 右
         * @param b 下
         */
        fun setMargins(v: View, l: Int, t: Int, r: Int, b: Int) {
            if (v.layoutParams is ViewGroup.MarginLayoutParams) {
                val p = v.layoutParams as ViewGroup.MarginLayoutParams
                p.setMargins(l, t, r, b)
                v.requestLayout()
            }
        }

        /**
         * 动态隐藏软键盘
         *
         * @param activity activity
         */
        fun hideSoftInput(activity: Activity) {
            val view = activity.window.peekDecorView()
            if (view != null) {
                val inputmanger = activity
                        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputmanger.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        /**
         * 打开系统软键盘
         */
        fun openSoftKeyBoard(context: Context) {
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS)
        }

        /**
         * 图片装进MultipartBody
         * @param listPath
         * @return
         */
        fun filesToMultipartBody(listPath: List<String>?): List<MultipartBody.Part>? {
            if (null == listPath) {
                return null
            }
            val bodys = ArrayList<MultipartBody.Part>()
            for (path in listPath) {
                val file = File(path)
                if (!file.exists()) {
                    break
                }
                val fileType = file.name.substring(file.name.lastIndexOf(".") + 1, file.name.length)
                val requestBody = RequestBody.create(MediaType.parse("image/$fileType"), file)
                val body = MultipartBody.Part.createFormData("files", file.name, requestBody)

                bodys.add(body)
            }
            return bodys
        }

        val NETTYPE_WIFI = 0x01
        val NETTYPE_CMWAP = 0x02
        val NETTYPE_CMNET = 0x03

        /**
         * 判断是否联网。
         *
         * @return boolean
         */
        fun isNetworkConnected(context: Context?): Boolean {
            if (null == context) return true
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val ni = cm.activeNetworkInfo

            return ni != null && ni.isConnectedOrConnecting
        }

        /**
         * 获取当前网络类型
         *
         * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
         */
        fun getNetworkType(context: Context): Int {
            var netType = 0
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo ?: return netType
            val nType = networkInfo.type
            if (nType == ConnectivityManager.TYPE_MOBILE) {
                val extraInfo = networkInfo.extraInfo
                if (!TextUtils.isEmpty(extraInfo)) {
                    if (extraInfo.toLowerCase(Locale.ENGLISH) == "cmnet") {
                        netType = NETTYPE_CMNET
                    } else {
                        netType = NETTYPE_CMWAP
                    }
                }
            } else if (nType == ConnectivityManager.TYPE_WIFI) {
                netType = NETTYPE_WIFI
            }
            return netType
        }


        fun timeStamp2Date(seconds: String?, format: String?): String {
            var format = format
            if (seconds != null && !seconds.isEmpty() && seconds != "null") {
                if (format == null || format.isEmpty()) {
                    format = "MM-dd HH:mm"
                }

                val sdf = SimpleDateFormat(format, Locale.CHINA)
                return sdf.format(Date(java.lang.Long.valueOf(seconds)))
            } else {
                return ""
            }
        }

        /**
         * Created by chen.zhiwei on 2016/12/29.
         * 验证电话号码统一工具
         */

        fun isMobile(mobiles: String): Boolean {
            val p = Pattern.compile("^((13[0-9])|(17[0-9])|(15[^4,\\D])|(18[0-9])|(14[0-9])|(16[0-9])|(19[0-9]))\\d{8}$")

            val m = p.matcher(mobiles)

            println(m.matches().toString() + "---")

            return m.matches()
        }

        fun getTextType(type: Int=0): Typeface? {
            var typeface: Typeface? = null
            if (type == 0) {
                typeface = Typeface.createFromAsset(BaseContext.instance.assets, "fonts/chinese.TTF")
            }
            return typeface
        }
    }

}