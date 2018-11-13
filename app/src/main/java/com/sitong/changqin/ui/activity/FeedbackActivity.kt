package com.sitong.changqin.ui.activity

import android.app.Activity
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.view.View
import com.jyall.android.common.utils.LogUtils
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.extension.loadLocalImage
import com.jyall.bbzf.extension.toast
import com.sevenstringedzithers.sitong.R
import com.sitong.changqin.mvp.contract.FeedbackContract
import com.sitong.changqin.mvp.persenter.FeedbackPresenter
import com.sitong.changqin.ui.listerner.ProgressCallback
import com.sitong.changqin.utils.UploadImageUtils
import com.sitong.changqin.view.ImageDialog
import com.yanzhenjie.album.Album
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

    val ACTIVITY_REQUEST_SELECT_PHOTO: Int = 10086
    private var localImageUrl: String? = null
    private var uploadedImageurl: String? = null
    private var mDialog: ImageDialog? = null
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_pic -> {
                if (mDialog == null) {

                    mDialog = ImageDialog(this)
                }
                mDialog?.setLeftTitleListerner(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        Album.startAlbumWithCrop(this@FeedbackActivity, null, ACTIVITY_REQUEST_SELECT_PHOTO
                                , ContextCompat.getColor(this@FeedbackActivity, R.color.color_ffffff)
                                , ContextCompat.getColor(this@FeedbackActivity, R.color.color_20232b))
                    }

                })
                mDialog?.setRightTitleListerner(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        Album.startAlbumWithCrop(this@FeedbackActivity, null, ACTIVITY_REQUEST_SELECT_PHOTO
                                , ContextCompat.getColor(this@FeedbackActivity, R.color.color_ffffff)
                                , ContextCompat.getColor(this@FeedbackActivity, R.color.color_20232b))
                    }

                })
                mDialog?.show()

            }
            R.id.tv_send -> {
                if (!et_content.text.toString().isNullOrEmpty()) {

                    if (!localImageUrl.isNullOrEmpty()) {
                        if (!uploadedImageurl.isNullOrEmpty()) {
                            startUpload()
                        } else {
                            showLoading(false)
                            UploadImageUtils.uploadImage(this@FeedbackActivity,1, localImageUrl!!, object : ProgressCallback {
                                override fun onProgressCallback(progress: Double) {
                                    LogUtils.e("progress:" + progress)
                                }

                                override fun onProgressFailed() {
                                    dismissLoading()
                                    toast_msg("上传图片失败")
                                }

                                override fun onProgressSuccess() {
//                                    uploadedImageurl = result?.requestId
                                    startUpload()
                                    dismissLoading()
                                }

                            })
                        }

                    } else {
                        startUpload()
                    }

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
        if (requestCode == ACTIVITY_REQUEST_SELECT_PHOTO && resultCode == Activity.RESULT_OK) {
            val hasChoosePath = Album.parseResult(data) as java.util.ArrayList<String>
            if (hasChoosePath != null) {
                localImageUrl = hasChoosePath[0]
                iv_pic.loadLocalImage(this, localImageUrl!!)

            }

        }
    }
    private fun initTitle() {
        iv_back.setOnClickListener { finish() }
        tv_title.setText("意见反馈")
        iv_menu.visibility = View.GONE
    }
}