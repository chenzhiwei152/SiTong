package com.sitong.changqin.ui.activity

import android.content.Intent
import android.os.Handler
import android.view.View
import com.jyall.android.common.utils.SharedPrefUtil
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.base.IBaseView
import com.sitong.changqin.R

/**
 * create by chen.zhiwei on 2018-8-13
 */
class SplashActivity : BaseActivity<IBaseView, BasePresenter<IBaseView>>() {
    var isFirst = "0"
    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()

    override fun getRootView(): IBaseView = this

    override fun getLayoutId(): Int = R.layout.activity_splash

    override fun initViewsAndEvents() {

//        val decorView = window.decorView
//        val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
//        decorView.systemUiVisibility = uiOptions

        isFirst = SharedPrefUtil.getString(this, "START_COUNT", "0")
        Handler().postDelayed({
            //第一次启动
            if ("0" == isFirst) {
                intentGuidView()
                SharedPrefUtil.saveString(this@SplashActivity, "START_COUNT", "1")
            } else {
                intentMainActivity()
            }
        }, 1000)
    }

    private fun intentMainActivity() {
        val intent = Intent(this, LoginOrRegisterActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    private fun intentGuidView() {
        intentMainActivity()
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null


}