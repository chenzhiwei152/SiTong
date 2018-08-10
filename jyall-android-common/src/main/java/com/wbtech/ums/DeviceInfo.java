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

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author apple
 */
class DeviceInfo {

    private static final String tag = "DeviceInfo";


    static int phoneType = -1;
    static String subscriberId = "";
    static String deviceId = "";
    static String simSerialNumber = "";
    static String networkOperator = "";
    static String loacationStat;
    static double latitude;
    static double altitude;
    static boolean isBluetoothAvaliable = false;

    public static void init(Context context) {
        TelephonyManager telephonyManager;
        Location location;
        BluetoothAdapter bluetoothAdapter;
        try {
            telephonyManager = (TelephonyManager) (context
                    .getSystemService(Context.TELEPHONY_SERVICE));

            if (telephonyManager == null) {
                phoneType = -1;
            } else {
                phoneType = telephonyManager.getPhoneType();
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    subscriberId = telephonyManager.getSubscriberId();
                    deviceId = telephonyManager.getDeviceId();
                    simSerialNumber = telephonyManager.getSimSerialNumber();
                    networkOperator = telephonyManager.getNetworkOperator();

                }

            }

            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
                isBluetoothAvaliable = false;
            } else {
                isBluetoothAvaliable = true;
            }

        } catch (Exception e) {
            CobubLog.e(tag, e.toString());
        }
        location = getLocation(context);
        if(null != location){
            latitude = location.getLatitude();
            altitude = location.getAltitude();
        }

        loacationStat = getLoacationStat(location);
    }

    public static String getLanguage() {
        String language = Locale.getDefault().getLanguage();
        CobubLog.i(tag, "getLanguage()=" + language);
        if (language == null)
            return "";
        return language;
    }

    public static String getResolution(Context context) {

        DisplayMetrics displaysMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displaysMetrics);
        CobubLog.i(tag, "getResolution()=" + displaysMetrics.widthPixels + "x"
                + displaysMetrics.heightPixels);
        return displaysMetrics.widthPixels + "x" + displaysMetrics.heightPixels;
    }

    public static String getDeviceProduct() {
        String result = Build.PRODUCT;
        CobubLog.i(tag, "getDeviceProduct()=" + result);
        if (result == null)
            return "";
        return result;
    }

    public static boolean getBluetoothAvailable() {

        return isBluetoothAvaliable;
    }

    private static boolean isSimulator(Context context) {
        if (getDeviceIMEI(context).equals("000000000000000"))
            return true;
        else
            return false;
    }

    public static boolean getGravityAvailable(Context context) {
        try {
            SensorManager sensorManager;
            // This code getSystemService(Context.SENSOR_SERVICE);
            // often hangs out the application when it runs in Android Simulator.
            // so in simulator, this line will not be run.
            if (isSimulator(context))
                sensorManager = null;
            else
                sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            CobubLog.i(tag, "getGravityAvailable()");
            return (sensorManager == null) ? false : true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getOsVersion() {
        String result = Build.VERSION.RELEASE;
        CobubLog.i(tag, "getOsVersion()=" + result);
        if (result == null)
            return "";

        return result;
    }

    /**
     * Returns a constant indicating the device phone type. This indicates the
     * type of radio used to transmit voice calls.
     *
     * @return PHONE_TYPE_NONE //0 PHONE_TYPE_GSM //1 PHONE_TYPE_CDMA //2
     * PHONE_TYPE_SIP //3
     */
    public static int getPhoneType() {

        CobubLog.i(tag, "getPhoneType()=" + phoneType);
        return phoneType;
    }

    /**
     * get IMSI for GSM phone, return "" if it is unavailable.
     *
     * @return IMSI string
     */
    public static String getIMSI(Context context) {
        String result = "";
        try {
            if (!CommonUtil.checkPermissions(context, Manifest.permission.READ_PHONE_STATE)) {
                CobubLog.e(tag,
                        "READ_PHONE_STATE permission should be added into AndroidManifest.xml.");
                return "";
            }
            result = subscriberId;

            CobubLog.i(tag, "getIMSI()=" + result);
            if (result == null)
                return "";
            return result;

        } catch (Exception e) {
            CobubLog.e(tag, e);
        }

        return result;
    }

    public static String getWifiMac(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wi = wifiManager.getConnectionInfo();
            String result = wi.getMacAddress();
            if (result == null)
                result = "";
            CobubLog.i(tag, "getWifiMac()=" + result);
            return result;
        } catch (Exception e) {
            CobubLog.e(tag, e);
            return "";
        }

    }

    public static String getDeviceTime() {
        try {
            Date date = new Date();
            SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss", Locale.US);
            String result = localSimpleDateFormat.format(date);
            return result;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getDeviceName() {
        try {
            String manufacturer = Build.MANUFACTURER;
            if (manufacturer == null)
                manufacturer = "";
            String model = Build.MODEL;
            if (model == null)
                model = "";

            if (model.startsWith(manufacturer)) {
                return capitalize(model).trim();
            } else {
                return (capitalize(manufacturer) + " " + model).trim();
            }
        } catch (Exception e) {
            CobubLog.e(tag, e);
            return "";
        }
    }

    public static String getNetworkTypeWIFI2G3G(Context contex) {

        try {
            ConnectivityManager cm = (ConnectivityManager) contex
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            String type = ni.getTypeName().toLowerCase(Locale.US);
            if (!type.equals("wifi")) {
                type = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                        .getExtraInfo();
            }
            return type;
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean getWiFiAvailable(Context context) {
        try {
            if (!CommonUtil.checkPermissions(context, Manifest.permission.ACCESS_WIFI_STATE)) {
                CobubLog.e(tag,
                        "ACCESS_WIFI_STATE permission should be added into AndroidManifest.xml.");
                return false;
            }
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getTypeName().equals("WIFI")
                                && info[i].isConnected()) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getDeviceIMEI(Context context) {
        String result = "";
        try {
            if (!CommonUtil.checkPermissions(context, Manifest.permission.READ_PHONE_STATE)) {
                CobubLog.e(tag,
                        "READ_PHONE_STATE permission should be added into AndroidManifest.xml.");
                return "";
            }

            result = deviceId;
            if (result == null)
                result = "";
        } catch (Exception e) {
            CobubLog.e(tag, e);
        }
        return result;
    }

    private static String getSSN(Context context) {
        String result = "";
        try {

            if (!CommonUtil.checkPermissions(context, Manifest.permission.READ_PHONE_STATE)) {
                CobubLog.e(tag,
                        "READ_PHONE_STATE permission should be added into AndroidManifest.xml.");
                return "";
            }

            result = simSerialNumber;
            if (result == null)
                result = "";
        } catch (Exception e) {
            CobubLog.e(tag, e);
        }
        return result;
    }

    public static String getDeviceId(Context context) {
        String result = null;
        try {
            String imei = getDeviceIMEI(context);
            String imsi = getIMSI(context);
            String salt = CommonUtil.getSALT(context);

            result = CommonUtil.md5Appkey(imei + imsi + salt);

        } catch (Exception e) {
            CobubLog.e(tag, e);
        }
        return result;
    }

    public static String getLatitude() {

        return String.valueOf(latitude);
    }

    public static String getLongitude() {

        return String.valueOf(altitude);

    }

    public static String getGPSAvailable() {
        return loacationStat;
    }

    @NonNull
    private static String getLoacationStat(Location location) {
        if (location == null)
            return "false";
        else
            return "true";
    }

    private static Location getLocation(Context context) {
        Location location = null;
        CobubLog.i(tag, "getLocation");
        LocationManager locationManager;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            List<String> matchingProviders = locationManager.getAllProviders();
            for (String prociderString : matchingProviders) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    location = locationManager.getLastKnownLocation(prociderString);
                }

                if (location != null)
                    break;
            }
        } catch (Exception e) {
            CobubLog.e(tag, e.toString());
        }
        return location;
    }

    public static String getMCCMNC() {
        String result = "";
        try {

            String operator = networkOperator;
            if (operator == null)
                result = "";
            else
                result = operator;
        } catch (Exception e) {
            result = "";
            CobubLog.e(tag, e.toString());
        }
        return result;
    }

    /**
     * Capitalize the first letter
     *
     * @param s model,manufacturer
     * @return Capitalize the first letter
     */
    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }

    }
}
