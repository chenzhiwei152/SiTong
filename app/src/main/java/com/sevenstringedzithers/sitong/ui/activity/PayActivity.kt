package com.sevenstringedzithers.sitong.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.alipay.sdk.app.PayTask
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.base.EventBusCenter
import com.jyall.bbzf.extension.toast
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.base.Constants
import com.sevenstringedzithers.sitong.mvp.contract.PayContract
import com.sevenstringedzithers.sitong.mvp.model.bean.*
import com.sevenstringedzithers.sitong.mvp.persenter.PayPresenter
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.android.synthetic.main.activity_pay.*
import kotlinx.android.synthetic.main.layout_common_title.*
import org.greenrobot.eventbus.EventBus

class PayActivity : BaseActivity<PayContract.View, PayPresenter>(), PayContract.View {
    private val SDK_PAY_FLAG = 1
    private val SDK_AUTH_FLAG = 2
    @SuppressLint("HandlerLeak")
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                SDK_PAY_FLAG -> {
                    val payResult = PayResult(msg.obj as Map<String, String>)
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    val resultInfo = payResult.getResult()// 同步返回需要验证的信息
                    val resultStatus = payResult.getResultStatus()
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        showAlert(this@PayDemoActivity, "支付成功: $payResult")
                        toast_msg("支付宝支付成功")
                        EventBus.getDefault().post(EventBusCenter<Int>(Constants.Tag.RELOAD_USERINFO))
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        toast_msg("支付宝支付失败")
                    }
                }
//                SDK_AUTH_FLAG -> {
//                    val authResult = AuthResult(msg.obj as Map<String, String>, true)
//                    val resultStatus = authResult.getResultStatus()
//
//                    // 判断resultStatus 为“9000”且result_code
//                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
//                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
//                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
//                        // 传入，则支付账户为该授权账户
////                        showAlert(this@PayDemoActivity, "授权成功: $authResult")
//                    } else {
//                        // 其他状态值则为授权失败
////                        showAlert(this@PayDemoActivity, "授权失败: $authResult")
//                    }
//                }
                else -> {
                }
            }
        }
    }
    private var type: Int? = 0
    private var wxAPI: IWXAPI? = null
    override fun getPresenter(): PayPresenter = PayPresenter()
    override fun getRootView(): PayContract.View = this
    /**
     * 获取权限使用的 RequestCode
     */
    private val PERMISSIONS_REQUEST_CODE = 1002

    override fun toast_msg(msg: String) {
        toast(msg)
    }

    override fun getOrderSuccess(order: OrderBean) {
        if (type == 0) {
            mPresenter?.getWXPermission(order.orderid)
        } else {
            mPresenter?.getaliPermission(order.orderid)
        }
    }

    override fun getAliPermissionSuccess(ali: AliPremissionBean) {
        dismissLoading()
        payV2(ali.orderstring)
    }

    override fun getWXpermissionSuccess(wx: WXPremissionBean) {
        dismissLoading()
        pay(wx)
    }

    private var musicId: String? = null
    private var bean: MemberBean? = null
    var map = hashMapOf<String, String>()

    override fun getLayoutId(): Int = R.layout.activity_pay

    override fun initViewsAndEvents() {
        initTitle()
        var bundle = intent.extras
        if (bundle != null) {
            musicId = bundle.getString("musicId")
            bean = bundle.getParcelable("bean") as MemberBean
            tv_title.text = bean?.name
            tv_name.text = bean?.name
            tv_price.text = bean?.price
            tv_date.text = bean?.duration
        }

        if (musicId.isNullOrEmpty()) {
            map.put("type", "1")
            map.put("memberid", bean?.id!!)
        } else {
            map.put("type", "0")
            map.put("musicid", musicId!!)
            map.put("memberid", bean?.id!!)
        }

        wxAPI = WXAPIFactory.createWXAPI(this, Constants.WECHAT_APPID, true)
        wxAPI?.registerApp(Constants.WECHAT_APPID)
        rl_wx.setOnClickListener {
            type = 0
            mPresenter?.getOrder(map)

        }

        rl_ali.setOnClickListener {
            type = 1
            mPresenter?.getOrder(map)
        }
    }

    override fun isRegistEventBus(): Boolean = true
    override fun onMessageEvent(eventBusCenter: EventBusCenter<Object>) {
        super.onMessageEvent(eventBusCenter)
        if (eventBusCenter != null) {
            if (eventBusCenter.evenCode == Constants.Tag.WX_PAY) {
                var bean = eventBusCenter.data as WeiXin
                if (bean.getErrCode() == BaseResp.ErrCode.ERR_OK) {
//                    成功
                    toast_msg("微信支付成功")
                    EventBus.getDefault().post(EventBusCenter<Int>(Constants.Tag.RELOAD_USERINFO))
                }
            }
        }
    }

    override fun isNeedLec(): View? = null

    companion object {
        fun newIntentce(mContext: Context, bean: MemberBean, musicId: String = "") {
            var intents = Intent(mContext, PayActivity::class.java)
            var bun = Bundle()
            bun.putParcelable("bean", bean)
            bun.putString("musicId", musicId)
            intents.putExtras(bun)
            mContext.startActivity(intents)
        }
    }

    private fun initTitle() {
        iv_back.setOnClickListener { finish() }
        tv_title.setText("")
        iv_menu.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        requestPermission()
    }
    /**
     * 微信发起支付
     * @param weiXinPay
     */
    fun pay(weiXinPay: WXPremissionBean) {
        val req = PayReq()
        req.appId = Constants.WECHAT_APPID//appid
        req.nonceStr = weiXinPay.noncestr//随机字符串，不长于32位。推荐随机数生成算法
        req.packageValue = weiXinPay.packageX//暂填写固定值Sign=WXPay
        req.sign = weiXinPay.sign//签名
        req.partnerId = weiXinPay.partnerid//微信支付分配的商户号
        req.prepayId = weiXinPay.prepayid//微信返回的支付交易会话ID
        req.timeStamp = weiXinPay.timestamp//时间戳
        wxAPI?.registerApp(Constants.WECHAT_APPID)
        wxAPI?.sendReq(req)
    }

    /**
     * 支付宝支付业务示例
     */
    fun payV2(orderInfo: String) {

        val payRunnable = Runnable {
            val alipay = PayTask(this@PayActivity)
            val result = alipay.payV2(orderInfo, true)
            Log.i("msp", result.toString())

            val msg = Message()
            msg.what = SDK_PAY_FLAG
            msg.obj = result
            mHandler.sendMessage(msg)
        }

        val payThread = Thread(payRunnable)
        payThread.start()
    }

    /**
     * 检查支付宝 SDK 所需的权限，并在必要的时候动态获取。
     * 在 targetSDK = 23 以上，READ_PHONE_STATE 和 WRITE_EXTERNAL_STORAGE 权限需要应用在运行时获取。
     * 如果接入支付宝 SDK 的应用 targetSdk 在 23 以下，可以省略这个步骤。
     */
    private fun requestPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_CODE)

        } else {
//            showToast(this, "支付宝 SDK 已有所需的权限")
        }
    }

    /**
     * 权限获取回调
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> {

                // 用户取消了权限弹窗
                if (grantResults.size == 0) {
                    toast_msg("无法获取支付宝 SDK 所需的权限, 请到系统设置开启")
                    return
                }

                // 用户拒绝了某些权限
                for (x in grantResults) {
                    if (x == PackageManager.PERMISSION_DENIED) {
                        toast_msg("无法获取支付宝 SDK 所需的权限, 请到系统设置开启")
                        return
                    }
                }

                // 所需的权限均正常获取
                toast_msg("支付宝 SDK 所需的权限已经正常获取")
            }
        }
    }
}