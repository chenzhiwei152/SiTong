package com.sitong.changqin.base


data class OSSPermissionBean(
        var token: Token,
        var endpoint: String,
        var bucket: String,
        var objectkey: String,
        var callbackBody: String,
        var callbackBodyType: String,
        var callbackurl: String
) {

    data class Token(
            var AccessKeySecret: String,
            var SecurityToken: String,
            var Expiration: String,
            var AccessKeyId: String
    )
}