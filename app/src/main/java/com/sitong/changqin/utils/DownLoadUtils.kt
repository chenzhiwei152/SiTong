package com.sitong.changqin.utils

import android.content.Context
import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.jyall.bbzf.api.scheduler.APIManager
import com.jyall.bbzf.api.scheduler.CommonObserver
import com.jyall.bbzf.api.scheduler.ErrorResponseBean
import com.jyall.bbzf.base.BaseBean
import com.sitong.changqin.base.OSSPermissionBean
import com.sitong.changqin.ui.listerner.ProgressCallback
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import retrofit2.Response

class DownLoadUtils {
    companion object {
        fun downLoadMusic(mContext: Context, url: String, callback: ProgressCallback) {
//            var map = hashMapOf<String, String>()
//            map.put("path", url)
            var ss: Observable<Response<BaseBean<OSSPermissionBean>>>? = null

            ss = APIManager.jyApi.getOOSPremession().compose(SchedulerUtils.ioToMain())

            ss!!.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : CommonObserver<BaseBean<OSSPermissionBean>>() {
                        override fun onError(errorResponseBean: ErrorResponseBean): Boolean = false
                        override fun onFail(errorResponseBean: BaseBean<OSSPermissionBean>): Boolean = false

                        override fun onSuccess(body: BaseBean<OSSPermissionBean>) {
                            downLoad(mContext, url, callback, body.data)
                        }

                        override fun onError(e: Throwable?) {
                            super.onError(e)
//                            ExtraUtils.toasts(e?.message!!)
                            callback.onProgressFailed()
                        }

                    })
        }

        fun downLoad(mContext: Context, url: String, callback: ProgressCallback, oss: OSSPermissionBean) {
            //初始化OssService类，参数分别是Content，accessKeyId，accessKeySecret，endpoint，bucketName（后4个参数是您自己阿里云Oss中参数）
            var ossService = OssService(mContext, oss.token.AccessKeyId, oss.token.AccessKeySecret, oss.endpoint, oss.bucket, oss.token.SecurityToken)
////初始化OSSClient
            ossService.initOSSClient()
////开始上传，参数分别为content，上传的文件名filename，上传的文件路径filePath
            ossService.beginLoad(url, oss)
////上传的进度回调
            ossService.progressCallback = object : ProgressCallback {
                override fun onProgressSuccess() {
                    callback.onProgressSuccess()
                }

                override fun onProgressCallback(progress: Double) {
                    callback.onProgressCallback(progress)
                }

                override fun onProgressFailed() {
                    callback.onProgressFailed()
                }


            }

        }

    }
}