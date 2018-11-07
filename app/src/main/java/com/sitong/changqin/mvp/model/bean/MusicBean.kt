package com.sitong.changqin.mvp.model.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class MusicBean(
        @SerializedName("level_name") var levelName: String,
        @SerializedName("type") var type: Int,
        @SerializedName("musics") var musics: ArrayList<Music>
) :Serializable{

    data class Music(
            @SerializedName("levelcode") var levelcode: Int,
            @SerializedName("iscollection") var iscollection: Boolean,
            @SerializedName("en_name") var enName: String,
            @SerializedName("name") var name: String,
            @SerializedName("isbuy") var isbuy: Boolean,
            @SerializedName("level") var level: String,
            @SerializedName("onshelf") var onshelf: Int,
            @SerializedName("id") var id: Long,
            @SerializedName("icon") var icon: String
    ):Serializable
}