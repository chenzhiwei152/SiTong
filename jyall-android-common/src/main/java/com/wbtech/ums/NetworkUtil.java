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


import com.jyall.android.common.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;


class NetworkUtil {
    private static final String TAG = "NetworkUtil";
    private static OkHttpClient okHttpClient = initOkHttp();

    public static String Post(String url, String data) {

        CobubLog.d(TAG, "URL = " + url);
        CobubLog.d(TAG, "Data = " + data);

        try {

            RequestBody formBody = new FormBody.Builder()
                    .add("content", data)
                    .build();
            Request request = new Request.Builder().url(url).post(formBody).build();
            Call call = genericClient().newCall(request);
            Response response = call.execute();
            //请求执行成功
            if (response.isSuccessful()) {
                String result = response.body().string();
                LogUtils.e(result);
                return URLDecoder.decode(result);
            } else return null;


        } catch (Exception e) {
            CobubLog.e(TAG, e);
            return null;
        }
    }
    public static OkHttpClient genericClient() {
        if (okHttpClient == null) {
            okHttpClient = initOkHttp();
        }
        return okHttpClient;
    }

    private static OkHttpClient initOkHttp() {
        return new OkHttpClient.Builder()
                .connectTimeout(20000L, TimeUnit.MILLISECONDS)//超时
                .readTimeout(20000L, TimeUnit.MILLISECONDS)
                .addInterceptor(getLogInterceptor())//打印http整个请求的Log
                .build();
    }
    private static HttpLoggingInterceptor getLogInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtils.e("RetrofitLog", "retrofitBack = " + message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    public static MyMessage parse(String str) {
        try {
            if (str == null)
                return null;
            JSONObject jsonObject = new JSONObject(str);
            MyMessage message = new MyMessage();
            message.setFlag(jsonObject.getInt("flag"));
            message.setMsg(jsonObject.getString("msg"));
            return message;
        } catch (JSONException e) {
            CobubLog.e(TAG, e);
            return null;
        } catch (Exception e1) {

            CobubLog.e(TAG, e1);
            return null;
        }
    }

}
