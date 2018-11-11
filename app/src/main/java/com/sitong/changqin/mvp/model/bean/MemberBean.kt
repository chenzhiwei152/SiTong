package com.sitong.changqin.mvp.model.bean

import com.google.gson.annotations.SerializedName

data class MemberBean(
        @SerializedName("desc")
        var desc: String,
        @SerializedName("duration")
        var duration: String,
        @SerializedName("id")
        var id: String,
        @SerializedName("level")
        var level: String,
        @SerializedName("levelcode")
        var levelcode: String,
        @SerializedName("name")
        var name: String,
        @SerializedName("price")
        var price: String
)