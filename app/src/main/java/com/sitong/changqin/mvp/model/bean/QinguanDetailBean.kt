package com.sitong.changqin.mvp.model.bean


data class QinguanDetailBean(
        var content: ArrayList<Content>,
        var name: String,
        var img: String,
        var desc: String
) {

    data class Content(
            var ctn: String,
            var img: String,
            var video: String
    )
}