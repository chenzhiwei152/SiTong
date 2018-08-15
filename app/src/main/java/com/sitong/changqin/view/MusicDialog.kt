package com.sitong.changqin.view

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.jyall.bbzf.extension.loadImage
import com.sitong.changqin.R
import kotlinx.android.synthetic.main.dialog_music.*

/**
 * create by chen.zhiwei on 2018-8-15
 */
class MusicDialog(context: Context?, leftTitle: String, rightTitle: String, imageUrl: String = "", isCollection: Boolean = false) : Dialog(context) {
    var mContext: Context? = null
    var leftTitleListerner: View.OnClickListener? = null
    var rightTitleListerner: View.OnClickListener? = null

    init {
        this.mContext = context
        init(leftTitle, rightTitle, imageUrl, isCollection)
    }

    fun init(leftTitle: String, rightTitle: String, imageUrl: String = "", isCollection: Boolean = false) {


        window!!.setContentView(R.layout.dialog_music)
        val window = window
        val wlp = window!!.attributes
        wlp.gravity = Gravity.CENTER
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = wlp
        tv_left.text = leftTitle
        tv_right.text = rightTitle
        if (!imageUrl.isNullOrEmpty()) {
            iv_music.loadImage(mContext!!, imageUrl)
            iv_music.visibility = View.VISIBLE
        } else {
            iv_music.visibility = View.GONE
        }
        if (isCollection) {
            iv_collection.visibility = View.VISIBLE
        } else {
            iv_collection.visibility = View.GONE
        }
        tv_left.setOnClickListener { v ->
            leftTitleListerner?.onClick(v)
            dismiss()
        }
        tv_right.setOnClickListener { v ->
            rightTitleListerner?.onClick(v)
            dismiss()
        }
    }

    fun setLeftTitleListerner(lister: View.OnClickListener): MusicDialog {
        this.leftTitleListerner = lister
        return this
    }

    fun setRightTitleListerner(lister: View.OnClickListener): MusicDialog {
        this.rightTitleListerner = lister
        return this
    }
}