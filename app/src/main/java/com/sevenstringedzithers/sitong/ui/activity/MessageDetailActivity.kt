package com.sevenstringedzithers.sitong.ui.activity

import android.content.Context
import android.content.Intent
import android.view.View
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.base.IBaseView
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.view.ShareDialog
import kotlinx.android.synthetic.main.activity_message_detail.*
import kotlinx.android.synthetic.main.layout_common_title.*

class MessageDetailActivity : BaseActivity<IBaseView, BasePresenter<IBaseView>>(), IBaseView {
    private var title: String = ""
    private var content: String = ""
    private var dialog: ShareDialog? = null
    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()

    override fun getRootView(): IBaseView = this

    override fun getLayoutId(): Int = R.layout.activity_message_detail

    override fun initViewsAndEvents() {
        initTitle()
        if (intent.extras != null) {
            title=intent.extras.getString("title")
            content=intent.extras.getString("content")
            tv_titles.text = title
            tv_content.text = content
        }
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null

    companion object {
        fun newIntentce(mContext: Context, title: String, content: String) {
            var intents = Intent(mContext, MessageDetailActivity::class.java)
            intents.putExtra("title", title)
            intents.putExtra("content", content)
            mContext.startActivity(intents)
        }
    }

    private fun initTitle() {
        iv_back.setOnClickListener { finish() }
        tv_title.setText("消息")
//        iv_menu.setImageResource(R.mipmap.ic_share)
//        iv_menu.setOnClickListener {
//            if (title == null) {
//                return@setOnClickListener
//            }
//            var list = arrayListOf<String>()
//            dialog = ShareDialog(this@MessageDetailActivity, title, content, Constants.SHARE_URL, arrayListOf())
//            dialog?.setShareCallback(object : RVAdapterItemOnClick {
//                override fun onItemClicked(data: Any) {
//                    toast(data as String)
//                }
//
//            })
//            dialog?.show()
//        }
    }
}