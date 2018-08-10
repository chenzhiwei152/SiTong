package com.sitong.changqin.mvp.contract

import com.jyall.bbzf.base.IBaseView
import com.jyall.bbzf.base.IPresenter

interface LoginContract {

    interface View : IBaseView {
        /**
         * 发送验证码
         */
        fun sendCodeSuccess()

        /**
         * 校验验证码
         */
        fun checkCodeSuccess()

        /**
         * 错误显示弹窗
         */
        fun showTaost(DialogMsg: String)
        /**
         * 错误toast
         */
        fun showErrorDialog(dialogTitle:String,dialogMsg: String,directLogin:Boolean=false)

        /**
         * 直接登录
         */
        fun directLogin()

        /**
         * 注册完成
         */
        fun registerComplete(){

        }
    }

    interface Presenter : IPresenter<View> {
        /**
         * 发送验证码
         */
        fun sendCode(phone: String)
        /**
         * 校验验证码
         */
        fun checkCode(cityId:String,phone: String,shortCode:String)

        /**
         * 用户角色(10002经纪人，10003用户)
         */
        fun login(cityId:String,phone: String,userRole:String)
    }
}