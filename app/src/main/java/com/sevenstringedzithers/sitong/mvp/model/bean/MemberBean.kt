package com.sevenstringedzithers.sitong.mvp.model.bean

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class MemberBean(
        @SerializedName("desc")
        var desc: String?=null,
        @SerializedName("duration")
        var duration: String?=null,
        @SerializedName("memberid")
        var id: String?=null,
        @SerializedName("level")
        var level: String?=null,
        @SerializedName("levelcode")
        var levelcode: String?=null,
        @SerializedName("name")
        var name: String?=null,
        @SerializedName("price")
        var price: String?=null
) : Parcelable {
        constructor(source: Parcel) : this(
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                writeString(desc)
                writeString(duration)
                writeString(id)
                writeString(level)
                writeString(levelcode)
                writeString(name)
                writeString(price)
        }

        companion object {
                @JvmField
                val CREATOR: Parcelable.Creator<MemberBean> = object : Parcelable.Creator<MemberBean> {
                        override fun createFromParcel(source: Parcel): MemberBean = MemberBean(source)
                        override fun newArray(size: Int): Array<MemberBean?> = arrayOfNulls(size)
                }
        }
}