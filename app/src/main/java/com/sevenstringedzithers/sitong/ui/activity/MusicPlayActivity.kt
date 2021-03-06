package com.sevenstringedzithers.sitong.ui.activity

import android.annotation.SuppressLint
import android.app.Service
import android.media.AudioManager
import android.os.Handler
import android.os.Message
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import cn.qssq666.voiceutil.record.RecordFactory
import cn.qssq666.voiceutil.record.RecordManagerI
import cn.qssq666.voiceutil.utils.MediaDirectoryUtils
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.jyall.android.common.utils.LogUtils
import com.jyall.bbzf.extension.jump
import com.jyall.bbzf.extension.toast
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.base.BaseActivity
import com.sevenstringedzithers.sitong.base.Constants
import com.sevenstringedzithers.sitong.base.EventBusCenter
import com.sevenstringedzithers.sitong.mvp.contract.MusicPlayContract
import com.sevenstringedzithers.sitong.mvp.model.bean.MusicDetailBean
import com.sevenstringedzithers.sitong.mvp.model.bean.QinViewPointBean
import com.sevenstringedzithers.sitong.mvp.persenter.MusicPlayPresenter
import com.sevenstringedzithers.sitong.ui.adapter.MainAdapter
import com.sevenstringedzithers.sitong.ui.listerner.ProgressCallback
import com.sevenstringedzithers.sitong.utils.DownLoadUtils
import com.sevenstringedzithers.sitong.utils.ExerciseRecordUploadUtils
import com.sevenstringedzithers.sitong.utils.ExtraUtils
import com.sevenstringedzithers.sitong.utils.files.DownLoadFilesUtils
import com.sevenstringedzithers.sitong.utils.files.FilesUtils
import com.sevenstringedzithers.sitong.utils.files.RecordFilesUtils
import com.sevenstringedzithers.sitong.view.MusicDownloadDialog
import com.sevenstringedzithers.sitong.view.MusicRecordDialog
import com.smp.soundtouchandroid.OnProgressChangedListener
import com.smp.soundtouchandroid.SoundStreamAduioRecorder
import com.smp.soundtouchandroid.SoundStreamAudioPlayer
import com.smp.soundtouchandroid.SoundTouch
import kotlinx.android.synthetic.main.activity_music_play.*
import kotlinx.android.synthetic.main.layout_play_title.*
import java.io.File
import java.io.IOException
import java.util.*


/**
 * create by chen.zhiwei on 2018-8-15
 */
class MusicPlayActivity : BaseActivity<MusicPlayContract.View, MusicPlayPresenter>(), MusicPlayContract.View, MainAdapter.Listener, View.OnClickListener {
    private var du: Long? = null
    private var recordManager: RecordManagerI? = null
    private var playThread: Thread? = null
    private var soundTouchRec: SoundStreamAduioRecorder? = null
    private var soundTouch: SoundTouch? = null
    private var lastRecordFile: String? = null
    private var lastRecordFileName: String? = null
    private var isLoaded = false
    private var isLoading = false
    private var isSlience: Boolean = false
    private var isABStyle: Boolean = false
    var mLoadDialog: MusicDownloadDialog? = null
    private var pointList: ArrayList<QinViewPointBean>? = null
    private var mMoveMap: HashMap<Int, Float> = hashMapOf()//在线上动态显示的点
    private var mEndMap: HashMap<Int, Float> = hashMapOf()//在线上动态显示的点的结束位置

    private var currentSort: Int? = null//上一个
    private var nextSort: Int? = null//当前的
    private var musicBean: MusicDetailBean? = null
    private var startTime: Double? = null
    private var endTime: Double? = null
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_play -> {
                if (musicBean != null && !musicBean!!.url.isNullOrEmpty()) {
                    runOnUiThread {
                        checkFile(musicBean!!.url)
                    }
                } else {
                    toast_msg("数据加载中，请稍后再试")
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
            R.id.iv_record -> {
                if (getRecordManager()?.isRecordIng) {
//                    lastRecordFile = soundTouchRec?.stopRecord()
                    getRecordManager().stopRecord()//否则停止
//                    弹窗

                    var mRecordDialog = MusicRecordDialog(this, "存储", "删除", ExtraUtils.secToTime((recordTime / 1000)))
                    mRecordDialog.setLeftTitleListerner(object : View.OnClickListener {
                        override fun onClick(v: View?) {
                            kotlin.run {
                                lastRecordFileName = mRecordDialog.getEdittext()

                                val tempCacheMp3FileName = MediaDirectoryUtils.getTempMp3FileName()
                                try {
                                    tempCacheMp3FileName.createNewFile()
                                    var ff = File(lastRecordFile)
                                    ff.renameTo(tempCacheMp3FileName)
                                    RecordFilesUtils.getInstance()?.deletedFile(lastRecordFile!!)
                                    lastRecordFileName = ""
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }
//                                FilesUtils.makePCMFileToWAVFile(lastRecordFile!!, RecordFilesUtils.getInstance()!!.getCurrentUri() + "/" + mRecordDialog.getEdittext() + ".wav", true)
                            }
                        }

                    })
                    mRecordDialog.setRightTitleListerner(object : View.OnClickListener {
                        override fun onClick(v: View?) {
                            var ff = File(lastRecordFile)
                            ff.delete()
//                            RecordFilesUtils.getInstance()?.deletedFile(lastRecordFile!!)
                        }

                    })
                    mRecordDialog.show()
                } else {
                    try {
                        getRecordManager().startRecordCreateFile(60)
                    } catch (e: IOException) {
                        getRecordManager().stopRecord()
                        e.printStackTrace()
                        Toast.makeText(this, "无法录制 $e", Toast.LENGTH_SHORT).show()
                    }
                }
                setButtonState()
            }
            R.id.iv_ab -> {
//                mMoveMap.clear()
//                mMoveMap.put(1, 0.3f)
//                cq_view.setmMoveMap(mMoveMap, false, true, 2.0, "0.7")
//                cq_view.startAnim()
//                return

                if (isABStyle) {
//                    取消ab句模式
                    adapter?.clearSelected()
                    iv_ab.setImageResource(R.mipmap.ic_ab_normal)
                } else {
                    iv_ab.setImageResource(R.mipmap.ic_ab_pressed)
//                    设置ab句模式
                    player?.pause()
                }
                isABStyle = !isABStyle
                setButtonState()
            }
            R.id.iv_learn -> {
                jump<MusicSearchActivity>()
            }
        }
    }

    private var player: SoundStreamAudioPlayer? = null
    private var f: String? = null
    private var tempo = 1.0f//这个是速度，1.0表示正常设置新的速度控制值，
    private var pitchSemi = 1.0f//这个是音调，1.0表示正常，
    private var rate = 1.0f//这个参数是变速又变声的，这个参数大于0，否则会报错

    private var mLinesMap = hashMapOf<Int, Array<Int>>()
    override fun getDataSuccess(musicBean: MusicDetailBean) {
        this.musicBean = musicBean
        iv_music_title.text = musicBean.name

        chenckIsLoaded(musicBean.url)
        if (musicBean?.score?.size > 40) {
            rv_list.setItemViewCacheSize(musicBean?.score?.size)
        } else {
            rv_list.setItemViewCacheSize(40)
        }
        adapter?.setList(musicBean.score)
        var line = 0
        var start = 0
        var end = 0
        mLinesMap.clear()
        musicBean.score.forEachIndexed { index, score ->
            if (score.start_second?.size > 0 && score.end_second?.size > 0 && score.start_second[0] > 0) {
                Collections.sort(score.start_second)
                Collections.sort(score.end_second)
                score.start_second.forEachIndexed { i, d ->
                    var bean = QinViewPointBean(index, d, score.end_second[i], score.duration, score.percent, score.string, score.toposition)
                    pointList?.add(bean)
                }
            }
            if (score.islinefeed == 1) {
                end = index
                mLinesMap[line] = arrayOf(start, end)
                line++
                start = end
            }
        }
//        LogUtils.e("Lines:"+mLinesMap.size)
    }

    //ab句 单选，多选
    override fun onClick(index: Int) {
        if (isABStyle) {
            adapter?.toggleSelected(index)
        }
    }

    override fun onLongClick(index: Int) {
        if (isABStyle) {
            adapter?.clearSelected()
            player?.pause()
            rv_list.setDragSelectActive(true, index)
        }
    }

    override fun onSelectionChanged(count: Int) {
    }

    private var mThread: Thread? = null
    var id: String? = null
    private var adapter: MainAdapter? = null

    override fun getRootView(): MusicPlayContract.View = this

    override fun getLayoutId(): Int = R.layout.activity_music_play

    override fun initViewsAndEvents() {
        initTitle()
        if (getVolum()) {
            isSlience = true
        }
//        rv_list.isNestedScrollingEnabled = false
        var bundle = intent.extras
        if (bundle != null) {
            id = bundle.getString("id")
        }
        iv_play.setOnClickListener(this)
        iv_tool.setOnClickListener(this)
        iv_load.setOnClickListener(this)
        iv_voice.setOnClickListener(this)
        iv_record.setOnClickListener(this)
        iv_ab.setOnClickListener(this)
        iv_learn.setOnClickListener(this)
        pointList = arrayListOf()

        var map: HashMap<String, String>? = null
        map = hashMapOf()
        map.put("music", id!!)
        map.put("needdetail", "1")
        mPresenter?.getMusicDetail(map)
        init()
//        seek_bar.onProgressChangedListener = object : BubbleSeekBar.OnProgressChangedListenerAdapter() {
//            override fun onProgressChanged(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float, fromUser: Boolean) {
//                super.onProgressChanged(bubbleSeekBar, progress, progressFloat, fromUser)
//            }
//
//            override fun getProgressOnFinally(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float, fromUser: Boolean) {
//                super.getProgressOnFinally(bubbleSeekBar, progress, progressFloat, fromUser)
//
//            }
//
//            override fun getProgressOnActionUp(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float) {
//                try {
//                    if (nextSort != null) {
//                        var msg = Message()
//                        msg.what = 3
//                        msg.arg1 = nextSort!!
//                        msg.arg2 = -1
//                        handler.sendMessage(msg)
//                    }
//                    player?.seekTo((progressFloat.toDouble() / du!!), false)
//                } catch (ex: java.lang.Exception) {
//                }
//
//            }
//        }
    }

    override fun isRegistEventBus(): Boolean = true
    override fun onMessageEvent(eventBusCenter: EventBusCenter<Object>) {
        super.onMessageEvent(eventBusCenter)
        if (eventBusCenter != null) {
            if (eventBusCenter.evenCode == Constants.Tag.SETTING_DELAY) {
                tempo = eventBusCenter.data as Float
                player?.tempo = tempo
            }
        }
    }

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
        rv_list.setItemViewCacheSize(132)
        adapter = MainAdapter(this, this)
        adapter?.setmRecyclerView(rv_list)
        adapter?.setLinesMap(mLinesMap)
//        rv_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) { // 滚动静止时才加载图片资源，极大提升流畅度
//                    adapter?.setScrolling(false)
//                    adapter?.notifyDataSetChanged() // notify调用后onBindViewHolder会响应调用
//                } else
//                    adapter?.setScrolling(true)
//
//                super.onScrollStateChanged(recyclerView, newState)
//            }
//        })
        rv_list.adapter = adapter
        var mPosX = 0f
        var mPosY = 0f
        var mCurPosX = 0f
        var mCurPosY = 0f
        ll_scroll.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        mPosX = event.x
                        mPosY = event.y
                    }
                    MotionEvent.ACTION_MOVE -> {
                        mCurPosX = event.x
                        mCurPosY = event.y
                    }
                    MotionEvent.ACTION_UP -> if (mCurPosY - mPosY > 0 && Math.abs(mCurPosY - mPosY) > 25) {
                        //向下滑動
                        rl_qin.visibility = View.VISIBLE
                    } else if (mCurPosY - mPosY < 0 && Math.abs(mCurPosY - mPosY) > 25) {
                        //向上滑动
                        rl_qin.visibility = View.GONE
                    }
                }
                return true

            }
        })
//录音
        MediaDirectoryUtils.setMediaManagerProvider(object : MediaDirectoryUtils.MediaManagerProvider {
            override fun getTempMp3FileName(): File? {
                return File(if (lastRecordFileName.isNullOrEmpty()) (RecordFilesUtils.getInstance()!!.getCurrentUri() + "/" + System.currentTimeMillis()) else (RecordFilesUtils.getInstance()!!.getCurrentUri() + "/" + lastRecordFileName + ".mp3")
                )
            }

            override fun getTempAmrFileName(): File? {
                return null
            }

            override fun getTempCachePcmFileName(): File? {
                return null
            }

            override fun productFileName(postfix: String?): String? {
                return null
            }

            override fun getTempCacheWavFileName(): File? {
                return null
            }

            override fun getTempAACFileName(): File? {
                return null
            }

            override fun getCachePath(): File? {
                return File(RecordFilesUtils.getInstance()!!.getCurrentUri() + "cache")
            }

        })

    }

    /*检查文件本地是否有*/
    private fun chenckIsLoaded(url: String): Boolean {
        isLoaded = DownLoadFilesUtils.getInstance()!!.isExist(FilesUtils.getFileName(url))
        setButtonState()
        return isLoaded
    }

    /*
    * 初始化播放器
    * */
    private fun checkFile(url: String) {


        if (chenckIsLoaded(url)) {
            f = DownLoadFilesUtils.getInstance()!!.getCurrentUri() + "/" + FilesUtils.getFileName(url)
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
                    runOnUiThread {
                        checkFile(url)
                    }

//                    f = DownLoadFilesUtils.getInstance(this@MusicPlayActivity)!!.getCurrentUri() + "/" + FilesUtils.getFileName(url)
//                    initPlayer()
                }
            })
        }
    }

    private fun initPlayer() {
        try {
            if (player == null) {
                player = SoundStreamAudioPlayer(0, f, tempo, pitchSemi)
//播放器进度条
                player?.setOnProgressChangedListener(object : OnProgressChangedListener {
                    override fun onProgressChanged(track: Int, currentPercentage: Double, position: Long) {
                        if (isABStyle && endTime != null) {
                            if (currentPercentage >= endTime!! / du!!) {
                                player!!.seekTo((startTime!! / du!!), false)
//                                setButtonState()
                                return
                            }
                        } else if (currentPercentage >= 1) {
                            player?.seekTo(0, true)
                            seek_bar?.setProgress(0f)
                            setButtonState()
                            adapter?.clearSelected()
                            tv_start_time.text = "00:00"
                            var msg = Message()
                            msg.what = 3
                            if (currentSort != null) {
                                msg.arg1 = currentSort!!
                            } else {
                                msg.arg1 = -1
                            }
                            msg.arg2 = -1
                            handler.sendMessage(msg)
                            rv_list.smoothScrollToPosition(0)
                            return
                        }
                        seek_bar.setProgress((currentPercentage * du!!).toFloat())
//                        时间
                        tv_start_time.text = ExtraUtils.secToTime((currentPercentage * du!!).toInt())
//                       琴谱上面的打点

                        getPoints((currentPercentage * du!!).toFloat())
//                        LogUtils.e("currentSort:"+currentSort+"------nextSort:"+nextSort)
                        if (currentSort == null && nextSort != null) {
                            var msg = Message()
                            msg.what = 3
                            msg.arg1 = -1
                            msg.arg2 = nextSort!!
                            handler.sendMessage(msg)
                        }
                        if (currentSort != nextSort && nextSort != null) {
                            if (currentSort != null) {
                                if (isABStyle) {
                                    rv_list.smoothScrollToPosition(currentSort!!)
                                } else {
                                    rv_list.smoothScrollToPosition(nextSort!!)
                                }
                                var msg = Message()
                                msg.what = 3
                                msg.arg1 = currentSort!!
                                msg.arg2 = nextSort!!
                                handler.sendMessage(msg)

                            }

                            if (rl_qin.visibility == View.VISIBLE) {
                                cq_view.setmMoveMap(mMoveMap, musicBean?.score?.get(nextSort!!)?.overtone!!, musicBean?.score?.get(nextSort!!)?.portamento!!, musicBean?.score?.get(nextSort!!)?.duration, mEndMap)
                                if (musicBean?.score?.get(nextSort!!)?.portamento!!) {
                                    cq_view.startAnim()
                                }
                                tv_left.text = musicBean?.score?.get(nextSort!!)?.left_str
                                tv_right.text = musicBean?.score?.get(nextSort!!)?.right_str
                            }
                            currentSort = nextSort
                        }

                    }

                    override fun onTrackEnd(track: Int) {

                    }

                    override fun onExceptionThrown(string: String) {

                    }
                })
                player?.setRate(rate)
//                Thread(player).start()
                if (playThread == null) {
                    playThread = Thread(player)
                }

                du = ExtraUtils.getMP3FileInfo(f!!) / 1000
                seek_bar.configBuilder.max(du!!.toFloat()).min(0f).build()
                tv_end_time.text = ExtraUtils.secToTime((du!!).toInt())
                if (playTimeCountThread == null) {
                    playTimeCountThread = Thread(MyThread())
                    playTimeCountThread?.start()
                }

                isPlaying = true
                playThread?.start()
                if (isABStyle) {
                    initABRange()
                }
                player?.start()
                setButtonState()
                dismissLoading()
            } else {
                if (isABStyle) {
                    initABRange()
                }
                if (player!!.isPaused || player!!.isFinished) {
                    player?.start()
                    isPlaying = true
                } else {
                    player!!.pause()
                    isPlaying = false
                }
                setButtonState()
            }
//                    var du=player?.playedDuration!!.toFloat()/1000

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initABRange() {
        if (isABStyle && adapter?.selectedIndices!!.size > 0) {
            startTime = musicBean?.score!![adapter?.selectedIndices!![0]].start_second[0]
            endTime = musicBean?.score!![adapter?.selectedIndices!![adapter?.selectedIndices!!.size - 1]].end_second[0]
//            LogUtils.e("PlayActivity---" + "startTime:" + startTime + "-----endTime:" + endTime)
            player?.seekTo((startTime!! / du!!), false)
        }
    }

    private var isGet = false
    /*获取琴上面的点*/
    private fun getPoints(currenttime: Float) {
        if (pointList == null || pointList!!.isEmpty()) {
            return
        }
        mMoveMap.clear()
        mEndMap.clear()
        isGet=false
        for (pos in (if (currentSort == null) 0 else currentSort!!) until pointList?.size!!) finished@ {
            var qinViewPointBean = pointList!![pos]
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
                if (!qinViewPointBean.toPosition.isNullOrEmpty() && !qinViewPointBean.string.isNullOrEmpty()) {
                    if (!qinViewPointBean.toPosition.contains("+")) {
                        mEndMap.put(qinViewPointBean.string.toInt(), qinViewPointBean.toPosition.toFloat())
                    } else {
                        var ss = qinViewPointBean.string.split("+")
                        var pp = qinViewPointBean.toPosition.split("+")
                        ss.forEachIndexed { index, s ->
                            mEndMap.put(s.toInt(), pp[index].toFloat())
                        }
                    }
                }

                nextSort = qinViewPointBean.index
                isGet = true
                if (nextSort !== null && currentSort != null) {
                    if (currentSort!! > nextSort!!) {
//                        判断current 和next 是否在一行
                        var ieLine = false
                        for (i in nextSort!!..currentSort!!) complete@ {
                            if (musicBean?.score?.get(i)!!.islinefeed == 1) {
                                ieLine = true
                                return@complete
                            }
                        }
                        if (!ieLine) {
                            rv_list.scrollToPosition(nextSort!!)
                        }
                    }
                }
                return@finished
            }
        }
        if (!isGet&&currentSort!=null) {
            for (pos in currentSort!! downTo 0) finished@ {
                var qinViewPointBean = pointList!![pos]
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
                    if (!qinViewPointBean.toPosition.isNullOrEmpty() && !qinViewPointBean.string.isNullOrEmpty()) {
                        if (!qinViewPointBean.toPosition.contains("+")) {
                            mEndMap.put(qinViewPointBean.string.toInt(), qinViewPointBean.toPosition.toFloat())
                        } else {
                            var ss = qinViewPointBean.string.split("+")
                            var pp = qinViewPointBean.toPosition.split("+")
                            ss.forEachIndexed { index, s ->
                                mEndMap.put(s.toInt(), pp[index].toFloat())
                            }
                        }
                    }

                    nextSort = qinViewPointBean.index
                    isGet = true
                    if (nextSort !== null && currentSort != null) {
                        if (currentSort!! > nextSort!!) {
//                        判断current 和next 是否在一行
                            var ieLine = false
                            for (i in nextSort!!..currentSort!!) complete@ {
                                if (musicBean?.score?.get(i)!!.islinefeed == 1) {
                                    ieLine = true
                                    return@complete
                                }
                            }
                            if (!ieLine) {
                                rv_list.scrollToPosition(nextSort!!)
                            }
                        }
                    }
                    return@finished
                }
            }
        }

//        pointList?.forEachIndexed { index, qinViewPointBean ->
//
//        }
    }


    /*
    * 按钮状态*/
    private fun setButtonState() {
        if (player != null) {
            if (player!!.isPaused) {
                iv_play.setBackgroundResource(R.drawable.selector_play)
            } else if (player!!.isFinished) {
                iv_play.setBackgroundResource(R.drawable.selector_play)
                seek_bar.setProgress(0f)
            } else {
                iv_play.setBackgroundResource(R.drawable.selector_pause)
            }

        }
        if (isLoaded) {
            iv_load.setImageResource(R.mipmap.ic_load_pressed)
        } else {
            iv_load.setImageResource(R.mipmap.ic_load_normal)
        }
        if (isSlience) {
            iv_voice.setImageResource(R.mipmap.ic_voice_closed)
        } else {
            iv_voice.setImageResource(R.mipmap.ic_voice)
        }
        if (recordManager != null && getRecordManager()?.isRecordIng) {
            iv_record.setImageResource(R.mipmap.ic_record_pressed)
        } else {
            iv_record.setImageResource(R.mipmap.ic_record_normal)
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

    override fun onPause() {
        super.onPause()
        if (player != null) {
            player?.pause()
            isPlaying = false
            setButtonState()
        }
        if (getRecordManager()?.isRecordIng) {
            getRecordManager()?.stopRecord()
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onResume() {
        super.onResume()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onDestroy() {
        if (playTime > 0 && id != null) {
            var map = hashMapOf<String, String>()
            map.put("duration", "" + playTime / 60)
            map.put("musicid", id!!)
            ExerciseRecordUploadUtils.uploadRecord(map)
        }
        try {
            if (playThread != null) {
                playThread?.interrupt()
            }
            if (playTimeCountThread != null) {
                playTimeCountThread?.interrupt()
            }
        } catch (e: java.lang.Exception) {

        }
        player?.stop()
        player = null
        try {
            if (recordManager != null && getRecordManager()?.isRecordIng) {
                getRecordManager()?.stopRecord()
                RecordFactory.release(recordManager)
                recordManager = null
                if (!lastRecordFile.isNullOrEmpty()) {
                    File(lastRecordFile).delete()
                }
            }
            soundTouchRec = null
            soundTouch = null
        } catch (e: java.lang.Exception) {

        }
        DownLoadUtils.cancle()
        LogUtils.e("播放时长：" + playTime)
        super.onDestroy()
    }

    private fun initTitle() {
        iv_back.setOnClickListener { finish() }
    }

    /*
    * 播放时长统计*/
    val handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg?.what) {
                1 -> {
                    playTime++
                }
                2 -> {
                    recordTime++
                }
                3 -> {
                    adapter?.setPlayPosition(msg?.arg1, msg?.arg2)
                }
            }
        }
    }
    private var isPlaying = false
    private var playTime: Int = 0
    private var recordTime: Int = 0
    private var playTimeCountThread: Thread? = null

    inner class MyThread : Runnable {
        override fun run() {
            while (true) {
                try {
                    Thread.sleep(1000)        // sleep 1000ms
                    if (isPlaying) {
                        val message = Message()
                        message.what = 1
                        handler.sendMessage(message)
                    }
                } catch (e: Exception) {
                }

            }
        }
    }

    fun getRecordManager(): RecordManagerI {
        if (recordManager == null) {
            recordManager = RecordFactory.getWavRecordMp3OutInstance()
            recordManager?.setOnTimeSecondChanage(object : RecordManagerI.OnTimeSecondChanage {
                override fun onSecondChnage(duration: Int) {
                    recordTime = duration
                }

            })
            recordManager?.setOnTimeOutStopListener(object : RecordManagerI.OnTimeOutStopListener {
                override fun onStop() {
//                    mAudioFile = recordManager.getFile()
//                    tvPath.setText("audioPth:" + if (mAudioFile == null) null else mAudioFile.getAbsolutePath())
                    lastRecordFile = if (recordManager?.file == null) null else recordManager?.file?.getAbsolutePath()
//                    runOnUiThread { toast_msg("保存路径："+lastRecordFile) }
//                    LogUtils.e("保存路径：" + lastRecordFile)
//                    val tempCacheMp3FileName = MediaDirectoryUtils.getTempMp3FileName()
//                    try {
//                        tempCacheMp3FileName.createNewFile()
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }

                }
            })
        }
        return recordManager!!
    }

}