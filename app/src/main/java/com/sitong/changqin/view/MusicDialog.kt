package com.sitong.changqin.view

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.stringedzithers.sitong.R
import kotlinx.android.synthetic.main.dialog_music.*

/**
 * create by chen.zhiwei on 2018-8-15
 */
class MusicDialog(context: Context?, leftTitle: String, rightTitle: String, title: String = "", enTitle: String, level: String, isCollection: Boolean = false) : Dialog(context) {
    var mContext: Context? = null
    var leftTitleListerner: View.OnClickListener? = null
    var rightTitleListerner: View.OnClickListener? = null
    var collectionListerner: View.OnClickListener? = null

    init {
        this.mContext = context
        init(leftTitle, rightTitle, title, enTitle, level, isCollection)
    }

    fun init(leftTitle: String, rightTitle: String, title: String = "", enTitle: String, level: String, isCollection: Boolean = false) {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        window!!.setContentView(R.layout.dialog_music)
        val window = window
        val wlp = window!!.attributes
        wlp.gravity = Gravity.CENTER
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = wlp
        tv_left.text = leftTitle
        tv_right.text = rightTitle
        tv_title.text = title
        tv_en_title.text = enTitle
        tv_level.text = level
        if (isCollection) {
            iv_collection.setImageResource(R.mipmap.ic_collectioned)
        } else {
            iv_collection.setImageResource(R.mipmap.ic_collection_normal)
        }
        tv_left.setOnClickListener { v ->
            leftTitleListerner?.onClick(v)
            dismiss()
        }
        tv_right.setOnClickListener { v ->
            rightTitleListerner?.onClick(v)
            dismiss()
        }
        iv_collection.setOnClickListener { v ->
            collectionListerner?.onClick(v)
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

    fun setColletionListerner(lister: View.OnClickListener): MusicDialog {
        this.collectionListerner = lister
        return this
    }

    fun setCollection(isCollection: Boolean) {
        if (isCollection) {
            iv_collection.setImageResource(R.mipmap.ic_collectioned)
        } else {
            iv_collection.setImageResource(R.mipmap.ic_collection_normal)
        }
    }
}