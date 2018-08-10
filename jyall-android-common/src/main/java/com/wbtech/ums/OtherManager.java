package com.wbtech.ums;

import android.content.Context;

import com.wbtech.ums.UmsAgent.SendPolicy;

import org.json.JSONException;
import org.json.JSONObject;

public class OtherManager {
    private String cid;
    private final String tag = "OtherManager";
    
    private final String USERID_URL = "/ums/postUserid";
    private final String CID_URL = "/ums/postPushid";
    

    public OtherManager(Context context) {
    }
    
    public OtherManager(Context context,String cid) {
        this.cid = cid;
    }

    private JSONObject prepareUserIDJSON(Context context) {
        
        JSONObject localJSONObject = new JSONObject();
        try
        {
            localJSONObject.put("appkey", AppInfo.getAppKey(context));
            localJSONObject.put("channelId", AppInfo.getChannel(context));
            localJSONObject.put("deviceid", DeviceInfo.getDeviceId(context));
            localJSONObject.put("userid", CommonUtil.getUserIdentifier(context));
        } catch (JSONException e)
        {
            CobubLog.e(tag, e.toString());
        }
        return localJSONObject;
    }
    
    private JSONObject prepareCIDJSON(Context context) {
        
        JSONObject localJSONObject = new JSONObject();
        try
        {
            localJSONObject.put("appkey", AppInfo.getAppKey(context));
            localJSONObject.put("channelId", AppInfo.getChannel(context));
            localJSONObject.put("deviceid", DeviceInfo.getDeviceId(context));
            localJSONObject.put("clientid", this.cid);
        } catch (JSONException e)
        {
            CobubLog.e(tag, e.toString());
        }
        return localJSONObject;
    }

    public void postUserId(Context context) {
        JSONObject localJSONObject;
        try {
            localJSONObject = prepareUserIDJSON(context);
        } catch (Exception e) {
            CobubLog.e(tag, e.toString());
            return;
        }
        if (CommonUtil.getReportPolicyMode(context) == SendPolicy.REALTIME
                && CommonUtil.isNetworkAvailable(context)) {

            String result = NetworkUtil.Post(UmsConstants.urlPrefix + USERID_URL,
                    localJSONObject.toString());
            MyMessage message = NetworkUtil.parse(result);
            if (message == null) {
                return;
            }
            if (message.getFlag() < 0) {
                CobubLog.e(tag, "Error Code=" + message.getFlag() + ",Message=" + message.getMsg());
                return;
            }

        } else {
            
            return;
        }

    }
    
    public void postCID(Context context) {
        try {
            SharedPrefUtil sp = new SharedPrefUtil(context);
            boolean postSuccess = sp.getValue("postCID", false);

            if (postSuccess) {
                return;
            }

            JSONObject localJSONObject;
            try {
                localJSONObject = prepareCIDJSON(context);
            } catch (Exception e) {
                CobubLog.e(tag, e.toString());
                return;
            }
            if (CommonUtil.getReportPolicyMode(context) == SendPolicy.REALTIME
                    && CommonUtil.isNetworkAvailable(context)) {

                String result = NetworkUtil.Post(UmsConstants.urlPrefix + CID_URL,
                        localJSONObject.toString());
                MyMessage message = NetworkUtil.parse(result);
                if (message == null) {
                    return;
                }
                if (message.getFlag() < 0) {
                    CobubLog.e(tag,
                            "Error Code=" + message.getFlag() + ",Message=" + message.getMsg());
                    return;
                }
                sp.setValue("postCID", true);

            } else {
                return;
            }
        } catch (Exception e) {

        }
        return;
    }
}

