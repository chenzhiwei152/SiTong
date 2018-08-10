/**
 * Cobub Razor
 *
 * An open source analytics android sdk for mobile applications
 *
 * @package		Cobub Razor
 * @author		WBTECH Dev Team
 * @copyright	Copyright (c) 2011 - 2015, NanJing Western Bridge Co.,Ltd.
 * @license		http://www.cobub.com/products/cobub-razor/license
 * @link		http://www.cobub.com/products/cobub-razor/
 * @since		Version 0.1
 * @filesource
 */

package com.wbtech.ums;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

class MyCrashHandler implements UncaughtExceptionHandler {
    private final String tag = "MyCrashHandler";
    private static MyCrashHandler myCrashHandler;
    private Context context;
    private Object stacktrace;
    private String activities;
    private String time;
    private String appkey;
    private String os_version;

    // private MyCrashHandler() {
    //
    // }

    public static synchronized MyCrashHandler getInstance() {
        if (myCrashHandler != null) {
            return myCrashHandler;
        } else {
            myCrashHandler = new MyCrashHandler();
            return myCrashHandler;
        }
    }

    private MyCrashHandler() {
        super();
    }

    public void init(Context context) {
        this.context = context;
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable arg1) {
        Log.e("AndroidRuntime", getErrorInfo(arg1));
        new Thread() {
            @Override
            public void run() {
                super.run();

                Looper.prepare();
                String errorinfo = getErrorInfo(arg1);

                String[] ss = errorinfo.split("\n\t");
                String headstring = ss[0] + "\n\t" + ss[1] + "\n\t" + ss[2];

                if (headstring.length() > 255) {
                    headstring = headstring.substring(0, 255) + "\n\t";
                } else {
                    headstring = headstring + "\n\t";
                }

                String newErrorInfoString = headstring + errorinfo;

                stacktrace = newErrorInfoString;
                activities = CommonUtil.getActivityName(context);
                time = DeviceInfo.getDeviceTime();
                appkey = AppInfo.getAppKey(context);
                os_version = DeviceInfo.getOsVersion();
                JSONObject errorInfo = getErrorInfoJSONString(context);
                CobubLog.i(tag, errorinfo.toString());
//                if (CommonUtil.getReportPolicyMode(context) == SendPolicy.REALTIME
//                        && CommonUtil.isNetworkAvailable(context)) {
//                    if (!stacktrace.equals("")) {
//                        String result = NetworkUtil.Post(
//                                UmsConstants.urlPrefix + ErrorManager.ERROR_URL,
//                                errorInfo.toString());
//                        MyMessage message = NetworkUtil.parse(result);
//                        if (message == null) {
//                            CommonUtil.saveInfoToFile("errorInfo", errorInfo, context);
//                            return;
//                        }
//                        if (message.getFlag() < 0) {
//                            CobubLog.e(tag, "Error Code=" + message.getFlag() + ",Message="
//                                    + message.getMsg());
//                            if (message.getFlag() == -4)
//                                CommonUtil.saveInfoToFile("errorInfo", errorInfo, context);
//                        }
//                    }
//                } else {
                    
//                }
                    CommonUtil.saveInfoToFileinMain("errorInfo", errorInfo, context);
                    android.os.Process.killProcess(android.os.Process.myPid());
                        
                    
                    Looper.loop();
            }

        }.start();

    }

    private JSONObject getErrorInfoJSONString(Context context) {
        JSONObject errorInfo = new JSONObject();
        try {
            errorInfo.put("stacktrace", stacktrace);
            errorInfo.put("time", time);
            errorInfo.put("version", AppInfo.getAppVersion(context));
            errorInfo.put("activity", activities);
            errorInfo.put("appkey", appkey);
            errorInfo.put("channelId", AppInfo.getChannel(context));
            errorInfo.put("os_version", os_version);
            errorInfo.put("deviceid", DeviceInfo.getDeviceName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return errorInfo;
    }

    private String getErrorInfo(Throwable arg1) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        arg1.printStackTrace(pw);
        pw.close();
        String error = writer.toString();
        return error;
    }

}
