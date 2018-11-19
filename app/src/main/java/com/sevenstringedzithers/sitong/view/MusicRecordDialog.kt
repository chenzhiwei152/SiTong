package com.sevenstringedzithers.sitong.view

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.sevenstringedzithers.sitong.R
import kotlinx.android.synthetic.main.dialog_music_record.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * create by chen.zhiwei on 2018-8-15
 */
class MusicRecordDialog(context: Context?, leftTitle: String, rightTitle: String, time: String) : Dialog(context) {
    var mContext: Context? = null
    var leftTitleListerner: View.OnClickListener? = null
    var rightTitleListerner: View.OnClickListener? = null

    init {
        this.mContext = context
        init(leftTitle, rightTitle, time)
    }

    fun init(leftTitle: String, rightTitle: String, enTitle: String) {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        window!!.setContentView(R.layout.dialog_music_record)
        val window = window
        val wlp = window!!.attributes
        wlp.gravity = Gravity.CENTER
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = wlp
        tv_left.text = leftTitle
        tv_right.text = rightTitle
        tv_en_title.text = enTitle
        tv_left.setOnClickListener { v ->
            leftTitleListerner?.onClick(v)
            dismiss()
        }
        tv_right.setOnClickListener { v ->
            rightTitleListerner?.onClick(v)
            dismiss()
        }
    }

    fun setLeftTitleListerner(lister: View.OnClickListener): MusicRecordDialog {
        this.leftTitleListerner = lister
        return this
    }

    fun setRightTitleListerner(lister: View.OnClickListener): MusicRecordDialog {
        this.rightTitleListerner = lister
        return this
    }

    fun getEdittext(): String {
        if (tv_title.text.toString().isNullOrEmpty()) {
            val formatter = SimpleDateFormat(
                    "yyyy_MM_dd_HH_mm_ss")
            val curDate = Date(System.currentTimeMillis())
            return formatter.format(curDate)
        }
        return tv_title.text.toString()
    }

}