package com.sevenstringedzithers.sitong.ui.activity

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.base.IBaseView
import com.jyall.bbzf.extension.jump
import com.jyall.bbzf.extension.toast
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.mvp.model.bean.FileInfo
import com.sevenstringedzithers.sitong.ui.adapter.RecordListAdapter
import com.sevenstringedzithers.sitong.ui.listerner.RVAdapterItemOnClick
import com.sevenstringedzithers.sitong.utils.ExtraUtils
import com.sevenstringedzithers.sitong.utils.files.RecordFilesUtils
import com.sevenstringedzithers.sitong.view.ShareDialog
import com.smp.soundtouchandroid.OnProgressChangedListener
import com.smp.soundtouchandroid.SoundStreamAudioPlayer
import com.tencent.connect.common.Constants
import com.tencent.tauth.Tencent
import com.xw.repo.BubbleSeekBar
import kotlinx.android.synthetic.main.activity_exercise_record.*
import kotlinx.android.synthetic.main.layout_common_title.*
import java.io.File


/*
* 本地的录音
* */
class LocalRecordActivity : BaseActivity<IBaseView, BasePresenter<IBaseView>>(), IBaseView {

    private var mAdapter: RecordListAdapter? = null
    private var localFileName: ArrayList<FileInfo>? = null
    private var dialog: ShareDialog? = null
    //    private var loacalFileInfo: ArrayList<MusicBean.Music>? = null
    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()

    override fun getRootView(): IBaseView = this

    override fun getLayoutId(): Int = R.layout.activity_exercise_record

    override fun initViewsAndEvents() {
        initTitle()
        localFileName = arrayListOf()
        iv_menu.setOnClickListener {
            jump<MenuActivity>(isAnimation = false)
        }

        mAdapter = RecordListAdapter(this)
        rv_list.layoutManager = LinearLayoutManager(this)
        rv_list.adapter = mAdapter
        mAdapter?.setShareListerne(object : RVAdapterItemOnClick {
            override fun onItemClicked(data: Any) {
                var list = arrayListOf<String>()
                dialog = ShareDialog(this@LocalRecordActivity, "Sitong古琴分享", "我在SiTong古琴录制了一首我自己弹的古琴曲，与大家分享！",com.sevenstringedzithers.sitong.base.Constants.SHARE_URL, list,1)
                dialog?.setShareCallback(object : RVAdapterItemOnClick {
                    override fun onItemClicked(data: Any) {
                        toast(data as String)
                    }

                })
                dialog?.show()
            }

        })
        localFileName = RecordFilesUtils.getInstance()?.getFilesInfoByPath("")
        mAdapter?.setData(localFileName!!)

        mAdapter?.setPlayMusic(object : RecordListAdapter.onPlayListerner {
            override fun onItemClicked(pos: Int, bubble: BubbleSeekBar, data: String) {
                if (!currentUrl.equals(data)) {
                    player?.onStop()

                    setButtonState(pos, false)
                    currentPos = pos
                    currentUrl = data
                    player = null
                }
                initFile(pos, bubble, data)
            }


        })


    }

    private fun initFile(pos: Int, bubble: BubbleSeekBar, url: String) {
        f = File(url)
        initPlayer(pos, bubble)
    }

    private var player: SoundStreamAudioPlayer? = null
    private var f: File? = null
    private var tempo = 1.0f//这个是速度，1.0表示正常设置新的速度控制值，
    private var pitchSemi = 1.0f//这个是音调，1.0表示正常，
    private var rate = 1.0f//这个参数是变速又变声的，这个参数大于0，否则会报错
    private var du: Long? = null
    private var currentUrl: String? = null
    private var currentPos: Int? = null
    private fun initPlayer(pos: Int, bubble: BubbleSeekBar) {
        try {
            if (player == null) {
                player = SoundStreamAudioPlayer(0, f?.getPath(), tempo, pitchSemi)

                player?.setOnProgressChangedListener(object : OnProgressChangedListener {
                    override fun onProgressChanged(track: Int, currentPercentage: Double, position: Long) {
                        Log.e("onProgressChanged", "" + currentPercentage)

                        rv_list?.findViewHolderForLayoutPosition(pos)?.itemView?.findViewById<TextView>(R.id.tv_start)?.text = ExtraUtils.secToTime((currentPercentage * du!!).toInt())
                        rv_list?.findViewHolderForLayoutPosition(pos)?.itemView?.findViewById<BubbleSeekBar>(R.id.seek_bar)?.setProgress((currentPercentage * du!!).toFloat())
                    }

                    override fun onTrackEnd(track: Int) {

                    }

                    override fun onExceptionThrown(string: String) {

                    }
                })
                player?.setRate(rate)
                Thread(player).start()
                player?.start()
                setButtonState(pos, !player!!.isPaused)
                dismissLoading()

            } else {
                if (player!!.isPaused()) {
                    player?.start()
                } else {
                    player!!.pause()
                }
//                setButtonState()
                setButtonState(pos, !player!!.isPaused)
            }

            du = ExtraUtils.getMP3FileInfo(f?.absolutePath!!) / 1000
            rv_list?.findViewHolderForLayoutPosition(pos)?.itemView?.findViewById<BubbleSeekBar>(R.id.seek_bar)?.configBuilder?.max(du!!.toFloat())?.min(0f)
            rv_list?.findViewHolderForLayoutPosition(pos)?.itemView?.findViewById<TextView>(R.id.tv_end)?.text = ExtraUtils.secToTime((du!!).toInt())
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setButtonState(pos: Int, isPlay: Boolean) {
        if (isPlay) {
            rv_list?.findViewHolderForLayoutPosition(pos)?.itemView?.findViewById<ImageView>(R.id.iv_more)?.setBackgroundResource(R.drawable.selector_pause)
        } else {
            rv_list?.findViewHolderForLayoutPosition(pos)?.itemView?.findViewById<ImageView>(R.id.iv_more)?.setBackgroundResource(R.drawable.selector_play)

        }
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null
    private fun initTitle() {
        iv_back.setOnClickListener { finish() }
        tv_title.text = "我的录音"
        iv_menu.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Tencent.onActivityResultData(requestCode, resultCode, data, dialog)
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_QQ_SHARE || resultCode == Constants.REQUEST_QZONE_SHARE || resultCode == Constants.REQUEST_OLD_SHARE) {
                Tencent.handleResultData(data, dialog)
            }
        }
    }
}