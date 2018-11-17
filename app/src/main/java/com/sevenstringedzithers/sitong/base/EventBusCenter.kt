package com.jyall.bbzf.base

/**
 */
class EventBusCenter<T> {

    /**
     * 接收的数据
     */
    var data: T? = null
    /**
     * 接收码
     */
    var evenCode = -1
        private set

    constructor(eventCode: Int) {
        this.evenCode = eventCode
    }

    constructor(eventCode: Int, data: T) {
        this.evenCode = eventCode
        this.data = data
    }


}
