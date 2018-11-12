package com.sitong.changqin.mvp.model.bean

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class MusicBean(
        @SerializedName("level_name") var levelName: String,
        @SerializedName("type") var type: Int,
        @SerializedName("musics") var musics: ArrayList<Music>
) : Parcelable {
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
    ) : Parcelable {
        constructor(source: Parcel) : this(
                source.readInt(),
                1 == source.readInt(),
                source.readString(),
                source.readString(),
                1 == source.readInt(),
                source.readString(),
                source.readInt(),
                source.readLong(),
                source.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeInt(levelcode)
            writeInt((if (iscollection) 1 else 0))
            writeString(enName)
            writeString(name)
            writeInt((if (isbuy) 1 else 0))
            writeString(level)
            writeInt(onshelf)
            writeLong(id)
            writeString(icon)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<Music> = object : Parcelable.Creator<Music> {
                override fun createFromParcel(source: Parcel): Music = Music(source)
                override fun newArray(size: Int): Array<Music?> = arrayOfNulls(size)
            }
        }
    }

    constructor(source: Parcel) : this(
            source.readString(),
            source.readInt(),
            ArrayList<Music>().apply { source.readList(this, Music::class.java.classLoader) }
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(levelName)
        writeInt(type)
        writeList(musics)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MusicBean> = object : Parcelable.Creator<MusicBean> {
            override fun createFromParcel(source: Parcel): MusicBean = MusicBean(source)
            override fun newArray(size: Int): Array<MusicBean?> = arrayOfNulls(size)
        }
    }
}