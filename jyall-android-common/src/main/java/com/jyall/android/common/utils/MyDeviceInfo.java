package com.jyall.android.common.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.util.UUID;

/**
 * Created by liu.zhenrong on 2016/5/13.
 */
public class MyDeviceInfo {


    public static String getDeviceId(Context context) {
        String result = "";
        try {
            String imei = getDeviceIMEI(context);
            String imsi = getIMSI(context);
            String salt = getSALT(context);

            result = md5Appkey(imei + imsi + salt);

        } catch (Exception e) {
        }
        return result;
    }

    public static String getDeviceIMEI(Context context) {
        String result = "";
        try {
            if (!checkPermissions(context, Manifest.permission.READ_PHONE_STATE)) {
                return "";
            }
            TelephonyManager systemService = (TelephonyManager) (context.getSystemService(Context.TELEPHONY_SERVICE));
            result = systemService.getDeviceId();
            if (result == null)
                result = "";
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * get IMSI for GSM phone, return "" if it is unavailable.
     *
     * @return IMSI string
     */
    public static String getIMSI(Context context) {
        String result = "";
        try {
            if (!checkPermissions(context, Manifest.permission.READ_PHONE_STATE)) {
                return "";
            }
            TelephonyManager systemService = (TelephonyManager) (context.getSystemService(Context.TELEPHONY_SERVICE));
            result = systemService.getSubscriberId();
            if (result == null)
                return "";
            return result;

        } catch (Exception e) {
        }

        return result;
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
            return "";
        }
    }

    public static String getOsVersion() {
        // String result = Build.VERSION.RELEASE;
        String result = Build.VERSION.RELEASE;
        if (result == null)
            return "";

        return "Android " + result;
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


    /**
     * 返回该设备在此程序上的随机数。
     *
     * @param context Context对象。
     * @return 表示该设备在此程序上的随机数。
     */
    public synchronized static String getSALT(Context context) {
        String file_name = context.getPackageName().replace(".", "");
        String sdCardRoot = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        int apiLevel = Integer.parseInt(Build.VERSION.SDK);
        File fileFromSDCard = new File(sdCardRoot + File.separator, "." + file_name);
        File fileFromDData = new File(context.getFilesDir(), file_name);// 获取data/data/<package>/files
        //4.4之後 /storage/emulated/0/Android/data/<package>/files
        if (apiLevel >= 19) {
            sdCardRoot = context.getExternalFilesDir(null).getAbsolutePath();
            fileFromSDCard = new File(sdCardRoot, file_name);
        }

        String saltString = "";
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            // sdcard存在
            if (!fileFromSDCard.exists()) {

                String saltId = getSaltOnDataData(fileFromDData, file_name);
                try {
                    writeToFile(fileFromSDCard, saltId);
                } catch (Exception e) {
                }
                return saltId;

            } else {
                // SD卡上存在salt
                saltString = getSaltOnSDCard(fileFromSDCard);
                try {
                    writeToFile(fileFromDData, saltString);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return saltString;
            }

        } else {
            // sdcard 不可用
            return getSaltOnDataData(fileFromDData, file_name);
        }

    }

    public static String md5Appkey(String str) {
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(str.getBytes());
            byte[] arrayOfByte = localMessageDigest.digest();
            StringBuffer localStringBuffer = new StringBuffer();
            for (int i = 0; i < arrayOfByte.length; i++) {
                int j = 0xFF & arrayOfByte[i];
                if (j < 16)
                    localStringBuffer.append("0");
                localStringBuffer.append(Integer.toHexString(j));
            }
            return localStringBuffer.toString();
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * checkPermissions
     *
     * @param context
     * @param permission
     * @return true or false
     */
    public static boolean checkPermissions(Context context, String permission) {
        PackageManager pm = context.getPackageManager();
        return pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED;
    }

    private static String getSaltOnDataData(File fileFromDData, String file_name) {
        try {
            if (!fileFromDData.exists()) {
                String uuid = getUUID();
                writeToFile(fileFromDData, uuid);
                return uuid;
            }
            return readSaltFromFile(fileFromDData);

        } catch (IOException e) {
        }
        return "";
    }

    private static String getUUID() {
        // TODO Auto-generated method stub
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 将表示此设备在该程序上的唯一标识符写入程序文件系统中
     *
     * @param file 保存唯一标识符的File对象。
     * @throws IOException IO异常。
     */
    private static void writeToFile(File file, String uuid)
            throws IOException {
        file.createNewFile();
        FileOutputStream out = new FileOutputStream(file);

        out.write(uuid.getBytes());
        out.close();

    }

    /**
     * 读出保存在程序文件系统中的表示该设备在此程序上的唯一标识符。。
     *
     * @param file 保存唯一标识符的File对象。
     * @return 唯一标识符。
     * @throws IOException IO异常。
     */
    private static String readSaltFromFile(File file) throws IOException {
        RandomAccessFile accessFile = new RandomAccessFile(file, "r");
        byte[] bs = new byte[(int) accessFile.length()];
        accessFile.readFully(bs);
        accessFile.close();
        return new String(bs);
    }

    private static String getSaltOnSDCard(File fileFromSDCard) {
        // TODO Auto-generated method stub
        try {
            String saltString = readSaltFromFile(fileFromSDCard);
            return saltString;
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * 获取本地版本号，用来对比服务器版本号
     */
    public static int getLocalVersion(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取当前版本号
     * v1.0.2
     *
     * @param context
     * @return
     */
    public static String getLocalName(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}
