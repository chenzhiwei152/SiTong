/**
 * Cobub Razor
 *
 * An open source analytics android sdk for mobile applications
 *
 * @package     Cobub Razor
 * @author      WBTECH Dev Team
 * @copyright   Copyright (c) 2011 - 2015, NanJing Western Bridge Co.,Ltd.
 * @license     http://www.cobub.com/products/cobub-razor/license
 * @link        http://www.cobub.com/products/cobub-razor/
 * @since       Version 0.1
 * @filesource 
 */
package com.wbtech.ums;

import android.content.Context;

import com.wbtech.ums.UmsAgent.SendPolicy;

import org.json.JSONException;
import org.json.JSONObject;

class TagManager {

    private String tags;
    private final String tag = "TagManager";
//    private final String TAG_URL = "/ums/tag.php";
    private final String TAG_URL = "/ums/posttag";

    public TagManager(Context context, String tags) {
        this.tags = tags;
    }

    private JSONObject prepareTagJSON(Context context) {
        JSONObject object = new JSONObject();
        try {
            object.put("tags", tags);
            object.put("deviceid", DeviceInfo.getDeviceId(context));
            object.put("appkey", AppInfo.getAppKey(context));
            object.put("channelId", AppInfo.getChannel(context));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public void PostTag(Context context) {
        JSONObject tagJSON;
        try {
            tagJSON = prepareTagJSON(context);
        } catch (Exception e) {
            CobubLog.e(tag, e);
            return;
        }

        if (CommonUtil.getReportPolicyMode(context) == SendPolicy.REALTIME
                && CommonUtil.isNetworkAvailable(context)) {

            String result = NetworkUtil.Post(
                    UmsConstants.urlPrefix + TAG_URL,
                    tagJSON.toString());

            MyMessage message = NetworkUtil.parse(result);
            if (message == null) {
                CommonUtil.saveInfoToFile("tags", tagJSON, context);
                return;
            }
            if (message.getFlag() < 0) {
                CobubLog.e(tag, "Error Code=" + message.getFlag() + ",Message=" + message.getMsg());
                if (message.getFlag() == -4)
                    CommonUtil.saveInfoToFile("tags", tagJSON, context);
            }
        } else {
            CommonUtil.saveInfoToFile("tags", tagJSON, context);
        }

    }

}
