package com.sitong.changqin.ui.activity

import android.view.View
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.extension.toast
import com.sitong.changqin.R
import com.sitong.changqin.mvp.contract.MusicPlayContract
import com.sitong.changqin.mvp.model.bean.MusicBean
import com.sitong.changqin.mvp.persenter.MusicPlayPresenter

/**
 * create by chen.zhiwei on 2018-8-15
 */
class MusicPlayActivity : BaseActivity<MusicPlayContract.View, MusicPlayPresenter>(), MusicPlayContract.View {
    private var mThread: Thread? = null
    var id: String? = null

    override fun getRootView(): MusicPlayContract.View = this

    override fun getLayoutId(): Int = R.layout.activity_music_play

    override fun initViewsAndEvents() {
        var bundle = intent.extras
        if (bundle != null) {
            id = bundle.getString("id")
        }
var map:HashMap<String,String>?=null
        map= hashMapOf()
        map.put("music",id!!)
        map.put("needdetail","1")
        mPresenter?.getMusicDetail(map)
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null

    override fun toast_msg(msg: String) {
        toast(msg)
    }

    override fun getDataSuccess(musicList: ArrayList<MusicBean>) {
    }

    override fun getPresenter(): MusicPlayPresenter = MusicPlayPresenter()
}