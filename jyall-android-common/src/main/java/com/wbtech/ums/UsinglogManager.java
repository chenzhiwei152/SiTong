/**
 * Cobub Razor
 * <p>
 * An open source analytics android sdk for mobile applications
 *
 * @package Cobub Razor
 * @author WBTECH Dev Team
 * @copyright Copyright (c) 2011 - 2015, NanJing Western Bridge Co.,Ltd.
 * @license http://www.cobub.com/products/cobub-razor/license
 * @link http://www.cobub.com/products/cobub-razor/
 * @filesource
 * @since Version 0.1
 */

package com.wbtech.ums;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.wbtech.ums.UmsAgent.SendPolicy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

public class UsinglogManager {

    private final String tag = "UsinglogManager";
    //    private final String USINGLOG_URL = "/ums/usinglog.php";
    private final String USINGLOG_URL = "/ums/postActivityLog";

    private String session_id;
    private static String activities;
//    private String start_millis = null;
//    private String end_millis = null;
//    private long end = 0;
//    private long start = 0;
//    private String duration;
//    private String pageName;

    public UsinglogManager(Context context) {
    }

    JSONObject prepareUsinglogJSON(String start_millis, String end_millis, String duration, String activities, Context context) throws JSONException {
        JSONObject jsonUsinglog = new JSONObject();

        if (session_id == null) {
            try {
                session_id = CommonUtil.generateSession(context);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        jsonUsinglog.put("session_id", session_id);
        jsonUsinglog.put("start_millis", start_millis);
        jsonUsinglog.put("end_millis", end_millis);
        jsonUsinglog.put("duration", duration);
        jsonUsinglog.put("version", AppInfo.getAppVersion(context));
        jsonUsinglog.put("activities", activities);
        jsonUsinglog.put("appkey", AppInfo.getAppKey(context));
        jsonUsinglog.put("channelId", AppInfo.getChannel(context));
        jsonUsinglog.put("userid", CommonUtil.getUserIdentifier(context));
        jsonUsinglog.put("deviceid", DeviceInfo.getDeviceId(context));

        return jsonUsinglog;
    }

    public void onResume(final Context context) {
        if (checkNameIsEmpty()) {
            return;
        }
        CobubLog.i(tag, "Call onResume()");
        try {
            if (CommonUtil.isNewSession(context)) {
                session_id = CommonUtil.generateSession(context);
                CobubLog.i(tag, "New Sessionid is " + session_id);

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        CobubLog.i(tag, "Start postClientdata thread");
                        ClientdataManager cm = new ClientdataManager(context);
                        cm.postClientData(context);
                    }
                });
                thread.run();
            }
        } catch (Exception e) {
            CobubLog.e(tag, e);
        }
        CommonUtil.saveSessionTime(context);
        if (null != activities && !TextUtils.isEmpty(activities)) {
            CommonUtil.savePageName(context, activities);
        } else {
            CommonUtil.savePageName(context, CommonUtil.getActivityName(context));
        }

//        activities = CommonUtil.getActivityName(context);

//        start_millis = DeviceInfo.getDeviceTime();
//        start = Long.valueOf(System.currentTimeMillis());
    }

    public void onPause(Context context) {
        if (checkNameIsEmpty()) {
            return;
        }
        CobubLog.i(tag, "Call onPause()");

        SharedPrefUtil sp = new SharedPrefUtil(context);

        String pageName = sp.getValue("CurrentPage", "");
        Log.i("page=", pageName);
        long start = sp.getValue("session_save_time", System.currentTimeMillis());
        String start_millis = CommonUtil.getFormatTime(start);

        long end = System.currentTimeMillis();
        String end_millis = CommonUtil.getFormatTime(end);

        String duration = end - start + "";

        CommonUtil.saveSessionTime(context);

        JSONObject info;
        try {
            byte[] b1;
            b1 = pageName.getBytes("UTF-8");
            String s = new String(b1, "UTF-8");
            info = prepareUsinglogJSON(start_millis, end_millis, duration, s, context);
        } catch (Exception e) {
            CobubLog.e(tag, e);
            return;
        }

        if (CommonUtil.getReportPolicyMode(context) == SendPolicy.REALTIME
                && CommonUtil.isNetworkAvailable(context)) {
            CobubLog.i(tag, "post activity info");
            String result = NetworkUtil.Post(UmsConstants.urlPrefix
                            + USINGLOG_URL,
                    info.toString());
            MyMessage message = NetworkUtil.parse(result);
            if (message == null) {
                CommonUtil.saveInfoToFile("activityInfo", info, context);
                return;
            }

            if (message.getFlag() < 0) {
                CobubLog.e(tag, "Error Code=" + message.getFlag() + ",Message=" + message.getMsg());
                if (message.getFlag() == -4 || message.getFlag() == -5)
                    CommonUtil.saveInfoToFile("activityInfo", info, context);
            }
        } else {
            CommonUtil.saveInfoToFile("activityInfo", info, context);
        }
        activities = "";
    }

    void onWebPage(String pageName, Context context) {

        SharedPrefUtil sp = new SharedPrefUtil(context);
        String lastView = sp.getValue("CurrentPage", "");
        if (lastView.equals("")) {
            sp.setValue("CurrentPage", pageName);
            sp.setValue("session_save_time", System.currentTimeMillis());
        } else {
            long start = sp.getValue("session_save_time", Long.valueOf(System.currentTimeMillis()));
            String start_millis = CommonUtil.getFormatTime(start);

            long end = System.currentTimeMillis();
            String end_millis = CommonUtil.getFormatTime(end);

            String duration = end - start + "";

            sp.setValue("CurrentPage", pageName);
            sp.setValue("session_save_time", end);

            JSONObject obj;
            try {
                obj = prepareUsinglogJSON(start_millis, end_millis, duration, pageName, context);
            } catch (Exception e) {
                CobubLog.e(tag, e);
                return;
            }

            if (CommonUtil.getReportPolicyMode(context) == SendPolicy.REALTIME
                    && CommonUtil.isNetworkAvailable(context)) {

                CobubLog.i(tag, "post activity info");
                String result = NetworkUtil.Post(UmsConstants.urlPrefix
                                + USINGLOG_URL,
                        obj.toString());
                MyMessage message = NetworkUtil.parse(result);
                if (message == null) {
                    CommonUtil.saveInfoToFile("activityInfo", obj, context);
                    return;
                }

                if (message.getFlag() < 0) {
                    CobubLog.e(tag,
                            "Error Code=" + message.getFlag() + ",Message=" + message.getMsg());
                    if (message.getFlag() == -4 || message.getFlag() == -5)
                        CommonUtil.saveInfoToFile("activityInfo", obj, context);
                }
            } else {
                CommonUtil.saveInfoToFile("activityInfo", obj, context);
            }

        }

    }

    public static void setActivityName(String name) {
        byte[] b1;
        try {
            b1 = name.getBytes("UTF-8");
            String s = new String(b1, "UTF-8");
            activities = s;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            activities = name;
        }
    }

    private Boolean checkNameIsEmpty() {
        if (TextUtils.isEmpty(activities)) {
            return true;
        } else {
            return false;
        }
    }
}
