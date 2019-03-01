package com.sevenstringedzithers.sitong.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jyall.multiplestatusview.EmptyLayout
import com.sevenstringedzithers.sitong.utils.EmptyLayoutEnum
import com.sevenstringedzithers.sitong.utils.StatusBarUtil
import com.sevenstringedzithers.sitong.utils.TypefaceUtil
import com.sevenstringedzithers.sitong.view.CustomProgressDialog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


abstract class BaseFragment<T : IBaseView, K : BasePresenter<T>> : Fragment(), IBaseView {

    /**
     * BasePresenter<T>类型 K 用于attachView 、detachView 统一处理
     */
    var mPresenter: K? = null

    /**
     * 统一进度弹窗
     */
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }


    /**
     * 视图是否加载完毕
     */
    private var isViewPrepare = false
    /**
     * 数据是否加载过了
     */
    private var hasLoadData = false

    /**
     * loading content error 显示布局
     */
    open val emptyLayout by lazy { EmptyLayout(requireContext()) }
    /**
     * 视图缓存
     */
    var mView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return mView ?: inflater?.inflate(getLayoutId(), null)
    }

    fun statusBarDark() {
        StatusBarUtil.darkMode(requireActivity())
//        Eyes.setStatusBarLightMode(requireActivity(), Color.WHITE)
    }

    fun statusBarDark(color: Int, isBlack: Boolean = true) {
        StatusBarUtil.darkMode(requireActivity(), color, isBlack)
    }

    fun paddingStatusBar(view: View) {
        StatusBarUtil.setPaddingSmart(requireActivity(), view)

    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            lazyLoadDataIfPrepared()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        TypefaceUtil.replaceFont(requireActivity(), "fonts/agaramondproregular.otf")
        isViewPrepare = true
        mPresenter = getPresenter()
        mPresenter?.attachView(getRootView())
        if (isRegistEventBus()) EventBus.getDefault().register(this)
        if (null != isNeedLec()) intEmptyLayout()
        initViewsAndEvents()
        lazyLoadDataIfPrepared()
    }

    private fun lazyLoadDataIfPrepared() {
        if (userVisibleHint && isViewPrepare && !hasLoadData) {
            lazyLoad()
            hasLoadData = true
        }
    }


    /**
     * 加载布局
     */
    @LayoutRes
    abstract fun getLayoutId(): Int


    /**
     * 懒加载
     */
    abstract fun lazyLoad()

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
     *初始化View与事件
     */
    abstract fun initViewsAndEvents()

    /**
     * 是否注册EventBus true 注册 false 不注册
     */
    abstract fun isRegistEventBus(): Boolean

    /**
     *显示进度弹窗
     * @isCancleAble 是否可取消，默认是
     */
    override fun showLoading(isCancleAble: Boolean) {
        if (!progressDialog?.isShowing){
            progressDialog.setCancleAbled(isCancleAble)
            progressDialog?.show()
        }
    }

    /**
     * 进度弹窗消失
     */
    override fun dismissLoading() {
        progressDialog?.dismiss()
    }

    /***
     * 是否需要LCE 内容
     */
    protected abstract fun isNeedLec(): View?


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
//        emptyLayout?.errorViewButton?.width= UIUtil.dip2px(requireActivity(),150f)
//        emptyLayout?.errorViewButton?.gravity= Gravity.CENTER
    }

    override fun showErrorView() {
//        setErrorRes(getString(R.string.load_err), R.mipmap.ic_load_err)
        emptyLayout?.showError()
//        emptyLayout?.errorViewButton?.background=resources.getDrawable(R.drawable.shape_circle_defualt_stroke_orange_normal)
//        emptyLayout?.errorViewButton?.width=UIUtil.dip2px(requireActivity(),150f)
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
        mPresenter?.detachView()
        mView = null
        if (isRegistEventBus()) EventBus.getDefault().unregister(this)
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
}
