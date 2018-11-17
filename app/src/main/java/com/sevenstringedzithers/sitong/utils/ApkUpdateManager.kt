package com.jyall.app.home.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.jyall.android.common.utils.NetUtil
import com.jyall.apkupdate.ApkDownloadServiceN
import com.jyall.apkupdate.ApkUpdateTool
import com.jyall.apkupdate.OnUpdateListener
import com.jyall.apkupdate.UpdateInfo
import com.jyall.bbzf.api.scheduler.APIManager
import com.jyall.bbzf.api.scheduler.CommonObserver
import com.jyall.bbzf.api.scheduler.ErrorResponseBean
import com.jyall.bbzf.base.BaseBean
import com.jyall.bbzf.extension.toast
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.mvp.model.bean.AppVersionBean
import com.sevenstringedzithers.sitong.utils.ExtraUtils

/**
 * Created by chenzhiwei on 2018/5/28.
 *
 */
class ApkUpdateManager {
    companion object {
        val instance: ApkUpdateManager by lazy { ApkUpdateManager() }
    }

    fun checkVersion(context: Context, showToast: Boolean) {
        if (!NetUtil.isNetworkConnected(context)) {
            if (showToast) {
                ExtraUtils.toasts("网络连接失败,请检查您的网络")
            }
            return
        }

        APIManager.jyApi.checkVersion().compose(SchedulerUtils.ioToMain())
                .subscribe(object : CommonObserver<BaseBean<AppVersionBean>>() {
                    override fun onError(errorResponseBean: ErrorResponseBean): Boolean {
                        return false
                    }

                    override fun onFail(errorResponseBean: BaseBean<AppVersionBean>): Boolean {
                        return false
                    }

                    override fun onSuccess(body: BaseBean<AppVersionBean>) {
                        val appVersionBean = body.data
                        //空实体，不需要升级
                        if (appVersionBean == null || appVersionBean.isforce?.equals("0")) {
                            return
                        }
                        val updateInfo = UpdateInfo()
                        updateInfo.updateUrl = appVersionBean.url
                        // 不需要升级上一步已过滤， 服务器返回的 version 是app版本号 versionName
                        // ApkUpdateTool 内部根据 versionCode 判断是否需要升级，该逻辑已放服务器中，本地暂时传一个int最大值，确保 需要升级 流程正常
//                        updateInfo.version = appVersionBean.version
                        updateInfo.version = Integer.MAX_VALUE.toString()
                        ApkUpdateTool(context, object : OnUpdateListener {
                            override fun initDialog(p0: UpdateInfo?) {
                                var versionDialogBuilder = AlertDialog.Builder(context)
                                var versionDialog: AlertDialog? = null
                                versionDialogBuilder.setTitle("新版本升级")
                                versionDialogBuilder.setCancelable(false)
                                versionDialogBuilder.setPositiveButton("升级", null)
                                if (appVersionBean.isforce.equals("1")) {
//普通更新
                                    versionDialogBuilder.setNegativeButton("取消", object : DialogInterface.OnClickListener {
                                        override fun onClick(p0: DialogInterface?, p1: Int) {
                                            versionDialog?.dismiss()
                                        }

                                    })
                                }
                                versionDialog = versionDialogBuilder.create()
                                versionDialog.show()
                                versionDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(object : View.OnClickListener {
                                    override fun onClick(p0: View?) {
                                        val intent = Intent(context, ApkDownloadServiceN::class.java)
                                        intent.putExtra("url", updateInfo.updateUrl)
                                        intent.putExtra("fileName", context.resources.getString(R.string.app_name))
                                        context.startService(intent)
                                        context.toast("正在下载，请稍后...")
//                                        versionDialog.setMessage("正在下载，请稍后~")
                                        if (appVersionBean.isforce.equals("1")) {
                                            versionDialog?.dismiss()
                                        }

                                    }

                                })

                            }

                            override fun checkSDPermision(): Boolean = true
                        }).updateVersion(context, updateInfo, showToast)
                    }
                })
    }
}