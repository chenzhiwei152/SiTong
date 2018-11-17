package com.jyall.bbzf.api.scheduler

import com.jyall.android.common.utils.MyDeviceInfo
import com.jyall.android.common.utils.SysUtils
import com.jyall.bbzf.base.BaseContext
import java.util.*

object CommonHeaders {


    /**
     * 通用的http请求头 header
     */
    private var sCommonHeaders: HashMap<String, String>? = null

    init {
        sCommonHeaders = HashMap()
        sCommonHeaders!!["deviceid"] = MyDeviceInfo.getDeviceId(BaseContext.instance)
        sCommonHeaders!!["devicebrand"] = MyDeviceInfo.getDeviceName() //设备型号
        sCommonHeaders!!["systembrand"] = MyDeviceInfo.getOsVersion() //操作系统版本
        sCommonHeaders!!["version"] = SysUtils.getVersionName(BaseContext.instance)//应用版本
        sCommonHeaders!!["lon"] = "0"//该版本默认0
        sCommonHeaders!!["lat"] = "0" //该版本默认0
        sCommonHeaders!!["cityId"] = "0"//该版本默认0
        sCommonHeaders!!["deviceresolution"] = SysUtils.getScreenWidth(BaseContext.instance).toString() + "x" + SysUtils.getScreenHeight(BaseContext.instance)//分辨率
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                }

    /**
     * 设置通用的http请求的header
     */
    fun getCommonHeaders(): HashMap<String, String> {
        val signMap = HashMap<String, String>()
        signMap.putAll(sCommonHeaders!!)
        return signMap
    }


}