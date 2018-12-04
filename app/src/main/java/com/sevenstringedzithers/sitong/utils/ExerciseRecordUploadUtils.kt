package com.sevenstringedzithers.sitong.utils

import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.jyall.bbzf.api.scheduler.APIManager
import com.jyall.bbzf.api.scheduler.CommonObserver
import com.jyall.bbzf.api.scheduler.ErrorResponseBean
import com.jyall.bbzf.base.BaseBean
import com.sevenstringedzithers.sitong.mvp.model.bean.ResultBean
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import retrofit2.Response

class ExerciseRecordUploadUtils {
    /*
    * 上传练琴记录
    * */
    companion object {
        fun uploadRecord(map: HashMap<String, String>) {
            var ss: Observable<Response<BaseBean<ResultBean>>>? = null
            ss = APIManager.jyApi.upload_exercise_record(map).compose(SchedulerUtils.ioToMain())
            ss.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : CommonObserver<BaseBean<ResultBean>>() {
                        override fun onError(errorResponseBean: ErrorResponseBean): Boolean = false

                        override fun onFail(errorResponseBean: BaseBean<ResultBean>): Boolean = false

                        override fun onSuccess(body: BaseBean<ResultBean>) {
                            ExtraUtils.toasts(body.message)
                        }

                    })
        }
    }
}