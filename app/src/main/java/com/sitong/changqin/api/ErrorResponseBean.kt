package com.jyall.bbzf.api.scheduler

import com.google.gson.annotations.SerializedName

data class ErrorResponseBean(@SerializedName("error_code") var code: Int = 0,
                             @SerializedName("message") var message: String? ,
                             @SerializedName("detail") var detail: String? )