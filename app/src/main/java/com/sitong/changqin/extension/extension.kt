package com.jyall.bbzf.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.jyall.android.common.utils.ImageLoadedrManager
import com.sevenstringedzithers.sitong.R
import com.sitong.changqin.ui.activity.LoginActivity
import com.sitong.changqin.utils.ExtraUtils
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


fun Activity.toast(message: String, duration: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(this, message, duration).show()

fun Activity.toast(resId: Int, duration: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(this, resId, duration).show()

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(this, message, duration).show()

inline fun <reified T : Activity> Activity.jump(checkLogin: Boolean = false, isAnimation: Boolean = true, dataBundle: Bundle = Bundle()) =
        if (checkLogin) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            if (isAnimation) {

            } else {
                overridePendingTransition(0, 0)
            }
        } else {
            val intent = Intent(this, T::class.java)
            intent.putExtras(dataBundle)
            startActivity(intent)
            if (isAnimation) {

            } else {
                overridePendingTransition(0, 0)
            }
        }

fun ImageView.loadImage(context: Context, url: String?, defaultResId: Int = R.mipmap.ic_default, errorResId: Int = R.mipmap.ic_default, width: Int = 0, height: Int = 0) {
    if (null != url)
        ImageLoadedrManager.getInstance().display(context, url, this, defaultResId, errorResId)

}

fun ImageView.loadRoundImage(context: Context, url: String?, defaultResId: Int = R.mipmap.ic_default, width: Int = 0, height: Int = 0) {
    if (null != url)
        ImageLoadedrManager.getInstance().displayCycle(context, url, this, defaultResId)

}

fun ImageView.loadCircleImage(context: Context, url: String?, degree: Int = 0, width: Int = 0, height: Int = 0) {
    if (null != url)
        ImageLoadedrManager.getInstance().displayRound(context, url, this, degree)
//        Glide.with(context).load(ExtraUtils.hanleUrlSize(url!!, width, height)).apply(RequestOptions().placeholder(R.mipmap.house_defalut_bg).error(R.mipmap.house_defalut_bg).transform(ImageLoadedrManager.GlideRoundTransform(context, degree))).into(this)

}


fun ImageView.loadLocalImage(context: Context, url: String, defaultResId: Int = R.mipmap.ic_default, errorResId: Int = R.mipmap.ic_default) =
        ImageLoadedrManager.getInstance().display(context, "file://$url", this, defaultResId, errorResId)

fun Bitmap.toByteArray(bitmap: Bitmap, format: Bitmap.CompressFormat): ByteArray {
    val baos = ByteArrayOutputStream()
    bitmap.compress(format, 100, baos)
    return baos.toByteArray()
}


fun String.formateDate(timeMillis: Long?, format: String = "yyyy-MM-dd HH:mm:ss"): String {
    return if (null != timeMillis) {
        try {
            var date = Date(timeMillis)
            val sdf = SimpleDateFormat(format, Locale.CHINA)
            sdf.format(date)
        } catch (e: Exception) {
            ""
        }
    } else {
        ""
    }
}

fun String.formateDate(timeMillis: String?, format: String = "yyyy-MM-dd HH:mm:ss"): String {
    return if (null != timeMillis) {
        try {
            var date = Date(timeMillis.toLong())
            val sdf = SimpleDateFormat(format, Locale.CHINA)
            sdf.format(date)
        } catch (e: Exception) {
            ""
        }
    } else {
        ""
    }
}

fun TextView.loadText(text: String, type: Int = 0) {
    if (ExtraUtils.getTextType(type) != null) {
        this.typeface = ExtraUtils.getTextType(type)
    }
    this.setText(text)
}