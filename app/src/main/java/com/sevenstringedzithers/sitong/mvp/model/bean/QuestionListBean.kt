package com.sevenstringedzithers.sitong.mvp.model.bean

import com.google.gson.annotations.SerializedName

data class QuestionListBean(
        @SerializedName("id")
        var id: String,
        @SerializedName("title")
        var title: String,
        @SerializedName("total")
        var total: Int
)