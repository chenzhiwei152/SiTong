package com.sitong.changqin.ui.activity

import android.content.Context
import android.content.Intent
import android.text.Html
import android.view.View
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.base.IBaseView
import com.stringedzithers.sitong.R
import kotlinx.android.synthetic.main.activity_music_info.*
import kotlinx.android.synthetic.main.layout_play_title_fff.*

class MusicInfoActivity : BaseActivity<IBaseView, BasePresenter<IBaseView>>(), IBaseView {
    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()

    override fun getRootView(): IBaseView = this

    override fun getLayoutId(): Int = R.layout.activity_music_info

    override fun initViewsAndEvents() {
        initTitle()
        if (intent.extras!=null){}
        tv_type.text=intent.extras.getString("type")
        tv_source.text=intent.extras.getString("source")
        tv_content.text = Html.fromHtml(intent.extras.getString("content"))
        tv_close.setOnClickListener { finish() }
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null
    companion object {
        fun nweIntentce(mContext:Context,type:String,source:String,content:String){
            var intent= Intent(mContext,MusicInfoActivity::class.java)
            intent.putExtra("type",type)
            intent.putExtra("source",source)
            intent.putExtra("content",content)
            mContext.startActivity(intent)

        }
    }
    private fun initTitle(){
        iv_back.setOnClickListener { finish() }
    }
}