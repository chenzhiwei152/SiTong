package com.sevenstringedzithers.sitong.mvp.model.bean

import com.google.gson.annotations.SerializedName

data class QinHallDetailBean(
        @SerializedName("content")
        var content: List<Content>,
        @SerializedName("desc")
        var desc: String,
        @SerializedName("img")
        var img: String,
        @SerializedName("name")
        var name: String
)