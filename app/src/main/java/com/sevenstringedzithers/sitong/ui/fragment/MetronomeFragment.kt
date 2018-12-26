package com.sevenstringedzithers.sitong.ui.fragment

import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.view.View
import com.jyall.bbzf.base.BaseFragment
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.base.IBaseView
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.view.pickview.ScrollPickerView
import kotlinx.android.synthetic.main.fragment_metronome.*
import java.io.IOException


/**
 * 节拍器
 * create by chen.zhiwei on 2018-8-15
 */
class MetronomeFragment : BaseFragment<IBaseView, BasePresenter<IBaseView>>(), IBaseView {
    override fun getLayoutId(): Int = R.layout.fragment_metronome
    private var list_1 = arrayListOf<String>("7/8", "6/8", "5/8", "4/8", "3/8", "2/8", "1/8")
    private var list_2 = arrayListOf<String>("80", "70", "60", "50", "40", "30")
    var mPlayer: MediaPlayer? = null
    var afd: AssetFileDescriptor? = null
    var mThread: Thread? = null
    private var count = 0
    private var nn = 4
    private var currentCount = 0
    private var playTime = 0
    override fun lazyLoad() {
    }

    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()
    override fun getRootView(): IBaseView = this

    override fun initViewsAndEvents() {

        picker_01.data = list_1 as List<CharSequence>?
        picker_02.data = list_2 as List<CharSequence>?

        picker_01.setOnSelectedListener(object : ScrollPickerView.OnSelectedListener {
            override fun onSelected(scrollPickerView: ScrollPickerView<*>?, position: Int) {
                nn = list_1.get(position).split("/").get(0).toInt()
                iv_points.selectedNum = -1
                iv_points.setNums(nn)
                currentCount = 0
            }

        })
        picker_02.setOnSelectedListener(object : ScrollPickerView.OnSelectedListener {
            override fun onSelected(scrollPickerView: ScrollPickerView<*>?, position: Int) {
                playTime = list_2.get(position).toInt()
                currentCount = 0
            }

        })

        iv_play.setOnClickListener {
            if (mThread == null) {
                mThread = Thread(Runnable() {
                    //创建一个新线程
                    try {
                        while (!isStopThread) {
                            try {
                                if (isPlaying){
                                    activity?.runOnUiThread { iv_points.selectedNum = currentCount }
                                    initPlayer()
                                }
                                Thread.sleep(1000)
                                if (currentCount < nn - 1) {
                                    currentCount++
                                } else {
                                    currentCount = 0
                                }
                            } catch (e: Exception) {
                            }
                        }
                    } catch (e: java.lang.Exception) {

                    }

                })
                mThread?.start()
            } else {
                isPlaying = !isPlaying
                currentCount = 0
            }


        }
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null

    companion object {
        fun newInstance(): MetronomeFragment {
            return MetronomeFragment()
        }
    }

    private fun initPlayer() {
        try {
            if (mPlayer == null) {
                mPlayer = MediaPlayer()
                // 打开指定音乐文件,获取assets目录下指定文件的AssetFileDescriptor对象
                afd = activity?.getAssets()?.openFd("dida.mp3")
                mPlayer?.reset()
// 使用MediaPlayer加载指定的声音文件。
                mPlayer?.setDataSource(afd?.getFileDescriptor(),
                        afd?.getStartOffset()!!, afd?.getLength()!!)
// 准备声音
                mPlayer?.prepare()
            }
// 播放
            mPlayer?.start()
        } catch (e: IOException) {

        } finally {
            afd?.close()
        }

    }

    private var isPlaying = true
    private var isStopThread = false


    override fun onDestroy() {
        super.onDestroy()
        if (mThread != null) {
            isPlaying = false
            isStopThread = true
            mThread?.interrupt()
            mThread = null
        }
    }
}