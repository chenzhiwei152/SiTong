package com.sevenstringedzithers.sitong.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.extension.jump
import com.jyall.bbzf.extension.toast
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.mvp.contract.IndexContract
import com.sevenstringedzithers.sitong.mvp.model.bean.MusicBean
import com.sevenstringedzithers.sitong.mvp.persenter.IndexPresenter
import com.sevenstringedzithers.sitong.ui.adapter.CollectionListAdapter
import com.sevenstringedzithers.sitong.ui.listerner.RVAdapterItemOnClick
import com.sevenstringedzithers.sitong.ui.listerner.ResultCallback
import com.sevenstringedzithers.sitong.utils.CollectionUtils
import com.yanzhenjie.recyclerview.swipe.SwipeMenu
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener
import kotlinx.android.synthetic.main.activity_exercise_record.*
import kotlinx.android.synthetic.main.layout_common_title.*


/*
* 收藏列表
* */
class CollectionActivity : BaseActivity<IndexContract.View, IndexPresenter>(), IndexContract.View {
    private var mAdapter: CollectionListAdapter? = null
    private var lists = arrayListOf<MusicBean.Music>()
    override fun getPresenter(): IndexPresenter = IndexPresenter()

    override fun getRootView(): IndexContract.View = this

    override fun toast_msg(msg: String) {
        toast(msg)
    }

    override fun getDataSuccess(musicList: ArrayList<MusicBean>) {
        lists.clear()
        musicList.forEach {
            it.musics.forEach {
                lists.add(it)
            }
        }
        mAdapter?.setData(lists)

    }

    override fun getLayoutId(): Int = R.layout.activity_exercise_record

    override fun initViewsAndEvents() {
        initTitle()



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
                var deleteItem = SwipeMenuItem(this@CollectionActivity).setText("删除").setWidth(width).setHeight(height).setTextColor(Color.WHITE).setBackgroundColor(resources.getColor(R.color.color_d0a670))
                // 各种文字和图标属性设置。
                swipeRightMenu?.addMenuItem(deleteItem) // 在Item左侧添加一个菜单。

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
            showLoading()
            CollectionUtils.collectionUtils(0, "" + lists[adapterPosition].id, object : ResultCallback<String> {
                override fun onsFailed(reason: String?) {
                    toast_msg(reason!!)
                    dismissLoading()
                }

                override fun onSuccess(result: String?) {
                    dismissLoading()
                    toast_msg(result!!)
                    mPresenter?.getMusicList("2")
                }

            })
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
                    jump<MusicEnjoyActivity>(isAnimation = false, dataBundle = bundle)
                }

            }

        })
        mPresenter?.getMusicList("2")
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null
    private fun initTitle() {
        iv_back.setOnClickListener { finish() }
        tv_title.setText("我喜欢的乐谱")
        iv_menu.visibility = View.GONE
    }
}