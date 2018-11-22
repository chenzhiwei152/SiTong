package com.sevenstringedzithers.sitong.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import com.jyall.bbzf.base.EventBusCenter
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.base.Constants
import com.sevenstringedzithers.sitong.mvp.model.bean.WeiXin
import com.sevenstringedzithers.sitong.ui.listerner.RVAdapterItemOnClick
import com.tencent.connect.share.QQShare
import com.tencent.connect.share.QzoneShare
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import kotlinx.android.synthetic.main.dialog_share.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


/**
 * create by chen.zhiwei on 2018-8-15
 */
class ShareDialog(context: Context?, leftTitle: String, rightTitle: String, title: String = "") : Dialog(context), IUiListener {
    override fun onComplete(p0: Any?) {
        shareCallBack?.onItemClicked("分享成功")
    }

    override fun onCancel() {
        shareCallBack?.onItemClicked("取消分享")
    }

    override fun onError(p0: UiError?) {
        shareCallBack?.onItemClicked("分享失败")
    }

    var mContext: Context? = null
    private var wxAPI: IWXAPI? = null
    private var mTencent: Tencent? = null
    var shareCallBack: RVAdapterItemOnClick? = null

    init {
        this.mContext = context
        init(leftTitle, rightTitle, title)
    }

    fun init(leftTitle: String, rightTitle: String, title: String = "") {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        window!!.setContentView(R.layout.dialog_share)
        val window = window
        val wlp = window!!.attributes
        wlp.gravity = Gravity.BOTTOM
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = wlp
        iv_qq.setOnClickListener {
            qqShare(false)
        }
        iv_weixin.setOnClickListener {
            weixinShare(false)

        }
        iv_weixin_friend.setOnClickListener {
            weixinShare(true)
        }
        EventBus.getDefault().register(this)
    }


    fun setShareCallback(lister: RVAdapterItemOnClick): ShareDialog {
        this.shareCallBack = lister
        return this
    }


    @Subscribe
    public fun onMessageEvent(eventBusCenter: EventBusCenter<Object>) {
        if (eventBusCenter != null) {
            if (eventBusCenter.evenCode == Constants.Tag.WX_SHARE) {
                var bean = eventBusCenter.data as WeiXin
                when (bean.errCode) {
                    BaseResp.ErrCode.ERR_OK -> {
                        shareCallBack?.onItemClicked("分享成功")
                    }
                    BaseResp.ErrCode.ERR_USER_CANCEL -> {
                        shareCallBack?.onItemClicked("分享取消")
                    }
                    BaseResp.ErrCode.ERR_AUTH_DENIED -> {
                        shareCallBack?.onItemClicked("分享失败")
                    }
                }
            }
        }
    }

    private fun weixinShare(friendsCircle: Boolean) {
        val webpage = WXWebpageObject()
        webpage.webpageUrl = "www.baidu.com"//分享url
        var msg = WXMediaMessage(webpage)
        msg.title = "分享标题"
        msg.description = "分享描述"
//        msg.thumbData =getThumbData();//封面图片byte数组
        var req = SendMessageToWX.Req()
        req.transaction = System.currentTimeMillis().toString()
        req.message = msg
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

    private fun qqShare(isFriend: Boolean) {
        /*Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_QQ_SHARE || resultCode == Constants.REQUEST_QZONE_SHARE || resultCode == Constants.REQUEST_OLD_SHARE) {
                Tencent.handleResultData(data, mIUiListener);
            }
        }

*/
        if (mTencent == null) {
            mTencent = Tencent.createInstance(Constants.WECHAT_APPID, context)
        }
        if (isFriend) {
            shareToQQZone()
        } else {
            shareToQQ()
        }
    }

    private fun shareToQQ() {
        val params = Bundle()
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT)
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "医生工作站")// 标题
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "最新前沿文章推荐阅读")// 摘要
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "")// 内容地址
        // params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");// 网络图片地址　　
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "医师工作站")// 应用名称
        // params.putString(QQShare.SHARE_TO_QQ_EXT_INT, "其它附加功能");
        // 分享操作要在主线程中完成
        mTencent?.shareToQQ(scanForActivity(context), params, this)
    }

    private fun shareToQQZone() {
        val params = Bundle()
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT)
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "医生工作站")//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "最新前沿文章推荐阅读")//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "")//必填
        val imageUrls = arrayListOf<String>()
        imageUrls.add("http://www.beehood.com/uploads/allimg/150310/2-150310142133.jpg")
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls)// 必填，否则无法启动分享
        mTencent?.shareToQzone(context as Activity, params, this)
    }

    private fun  scanForActivity( cont:Context):Activity?{
        if (cont == null)
            return null;
        else if (cont is Activity)
            return  cont
        else if (cont is ContextWrapper)
            return scanForActivity(( cont ).getBaseContext())

        return null;
    }
}