package com.sitong.changqin.mvp.model.bean

import com.google.gson.annotations.SerializedName

data class AppVersionBean(
        @SerializedName("isforce")
        var isforce: String,
        @SerializedName("url")
        var url: String
)