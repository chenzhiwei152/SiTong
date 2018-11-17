package com.sevenstringedzithers.sitong.mvp.model.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class UserInfo(
        @SerializedName("nickname") var nickname: String,
        @SerializedName("phone") var phone: String,
        @SerializedName("email") var email: String,
        @SerializedName("header") var header: String,
        @SerializedName("token") var token: String,
        @SerializedName("address") var address: String,
        @SerializedName("vip") var vip: Boolean,
        @SerializedName("award") var award: String,
        @SerializedName("level") var level: String,
        @SerializedName("duration") var duration: String,
        @SerializedName("days") var days: String,
        @SerializedName("msgs") var msgs: String,
        @SerializedName("carillon") var carillon: String
) : Serializable