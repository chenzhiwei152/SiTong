package com.sitong.changqin.ui.activity

import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.jyall.android.common.utils.LogUtils
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.extension.jump
import com.jyall.bbzf.extension.loadImage
import com.jyall.bbzf.extension.toast
import com.sitong.changqin.mvp.contract.MusicPlayContract
import com.sitong.changqin.mvp.model.bean.MusicDetailBean
import com.sitong.changqin.mvp.model.bean.QinViewPointBean
import com.sitong.changqin.mvp.persenter.MusicPlayPresenter
import com.sitong.changqin.ui.adapter.MainAdapter
import com.sitong.changqin.ui.listerner.ProgressCallback
import com.sitong.changqin.utils.DownLoadUtils
import com.sitong.changqin.utils.ExtraUtils
import com.sitong.changqin.utils.files.DownLoadFilesUtils
import com.sitong.changqin.utils.files.FilesUtils
import com.smp.soundtouchandroid.OnProgressChangedListener
import com.smp.soundtouchandroid.SoundStreamAudioPlayer
import com.stringedzithers.sitong.R
import com.stringedzithers.sitong.R.string.file
import com.xw.repo.BubbleSeekBar
import kotlinx.android.synthetic.main.activity_music_enjoy.*
import java.io.File


/**
 * create by chen.zhiwei on 2018-8-15
 */
class MusicEnjoyActivity : BaseActivity<MusicPlayContract.View, MusicPlayPresenter>(), MusicPlayContract.View, MainAdapter.Listener, View.OnClickListener {
    private var du: Long? = null
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
            R.id.iv_tool -> {
                jump<MusicInfoActivity>()
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
        iv_image.loadImage(this@MusicEnjoyActivity,musicBean.icon)
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

    override fun getLayoutId(): Int = R.layout.activity_music_enjoy

    override fun initViewsAndEvents() {
        var bundle = intent.extras
        if (bundle != null) {
            id = bundle.getString("id")
        }
        iv_play.setOnClickListener(this)
        iv_tool.setOnClickListener(this)
        pointList = arrayListOf()

        var map: HashMap<String, String>? = null
        map = hashMapOf()
        map.put("music", id!!)
        map.put("needdetail", "1")
        mPresenter?.getMusicDetail(map)
        init()
//        initPlayer()
        seek_bar.onProgressChangedListener = object : BubbleSeekBar.OnProgressChangedListenerAdapter() {
            override fun onProgressChanged(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float, fromUser: Boolean) {
                super.onProgressChanged(bubbleSeekBar, progress, progressFloat, fromUser)
                LogUtils.e("progress:" + progress + "-------" + progressFloat)
//                tv_start_time.setText(ExtraUtils.secToTime(progress))
            }

            override fun getProgressOnFinally(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float, fromUser: Boolean) {
                super.getProgressOnFinally(bubbleSeekBar, progress, progressFloat, fromUser)

            }

            override fun getProgressOnActionUp(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float) {
                super.getProgressOnActionUp(bubbleSeekBar, progress, progressFloat)
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
//        val layoutManager = FlexboxLayoutManager(this)
//        layoutManager.flexDirection = FlexDirection.ROW
//        layoutManager.flexWrap = FlexWrap.WRAP
//        layoutManager.justifyContent = JustifyContent.CENTER
//        rv_list.layoutManager = layoutManager
//
//        adapter = MainAdapter(this)
//        rv_list.adapter = adapter

//        var mPosX = 0f
//        var mPosY = 0f
//        var mCurPosX = 0f
//        var mCurPosY = 0f
//        ll_scroll.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                when (event?.getAction()) {
//                    MotionEvent.ACTION_DOWN -> {
//                        mPosX = event.getX()
//                        mPosY = event.getY()
//                    }
//                    MotionEvent.ACTION_MOVE -> {
//                        mCurPosX = event.getX()
//                        mCurPosY = event.getY()
//                    }
//                    MotionEvent.ACTION_UP -> if (mCurPosY - mPosY > 0 && Math.abs(mCurPosY - mPosY) > 25) {
//                        //向下滑動
//                        cq_view.visibility = View.VISIBLE
//                    } else if (mCurPosY - mPosY < 0 && Math.abs(mCurPosY - mPosY) > 25) {
//                        //向上滑动
//                        cq_view.visibility = View.GONE
//                    }
//                }
//                return true
//
//            }
//        })
    }

    /*
    * 初始化播放器
    * */
    private fun checkFile(url: String) {
//        f = File("/sdcard/Download.mp3")
//        f = File(Environment.getExternalStorageDirectory().absolutePath + "/Download/Downloada.mp3")

        showLoading()
        if (DownLoadFilesUtils.getInstance(this)!!.isExist(FilesUtils.getFileName(url))) {
            f = File(DownLoadFilesUtils.getInstance(this)!!.getCurrentUri() + "/" + FilesUtils.getFileName(url))
            initPlayer()
        } else {
//开始下载
            DownLoadUtils.downLoadMusic(this@MusicEnjoyActivity, url, object : ProgressCallback {
                override fun onProgressCallback(progress: Double) {
                }

                override fun onProgressFailed() {
                    dismissLoading()
                    toast_msg("获取签名失败")
                }

                override fun onProgressSuccess() {
                    dismissLoading()
                    f = File(DownLoadFilesUtils.getInstance(this@MusicEnjoyActivity)!!.getCurrentUri() + "/" + file)
                    initPlayer()
                }

            })

        }


//        player = SoundStreamAudioPlayer(0, f?.getPath(), tempo, pitchSemi)
//        if (!f!!.exists()) {
//            try {
//                //InputStream is = this.getResources().openRawResource(R.raw.bjbj);
//                val `is` = this.resources.assets.open("Downloada.mp3")
//                val size = `is`.available()
//                val buffer = ByteArray(size)
//                `is`.read(buffer)
//                `is`.close()
//                val fos = FileOutputStream(f)
//                fos.write(buffer)
//                fos.close()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//
//        }

    }

    private fun initPlayer() {
        try {
            if (player == null) {
                player = SoundStreamAudioPlayer(0, f?.getPath(), tempo, pitchSemi)
                //player.setChannels(1);//取值范围是1，2两个声道
                //player.setPitchSemi(9.0f);//取值范围是-12到12，超过声音会很差
                //player.setTempoChange(-10.0f);//设置变速不变调，取值范围是-50到100超过则不处理
                //player.setRateChange(50f);//设置声音的速率，取值范围是-50到100超过则不处理
                //mSoundTouch.setSampleRate(sampleRate);//设置声音的采样频率
                //m_SoundTouch.setPitchSemiTones(pitchDelta);//设置声音的pitch
                //quick是一个bool变量，USE_QUICKSEEK具体有什么用我暂时也不太清楚。
                //mSoundTouch.setSetting(SETTING_USE_QUICKSEEK, quick);
                //noAntiAlias是一个bool变量，USE_AA_FILTER具体有什么用我暂时也不太清楚。
                //mSoundTouch.setSetting(SETTING_USE_AA_FILTER, !(noAntiAlias));

                //这个参数是变速又变声的，这个参数大于0，否则会报错

                player?.setOnProgressChangedListener(object : OnProgressChangedListener {
                    override fun onProgressChanged(track: Int, currentPercentage: Double, position: Long) {
                        Log.e("onProgressChanged", "" + currentPercentage)
                        seek_bar.setProgress((currentPercentage * du!!).toFloat())

                        tv_start_time.setText(ExtraUtils.secToTime((currentPercentage * du!!).toInt()))
//                        getPoints((currentPercentage * du!!).toFloat())
//                        if (currentSort != nextSort) {
//                            cq_view.setmMoveMap(mMoveMap)
//                            currentSort = nextSort
//                        }
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
                    setButtonState()
                } else {
                    player!!.pause()
                    setButtonState()
                }
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

//    private fun getPoints(currenttime: Float) {
//        mMoveMap.clear()
//        pointList?.forEachIndexed { index, qinViewPointBean ->
//            if (qinViewPointBean.start_second <= currenttime && qinViewPointBean.end_second >= currenttime) {
//                if (!qinViewPointBean.string.isNullOrEmpty()) {
//                    if (!qinViewPointBean.string.contains("+")) {
//                        mMoveMap.put(qinViewPointBean.string.toInt(), qinViewPointBean.percent.toFloat())
//                    } else {
//                        var ss = qinViewPointBean.string.split("+")
//                        var pp = qinViewPointBean.percent.split("+")
//                        ss.forEachIndexed { index, s ->
//                            mMoveMap.put(s.toInt(), pp[index].toFloat())
//                        }
//                    }
//                }
//                nextSort = index
//            }
//
//        }
//    }


    /*
    * 按钮状态*/
    private fun setButtonState() {
        if (player != null) {
            if (player!!.isPaused||player!!.isFinished||player!!.isLooping) {
                iv_play.setBackgroundResource(R.drawable.selector_play)
            }else {
                iv_play.setBackgroundResource(R.drawable.selector_pause)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.stop()
        player = null
    }
}