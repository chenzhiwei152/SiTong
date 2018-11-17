package com.sevenstringedzithers.sitong.ui.activity

import android.content.Intent
import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.base.BaseContext
import com.jyall.bbzf.base.EventBusCenter
import com.jyall.bbzf.extension.jump
import com.jyall.bbzf.extension.toast
import com.sevenstringedzithers.sitong.MainActivity
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.base.Constants
import com.sevenstringedzithers.sitong.mvp.contract.LoginContract
import com.sevenstringedzithers.sitong.mvp.model.bean.UserInfo
import com.sevenstringedzithers.sitong.mvp.model.bean.WeiXin
import com.sevenstringedzithers.sitong.mvp.model.bean.WeiXinToken
import com.sevenstringedzithers.sitong.mvp.persenter.LoginPresenter
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.auth.AuthInfo
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.WbConnectErrorMessage
import com.sina.weibo.sdk.auth.sso.SsoHandler
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.*
import org.greenrobot.eventbus.EventBus
import java.io.IOException
import kotlin.concurrent.thread


/**
 * create by chen.zhiwei on 2018-8-13
 */
class LoginActivity : BaseActivity<LoginContract.View, LoginPresenter>(), LoginContract.View, View.OnClickListener,IUiListener,com.sina.weibo.sdk.auth.WbAuthListener {
    private var mAccessToken:Oauth2AccessToken?=null
    override fun onSuccess(p0: Oauth2AccessToken?) {
        mAccessToken=p0
        var map = hashMapOf<String, String>()
        map.put("type", "1")
        map.put("uid", mAccessToken!!.uid)
        mPresenter?.login(map)
    }

    override fun onFailure(p0: WbConnectErrorMessage?) {
        dismissLoading()
        runOnUiThread{
            toast_msg(""+p0?.errorMessage)
        }
    }

    override fun cancel() {
        runOnUiThread {
        dismissLoading()
            toast_msg("取消登录")
        }
    }

    companion object {

        private  var  mSsoHandler:SsoHandler?=null

    }
    override fun onComplete(p0: Any?) {
        var data=p0.toString()
        val parser = JsonParser()
        val objectss = parser.parse(data).getAsJsonObject()
        var map = hashMapOf<String, String>()
        map.put("type", "1")
        map.put("uid", objectss.get("openid").asString)
        mPresenter?.login(map)
    }

    override fun onCancel() {
        toast_msg("取消登录")
        dismissLoading()
    }


    override fun onError(p0: UiError?) {
        toast_msg(""+p0?.errorMessage)
        dismissLoading()
    }

    private var mTencent:Tencent?=null
    private var wxAPI: IWXAPI? = null
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_login -> {
                var map = hashMapOf<String, String>()
                map.put("phone", et_phone.text.toString())
                map.put("passwd", et_pw.text.toString())
                map.put("type", "0")
                mPresenter?.login(map)
            }
            R.id.tv_register -> {
                jump<RegisterActivity>()
            }
            R.id.iv_weibo -> {
                showLoading()
                sinaLogin()
            }
            R.id.iv_weixin -> {
                showLoading()
                login()
            }
            R.id.iv_qq -> {
                showLoading()
                mTencent?.login(this@LoginActivity, "get_user_info", this)
            }
            R.id.tv_find_pw->{
                jump<FindPasswordOneActivity>()
            }
            R.id.tv_tourist->{
                var map = hashMapOf<String, String>()
                map.put("type", "2")
                mPresenter?.login(map)
            }
        }
    }

    override fun loginSuccess(user: UserInfo) {
        BaseContext.instance.setUserInfo(user)
        jump<MainActivity>()
        EventBus.getDefault().post(EventBusCenter<Int>(Constants.Tag.LOGIN_SUCCESS))
        finish()
    }

    override fun getPresenter(): LoginPresenter = LoginPresenter()

    override fun getRootView(): LoginContract.View = this


    override fun getLayoutId(): Int = R.layout.activity_login

    override fun initViewsAndEvents() {
        mTencent = Tencent.createInstance(Constants.WECHAT_APPID, this@LoginActivity)
        wxAPI = WXAPIFactory.createWXAPI(this, Constants.WECHAT_APPID, true)
        wxAPI?.registerApp(Constants.WECHAT_APPID)
        tv_login.setOnClickListener(this)
        tv_register.setOnClickListener(this)
        iv_weibo.setOnClickListener(this)
        iv_weixin.setOnClickListener(this)
        iv_qq.setOnClickListener(this)
        tv_tourist.setOnClickListener(this)
        tv_find_pw.setOnClickListener(this)
    }

    override fun isRegistEventBus(): Boolean = true
    override fun onMessageEvent(eventBusCenter: EventBusCenter<Object>) {
        super.onMessageEvent(eventBusCenter)
//        toast_msg(""+eventBusCenter?.evenCode)
        if (eventBusCenter != null) {
            when (eventBusCenter.evenCode) {
                Constants.Tag.WX_LOGIN -> {
                    var bean = eventBusCenter.data as WeiXin
//                    toast_msg("获取到的code"+bean.code)
                    getAccessToken(bean.getCode())
                }
                Constants.Tag.REGISTER_SUCCESS->{
                    finish()
                }
            }
        }
    }

    override fun isNeedLec(): View? = null

    override fun toast_msg(msg: String) {
        toast(msg)
    }


    /**
     * 微信登陆(三个步骤)
     * 1.微信授权登陆
     * 2.根据授权登陆code 获取该用户token
     * 3.根据token获取用户资料
     */
    fun login() {
        val req = SendAuth.Req()
        req.scope = "snsapi_userinfo"
        req.state = "wechat_sdk_demo_test"
        wxAPI?.sendReq(req)
//        toast_msg("发起微信")
    }

    fun getAccessToken(code: String) {
        val url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=" + Constants.WECHAT_APPID + "&secret=" + Constants.WECHAT_SECRET +
                "&code=" + code + "&grant_type=authorization_code"
        var client = OkHttpClient()
        thread {
            var request = Request.Builder()
                    .url(url)
                    .get()
                    .build()
            var response = client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
//                    var obj=response.body() as String
//                    var bean=WeiXinToken()
                    var obj = Gson().fromJson(response.body()?.string(), WeiXinToken::class.java)

//                    bean.access_token=response!!.body()?.string()
//                    bean.errcode=response.body().errcode
                    if (obj.getErrcode() === 0) {//请求成功
                        getWeiXinUserInfo(obj)
//                        toast_msg("请求成功")
                    } else {//请求失败
                    toast_msg(obj.getErrmsg())
                        dismissLoading()
//                        toast_msg("请求失败")
                    }
                }

            })

        }
    }

    fun getWeiXinUserInfo(weiXinToken: WeiXinToken) {
        val url = "https://api.weixin.qq.com/sns/userinfo?access_token=" +
                weiXinToken.getAccess_token() + "&openid=" + weiXinToken.getOpenid()

        var client = OkHttpClient()
        thread {
            var request = Request.Builder()
                    .url(url)
                    .get()
                    .build()
            var response = client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
//                    toast_msg("getUserInfo请求失败")
                }

                override fun onResponse(call: Call, response: Response) {
//                    var obj=response.body() as WeiXinInfo
                    var obj = Gson().fromJson(response.body()?.string(), WeiXinToken::class.java)
//                    toast_msg("getUserInfo请求成功")
//                        getWeiXinUserInfo(obj)
                    var map = hashMapOf<String, String>()
                    map.put("type", "1")
                    map.put("uid", obj.openid)
                    mPresenter?.login(map)
                }

            })

        }

    }

    /**
     * 新浪微博登录
     */
    private fun sinaLogin() {
        // 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
        var mAuthInfo = AuthInfo(this@LoginActivity, Constants.SINA_APP_ID, Constants.REDIRECT_URL, Constants.SCOPE)
        WbSdk.install(this,mAuthInfo)
        mSsoHandler = SsoHandler(this@LoginActivity)
        // SSO 授权, ALL IN ONE   如果手机安装了微博客户端则使用客户端授权,没有则进行网页授权
        mSsoHandler?.authorize(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Tencent.onActivityResultData(requestCode, resultCode, data, this)
        if (mSsoHandler != null) {
            mSsoHandler?.authorizeCallBack(requestCode, resultCode, data)
        }
    }
}