package com.sitong.changqin.view

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.sevenstringedzithers.sitong.R
import kotlinx.android.synthetic.main.dialog_selected.*

/**
 * create by chen.zhiwei on 2018-8-15
 */
class ImageDialog(context: Context?) : Dialog(context) {
    var mContext: Context? = null
    var leftTitleListerner: View.OnClickListener? = null
    var rightTitleListerner: View.OnClickListener? = null

    init {
        this.mContext = context
        init()
    }

    fun init() {
        window!!.setContentView(R.layout.dialog_selected)
        val window = window
        window.setBackgroundDrawableResource(R.color.albumTransparent)
        val wlp = window!!.attributes
        wlp.gravity = Gravity.BOTTOM
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = wlp
        local.setOnClickListener { v ->
            leftTitleListerner?.onClick(v)
            dismiss()
        }
        camera.setOnClickListener { v ->
            rightTitleListerner?.onClick(v)
            dismiss()
        }
        bottomCancle.setOnClickListener { v ->
            dismiss()
        }
    }

    fun setLeftTitleListerner(lister: View.OnClickListener): ImageDialog {
        this.leftTitleListerner = lister
        return this
    }

    fun setRightTitleListerner(lister: View.OnClickListener): ImageDialog {
        this.rightTitleListerner = lister
        return this
    }
}