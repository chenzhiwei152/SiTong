package com.sitong.changqin.view

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.jyall.bbzf.extension.loadImage
import com.sevenstringedzithers.sitong.R
import com.sitong.changqin.ui.listerner.ProgressCallback
import kotlinx.android.synthetic.main.dialog_load.*

/**
 * 下载
 * create by chen.zhiwei on 2018-8-15
 */
class MusicDownloadDialog(context: Context?, leftTitle: String, rightTitle: String, size: String, imageurl: String) : Dialog(context) {
    var mContext: Context? = null
    var leftTitleListerner: View.OnClickListener? = null
    var rightTitleListerner: View.OnClickListener? = null
    var seekListerner: ProgressCallback? = null

    init {
        this.mContext = context
        init(leftTitle, rightTitle, size, imageurl)
    }

    fun init(leftTitle: String, rightTitle: String, size: String, imageurl: String) {

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

        tv_en_title.text =String.format("%.2f", size.toFloat())+"M"
        iv_image.loadImage(mContext!!,imageurl)
        seekListerner = object : ProgressCallback {
            override fun onProgressCallback(progress: Double) {
                seek_bar.setProgress(progress.toFloat())
            }

            override fun onProgressFailed() {
            }

            override fun onProgressSuccess() {
            }

        }
        tv_left.setOnClickListener { v ->
            leftTitleListerner?.onClick(v)
//            dismiss()
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

    fun getSeekBarLister(): ProgressCallback {
        return seekListerner!!
    }

}