package com.sitong.changqin.ui.activity

import android.view.View
import com.jyall.android.common.utils.LogUtils
import com.jyall.android.common.utils.SharedPrefUtil
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.base.IBaseView
import com.jyall.bbzf.extension.jump
import com.sevenstringedzithers.sitong.R
import com.sitong.changqin.base.Constants
import com.sitong.changqin.mvp.model.bean.MusicBean
import com.sitong.changqin.utils.files.DownLoadFilesUtils
import kotlinx.android.synthetic.main.layout_common_title.*

/*
* 本地下载的曲目
* */
class LocalDownloadActivity : BaseActivity<IBaseView, BasePresenter<IBaseView>>(), IBaseView {
    private var musicList: ArrayList<MusicBean>? = null
    private var fileList: ArrayList<String>? = null
    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()

    override fun getRootView(): IBaseView = this

    override fun getLayoutId(): Int = R.layout.activity_exercise_record

    override fun initViewsAndEvents() {
        initTitle()
        fileList = arrayListOf()
        iv_menu.setOnClickListener {
            jump<MenuActivity>(isAnimation = false)
        }
        kotlin.run {
            musicList = SharedPrefUtil.getObj(this@LocalDownloadActivity, Constants.musicList) as ArrayList<MusicBean>?
            fileList = DownLoadFilesUtils.getInstance(this)?.getFilesByPath("")
            if (fileList != null) {
                fileList?.forEach {
                    LogUtils.e("fileName:" + it)
                }
            }
        }
    }


    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null
    private fun initTitle() {
        iv_back.setOnClickListener { finish() }
        tv_title.setText("下载的乐谱")
        iv_menu.visibility = View.GONE
    }
}