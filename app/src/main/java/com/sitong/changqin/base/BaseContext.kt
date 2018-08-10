package com.jyall.bbzf.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.jyall.android.common.utils.LogUtils
import com.jyall.android.common.utils.SharedPrefUtil
import com.jyall.bbzf.mvp.model.bean.UserInfo
import com.scwang.smartrefresh.layout.SmartRefreshLayout.setDefaultRefreshFooterCreater
import com.scwang.smartrefresh.layout.SmartRefreshLayout.setDefaultRefreshHeaderCreater
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
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

    private var userInfo: UserInfo? = null

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
     * 百度地图开启定位定位
     */
    fun startLocation() {
//        BaiduLocationHelper.getInstance()?.startLocation()
    }

    /*
    * 获取定位的城市
    * */
//    fun getLocationCity(): CityData? {
//        return InitDefaultHelper.getInstance()?.getLocationCity()
//    }

    /*
    * 获取自己选择的城市
    * */
//    fun getSelectedCity(): CityData? {
//        return InitDefaultHelper.getInstance()?.getSelectedCity()
//    }

    fun getChannel(): String? {
        try {
            val appInfo = instance.packageManager.getApplicationInfo(instance.packageName, PackageManager.GET_META_DATA)
            return appInfo.metaData.getString("BBZF_CHANNEL")
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return ""
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
        //编译时注解 use the index
//        EventBus.builder().addIndex(MyEventBusIndex()).installDefaultEventBus()
        LogUtils.setUp(isApkDebugable(this))
        LogUtils.customTagPrefix = "JYALL_NEW"
//        ShareUtils.initShare(this)
//        initNIM()
//        initJPush()
//        SDKInitializer.initialize(this)
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
        if (this.userInfo != null) {
            this.userInfo = userInfo
        } else {
            regId = null
        }
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
//            JPushHelper.logout(userInfo!!.token)
        }
        userInfo = null
        SharedPrefUtil.saveObj(instance, "userInfo", null)
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
     * 极光推送
     */
    private fun initJPush() {
//        JPushInterface.init(this)
//        JPushHelper.register(this)
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