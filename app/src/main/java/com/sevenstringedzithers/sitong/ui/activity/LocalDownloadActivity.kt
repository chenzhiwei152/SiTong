package com.sevenstringedzithers.sitong.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import com.jyall.android.common.utils.SharedPrefUtil
import com.sevenstringedzithers.sitong.base.BaseActivity
import com.sevenstringedzithers.sitong.base.BasePresenter
import com.sevenstringedzithers.sitong.base.IBaseView
import com.jyall.bbzf.extension.jump
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.base.Constants
import com.sevenstringedzithers.sitong.mvp.model.bean.MusicBean
import com.sevenstringedzithers.sitong.ui.adapter.CollectionListAdapter
import com.sevenstringedzithers.sitong.ui.listerner.RVAdapterItemOnClick
import com.sevenstringedzithers.sitong.utils.files.DownLoadFilesUtils
import com.yanzhenjie.recyclerview.swipe.SwipeMenu
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener
import kotlinx.android.synthetic.main.activity_exercise_record.*
import kotlinx.android.synthetic.main.layout_common_title.*


/*
* 本地下载的曲目
* */
class LocalDownloadActivity : BaseActivity<IBaseView, BasePresenter<IBaseView>>(), IBaseView {
    private var uiHandler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg?.what) {
                1 -> {
                    mAdapter?.setData(loacalFileInfo!!)
                }
                2 -> {
                    initData()
                }
            }
        }
    }

    private fun initData() {
        if (localFileName != null) {
            musicList?.forEachIndexed { index, musicBean ->
                musicBean.musics.forEachIndexed { index, music ->
                    localFileName?.forEach {
                        if (music != null && !music.enName.isNullOrEmpty())
                            if (it.contains(music.enName)) {
                                loacalFileInfo?.add(music)
                            }
                    }

                }
            }
            uiHandler.sendEmptyMessage(1)
        }
    }

    private var mAdapter: CollectionListAdapter? = null
    private var musicList: ArrayList<MusicBean>? = null
    private var localFileName: ArrayList<String>? = null
    private var loacalFileInfo: ArrayList<MusicBean.Music>? = null
    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()

    override fun getRootView(): IBaseView = this

    override fun getLayoutId(): Int = R.layout.activity_exercise_record

    override fun initViewsAndEvents() {
        initTitle()
        localFileName = arrayListOf()
        loacalFileInfo = arrayListOf()
        iv_menu.setOnClickListener {
            jump<MenuActivity>(isAnimation = false)
        }

        mAdapter = CollectionListAdapter(this)
        rv_list.layoutManager = LinearLayoutManager(this)


// 创建菜单：
        var mSwipeMenuCreator = object : SwipeMenuCreator {
            override fun onCreateMenu(swipeLeftMenu: SwipeMenu?, swipeRightMenu: SwipeMenu?, viewType: Int) {
                val width = resources.getDimensionPixelSize(R.dimen.dp_90)

                // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
                // 2. 指定具体的高，比如80;
                // 3. WRAP_CONTENT，自身高度，不推荐;
                val height = ViewGroup.LayoutParams.MATCH_PARENT
                var deleteItem = SwipeMenuItem(this@LocalDownloadActivity).setText("删除").setWidth(width).setHeight(height).setTextColor(Color.WHITE).setBackgroundColor(resources.getColor(R.color.color_d0a670))
                // 各种文字和图标属性设置。
                swipeRightMenu?.addMenuItem(deleteItem) // 在Item右侧添加一个菜单。
            }

        }
        // 设置监听器。
        rv_list.setSwipeMenuCreator(mSwipeMenuCreator)


        val mMenuItemClickListener = SwipeMenuItemClickListener { menuBridge ->
            // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
            menuBridge.closeMenu()

            val direction = menuBridge.direction // 左侧还是右侧菜单。
            val adapterPosition = menuBridge.adapterPosition // RecyclerView的Item的position。
            val menuPosition = menuBridge.position // 菜单在RecyclerView的Item中的Position。
            DownLoadFilesUtils.getInstance()?.deletedFile(loacalFileInfo?.get(adapterPosition)?.enName + ".mp3")
            mAdapter?.removeItem(adapterPosition)
//            loacalFileInfo?.removeAt(adapterPosition)
        }
        // 菜单点击监听。
        rv_list.setSwipeMenuItemClickListener(mMenuItemClickListener)
        rv_list.adapter = mAdapter
        mAdapter?.setListerner(object : RVAdapterItemOnClick {
            override fun onItemClicked(data: Any) {
                var bean = data as MusicBean.Music
                if (bean.isbuy) {
                    var bund = Bundle()
                    bund.putString("id", "" + bean?.id)
                    jump<MemberListActivity>(dataBundle = bund)
                } else {
                    var bundle = Bundle()
                    bundle.putString("id", "" + bean.id)
                    jump<MusicPlayActivity>(isAnimation = false, dataBundle = bundle)
                }

            }

        })
        kotlin.run {
            musicList = SharedPrefUtil.getObj(this@LocalDownloadActivity, Constants.musicList) as ArrayList<MusicBean>?
            localFileName = DownLoadFilesUtils.getInstance()?.getFilesByPath("")

            rv_list.postDelayed({
                uiHandler.sendEmptyMessage(2)
            }, 1000)
        }
    }


    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null
    private fun initTitle() {
        iv_back.setOnClickListener { finish() }
        tv_title.setText("下载的乐谱")
        iv_menu.visibility = View.GONE
    }
}