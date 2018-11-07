package com.sitong.changqin.mvp.model.bean


data class ArticleDetailBean(
        var name: String,
        var desc: String,
        var content: List<Content>
) {

    data class Content(
            var ctn: String,
            var img: String,
            var video: String
    )
}