package com.sitong.changqin

import android.os.Bundle
import android.os.Environment
import android.support.design.widget.AppBarLayout
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.jyall.android.common.utils.LogUtils
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.extension.jump
import com.jyall.bbzf.extension.toast
import com.sevenstringedzithers.sitong.R
import com.sitong.changqin.mvp.contract.IndexContract
import com.sitong.changqin.mvp.model.bean.MusicBean
import com.sitong.changqin.mvp.persenter.IndexPresenter
import com.sitong.changqin.ui.activity.KnowledgeActivity
import com.sitong.changqin.ui.activity.MenuActivity
import com.sitong.changqin.ui.activity.MusicEnjoyActivity
import com.sitong.changqin.ui.activity.MusicPlayActivity
import com.sitong.changqin.ui.adapter.IndexAdapter
import com.sitong.changqin.ui.adapter.IndexRankAdapter
import com.sitong.changqin.ui.listerner.AppBarStateChangeListener
import com.sitong.changqin.ui.listerner.RVAdapterItemOnClick
import com.sitong.changqin.ui.listerner.ResultCallback
import com.sitong.changqin.utils.CollectionUtils
import com.sitong.changqin.utils.DownUtils.DownloadUtils
import com.sitong.changqin.utils.DownUtils.JsDownloadListener
import com.sitong.changqin.view.MusicDialog
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import java.io.InputStream
import java.util.*


class MainActivity : BaseActivity<IndexContract.View, IndexPresenter>(), IndexContract.View {

    private var mRVAdapter: IndexAdapter? = null
    private var mRankRVAdapter: IndexRankAdapter? = null
    private var lists = arrayListOf<MusicBean>()
    private var mKeys = arrayListOf<Int>()
    private var mValues = arrayListOf<Int>()
    private var dia: MusicDialog? = null

    override fun getDataSuccess(musicList: ArrayList<MusicBean>) {
//        toast_msg("" + musicList?.size)
        var beanMusic = MusicBean.Music(-1, false, "zhiqin", "知琴", false, "知琴", 1, 1, "")
        var list = arrayListOf<MusicBean.Music>()
        list.add(beanMusic)
        var beanOne = MusicBean("知琴", 1, list)
        musicList.add(0, beanOne)
        mRVAdapter?.setData(musicList)
        mRankRVAdapter?.setData(musicList)
        lists = musicList
        rv_rank.postDelayed({
            getValue()
//            controlLetters(true)
        }, 3000)


    }

    override fun getPresenter(): IndexPresenter = IndexPresenter()

    override fun getRootView(): IndexContract.View = this

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initViewsAndEvents() {
        setSupportActionBar(toolbar)
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
        mRVAdapter!!.setListerner(object : RVAdapterItemOnClick {
            override fun onItemClicked(data: Any) {
                var bean = data as MusicBean.Music

                if (bean.levelcode == -1) {
                    jump<KnowledgeActivity>()
                    return
                }

                dia = MusicDialog(this@MainActivity, resources.getString(R.string.enjoy), resources.getString(R.string.begin_experience), bean.name, bean.enName, bean.level, bean.iscollection).setRightTitleListerner(object : View.OnClickListener {
                    override fun onClick(p0: View?) {
                        var bundle = Bundle()
                        bundle.putString("id", "" + bean.id)
                        jump<MusicPlayActivity>(isAnimation = false, dataBundle = bundle)
//                        startLoad()
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

    }

    var mPosition = 1
    fun controlLetters(flag: Boolean) {
        try {
            //        var mKey = IntArray(2)
//        rv_rank.findViewHolderForLayoutPosition(mPosition)?.itemView?.getLocationOnScreen(mKey)


            if (mKeys.size <= 0 || mKeys[0] <= 0) {
                getValue()
                return
            }
            if (flag) {
                //上滑
                var mValue = IntArray(2)
                rv_list.findViewHolderForLayoutPosition(mPosition)?.itemView?.getLocationOnScreen(mValue)
                if (mKeys[mPosition] >= mValue[1]) {
//                mRVAdapter!!.setPosition(mPosition)
                    var hol = rv_list.findViewHolderForLayoutPosition(mPosition)?.itemView?.findViewById<TextView>(R.id.tv_rank)
                    hol?.visibility = View.VISIBLE
                    mRankRVAdapter!!.setPosition(mPosition)
                    if (mPosition < lists.size - 1) {
                        mPosition++
//                    LogUtils.e("++--mPosition:" + mPosition + "-----key:" + mKeys[mPosition] + "-----value:" + mValue[1])
                    }
                }

            } else {
//下滑
                var cache = 0
                if (mPosition > 0) {
                    cache = mPosition - 1
                } else {
                    cache = mPosition
                }
                var mValue = IntArray(2)
                rv_list.findViewHolderForLayoutPosition(cache)?.itemView?.getLocationOnScreen(mValue)
                if (mKeys[cache] <= mValue[1]) {
                    mRankRVAdapter!!.setPosition(cache - 1)
//                mRVAdapter!!.setPosition(cache - 1)
                    var hol = rv_list.findViewHolderForLayoutPosition(cache)?.itemView?.findViewById<TextView>(R.id.tv_rank)
                    hol?.visibility = View.INVISIBLE
                    mPosition = cache
//                LogUtils.e("----mPosition:" + mPosition + "-----key:" + mKeys[mPosition] + "-----value:" + mValue[1])
                }

            }
        } catch (ex: Exception) {

        }
    }

    fun getValue() {
//        mValues.clear()
        mKeys.clear()
        mRankRVAdapter?.list?.forEachIndexed { index, value ->
            var startLocation = IntArray(2)
            rv_rank.findViewHolderForLayoutPosition(index)?.itemView?.getLocationOnScreen(startLocation)
            mKeys.add(startLocation[1])
//            LogUtils.e("Stickysssssss:" + startLocation[0].toString() + "----" + startLocation[1].toString())
        }
//        mRVAdapter?.list?.forEachIndexed { index, value ->
//            var startLocation = IntArray(2)
//            rv_list.findViewHolderForLayoutPosition(index)?.itemView?.getLocationOnScreen(startLocation)
//            mValues.add(startLocation[1])
////            LogUtils.e("Stickysssssss:" + startLocation[0].toString() + "----" + startLocation[1].toString())
//        }
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null

    override fun toast_msg(msg: String) {
        toast(msg)
    }

    private fun startLoad() {
        val listener = object : JsDownloadListener {
            override fun onStartDownload() {
                toast_msg("开始下载")
            }

            override fun onProgress(progress: Int) {
                LogUtils.e("--------下载进度：" + progress);
            }

            override fun onFinishDownload() {
                LogUtils.e("--------下载完成：");
            }

            override fun onFail(errorInfo: String?) {
                LogUtils.e("--------下载失败：" + errorInfo);
            }
        }

        var base = "http://stsystem.oss-cn-beijing.aliyuncs.com"
        val downloadUtils = DownloadUtils(base, listener)
        var url = "/music/xianwengcao.mp3"
        var observab = object : Observer<InputStream> {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable?) {
            }

            override fun onNext(value: InputStream?) {
                listener.onFinishDownload()
            }

            override fun onError(e: Throwable?) {
                listener.onFail(e.toString())
            }
        }
        downloadUtils.download(url, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "a.mp3", observab)

    }
}
