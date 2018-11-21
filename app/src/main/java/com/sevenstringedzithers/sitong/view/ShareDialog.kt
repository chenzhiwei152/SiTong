package com.sevenstringedzithers.sitong.view

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.sevenstringedzithers.sitong.R

/**
 * create by chen.zhiwei on 2018-8-15
 */
class ShareDialog(context: Context?, leftTitle: String, rightTitle: String, title: String = "", enTitle: String, level: String) : Dialog(context) {
    var mContext: Context? = null
    var leftTitleListerner: View.OnClickListener? = null
    var rightTitleListerner: View.OnClickListener? = null

    init {
        this.mContext = context
        init(leftTitle, rightTitle, title, enTitle, level)
    }

    fun init(leftTitle: String, rightTitle: String, title: String = "", enTitle: String, level: String) {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        window!!.setContentView(R.layout.dialog_share)
        val window = window
        val wlp = window!!.attributes
        wlp.gravity = Gravity.CENTER
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = wlp
    }

    fun setLeftTitleListerner(lister: View.OnClickListener): ShareDialog {
        this.leftTitleListerner = lister
        return this
    }

    fun setRightTitleListerner(lister: View.OnClickListener): ShareDialog {
        this.rightTitleListerner = lister
        return this
    }


}