package com.sevenstringedzithers.sitong.ui.activity

import android.app.Service
import android.media.AudioManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.extension.loadImage
import com.jyall.bbzf.extension.toast
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.mvp.contract.MusicPlayContract
import com.sevenstringedzithers.sitong.mvp.model.bean.MusicDetailBean
import com.sevenstringedzithers.sitong.mvp.persenter.MusicPlayPresenter
import com.sevenstringedzithers.sitong.ui.adapter.MainAdapter
import com.sevenstringedzithers.sitong.ui.listerner.ProgressCallback
import com.sevenstringedzithers.sitong.ui.listerner.ResultCallback
import com.sevenstringedzithers.sitong.utils.CollectionUtils
import com.sevenstringedzithers.sitong.utils.DownLoadUtils
import com.sevenstringedzithers.sitong.utils.ExtraUtils
import com.sevenstringedzithers.sitong.utils.files.DownLoadFilesUtils
import com.sevenstringedzithers.sitong.utils.files.FilesUtils
import com.sevenstringedzithers.sitong.view.MusicDownloadDialog
import com.smp.soundtouchandroid.OnProgressChangedListener
import com.smp.soundtouchandroid.SoundStreamAudioPlayer
import com.xw.repo.BubbleSeekBar
import kotlinx.android.synthetic.main.activity_music_enjoy.*
import kotlinx.android.synthetic.main.activity_music_info.*
import kotlinx.android.synthetic.main.layout_play_title_fff.*


/**
 * create by chen.zhiwei on 2018-8-15
 */
class MusicEnjoyActivity : BaseActivity<MusicPlayContract.View, MusicPlayPresenter>(), MusicPlayContract.View, MainAdapter.Listener, View.OnClickListener {
    private var du: Long? = null
    private var isLoaded = false
    private var isLoading = false
    private var isSlience: Boolean = false
//    private var pointList: ArrayList<QinViewPointBean>? = null
    var mLoadDialog: MusicDownloadDialog? = null
    //    private var mMoveMap: HashMap<Int, Float> = hashMapOf()//在线上动态显示的点
//    private var currentSort: Int? = null
//    private var nextSort: Int? = null
    private var musicBean: MusicDetailBean? = null

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_play -> {
                if (musicBean != null && !musicBean!!.url.isNullOrEmpty()) {
                    checkFile(musicBean!!.url)
                } else {
                    toast_msg("获取音频失败")
                }

            }
            R.id.iv_tool -> {
                if (musicBean == null) {
                    return
                }
                tv_type.text = musicBean!!.level
                tv_source.text = musicBean!!.from_detail
                tv_content.text = musicBean!!.introduce.replace("</n>","\n")
                ll_info.visibility = View.VISIBLE
            }
            R.id.iv_back -> {
                finish()
            }
            R.id.tv_close -> {
                ll_info.visibility = View.GONE
            }
            R.id.iv_collection -> {
                var type = 1
                if (musicBean!!.iscollection) {
                    type = 0
                }
                CollectionUtils.collectionUtils(type, id!!, object : ResultCallback<String> {
                    override fun onSuccess(result: String?) {
                        musicBean!!.iscollection = type != 0
                        setCollection(musicBean!!.iscollection)
                        toast_msg(result!!)
                    }

                    override fun onsFailed(reason: String?) {
                        toast_msg(reason!!)
                    }
                })

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
                    mLoadDialog = MusicDownloadDialog(this@MusicEnjoyActivity, resources.getString(R.string.download), resources.getString(R.string.cancel), musicBean!!.size.toString(), musicBean!!.icon)
                    mLoadDialog?.setLeftTitleListerner(View.OnClickListener {
                        isLoading = true
                        DownLoadUtils.downLoadMusic(this@MusicEnjoyActivity, musicBean!!.url, object : ProgressCallback {
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
            R.id.iv_voice -> {
                isSlience = !isSlience
                setVolume(isSlience)
                setButtonState()
            }
        }
    }

    private var player: SoundStreamAudioPlayer? = null
    private var f: String? = null
    private var tempo = 1.0f//这个是速度，1.0表示正常设置新的速度控制值，
    private var pitchSemi = 1.0f//这个是音调，1.0表示正常，
    private var rate = 1.0f//这个参数是变速又变声的，这个参数大于0，否则会报错
    /*检查文件本地是否有*/
    private fun chenckIsLoaded(url: String): Boolean {
        isLoaded = DownLoadFilesUtils.getInstance()!!.isExist(FilesUtils.getFileName(url))
        setButtonState()
        return isLoaded
    }

    override fun getDataSuccess(musicBean: MusicDetailBean) {
        this.musicBean = musicBean
        iv_image.loadImage(this@MusicEnjoyActivity, musicBean.icon)
        iv_title.setText(musicBean.name)
        setCollection(musicBean.iscollection)
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
        iv_load.setOnClickListener(this)
        iv_voice.setOnClickListener(this)
        iv_collection.setOnClickListener(this)

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
//                LogUtils.e("progress:" + progress + "-------" + progressFloat)
//                tv_start_time.setText(ExtraUtils.secToTime(progress))
            }

            override fun getProgressOnFinally(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float, fromUser: Boolean) {
                super.getProgressOnFinally(bubbleSeekBar, progress, progressFloat, fromUser)

            }

            override fun getProgressOnActionUp(bubbleSeekBar: BubbleSeekBar?, progress: Int, progressFloat: Float) {
                try {
                    player?.seekTo((progressFloat.toDouble() / du!!), false)
                } catch (ex: java.lang.Exception) {
                }
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


        if (DownLoadFilesUtils.getInstance()!!.isExist(FilesUtils.getFileName(url))) {
            f =DownLoadFilesUtils.getInstance()!!.getCurrentUri() + "/" + FilesUtils.getFileName(url)
            initPlayer()
        } else {
//开始下载
            showLoading()
            DownLoadUtils.downLoadMusic(this@MusicEnjoyActivity, url, object : ProgressCallback {
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

                }
            })
        }

    }

    private fun initPlayer() {
        try {
            if (player == null) {
                player = SoundStreamAudioPlayer(0, f, tempo, pitchSemi)

                //这个参数是变速又变声的，这个参数大于0，否则会报错

                player?.setOnProgressChangedListener(object : OnProgressChangedListener {
                    override fun onProgressChanged(track: Int, currentPercentage: Double, position: Long) {
                        Log.e("onProgressChanged", "" + currentPercentage)
                        seek_bar.setProgress((currentPercentage * du!!).toFloat())

                        tv_start_time.setText(ExtraUtils.secToTime((currentPercentage * du!!).toInt()))
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
            du = ExtraUtils.getMP3FileInfo(f!!) / 1000
            seek_bar.getConfigBuilder().max(du!!.toFloat()).min(0f)
            tv_end_time.setText(ExtraUtils.secToTime((du!!).toInt()))
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        player?.stop()
        player = null
    }

    private fun initTitle() {
        iv_back.setOnClickListener { finish() }
    }

    fun setCollection(isCollection: Boolean) {
        if (isCollection) {
            iv_collection.setImageResource(R.mipmap.ic_round_collection_pressed)
        } else {
            iv_collection.setImageResource(R.mipmap.ic_round_collection_normal)
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
            setButtonState()
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
    override fun onResume() {
        super.onResume()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
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
        if (isSlience) {
            iv_voice.setImageResource(R.mipmap.ic_voice_closed)
        } else {
            iv_voice.setImageResource(R.mipmap.ic_voice)
        }
    }
}