package com.sevenstringedzithers.sitong.ui.activity

import android.app.Service
import android.media.AudioManager
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.extension.jump
import com.jyall.bbzf.extension.toast
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.mvp.contract.MusicPlayContract
import com.sevenstringedzithers.sitong.mvp.model.bean.MusicDetailBean
import com.sevenstringedzithers.sitong.mvp.model.bean.QinViewPointBean
import com.sevenstringedzithers.sitong.mvp.persenter.MusicPlayPresenter
import com.sevenstringedzithers.sitong.ui.adapter.MainAdapter
import com.sevenstringedzithers.sitong.ui.listerner.ProgressCallback
import com.sevenstringedzithers.sitong.utils.DownLoadUtils
import com.sevenstringedzithers.sitong.utils.ExtraUtils
import com.sevenstringedzithers.sitong.utils.files.DownLoadFilesUtils
import com.sevenstringedzithers.sitong.utils.files.FilesUtils
import com.sevenstringedzithers.sitong.view.MusicDownloadDialog
import com.smp.soundtouchandroid.OnProgressChangedListener
import com.smp.soundtouchandroid.SoundStreamAduioRecorder
import com.smp.soundtouchandroid.SoundStreamAudioPlayer
import com.smp.soundtouchandroid.SoundTouch
import com.xw.repo.BubbleSeekBar
import kotlinx.android.synthetic.main.activity_music_play.*
import kotlinx.android.synthetic.main.layout_play_title.*
import java.io.File




/**
 * create by chen.zhiwei on 2018-8-15
 */
class MusicPlayActivity : BaseActivity<MusicPlayContract.View, MusicPlayPresenter>(), MusicPlayContract.View, MainAdapter.Listener, View.OnClickListener {
    private var du: Long? = null
    private var soundTouchRec: SoundStreamAduioRecorder? = null
    private var soundTouch: SoundTouch? = null
    private var lastRecordFile:String?=null
    private var isRecording=false
    private var isLoaded = false
    private var isLoading = false
    private var isSlience: Boolean = false
    var mLoadDialog: MusicDownloadDialog? = null
    private var pointList: ArrayList<QinViewPointBean>? = null
    private var mMoveMap: HashMap<Int, Float> = hashMapOf()//在线上动态显示的点
    private var currentSort: Int? = null
    private var nextSort: Int? = null
    private var musicBean: MusicDetailBean? = null
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_play -> {
                if (musicBean != null && !musicBean!!.url.isNullOrEmpty()) {
                    checkFile(musicBean!!.url)
                } else {
                    toast_msg("获取视频失败")
                }
            }
            R.id.iv_load -> {
                if (isLoaded) {
                    toast_msg("已下载")
                    return
                }
                if (isLoading) {
                    return
                }
                if (musicBean == null) {
                    return
                }
                if (mLoadDialog == null) {
                    mLoadDialog = MusicDownloadDialog(this@MusicPlayActivity, resources.getString(R.string.download), resources.getString(R.string.cancel), musicBean!!.size.toString(), musicBean!!.icon)
                    mLoadDialog?.setLeftTitleListerner(View.OnClickListener {
                        isLoading = true
                        DownLoadUtils.downLoadMusic(this@MusicPlayActivity, musicBean!!.url, object : ProgressCallback {
                            override fun onProgressCallback(progress: Double) {
                                mLoadDialog?.getSeekBarLister()?.onProgressCallback(progress)
                            }

                            override fun onProgressFailed() {
//                                dismissLoading()
                                isLoading = false

                                runOnUiThread { toast_msg("获取签名失败") }
                            }

                            override fun onProgressSuccess() {
//                                dismissLoading()
                                runOnUiThread {
                                    toast_msg(
                                            "下载完成")
                                    mLoadDialog?.dismiss()
                                }


                                isLoading = false
                                chenckIsLoaded(musicBean!!.url)
                            }
                        })

                    })
                }
                mLoadDialog?.show()
            }
            R.id.iv_tool -> {
                jump<ToolActivity>(isAnimation = false)
            }
            R.id.iv_voice -> {
                isSlience = !isSlience
                setVolume(isSlience)
                setButtonState()
            }
            R.id.iv_record->{
                if (isRecording){
                    lastRecordFile=soundTouchRec?.stopRecord()
//                    弹窗
                }else{
                    soundTouchRec?.startRecord()
                }

            }
        }
    }

    private var player: SoundStreamAudioPlayer? = null
    private var f: File? = null
    private var tempo = 1.0f//这个是速度，1.0表示正常设置新的速度控制值，
    private var pitchSemi = 1.0f//这个是音调，1.0表示正常，
    private var rate = 1.0f//这个参数是变速又变声的，这个参数大于0，否则会报错


    override fun getDataSuccess(musicBean: MusicDetailBean) {
        this.musicBean = musicBean
        chenckIsLoaded(musicBean.url)
        adapter?.setList(musicBean.score)
        musicBean.score.forEachIndexed { index, score ->
            if (score.start_second?.size > 0 && score.end_second?.size > 0 && score.start_second[0] > 0) {
                var bean = QinViewPointBean(score.start_second[0], score.end_second[0], score.duration, score.percent, score.string)
                pointList?.add(bean)
            }
        }
    }

    override fun onClick(index: Int) {
    }

    override fun onLongClick(index: Int) {
    }

    override fun onSelectionChanged(count: Int) {
    }

    private var mThread: Thread? = null
    var id: String? = null
    private var adapter: MainAdapter? = null

    override fun getRootView(): MusicPlayContract.View = this

    override fun getLayoutId(): Int = R.layout.activity_music_play

    override fun initViewsAndEvents() {
        if (getVolum()){
            isSlience=true
        }

        var bundle = intent.extras
        if (bundle != null) {
            id = bundle.getString("id")
        }
        iv_play.setOnClickListener(this)
        iv_tool.setOnClickListener(this)
        iv_load.setOnClickListener(this)
        iv_voice.setOnClickListener(this)
        iv_record.setOnClickListener(this)
        pointList = arrayListOf()

        var map: HashMap<String, String>? = null
        map = hashMapOf()
        map.put("music", id!!)
        map.put("needdetail", "1")
        mPresenter?.getMusicDetail(map)
        init()
        seek_bar.onProgressChangedListener = object : BubbleSeekBar.OnProgressChangedListenerAdapter() {
            override fun onProgressChanged(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float, fromUser: Boolean) {
                super.onProgressChanged(bubbleSeekBar, progress, progressFloat, fromUser)
            }

            override fun getProgressOnFinally(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float, fromUser: Boolean) {
                super.getProgressOnFinally(bubbleSeekBar, progress, progressFloat, fromUser)

            }

            override fun getProgressOnActionUp(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float) {
                player?.seekTo((progressFloat.toDouble()/du!!),false)
            }
        }
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null

    override fun toast_msg(msg: String) {
        toast(msg)
    }


    override fun getPresenter(): MusicPlayPresenter = MusicPlayPresenter()

    private fun init() {
        soundTouch = SoundTouch(0, 2, 1, 2, 1f, 1f)
        soundTouchRec = SoundStreamAduioRecorder(this, soundTouch)


        setButtonState()
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.flexWrap = FlexWrap.WRAP
        layoutManager.justifyContent = JustifyContent.CENTER
        rv_list.layoutManager = layoutManager

        adapter = MainAdapter(this)
        rv_list.adapter = adapter

        var mPosX = 0f
        var mPosY = 0f
        var mCurPosX = 0f
        var mCurPosY = 0f
        ll_scroll.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.getAction()) {
                    MotionEvent.ACTION_DOWN -> {
                        mPosX = event.getX()
                        mPosY = event.getY()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        mCurPosX = event.getX()
                        mCurPosY = event.getY()
                    }
                    MotionEvent.ACTION_UP -> if (mCurPosY - mPosY > 0 && Math.abs(mCurPosY - mPosY) > 25) {
                        //向下滑動
                        cq_view.visibility = View.VISIBLE
                    } else if (mCurPosY - mPosY < 0 && Math.abs(mCurPosY - mPosY) > 25) {
                        //向上滑动
                        cq_view.visibility = View.GONE
                    }
                }
                return true

            }
        })
    }
/*检查文件本地是否有*/
    private fun chenckIsLoaded(url: String): Boolean {
        isLoaded = DownLoadFilesUtils.getInstance(this)!!.isExist(FilesUtils.getFileName(url))
        setButtonState()
        return isLoaded
    }

    /*
    * 初始化播放器
    * */
    private fun checkFile(url: String) {


        if (chenckIsLoaded(url)) {
            f = File(DownLoadFilesUtils.getInstance(this)!!.getCurrentUri() + "/" + FilesUtils.getFileName(url))
            initPlayer()
        } else {
//开始下载
            showLoading()
            DownLoadUtils.downLoadMusic(this@MusicPlayActivity, url, object : ProgressCallback {
                override fun onProgressCallback(progress: Double) {
                }

                override fun onProgressFailed() {
                    dismissLoading()
                    toast_msg("获取签名失败")
                }

                override fun onProgressSuccess() {
                    dismissLoading()
                    f = File(DownLoadFilesUtils.getInstance(this@MusicPlayActivity)!!.getCurrentUri() + "/" + FilesUtils.getFileName(url))
                    initPlayer()
                }
            })
        }
    }

    private fun initPlayer() {
        try {
            if (player == null) {
                player = SoundStreamAudioPlayer(0, f?.getPath(), tempo, pitchSemi)

                player?.setOnProgressChangedListener(object : OnProgressChangedListener {
                    override fun onProgressChanged(track: Int, currentPercentage: Double, position: Long) {
                        Log.e("onProgressChanged", "" + currentPercentage)
                        seek_bar.setProgress((currentPercentage * du!!).toFloat())

                        tv_start_time.setText(ExtraUtils.secToTime((currentPercentage * du!!).toInt()))
                        getPoints((currentPercentage * du!!).toFloat())
                        if (currentSort != nextSort) {
                            cq_view.setmMoveMap(mMoveMap)
                            currentSort = nextSort
                        }
                    }

                    override fun onTrackEnd(track: Int) {

                    }

                    override fun onExceptionThrown(string: String) {

                    }
                })
                player?.setRate(rate)
                Thread(player).start()
                player?.start()
                setButtonState()
                dismissLoading()

            } else {
                if (player!!.isPaused()) {
                    player?.start()
                } else {
                    player!!.pause()
                }
                setButtonState()
            }
//                    var du=player?.playedDuration!!.toFloat()/1000
            du = ExtraUtils.getMP3FileInfo(f?.absolutePath!!) / 1000
            seek_bar.getConfigBuilder().max(du!!.toFloat()).min(0f)
            tv_end_time.setText(ExtraUtils.secToTime((du!!).toInt()))
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }
/*获取琴上面的点*/
    private fun getPoints(currenttime: Float) {
        mMoveMap.clear()
        pointList?.forEachIndexed { index, qinViewPointBean ->
            if (qinViewPointBean.start_second <= currenttime && qinViewPointBean.end_second >= currenttime) {
                if (!qinViewPointBean.string.isNullOrEmpty()) {
                    if (!qinViewPointBean.string.contains("+")) {
                        mMoveMap.put(qinViewPointBean.string.toInt(), qinViewPointBean.percent.toFloat())
                    } else {
                        var ss = qinViewPointBean.string.split("+")
                        var pp = qinViewPointBean.percent.split("+")
                        ss.forEachIndexed { index, s ->
                            mMoveMap.put(s.toInt(), pp[index].toFloat())
                        }
                    }
                }
                nextSort = index
            }

        }
    }


    /*
    * 按钮状态*/
    private fun setButtonState() {
        if (player != null) {
            if (player!!.isPaused) {
                iv_play.setBackgroundResource(R.drawable.selector_play)
            } else {
                iv_play.setBackgroundResource(R.drawable.selector_pause)
            }
        }
        if (isLoaded) {
            iv_load.setImageResource(R.mipmap.ic_load_pressed)
        } else {
            iv_load.setImageResource(R.mipmap.ic_load_normal)
        }
        if (isSlience){
            iv_voice.setImageResource(R.mipmap.ic_voice_closed)
        }else{
            iv_voice.setImageResource(R.mipmap.ic_voice)
        }
    }

    /*设置是否静音*/
    fun setVolume(isSlience: Boolean) {
        val audioManager = getSystemService(Service.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, isSlience)
    }

    /*获取是否是静音*/
    fun getVolum(): Boolean {
        val audioManager = getSystemService(Service.AUDIO_SERVICE) as AudioManager
        var current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        return current <= 0
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.stop()
        player = null
    }
}