package com.sevenstringedzithers.sitong.utils.uploadimage

/**
 * Created by chenzhiwei
 */

class ImageBean {

    var url: String? = null
    var type: Int = 0

    constructor() {}

    constructor(url: String, type: Int) {
        this.url = url
        this.type = type
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val bean = o as ImageBean?

        if (type != bean!!.type) return false
        return if (url != null) url == bean.url else bean.url == null
    }

    override fun hashCode(): Int {
        var result = if (url != null) url!!.hashCode() else 0
        result = 31 * result + type
        return result
    }
}
