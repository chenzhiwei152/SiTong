package com.sevenstringedzithers.sitong.ui.activity

import android.view.View
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.base.BaseActivity
import com.sevenstringedzithers.sitong.base.BasePresenter
import com.sevenstringedzithers.sitong.base.Constants
import com.sevenstringedzithers.sitong.base.IBaseView
import com.sevenstringedzithers.sitong.ui.listerner.RVAdapterItemOnClick
import com.sevenstringedzithers.sitong.utils.TxVideoPlayerController1
import com.xiao.nicevideoplayer.Clarity
import com.xiao.nicevideoplayer.NiceVideoPlayer
import com.xiao.nicevideoplayer.NiceVideoPlayerManager
import kotlinx.android.synthetic.main.activity_video.*
import java.util.*

class VideoPlayActivity : BaseActivity<IBaseView, BasePresenter<IBaseView>>(), IBaseView {
    private var videoUrl: String? = null
    private var controller: TxVideoPlayerController1? = null
    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()

    override fun getRootView(): IBaseView = this

    override fun getLayoutId(): Int = R.layout.activity_video

    override fun initViewsAndEvents() {
        init()
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null
    private fun init() {
        if (intent.extras != null) {
            videoUrl = intent.extras.getString(Constants.VIDOE_URL)
        }
        nice_video_player.setPlayerType(NiceVideoPlayer.TYPE_IJK) // IjkPlayer or MediaPlayer
        controller = TxVideoPlayerController1(this)
//        controller.setTitle("Beautiful China...")
//        controller.setLenght(117000)
        controller?.setClarity(getClarites(), 0)
//        controller.imageView().loadImage(this,"")
        controller?.setBackListerner(object : RVAdapterItemOnClick {
            override fun onItemClicked(data: Any) {
                finish()
            }

        })
        nice_video_player.setController(controller)
        nice_video_player.enterFullScreen()
    }

    fun getClarites(): List<Clarity> {
        val clarities = ArrayList<Clarity>()
        clarities.add(Clarity("标清", "270P", videoUrl))
        clarities.add(Clarity("标清", "270P", videoUrl))
//        clarities.add(Clarity("高清", "480P", "http://play.g3proxy.lecloud.com/vod/v2/MjQ5LzM3LzIwL2xldHYtdXRzLzE0L3Zlcl8wMF8yMi0xMTA3NjQxMzkwLWF2Yy00MTk4MTAtYWFjLTQ4MDAwLTUyNjExMC0zMTU1NTY1Mi00ZmJjYzFkNzA1NWMyNDc4MDc5OTYxODg1N2RjNzEwMi0xNDk4NTU3OTYxNzQ4Lm1wNA==?b=479&mmsid=65565355&tm=1499247143&key=98c7e781f1145aba07cb0d6ec06f6c12&platid=3&splatid=345&playid=0&tss=no&vtype=13&cvid=2026135183914&payff=0&pip=08cc52f8b09acd3eff8bf31688ddeced&format=0&sign=mb&dname=mobile&expect=1&tag=mobile&xformat=super"))
//        clarities.add(Clarity("超清", "720P", "http://play.g3proxy.lecloud.com/vod/v2/MjQ5LzM3LzIwL2xldHYtdXRzLzE0L3Zlcl8wMF8yMi0xMTA3NjQxMzkwLWF2Yy00MTk4MTAtYWFjLTQ4MDAwLTUyNjExMC0zMTU1NTY1Mi00ZmJjYzFkNzA1NWMyNDc4MDc5OTYxODg1N2RjNzEwMi0xNDk4NTU3OTYxNzQ4Lm1wNA==?b=479&mmsid=65565355&tm=1499247143&key=98c7e781f1145aba07cb0d6ec06f6c12&platid=3&splatid=345&playid=0&tss=no&vtype=13&cvid=2026135183914&payff=0&pip=08cc52f8b09acd3eff8bf31688ddeced&format=0&sign=mb&dname=mobile&expect=1&tag=mobile&xformat=super"))
//        clarities.add(Clarity("蓝光", "1080P", "http://play.g3proxy.lecloud.com/vod/v2/MjQ5LzM3LzIwL2xldHYtdXRzLzE0L3Zlcl8wMF8yMi0xMTA3NjQxMzkwLWF2Yy00MTk4MTAtYWFjLTQ4MDAwLTUyNjExMC0zMTU1NTY1Mi00ZmJjYzFkNzA1NWMyNDc4MDc5OTYxODg1N2RjNzEwMi0xNDk4NTU3OTYxNzQ4Lm1wNA==?b=479&mmsid=65565355&tm=1499247143&key=98c7e781f1145aba07cb0d6ec06f6c12&platid=3&splatid=345&playid=0&tss=no&vtype=13&cvid=2026135183914&payff=0&pip=08cc52f8b09acd3eff8bf31688ddeced&format=0&sign=mb&dname=mobile&expect=1&tag=mobile&xformat=super"))
        return clarities
    }

    override fun onResume() {
        super.onResume()
        NiceVideoPlayerManager.instance().resumeNiceVideoPlayer()
    }

    override fun onStop() {
        super.onStop()
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer()
    }

    override fun onBackPressed() {
//        if (NiceVideoPlayerManager.instance().onBackPressd()) return
        super.onBackPressed()
    }
}