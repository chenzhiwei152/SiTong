package com.jyall.bbzf.mvp.model.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserInfo(
        @SerializedName("userId") var userId: String,
        @SerializedName("phone") var phone: String,
        @SerializedName("token") var token: String,
        @SerializedName("userName") var userName: String,
        @SerializedName("userRole") var userRole: String,
        @SerializedName("sex") var sex: String,
        @SerializedName("isFirst") var isFirst: String,
        @SerializedName("isMute") var isMute: String,
        @SerializedName("cityId") var cityId: String,
        @SerializedName("cityName") var cityName: String,
        @SerializedName("serviceCityId") var serviceCityId: String,
        @SerializedName("serviceCityName") var serviceCityName: String,
        @SerializedName("userNameState") var userNameState: String,
        @SerializedName("userImage") var userImage: String,
        @SerializedName("userImageState") var userImageState: String,
        @SerializedName("userCompanyId") var userCompanyId: String,
        @SerializedName("userCompany") var userCompany: String,
        @SerializedName("userCompanyState") var userCompanyState: String,
        @SerializedName("qualifications") var qualifications: String,
        @SerializedName("qualificationsState") var qualificationsState: String,
        @SerializedName("count1") var count1: String,
        @SerializedName("count2") var count2: String,
        @SerializedName("count3") var count3: String,
        @SerializedName("count4") var count4: String,
        @SerializedName("count5") var count5: String,
        @SerializedName("count6") var count6: String,
        @SerializedName("count7") var count7: String,
        @SerializedName("count8") var count8: String

) : Serializable {

    /**
     * 经济人
     * **/
    fun isAgent(): Boolean {
        return "10002" == userRole
    }

    /**
     * 普通用户
     * **/
    fun isUser(): Boolean {
        return "10003" == userRole
    }

    /**
     * 审核中
     * */
    fun isShenHeZhong(): Boolean {
        return "1" == this.userNameState || "1" == this.userCompanyState || "1" == this.qualificationsState
    }

    fun isSetSex(): Boolean {
        return "2" == this.sex || "3" == this.sex
    }

    /**
     * 性别
     * */
    fun sex(): String {
        if (this.sex == "1") {
            return "保密"
        }
        if (this.sex == "2") {
            return "男"
        }
        return if (this.sex == "3") {
            "女"
        } else ""
    }

    /**
     * 审核完成
     * **/
    fun isShenHeWanCheng(): Boolean {
        return "2" == this.userNameState && "2" == this.userCompanyState && "2" == this.qualificationsState
    }

    /*
    * 待认证
    * */
    fun isWaitRenZheng(): Boolean {
        return "" == this.userCompany
    }

    fun isBuTongGuo(): Boolean {
        return "3" == this.userNameState || "3" == this.userCompanyState || "3" == this.qualificationsState
    }

    /**
     * 审核状态
     * **/
    fun getStateStr(): String {
        return when {
            isShenHeWanCheng() -> "（认证通过）"
            isBuTongGuo() -> "（认证不通过）"
            isWaitRenZheng() -> "（待认证）"
            else -> "（认证中）"
        }
    }

    fun isFirst(): Boolean {
        return "1" == (isFirst)
    }
}