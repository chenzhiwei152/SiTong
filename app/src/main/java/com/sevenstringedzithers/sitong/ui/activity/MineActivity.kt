package com.sevenstringedzithers.sitong.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.jyall.android.common.utils.LogUtils
import com.jyall.android.common.utils.SharedPrefUtil
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.base.BaseContext
import com.jyall.bbzf.base.EventBusCenter
import com.jyall.bbzf.extension.jump
import com.jyall.bbzf.extension.loadRoundImage
import com.jyall.bbzf.extension.toast
import com.jyall.bbzf.ui.adapter.MineInfoFragmentAdapter
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.base.Constants
import com.sevenstringedzithers.sitong.base.Constants.Tag.RELOAD_USERINFO
import com.sevenstringedzithers.sitong.mvp.contract.MineContract
import com.sevenstringedzithers.sitong.mvp.model.bean.ExerciseRecordBean
import com.sevenstringedzithers.sitong.mvp.model.bean.UserInfo
import com.sevenstringedzithers.sitong.mvp.persenter.MinePresenter
import com.sevenstringedzithers.sitong.ui.adapter.ExerciseRecordTimeListAdapter
import com.sevenstringedzithers.sitong.ui.listerner.ProgressCallback
import com.sevenstringedzithers.sitong.utils.UploadImageUtils
import com.sevenstringedzithers.sitong.view.DailyPunchDialog
import com.sevenstringedzithers.sitong.view.ImageDialog
import com.yanzhenjie.album.Album
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.fragment_mine.view.*
import org.greenrobot.eventbus.EventBus


/**
 * create by chen.zhiwei on 2018-8-15
 */
class MineActivity : BaseActivity<MineContract.View, MinePresenter>(), MineContract.View, View.OnClickListener {
    override fun getExeRecordListSuccess(bean: ExerciseRecordBean) {
        if (mExeTimeAdapter == null) {
            mExeTimeAdapter = ExerciseRecordTimeListAdapter(this@MineActivity)
            rv_list.layoutManager = LinearLayoutManager(this@MineActivity)
            rv_list.adapter = mExeTimeAdapter

        }
        mExeTimeAdapter?.clearData()
        mExeTimeAdapter?.setData(bean.date)

    }

    override fun punchSuccess(msg: String) {
        mPresenter?.getUserInfo()
    }

    private var mExeTimeAdapter: ExerciseRecordTimeListAdapter? = null
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
                        mPresenter?.getExeRecordList()
                        rv_list.visibility = View.VISIBLE
                        ll_exe_title.visibility = View.VISIBLE
                        tablayout.visibility = View.GONE
                        view_pager.visibility = View.GONE
                    }


                }).setRightTitleListerner(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        rv_list.visibility = View.GONE
                        ll_exe_title.visibility = View.GONE
                        tablayout.visibility = View.VISIBLE
                        view_pager.visibility = View.VISIBLE
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
//        tv_rank.text = bean.level
        val typeface1 = Typeface.createFromAsset(assets, "fonts/agaramondpro_regular.otf")
        tv_rank.setTypeface(typeface1)

//        val typeface = Typeface.createFromAsset(assets, "fonts/chinese.ttf")
//        tv_rank_1.setTypeface(typeface)
        tv_duration.tv_duration.text = bean.duration
        tv_days.tv_days.text = bean.days

//        同步本地，合并本地已有的数据
        bean.token = BaseContext.instance.getUserInfo()!!.token

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
        iv_menu.setOnClickListener {
            jump<MenuActivity>(isAnimation = false)
        }


        if (SharedPrefUtil.getBoolean(this, "isShowedInformation", false)) {
            setInformationSete(false)
        } else {
            setInformationSete(true)
        }

    }

    fun setInformationSete(isShow: Boolean) {
        if (isShow) {
            rl_information.visibility = View.VISIBLE

            tv_close_info.setOnClickListener {
                setInformationSete(false)
            }
            var mPosY = 0f
            var mCurPosY = 0f
            rl_information.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.getAction()) {
                        MotionEvent.ACTION_DOWN -> {
                            mPosY = event.getY()
                        }
                        MotionEvent.ACTION_MOVE -> {
                            mCurPosY = event.getY()
                        }
                        MotionEvent.ACTION_UP -> if (mCurPosY - mPosY > 0 && Math.abs(mCurPosY - mPosY) > 25) {
                            //向下滑動
                            setInformationSete(false)
                        } else if (mCurPosY - mPosY < 0 && Math.abs(mCurPosY - mPosY) > 25) {
                            //向上滑动
                            setInformationSete(false)
                        }
                    }
                    return true

                }
            })
        } else {
            val out = AnimationUtils.loadAnimation(this@MineActivity, R.anim.animation_menu_up_out)
            rl_information.animation = out
            rl_information.startAnimation(out)
            out.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationEnd(p0: Animation?) {
                    rl_information.visibility = View.GONE
                }

                override fun onAnimationStart(p0: Animation?) {
                }

                override fun onAnimationRepeat(p0: Animation?) {
                }

            })
            SharedPrefUtil.saveBoolean(this@MineActivity, "isShowedInformation", true)
        }
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