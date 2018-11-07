package com.sitong.changqin.ui.activity

import android.content.Context
import android.content.Intent
import android.view.View
import com.jyall.bbzf.base.BaseActivity
import com.jyall.bbzf.base.EventBusCenter
import com.jyall.bbzf.extension.toast
import com.sitong.changqin.base.Constants
import com.sitong.changqin.mvp.contract.EditContract
import com.sitong.changqin.mvp.model.bean.UserInfo
import com.sitong.changqin.mvp.persenter.EditPresenter
import com.stringedzithers.sitong.R
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.layout_common_title.*
import org.greenrobot.eventbus.EventBus

/**
 * create by chen.zhiwei on 2018-8-15
 */
class EditActivity : BaseActivity<EditContract.View, EditPresenter>(), EditContract.View, View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_confirm -> {
                if (et_text.text.isNullOrEmpty()) {
                    toast_msg("内容不能为空")
                    return
                }
                if (mType.isNullOrEmpty()) {
                    toast_msg("类型错误")
                    return
                }
                var type: String? = null
                when (mType) {
                    "1" -> type = "nickname"
                    "2" -> type = "phone"
                    "3" -> type = "email"
                    "4" -> type = "address"
                }
                var map = hashMapOf<String, String>()
                map.put(type!!, et_text.text.toString())
                mPresenter?.updateUserInfo(map)
            }
        }
    }

    override fun getRootView(): EditContract.View = this

    override fun toast_msg(msg: String) {
        toast(msg)
    }

    override fun getDataSuccess(bean: UserInfo) {
        EventBus.getDefault().post(EventBusCenter<Int>(Constants.Tag.RELOAD_USERINFO))
        finish()
    }

    override fun getPresenter(): EditPresenter = EditPresenter()

    private var mType: String? = null
    private var mHint: String? = null
    private var mTitle: String? = null
    private var tabsTitle: ArrayList<String> = arrayListOf()


    override fun getLayoutId(): Int = R.layout.activity_edit

    override fun initViewsAndEvents() {
        if (intent?.extras != null) {
            mType = intent.extras.getString("mType")
            mHint = intent.extras.getString("mHint")
            mTitle = intent.extras.getString("mTitle")
        }
        if (mHint != null) {
            et_text.hint = mHint
        }
        if (mTitle != null) {
            tv_title.text = mTitle
        }
        tv_confirm.setOnClickListener(this)
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null

    companion object {
        fun newInstance(mContext: Context, mType: String, mHint: String, mTitle: String) {
            var inten = Intent(mContext, EditActivity::class.java)
            inten.putExtra("mType", mType)
            inten.putExtra("mHint", mHint)
            inten.putExtra("mTitle", mTitle)
            mContext.startActivity(inten)
        }
    }
}