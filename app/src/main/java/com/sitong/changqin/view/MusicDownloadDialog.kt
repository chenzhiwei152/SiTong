package com.sitong.changqin.view

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.stringedzithers.sitong.R
import kotlinx.android.synthetic.main.dialog_load.*

/**
 * create by chen.zhiwei on 2018-8-15
 */
class MusicDownloadDialog(context: Context?, leftTitle: String, rightTitle: String, title: String = "", enTitle: String, level: String, isCollection: Boolean = false) : Dialog(context) {
    var mContext: Context? = null
    var leftTitleListerner: View.OnClickListener? = null
    var rightTitleListerner: View.OnClickListener? = null
    var collectionListerner: View.OnClickListener? = null

    init {
        this.mContext = context
        init(leftTitle, rightTitle, enTitle)
    }

    fun init(leftTitle: String, rightTitle: String, enTitle: String) {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        window!!.setContentView(R.layout.dialog_load)
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

    fun setLeftTitleListerner(lister: View.OnClickListener): MusicDownloadDialog {
        this.leftTitleListerner = lister
        return this
    }

    fun setRightTitleListerner(lister: View.OnClickListener): MusicDownloadDialog {
        this.rightTitleListerner = lister
        return this
    }

    fun setColletionListerner(lister: View.OnClickListener): MusicDownloadDialog {
        this.collectionListerner = lister
        return this
    }

}