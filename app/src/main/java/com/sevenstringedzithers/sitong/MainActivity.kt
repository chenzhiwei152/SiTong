package com.sevenstringedzithers.sitong

import android.Manifest
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.jyall.android.common.utils.LogUtils
import com.jyall.android.common.utils.SharedPrefUtil
import com.jyall.app.home.utils.ApkUpdateManager
import com.jyall.bbzf.extension.jump
import com.jyall.bbzf.extension.toast
import com.sevenstringedzithers.sitong.base.BaseActivity
import com.sevenstringedzithers.sitong.base.Constants
import com.sevenstringedzithers.sitong.base.EventBusCenter
import com.sevenstringedzithers.sitong.mvp.contract.IndexContract
import com.sevenstringedzithers.sitong.mvp.model.bean.MusicBean
import com.sevenstringedzithers.sitong.mvp.persenter.IndexPresenter
import com.sevenstringedzithers.sitong.ui.activity.*
import com.sevenstringedzithers.sitong.ui.adapter.IndexAdapter
import com.sevenstringedzithers.sitong.ui.adapter.IndexLetterLinkdapter
import com.sevenstringedzithers.sitong.ui.adapter.IndexRankAdapter
import com.sevenstringedzithers.sitong.ui.listerner.AppBarStateChangeListener
import com.sevenstringedzithers.sitong.ui.listerner.RVAdapterItemOnClick
import com.sevenstringedzithers.sitong.ui.listerner.ResultCallback
import com.sevenstringedzithers.sitong.utils.CollectionUtils
import com.sevenstringedzithers.sitong.utils.DownUtils.permission.JsPermissionUtils
import com.sevenstringedzithers.sitong.utils.TypefaceUtil
import com.sevenstringedzithers.sitong.view.MusicDialog
import com.sevenstringedzithers.sitong.view.MusicPayDialog
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity<IndexContract.View, IndexPresenter>(), IndexContract.View {

    private var mRVAdapter: IndexAdapter? = null
    private var mRankRVAdapter: IndexRankAdapter? = null
    private var mLetterRVadapter: IndexLetterLinkdapter? = null
    private var lists = arrayListOf<MusicBean>()
    private var mKeys = arrayListOf<Int>()
    private var mValues = arrayListOf<Int>()
    private var dia: MusicDialog? = null

    override fun getDataSuccess(musicList: ArrayList<MusicBean>) {
//        toast_msg("" + musicList?.size)
//        kotlin.run {
        SharedPrefUtil.saveObj(this@MainActivity, Constants.musicList, musicList)
//        }
        var beanMusic = MusicBean.Music(-1, false, "zhiqin", "知琴", false, "知琴", 1, 1, "", false, "", arrayListOf())
        var list = arrayListOf<MusicBean.Music>()
        list.add(beanMusic)
        var beanOne = MusicBean("知琴", 1, list)
        musicList.add(0, beanOne)
        lists = musicList

        mRVAdapter?.setData(musicList)
        mRankRVAdapter?.setData(musicList)
        if (SharedPrefUtil.getObj(this@MainActivity, "mKeys") != null) {
            rv_rank.post {
                getValue()
            }
        } else {
            rv_rank.postDelayed({
                getValue()
            }, 3000)
        }

    }

    override fun getPresenter(): IndexPresenter = IndexPresenter()

    override fun getRootView(): IndexContract.View = this

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initViewsAndEvents() {
        if (JsPermissionUtils.needRequestPermission()) {
            JsPermissionUtils.getInstance().requestPermission(this, 100, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        }
        setSupportActionBar(toolbar)
        val typeface1 = TypefaceUtil.createagaTypeface(this)
        ct_layout.setCollapsedTitleTypeface(typeface1)
        ct_layout.setExpandedTitleTypeface(typeface1)
        iv_menu.setOnClickListener {
            jump<MenuActivity>(isAnimation = false)
        }
        appBar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: AppBarStateChangeListener.State) {
                if (state === AppBarStateChangeListener.State.EXPANDED) {

                    //展开状态
                    tv_subtitle.visibility = View.VISIBLE
                } else if (state === AppBarStateChangeListener.State.COLLAPSED) {
                    //折叠状态
                    tv_subtitle.visibility = View.GONE
                } else {
                    //中间状态
                    tv_subtitle.visibility = View.GONE
                }
            }
        })
        mRVAdapter = IndexAdapter(this)

        rv_list.isNestedScrollingEnabled = false
        rv_list.layoutManager = LinearLayoutManager(this)
        rv_list.adapter = mRVAdapter
        mRVAdapter?.setLetterOnclick(object : RVAdapterItemOnClick {
            override fun onItemClicked(data: Any) {
                var position = data as Int
                LogUtils.e("mPosition:" + position)
                stick_scroll.scrollBy(0, position)

            }

        })
        mRVAdapter!!.setListerner(object : RVAdapterItemOnClick {
            override fun onItemClicked(data: Any) {
                var bean = data as MusicBean.Music
                if (bean.levelcode == -1) {
                    jump<KnowledgeActivity>()
                    return
                }
                if (bean.onshelf.equals("0")) {
                    toast_msg("敬请期待")
                    return
                }
                dia = MusicDialog(this@MainActivity, resources.getString(R.string.enjoy), resources.getString(R.string.begin_experience), bean.name, bean.enName, bean.level, bean.iscollection).setRightTitleListerner(object : View.OnClickListener {
                    override fun onClick(p0: View?) {
                        if (!bean.isbuy) {
//                            需要付费的
                            dia?.dismiss()
                            var musicPayDialog = MusicPayDialog(this@MainActivity, "取消", "购买", bean.name, bean.enName).setRightTitleListerner(object : View.OnClickListener {
                                override fun onClick(p0: View?) {
//                                    跳支付
                                    var bund = Bundle()
                                    bund.putString("id", "" + bean?.id)
                                    jump<MemberListActivity>(dataBundle = bund)
                                }
                            })
                            musicPayDialog.show()

                        } else {
                            if (bean.onshelf == 1) {
                                var bundle = Bundle()
                                bundle.putString("id", "" + bean.id)
                                jump<MusicPlayActivity>(isAnimation = false, dataBundle = bundle)
                            } else {
                                toast_msg("该曲目未上架")
                            }

                        }

                    }

                }).setLeftTitleListerner(object : View.OnClickListener {
                    override fun onClick(p0: View?) {
                        var bundle = Bundle()
                        bundle.putString("id", "" + bean.id)
                        jump<MusicEnjoyActivity>(isAnimation = false, dataBundle = bundle)
                    }

                }).setColletionListerner(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        var type = 1
                        if (bean.iscollection) {
                            type = 0
                        }
                        CollectionUtils.collectionUtils(type, "" + bean!!.id, object : ResultCallback<String> {
                            override fun onSuccess(result: String?) {
                                bean.iscollection = type != 0
                                dia?.setCollection(bean.iscollection)
                                toast_msg(result!!)
                            }

                            override fun onsFailed(reason: String?) {
                                toast_msg(reason!!)
                            }
                        })
                    }
                })
                dia?.show()
            }

        })
        mRankRVAdapter = IndexRankAdapter(this)
        rv_rank.layoutManager = LinearLayoutManager(this)
        rv_rank.adapter = mRankRVAdapter

        mPresenter?.getMusicList("1")

        stick_scroll.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
                controlLetters(scrollY > oldScrollY)
            }

        })
        appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                controlLetters(true)
                controlLetters(false)
            }

        })
        ApkUpdateManager.instance.checkVersion(this, false)
    }

    var mPosition = 1
    fun controlLetters(flag: Boolean) {
        try {
            if (mKeys.size <= 0 || mKeys[0] <= 0) {
//                getValue()
                return
            }
            if (flag) {
//                LogUtils.e("AAAAAAAAA上滑:mPosition----" + mPosition)
                //上滑
                var mValue = IntArray(2)
                rv_list.findViewHolderForLayoutPosition(mPosition)?.itemView?.getLocationOnScreen(mValue)
                if (mKeys[mPosition] >= mValue[1]) {
                    var hol = rv_list.findViewHolderForLayoutPosition(mPosition)?.itemView?.findViewById<TextView>(R.id.tv_rank)
                    hol?.visibility = View.VISIBLE

                    var rank = rv_rank.findViewHolderForLayoutPosition(mPosition)?.itemView?.findViewById<TextView>(R.id.tv_rank_tag)
                    rank?.visibility = View.INVISIBLE
                    mPosition++
                    if (mPosition >= lists.size) {
//                        mPosition=lists.size-1
//                    LogUtils.e("++--mPosition:" + mPosition + "-----key:" + mKeys[mPosition] + "-----value:" + mValue[1])
                    }
                }

            } else {
//下滑
//                LogUtils.e("AAAAAAAAA下滑:mPosition----" + mPosition)
                var cache = 0
                if (mPosition > 0) {
                    cache = mPosition - 1
                } else {
                    cache = mPosition
                }
                var mValue = IntArray(2)
                rv_list.findViewHolderForLayoutPosition(cache)?.itemView?.getLocationOnScreen(mValue)
                if (mKeys[cache] <= mValue[1]) {
                    var hol = rv_list.findViewHolderForLayoutPosition(cache)?.itemView?.findViewById<TextView>(R.id.tv_rank)
                    hol?.visibility = View.INVISIBLE

                    var rank = rv_rank.findViewHolderForLayoutPosition(cache)?.itemView?.findViewById<TextView>(R.id.tv_rank_tag)
                    rank?.visibility = View.VISIBLE
                    mPosition = cache
//                LogUtils.e("----mPosition:" + mPosition + "-----key:" + mKeys[mPosition] + "-----value:" + mValue[1])
                }

            }
        } catch (ex: Exception) {

        }
    }

    fun getValue() {
        var mkeysNum = SharedPrefUtil.getInt(this, "mKeysNum", 0)
        if (SharedPrefUtil.getObj(this, "mKeys") != null) {
            mKeys = SharedPrefUtil.getObj(this, "mKeys") as ArrayList<Int>
            if (mKeys.size > 0 && mKeys[0] <= 0) {
                SharedPrefUtil.remove(this, "mKeys")
            }
        }
        if (mKeys?.size <= 0 || mkeysNum != mRankRVAdapter?.list?.size) {
            mRankRVAdapter?.list?.forEachIndexed { index, value ->
                var startLocation = IntArray(2)
                rv_rank.findViewHolderForLayoutPosition(index)?.itemView?.getLocationOnScreen(startLocation)
                mKeys.add(startLocation[1])
//            LogUtils.e("Stickysssssss:" + startLocation[0].toString() + "----" + startLocation[1].toString())
            }
            if (mKeys.size > 0 && mKeys[0] > 0) {
                SharedPrefUtil.saveObj(this, "mKeys", mKeys)
                SharedPrefUtil.saveInt(this, "mKeysNum", mKeys.size)
            }
        }
        controlLetters(false)
    }

    override fun isRegistEventBus(): Boolean = true
    override fun onMessageEvent(eventBusCenter: EventBusCenter<Object>) {
        super.onMessageEvent(eventBusCenter)
        if (eventBusCenter != null) {
            if (eventBusCenter.evenCode == Constants.Tag.RELOAD_USERINFO) {
                mPresenter?.getMusicList("1")
            }
        }
    }

    override fun isNeedLec(): View? = null

    override fun toast_msg(msg: String) {
        toast(msg)
    }
}
