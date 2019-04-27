package com.sevenstringedzithers.sitong.ui.activity

import android.support.design.widget.AppBarLayout
import android.view.View
import com.jyall.bbzf.extension.jump
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.base.*
import com.sevenstringedzithers.sitong.ui.listerner.AppBarStateChangeListener
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
        tv_subtitle.typeface = typeface1
        EventBus.getDefault().post(EventBusCenter<Int>(Constants.Tag.MINE_FINISH))
        EventBus.getDefault().post(EventBusCenter<Int>(Constants.Tag.SEARCH_FINISH))
        appBar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: AppBarStateChangeListener.State) {
                if (state === AppBarStateChangeListener.State.EXPANDED) {

                    //展开状态
                    tv_subtitle.visibility = View.VISIBLE
                } else if (state === AppBarStateChangeListener.State.COLLAPSED) {
                    //折叠状态
                    tv_subtitle.visibility = View.GONE
                } else {
                    //中间状态
                    tv_subtitle.visibility = View.GONE
                }
            }
        })
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