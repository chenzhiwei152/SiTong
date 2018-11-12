package com.sitong.changqin.mvp.model.bean

import com.google.gson.annotations.SerializedName

data class OrderBean(
        @SerializedName("orderid")
        var orderid: String,
        @SerializedName("ordernum")
        var ordernum: String
)