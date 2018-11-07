package com.sitong.changqin.mvp.model.bean


data class GetRewardBean(
        var nickname: String,
        var phone: String,
        var email: String,
        var header: String,
        var token: String,
        var address: String,
        var vip: Boolean,
        var award: String,
        var reason: String
)