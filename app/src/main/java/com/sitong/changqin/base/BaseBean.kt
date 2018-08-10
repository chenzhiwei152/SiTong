package com.jyall.bbzf.base

import java.io.Serializable

/**
 * 数据格式约定
 * **/
data class BaseBean<T>(var resultTrue: Int, var message: String, var error_code: Int, var timestamp: Long, var data: T, var page: Page  ) : Serializable {
    fun isSuccess(): Boolean {
        return resultTrue == error_code
    }
}