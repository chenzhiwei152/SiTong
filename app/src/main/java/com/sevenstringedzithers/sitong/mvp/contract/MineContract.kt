package com.sevenstringedzithers.sitong.mvp.contract

import com.sevenstringedzithers.sitong.base.IBaseView
import com.sevenstringedzithers.sitong.base.IPresenter
import com.sevenstringedzithers.sitong.mvp.model.bean.ExerciseRecordBean
import com.sevenstringedzithers.sitong.mvp.model.bean.UserInfo

interface MineContract {

    interface View : IBaseView {
        fun toast_msg(msg: String)
        fun getDataSuccess(bean: UserInfo)

        fun punchSuccess(msg: String)

        fun getExeRecordListSuccess(bean: ExerciseRecordBean)

    }

    interface Presenter : IPresenter<View> {

        fun getUserInfo()

        fun upLoadHeadImage(url: String)

        fun dailyPunch()

        fun getExeRecordList()

    }
}