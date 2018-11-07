package com.sitong.changqin.utils

import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.jyall.bbzf.api.scheduler.APIManager
import com.jyall.bbzf.api.scheduler.CommonObserver
import com.jyall.bbzf.api.scheduler.ErrorResponseBean
import com.jyall.bbzf.base.BaseBean
import com.sitong.changqin.mvp.model.bean.ResultBean
import com.sitong.changqin.ui.listerner.ResultCallback
import io.reactivex.android.schedulers.AndroidSchedulers

/*
* 收藏工具类
* */
class CollectionUtils {
    companion object {
//        1,收藏  0， 取消收藏
        fun collectionUtils(type: Int = 0,musicId:String,callback:ResultCallback<String>) {
    var map= hashMapOf<String,String>()
    map.put("collection",""+type)
    map.put("music",""+musicId)
    APIManager.jyApi.collection(map).compose(SchedulerUtils.ioToMain()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CommonObserver<BaseBean<ResultBean>>() {
                override fun onError(errorResponseBean: ErrorResponseBean): Boolean {
                    callback.onsFailed(errorResponseBean.message)
                    return false
                }

                override fun onFail(errorResponseBean: BaseBean<ResultBean>): Boolean {
                    callback.onsFailed(errorResponseBean.message)
                    return false
                }

                override fun onSuccess(body: BaseBean<ResultBean>) {
                    if (body.data.result==1){
                        callback.onSuccess(body.message)
                    }else{
                        callback.onsFailed(body.message)
                    }
                }

            })

        }
    }
}