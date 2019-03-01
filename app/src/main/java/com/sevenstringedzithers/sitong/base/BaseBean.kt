package com.sevenstringedzithers.sitong.base

import java.io.Serializable

/**
 * 数据格式约定
 * **/
data class BaseBean<T>(var statuscode: Int, var message: String, var data: T) : Serializable {
    fun isSuccess(): Boolean {
        return statuscode == 0
    }
}