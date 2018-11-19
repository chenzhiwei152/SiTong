package com.sevenstringedzithers.sitong.ui.activity

import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import com.jyall.android.common.utils.LogUtils
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.extension.loadImage
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
import com.smp.soundtouchandroid.OnProgressChangedListener
import com.smp.soundtouchandroid.SoundStreamAudioPlayer
import com.xw.repo.BubbleSeekBar
import kotlinx.android.synthetic.main.activity_music_enjoy.*
import kotlinx.android.synthetic.main.activity_music_info.*
import kotlinx.android.synthetic.main.layout_play_title_fff.*
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
                if (musicBean==null){
                    return
                }
                tv_type.text=musicBean!!.level
                tv_source.text=musicBean!!.from_detail
                tv_content.text = Html.fromHtml(musicBean!!.introduce)
                ll_info.visibility=View.VISIBLE
            }
            R.id.iv_back->{
                finish()
            }
            R.id.tv_close->{
                ll_info.visibility=View.GONE
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
        iv_title.setText(musicBean.name)
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
        initTitle()
        var bundle = intent.extras
        if (bundle != null) {
            id = bundle.getString("id")
        }
        iv_play.setOnClickListener(this)
        iv_tool.setOnClickListener(this)
        iv_back.setOnClickListener(this)
        tv_close.setOnClickListener(this)
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
                    f = File(DownLoadFilesUtils.getInstance(this@MusicEnjoyActivity)!!.getCurrentUri() + "/" + FilesUtils.getFileName(url))
                    initPlayer()
                }
            })
        }

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
    private fun initTitle(){
        iv_back.setOnClickListener { finish() }
    }
}