package com.jyall.bbzf.api.scheduler

import com.google.gson.annotations.SerializedName

data class ErrorResponseBean(@SerializedName("statuscode") var code: Int = -1,
                             @SerializedName("message") var message: String? ,
                             @SerializedName("detail") var detail: String? )