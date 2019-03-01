package com.sevenstringedzithers.sitong.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.jyall.android.common.utils.SharedPrefUtil
import com.jyall.bbzf.extension.jump
import com.sevenstringedzithers.sitong.MainActivity
import com.sevenstringedzithers.sitong.base.BaseActivity
import com.sevenstringedzithers.sitong.base.BaseContext
import com.sevenstringedzithers.sitong.base.BasePresenter
import com.sevenstringedzithers.sitong.base.IBaseView
import com.tencent.android.tpush.XGPushManager



/**
 * create by chen.zhiwei on 2018-8-13
 */
class SplashActivity : BaseActivity<IBaseView, BasePresenter<IBaseView>>() {
    var isFirst = "0"
    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()

    override fun getRootView(): IBaseView = this

    override fun getLayoutId(): Int = com.sevenstringedzithers.sitong.R.layout.activity_splash
    override fun onCreate(savedInstanceState: Bundle?) {
        if (!isTaskRoot) { //判断该Activity是不是任务空间的源Activity，“非”也就是说是被系统重新实例化出来
            //如果你就放在launcher Activity中话，这里可以直接return了
            //bug at:http://blog.csdn.net/love100628/article/details/43238135
            val mainIntent = intent
            val action = mainIntent.action
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action == Intent.ACTION_MAIN) {
                finish()
                return //finish()之后该活动会继续执行后面的代码，你可以logCat验证，加return避免可能的exception
            }
        }
        super.onCreate(savedInstanceState)
    }
    override fun initViewsAndEvents() {
        var  click = XGPushManager.onActivityStarted(this)

        if(click !=null) {

            //从推送通知栏打开Service打开Activity会重新执行Laucher流程  //查看是不是全新打开的面板                                                                     

            if(isTaskRoot()) {return;}

            finish()//如果有面板存在则关闭当前的面板

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
            XGPushManager.bindAccount(getApplicationContext(), BaseContext.instance.getUserInfo()?.phone)
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