package com.wbtech.ums;

import android.content.Context;

/**
 * Created by sun.luwei on 2018/1/16.
 */

public class UseingLogUtil {

    public static void pageInit(String pageName){
        UsinglogManager.setActivityName(pageName);//页面统计
    }
    public static void clickEvent(Context context ,String eventName){
        UmsAgent.onEvent(context,eventName);//事件统计
    }
}
