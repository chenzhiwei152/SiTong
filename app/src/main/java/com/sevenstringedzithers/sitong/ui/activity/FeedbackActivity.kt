package com.sevenstringedzithers.sitong.ui.activity

import android.content.Intent
import android.view.View
import com.jyall.bbzf.extension.toast
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.base.BaseActivity
import com.sevenstringedzithers.sitong.mvp.contract.FeedbackContract
import com.sevenstringedzithers.sitong.mvp.persenter.FeedbackPresenter
import com.sevenstringedzithers.sitong.utils.uploadimage.PhotoSelectUtils
import kotlinx.android.synthetic.main.activity_feed_back.*
import kotlinx.android.synthetic.main.layout_common_title.*

/**
 * create by chen.zhiwei on 2018-8-13
 */
class FeedbackActivity : BaseActivity<FeedbackContract.View, FeedbackPresenter>(), FeedbackContract.View, View.OnClickListener {
    override fun upLoadSuccess(msg: String) {
        toast_msg(msg)
        finish()
    }
    private var photoSelectUtils: PhotoSelectUtils? = null
    private var localImageUrl: String? = null
    private var uploadedImageurl: String? = null
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_pic -> {
                if (photoSelectUtils == null) {
                    photoSelectUtils = PhotoSelectUtils(this)
                    photoSelectUtils!!.setType(1)
                    photoSelectUtils!!.setOnUploadImagesListener(object : PhotoSelectUtils.OnUploadImagesListener{
                        override fun uploadStart() {
                            showLoading()
                        }
                        override fun uploadComplete(result: List<String>) {
                            if (result.isNotEmpty()){
                                uploadedImageurl=result[0]
                                dismissLoading()
                            }
                        }

                        override fun uploadError(msg: String) {
                            dismissLoading()
                        }
                    })
                }
                photoSelectUtils?.showPhotoChooseDialog(
                        null,
                        maxNum = 1
                )
            }
            R.id.tv_send -> {
                if (!et_content.text.toString().isNullOrEmpty()) {
                    startUpload()
                } else {
                    toast_msg("编辑内容不能为空")
                }
            }
        }
    }

    fun startUpload() {
        var map = hashMapOf<String, String>()
        map.put("content", et_content.text.toString())
        if (!uploadedImageurl.isNullOrEmpty()) {
//            map.put("imgs", uploadedImageurl!!)
        }
        if (!et_tel.text.toString().isNullOrEmpty()) {
            map.put("connect", et_tel.text.toString())
        }
        mPresenter?.upload(map)
    }

    override fun getPresenter(): FeedbackPresenter = FeedbackPresenter()

    override fun getRootView(): FeedbackContract.View = this
    override fun getLayoutId(): Int = R.layout.activity_feed_back


    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null

    override fun toast_msg(msg: String) {
        toast(msg)
    }

    override fun initViewsAndEvents() {
        initTitle()
        iv_pic.setOnClickListener(this)
        tv_send.setOnClickListener(this)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        photoSelectUtils?.onActivityResult(requestCode, resultCode, data)
    }
    private fun initTitle() {
        iv_back.setOnClickListener { finish() }
        tv_title.setText("意见反馈")
        iv_menu.visibility = View.GONE
    }
}