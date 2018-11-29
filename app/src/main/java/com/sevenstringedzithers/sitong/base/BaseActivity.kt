package com.jyall.bbzf.base

import android.content.ComponentCallbacks2
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.jyall.multiplestatusview.EmptyLayout
import com.sevenstringedzithers.sitong.utils.ActivityStackManager
import com.sevenstringedzithers.sitong.utils.EmptyLayoutEnum
import com.sevenstringedzithers.sitong.utils.StatusBarUtil
import com.sevenstringedzithers.sitong.view.CustomProgressDialog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


abstract class BaseActivity<T : IBaseView, K : BasePresenter<T>> : AppCompatActivity(), IBaseView {
    /**
     * BasePresenter<T>类型 K 用于attachView 、detachView 统一处理
     */
    var mPresenter: K? = null

    /**
     * 统一进度弹窗
     */
    private val progressDialog by lazy { CustomProgressDialog(this) }

    /**
     * loading content error 显示布局
     */
    open val emptyLayout by lazy { EmptyLayout(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        mPresenter = getPresenter()
        ActivityStackManager.getInstance().addActivity(this)

        mPresenter?.attachView(getRootView())
        if (isRegistEventBus()) EventBus.getDefault().register(this)
        if (null != isNeedLec()) intEmptyLayout()
        initViewsAndEvents()
//        JPushHelper.getRegisterationID()
    }

    fun statusBarDark() {
        StatusBarUtil.darkMode(this)
    }

    fun statusBarDark(color: Int, isBlack: Boolean = true) {
        StatusBarUtil.darkMode(this, color, isBlack)
    }

    fun paddingStatusBar(view: View) {
        StatusBarUtil.setPaddingSmart(this, view)

    }

    /**
     * 获取BasePresenter<T>类型 子类返回实现
     * T 为IBaseView 类型
     */
    abstract fun getPresenter(): K

    /**
     *获取IBaseView类型 子类返回实现
     */
    abstract fun getRootView(): T

    /**
     * 获取资源id 子类返回
     */
    abstract fun getLayoutId(): Int

    /**
     *初始化View与事件
     */
    abstract fun initViewsAndEvents()

    /**
     * 是否注册EventBus true 注册 false 不注册
     */
    abstract fun isRegistEventBus(): Boolean

    /***
     * 是否需要LCE 内容
     */
    protected abstract fun isNeedLec(): View?

    /**
     *显示进度弹窗
     */
    override fun showLoading(isCancleAble: Boolean) {
        progressDialog.setCancleAbled(isCancleAble)
        progressDialog?.show()
    }

    /**
     * 进度弹窗消失
     */
    override fun dismissLoading() {
        progressDialog?.dismiss()
    }

    /**
     * 初始化 EmptyLyaout
     */
    private fun intEmptyLayout() {
        emptyLayout?.setView(isNeedLec())
    }

    override fun showCommonView() {
        emptyLayout?.hideAll()
    }

    override fun showNoNetView() {
//        setErrorRes(getString(R.string.net_state_error), R.mipmap.ic_no_net)
        emptyLayout?.showError()
//        emptyLayout?.errorViewButton?.background=resources.getDrawable(R.drawable.shape_circle_defualt_stroke_orange_normal)
//        emptyLayout?.errorViewButton?.width=UIUtil.dip2px(this,150f)
//        emptyLayout?.errorViewButton?.gravity=Gravity.CENTER
    }

    override fun showErrorView() {
//        setErrorRes(getString(R.string.load_err), R.mipmap.ic_load_err)
        emptyLayout?.showError()
//        emptyLayout?.errorViewButton?.background=resources.getDrawable(R.drawable.shape_circle_defualt_stroke_orange_normal)
//        emptyLayout?.errorViewButton?.width=UIUtil.dip2px(this,150f)
//        emptyLayout?.errorViewButton?.gravity=Gravity.CENTER
    }

    override fun showEmptyView() {
        emptyLayout?.showEmpty()
    }

    /**
     * @param msg    loading描述
     * @param iconId loading 图片
     */
    fun setLoadingRes(msg: String, iconId: Int) {
        emptyLayout?.loadingMessage = msg
        emptyLayout?.setLoadingIcon(iconId)
    }

    /**
     * @param msg    空布局描述
     * @param iconId 空布局 图片
     */
    fun setEmptyRes(msg: String, iconId: Int) {
        emptyLayout?.emptyMessage = msg
        emptyLayout?.setEmptyIcon(iconId)
    }

    /**
     * @param msg    error 布局描述
     * @param iconId error 布局图片
     */
    fun setErrorRes(msg: String, iconId: Int) {
        emptyLayout?.errorMessage = msg
        emptyLayout?.setErrorIcon(iconId)
    }

    /**
     * @param clickListner 设置空内容点击事件
     */
    fun setEmtyClickListner(clickListner: View.OnClickListener) {
        emptyLayout?.emptyClickListener = clickListner
    }

    /**
     * @param clickListner 设置错误内容点击事件
     */
    fun setErrorClickListner(clickListner: View.OnClickListener) {
        emptyLayout?.errorClickListener = clickListner
    }


    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    open fun onMessageEvent(eventBusCenter: EventBusCenter<Object>) {
    }

    /**
     * 释放资源 与 取消掉 页面Presenter 相关请求
     */
    override fun onDestroy() {
        super.onDestroy()
        dismissLoading()
        mPresenter?.detachView()
        if (isRegistEventBus()) EventBus.getDefault().unregister(this)
        ActivityStackManager.getInstance().removeActivity(this)
    }


    /**
     * 根据EmptyLayoutEnum 显示EmptyLayout
     */
    open fun setEmptyByType(enumType: EmptyLayoutEnum) {
        when (enumType) {
            EmptyLayoutEnum.SHOW_CONTENT -> showCommonView()
            EmptyLayoutEnum.SHOW_NO_NET -> showNoNetView()
            EmptyLayoutEnum.SHOW_ERROR -> showErrorView()
            EmptyLayoutEnum.SHOW_EMPTY -> showEmptyView()
        }

    }

    override fun onResume() {
        super.onResume()
        BaseContext.instance.setRunningActivity(this);
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onTrimMemory(level: Int) {
        when (level) {
            ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN -> {
//                LogUtil.i("Alan", "TRIM_MEMORY_UI_HIDDEN") // 当某个app的所有UI都不显示时，就认为该app已经退到后台。
                BaseContext.instance.setRunningActivity(null)
            }
            ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW -> {
                //            LogUtil.i("Alan", "TRIM_MEMORY_RUNNING_LOW")
            }// the device is running much lower on

        }
    }
}