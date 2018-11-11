package com.sitong.changqin.mvp.model.bean

import com.google.gson.annotations.SerializedName

data class Content(
        @SerializedName("ctn")
        var ctn: String,
        @SerializedName("img")
        var img: String,
        @SerializedName("video")
        var video: String
)