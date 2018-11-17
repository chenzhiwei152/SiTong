package com.sevenstringedzithers.sitong.mvp.model.bean


data class UploadFeedbackImageBean(
        var token: String,
        var endpoint: String,
        var bucket: String,
        var objectkey: String,
        var callbackurl: String
)