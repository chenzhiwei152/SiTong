package com.sitong.changqin.mvp.model.bean


data class MessageListBean(
        var total: Int,
        var title: String,
        var id: String,
        var content: String,
        var parambody: String,
        var sendtime: String,
        var isread: String,
        var isreceivereward: Boolean,
        var award: Int,
        var reason: String
)