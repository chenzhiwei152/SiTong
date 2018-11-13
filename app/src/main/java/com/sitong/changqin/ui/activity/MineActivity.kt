package com.sitong.changqin.ui.activity

import android.app.Activity
import android.content.Intent
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import com.jyall.android.common.utils.LogUtils
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.base.BaseContext
import com.jyall.bbzf.base.EventBusCenter
import com.jyall.bbzf.extension.jump
import com.jyall.bbzf.extension.loadRoundImage
import com.jyall.bbzf.extension.toast
import com.jyall.bbzf.ui.adapter.MineInfoFragmentAdapter
import com.sevenstringedzithers.sitong.R
import com.sitong.changqin.base.Constants
import com.sitong.changqin.base.Constants.Tag.RELOAD_USERINFO
import com.sitong.changqin.mvp.contract.MineContract
import com.sitong.changqin.mvp.model.bean.UserInfo
import com.sitong.changqin.mvp.persenter.MinePresenter
import com.sitong.changqin.ui.listerner.ProgressCallback
import com.sitong.changqin.utils.UploadImageUtils
import com.sitong.changqin.view.DailyPunchDialog
import com.sitong.changqin.view.ImageDialog
import com.yanzhenjie.album.Album
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.fragment_mine.view.*
import org.greenrobot.eventbus.EventBus

/**
 * create by chen.zhiwei on 2018-8-15
 */
class MineActivity : BaseActivity<MineContract.View, MinePresenter>(), MineContract.View, View.OnClickListener {
    override fun punchSuccess(msg: String) {
        mPresenter?.getUserInfo()
    }

    private var mDialog: ImageDialog? = null
    val ACTIVITY_REQUEST_SELECT_PHOTO: Int = 10086
    private var localImageUrl: String? = null
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_head -> {
                if (mDialog == null) {

                    mDialog = ImageDialog(this)
                }
                mDialog?.setLeftTitleListerner(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        Album.startAlbumWithCrop(this@MineActivity, null, ACTIVITY_REQUEST_SELECT_PHOTO
                                , ContextCompat.getColor(this@MineActivity, R.color.color_ffffff)
                                , ContextCompat.getColor(this@MineActivity, R.color.color_20232b))
                    }

                })
                mDialog?.setRightTitleListerner(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        Album.startAlbumWithCrop(this@MineActivity, null, ACTIVITY_REQUEST_SELECT_PHOTO
                                , ContextCompat.getColor(this@MineActivity, R.color.color_ffffff)
                                , ContextCompat.getColor(this@MineActivity, R.color.color_20232b))
                    }

                })
                mDialog?.show()
            }
            R.id.tv_message -> {
                jump<MessageActivity>()
            }
            R.id.ll_daily_punch -> {
                var dialog = DailyPunchDialog(this@MineActivity, "查看任务", "今日打卡", "", BaseContext.instance.getUserInfo()!!.award, "已连续打卡" + BaseContext.instance.getUserInfo()!!.days + "天").setLeftTitleListerner(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        TaskActivity.newIntentce(this@MineActivity, "0")
                    }


                }).setRightTitleListerner(object : View.OnClickListener {
                    override fun onClick(v: View?) {
//                        打卡
                        mPresenter?.dailyPunch()
                    }

                })
                dialog?.show()
            }
            R.id.ll_exe_time -> {
                var dialog = DailyPunchDialog(this@MineActivity, "查看日期", "今日练琴", "", BaseContext.instance.getUserInfo()!!.award, "已经连续练琴" + BaseContext.instance.getUserInfo()!!.days + "分钟").setLeftTitleListerner(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                    }


                }).setRightTitleListerner(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                    }

                })
                dialog?.show()
            }

        }
    }


    override fun getPresenter(): MinePresenter = MinePresenter()
    override fun getRootView(): MineContract.View = this
    override fun toast_msg(msg: String) {
        toast(msg)
    }

    override fun getDataSuccess(bean: UserInfo) {
        iv_head.loadRoundImage(this, bean.header)
        tv_nick_name.text = bean.nickname

        if (bean.vip) {
            iv_vip.visibility = View.VISIBLE
        } else {
            iv_vip.visibility = View.GONE
        }
        tv_message.text = "消息 " + bean.msgs
        tv_rank.text = bean.level
        tv_duration.tv_duration.text = bean.duration
        tv_days.tv_days.text = bean.days
        BaseContext.instance.setUserInfo(bean)
        EventBus.getDefault().post(EventBusCenter<UserInfo>(Constants.Tag.UPDATE_USER_INFO, bean))

    }

    private var tabsTitle: ArrayList<String> = arrayListOf()
    private var pageAdapter: MineInfoFragmentAdapter? = null

    override fun getLayoutId(): Int = R.layout.fragment_mine

    override fun initViewsAndEvents() {


        view_pager.offscreenPageLimit = 4
        tabsTitle.add(resources.getString(R.string.information))
        tabsTitle.add(resources.getString(R.string.task))
        tabsTitle.add(resources.getString(R.string.file))
        tabsTitle.add(resources.getString(R.string.member))

        pageAdapter = MineInfoFragmentAdapter(this, supportFragmentManager, tabsTitle, "1")
        view_pager.adapter = pageAdapter
        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tablayout))
        tablayout.setupWithViewPager(view_pager)
        setUpTabBadge(tabsTitle)
        mPresenter?.getUserInfo()
        tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                setUpTabBadge(tabsTitle)
            }

        })
        iv_head.setOnClickListener(this)
        tv_message.setOnClickListener(this)
        ll_daily_punch.setOnClickListener(this)
        ll_exe_time.setOnClickListener(this)

    }

    override fun isRegistEventBus(): Boolean = true
    override fun onMessageEvent(eventBusCenter: EventBusCenter<Object>) {
        super.onMessageEvent(eventBusCenter)
        if (eventBusCenter != null) {
            if (eventBusCenter.evenCode == RELOAD_USERINFO)
                mPresenter?.getUserInfo()
        }
    }

    override fun isNeedLec(): View? = null
    /**
     * 更新tablayout itemview
     *
     */
    private fun setUpTabBadge(tabName: ArrayList<String>) {
        for (i in tabName.indices) {
            val tab = tablayout.getTabAt(i)

            // 更新Badge前,先remove原来的customView,否则Badge无法更新
            val customView = tab?.customView
            if (customView != null) {
                val parent = customView!!.parent
                if (parent != null) {
                    (parent as ViewGroup).removeView(customView)
                }
            }

            // 更新CustomView
            tab?.customView = pageAdapter?.getTabItemView(i, i == tablayout.selectedTabPosition)
        }
        // 需加上以下代码,不然会出现更新Tab角标后,选中的Tab字体颜色不是选中状态的颜色
        tablayout.getTabAt(tablayout.selectedTabPosition)?.customView?.isSelected = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ACTIVITY_REQUEST_SELECT_PHOTO && resultCode == Activity.RESULT_OK) {
            val hasChoosePath = Album.parseResult(data) as java.util.ArrayList<String>
            if (hasChoosePath != null) {
                localImageUrl = hasChoosePath[0]

                showLoading(false)
                UploadImageUtils.uploadImage(this@MineActivity, 2, localImageUrl!!, object : ProgressCallback {
                    override fun onProgressCallback(progress: Double) {
                        LogUtils.e("progress:" + progress)
                    }

                    override fun onProgressFailed() {
                        dismissLoading()
                        toast_msg("上传图片失败")
                    }

                    override fun onProgressSuccess() {
//                        uploadedImageurl = result?.requestId
//                        startUpload()
                        mPresenter?.getUserInfo()
                        runOnUiThread {
                            dismissLoading()
                        }
                    }

                })
            }

        }
    }
}