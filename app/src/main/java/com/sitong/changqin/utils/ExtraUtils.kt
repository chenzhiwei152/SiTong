package com.sitong.changqin.utils

import android.app.Activity
import android.content.Context
import android.graphics.PointF
import android.graphics.Typeface
import android.media.MediaMetadataRetriever
import android.net.ConnectivityManager
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.jyall.android.common.utils.LogUtils
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

        fun toasts(msg:String){
            Toast.makeText(BaseContext.instance,msg,Toast.LENGTH_LONG).show()
        }

        /**
         * 获取屏幕内容高度
         * @param activity
         * @return
         */
        fun getScreenHeight(activity: Activity): Int {
            val dm = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(dm)
            var result = 0
            val resourceId = activity.resources
                    .getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = activity.resources.getDimensionPixelSize(resourceId)
            }
            return dm.heightPixels - result
        }
        fun getMP3FileInfo(path: String): Long {
            var dur: Long? = null
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(path) //在获取前，设置文件路径（应该只能是本地路径）
            val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            retriever.release() //释放
            if (!TextUtils.isEmpty(duration)) {
                dur = java.lang.Long.parseLong(duration)
            } else {
                return 0L
            }
            LogUtils.e("時常："+dur)
            return dur
        }

        /**
         * B(t) = (1 - t)^2 * P0 + 2t * (1 - t) * P1 + t^2 * P2, t ∈ [0,1]
         *
         * @param t  曲线长度比例
         * @param p0 起始点
         * @param p1 控制点
         * @param p2 终止点
         * @return t对应的点
         */
        fun CalculateBezierPointForQuadratic2(t: Float, p0: PointF, p1: PointF, p2: PointF): PointF {
            val point = PointF()
            val temp = 1 - t
            point.x = temp * temp * p0.x + 2f * t * temp * p1.x + t * t * p2.x
            point.y = temp * temp * p0.y + 2f * t * temp * p1.y + t * t * p2.y
            return point
        }
        fun CalculateBezierPointForQuadratic1(t: Float, p0: PointF, p2: PointF): PointF {
            val point = PointF()
            val temp = 1 - t
            point.x = temp  * p0.x +  t  * p2.x
            point.y = temp  * p0.y + t  * p2.y
            return point
        }

        // a integer to xx:xx:xx
        fun secToTime(time: Int): String {
            var timeStr: String? = null
            var hour = 0
            var minute = 0
            var second = 0
            if (time <= 0)
                return "00:00"
            else {
                minute = time / 60
                if (minute < 60) {
                    second = time % 60
                    timeStr = unitFormat(minute) + ":" + unitFormat(second)
                } else {
                    hour = minute / 60
                    if (hour > 99)
                        return "99:59:59"
                    minute = minute % 60
                    second = time - hour * 3600 - minute * 60
                    timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second)
                }
            }
            return timeStr
        }

        fun unitFormat(i: Int): String {
            var retStr: String? = null
            if (i >= 0 && i < 10)
                retStr = "0" + Integer.toString(i)
            else
                retStr = "" + i
            return retStr
        }


        /**
         * 转全角的方法(SBC case)<br></br><br></br>
         * 全角空格为12288，半角空格为32
         * 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
         * @param input 任意字符串
         * @return 半角字符串
         */
        fun ToSBC(input: String): String {
            //半角转全角：
            val c = input.toCharArray()
            for (i in c.indices) {
                if (c[i].toInt() == 32) {
                    c[i] = 12288.toChar()
                    continue
                }
                if (c[i].toInt() < 127)
                    c[i] = (c[i].toInt() + 65248).toChar()
            }
            return String(c)
        }

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

        fun getTextType(type: Int = 0): Typeface? {
            var typeface: Typeface? = null
            if (type == 0) {
                typeface = Typeface.createFromAsset(BaseContext.instance.assets, "fonts/chinese.TTF")
            }
            return typeface
        }
    }

}