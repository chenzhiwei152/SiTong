package com.sitong.changqin.ui.activity

import android.Manifest
import android.os.Handler
import android.view.View
import com.jyall.android.common.utils.SharedPrefUtil
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.base.BaseContext
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.base.IBaseView
import com.jyall.bbzf.extension.jump
import com.sevenstringedzithers.sitong.R
import com.sitong.changqin.MainActivity
import com.sitong.changqin.utils.DownUtils.permission.JsPermissionUtils

/**
 * create by chen.zhiwei on 2018-8-13
 */
class SplashActivity : BaseActivity<IBaseView, BasePresenter<IBaseView>>() {
    var isFirst = "0"
    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()

    override fun getRootView(): IBaseView = this

    override fun getLayoutId(): Int = R.layout.activity_splash

    override fun initViewsAndEvents() {
        if (JsPermissionUtils.needRequestPermission()) {
            JsPermissionUtils.getInstance().requestPermission(this, 100, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        }
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
        if (BaseContext.instance.getUserInfo() != null) {
            jump<MainActivity>(isAnimation = true)
        } else {
//            val intent = Intent(this, LoginOrRegisterActivity::class.java)
//            startActivity(intent)
            jump<LoginOrRegisterActivity>(isAnimation = false)
        }
        finish()
    }

    private fun intentGuidView() {
        intentMainActivity()
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null


}