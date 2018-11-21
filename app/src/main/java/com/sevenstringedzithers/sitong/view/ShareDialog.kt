package com.sevenstringedzithers.sitong.view

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.jyall.bbzf.base.EventBusCenter
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.base.Constants
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.android.synthetic.main.dialog_share.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


/**
 * create by chen.zhiwei on 2018-8-15
 */
class ShareDialog(context: Context?, leftTitle: String, rightTitle: String, title: String = "", enTitle: String, level: String) : Dialog(context) {
    var mContext: Context? = null
    private var wxAPI: IWXAPI? = null
    var leftTitleListerner: View.OnClickListener? = null
    var rightTitleListerner: View.OnClickListener? = null

    init {
        this.mContext = context
        init(leftTitle, rightTitle, title, enTitle, level)
    }

    fun init(leftTitle: String, rightTitle: String, title: String = "", enTitle: String, level: String) {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        window!!.setContentView(R.layout.dialog_share)
        val window = window
        val wlp = window!!.attributes
        wlp.gravity = Gravity.CENTER
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = wlp
        iv_qq.setOnClickListener { }
        iv_weixin.setOnClickListener {


        }
        iv_weixin_friend.setOnClickListener { }
        EventBus.getDefault().register(this)
    }


    fun setLeftTitleListerner(lister: View.OnClickListener): ShareDialog {
        this.leftTitleListerner = lister
        return this
    }

    fun setRightTitleListerner(lister: View.OnClickListener): ShareDialog {
        this.rightTitleListerner = lister
        return this
    }

    @Subscribe
    public fun onMessageEvent(eventBusCenter: EventBusCenter<Object>) {

    }

    private fun share(friendsCircle: Boolean) {
        val webpage = WXWebpageObject()
        webpage.webpageUrl = "www.baidu.com"//分享url
        var msg = WXMediaMessage(webpage);
        msg.title = "分享标题";
        msg.description = "分享描述";
//        msg.thumbData =getThumbData();//封面图片byte数组
        var req = SendMessageToWX.Req();
        req.transaction = System.currentTimeMillis().toString()
        req.message = msg;
        if (friendsCircle) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline
        } else {
            req.scene = SendMessageToWX.Req.WXSceneSession
        }
        if (wxAPI == null) {
            wxAPI = WXAPIFactory.createWXAPI(context, Constants.WECHAT_APPID, true)
            wxAPI?.registerApp(Constants.WECHAT_APPID)
        }
        wxAPI?.sendReq(req)
    }
}