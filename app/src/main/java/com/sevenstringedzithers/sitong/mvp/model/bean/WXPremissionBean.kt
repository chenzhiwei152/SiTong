package com.sevenstringedzithers.sitong.mvp.model.bean

import com.google.gson.annotations.SerializedName

data class WXPremissionBean(
        @SerializedName("noncestr")
        var noncestr: String,
        @SerializedName("package")
        var packageX: String,
        @SerializedName("partnerid")
        var partnerid: String,
        @SerializedName("prepayid")
        var prepayid: String,
        @SerializedName("sign")
        var sign: String,
        @SerializedName("timestamp")
        var timestamp: String
)