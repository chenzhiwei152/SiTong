package com.sevenstringedzithers.sitong.ui.activity

import android.view.View
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.base.EventBusCenter
import com.jyall.bbzf.base.IBaseView
import com.jyall.bbzf.extension.jump
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.base.Constants
import com.sevenstringedzithers.sitong.utils.TypefaceUtil
import kotlinx.android.synthetic.main.activity_about.*
import org.greenrobot.eventbus.EventBus

class AboutActivity : BaseActivity<IBaseView, BasePresenter<IBaseView>>(), IBaseView {
    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()

    override fun getRootView(): IBaseView = this

    override fun getLayoutId(): Int = R.layout.activity_about

    override fun initViewsAndEvents() {
        iv_menu.setOnClickListener {
            jump<MenuActivity>(isAnimation = false)
        }
        val typeface1 = TypefaceUtil.createagaTypeface(this)
        ct_layout.setCollapsedTitleTypeface(typeface1)
        ct_layout.setExpandedTitleTypeface(typeface1)
        EventBus.getDefault().post(EventBusCenter<Int>(Constants.Tag.MINE_FINISH))
        EventBus.getDefault().post(EventBusCenter<Int>(Constants.Tag.SEARCH_FINISH))
    }


    override fun isRegistEventBus(): Boolean = true
    override fun onMessageEvent(eventBusCenter: EventBusCenter<Object>) {
        super.onMessageEvent(eventBusCenter)
        if (eventBusCenter != null) {
            if (eventBusCenter.evenCode == Constants.Tag.ABOUT_FINISH) {
                finish()
            }
        }
    }
    override fun isNeedLec(): View? = null
}