package com.jyall.bbzf.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.jyall.android.common.utils.LogUtils
import com.jyall.android.common.utils.SharedPrefUtil
import com.scwang.smartrefresh.layout.SmartRefreshLayout.setDefaultRefreshFooterCreater
import com.scwang.smartrefresh.layout.SmartRefreshLayout.setDefaultRefreshHeaderCreater
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.mvp.model.bean.UserInfo
import com.sevenstringedzithers.sitong.utils.ActivityStackManager
import com.tencent.android.tpush.XGIOperateCallback
import com.tencent.android.tpush.XGPushConfig
import com.tencent.android.tpush.XGPushManager
import com.tencent.android.tpush.XGPushManager.registerPush
import javax.crypto.SecretKey
import kotlin.properties.Delegates


class BaseContext : MultiDexApplication() {
    init {
        //设置全局的Header构建器
        setDefaultRefreshHeaderCreater(DefaultRefreshHeaderCreater { context, layout ->
            ClassicsHeader(context)
        })
        //设置全局的Footer构建器
        setDefaultRefreshFooterCreater { context, _ -> ClassicsFooter(context).setDrawableSize(20f) }
    }


    private var secretKey: SecretKey? = null

    private var  userInfo: UserInfo? = null

    private var runningActivity: Activity? = null

    fun <T : Activity> getRunningActivity(): T {
        return runningActivity as T
    }

    fun setRunningActivity(baseActivity: Activity?) {
        runningActivity = baseActivity
    }

    /**
     * BaseContext instance
     */
    companion object {
        var instance: BaseContext by Delegates.notNull()
    }



    /**
     * dex 分包
     */
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
//        if (isApkDebugable(instance)){
//            if (LeakCanary.isInAnalyzerProcess(this)) {
//                // This process is dedicated to LeakCanary for heap analysis.
//                // You should not init your app in this process.
//                return
//            }
//            LeakCanary.install(this)
//        }
        LogUtils.setUp(isApkDebugable(this))
        LogUtils.customTagPrefix = this.resources.getString(R.string.app_name)
//        ShareUtils.initShare(this)
        initPush()
        registerActivityLifeCircleListener()//注册Activity声明周期回调，以便于分辨前后台
    }


    /**
     * 是否是debug包
     */
    private fun isApkDebugable(context: Context): Boolean {
        try {
            val info = context.applicationInfo
            return info.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } catch (e: Exception) {

        }
        return false
    }


    fun setUserInfo(userInfo: UserInfo) {
            this.userInfo = userInfo
        SharedPrefUtil.saveObj(instance, "userInfo", userInfo)
    }

    //
    fun getUserInfo(): UserInfo? {
        if (userInfo == null) {
            userInfo = SharedPrefUtil.getObj(instance, "userInfo") as UserInfo?
        }
        return userInfo
    }

    //
    fun logout() {

        if (userInfo != null) {
            registerPush(this, "*")
        }
        userInfo = null
        SharedPrefUtil.saveObj(instance, "userInfo", null)
        var intent = getPackageManager()
                .getLaunchIntentForPackage(getPackageName())
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        ActivityStackManager.getInstance().finishAllActivity()
    }

    fun isLoginIn(): Boolean {
        return null != getUserInfo()
    }


    // 如果已经存在用户登录信息，返回LoginInfo，否则返回null即可
//    private fun loginInfo(): LoginInfo? {
//        return null
//    }

    var isForeground = false

    var regId: String? = null
    /**
     * 腾讯信鸽推送
     */
    private fun initPush() {
        XGPushConfig.enableDebug(this,true)
        XGPushManager.registerPush(this,object :XGIOperateCallback{
            override fun onSuccess(p0: Any?, p1: Int) {
                LogUtils.e("成功XGPushManager:"+p0.toString())
            }

            override fun onFail(p0: Any?, p1: Int, p2: String?) {
                LogUtils.e("失败XGPushManager:"+p2)
            }

        })
    }

    private fun registerActivityLifeCircleListener() {
        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

            }

            override fun onActivityStarted(activity: Activity?) {

            }

            override fun onActivityResumed(activity: Activity?) {
                isForeground = true
            }

            override fun onActivityPaused(activity: Activity?) {
                isForeground = false
            }

            override fun onActivityStopped(activity: Activity?) {

            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

            }

            override fun onActivityDestroyed(activity: Activity?) {

            }
        })
    }
}