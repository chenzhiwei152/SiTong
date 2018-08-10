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
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class AppInfo {

	private static final String TAG = "AppInfo";
	private static final String UMS_APPKEY = "UMS_APPKEY";



	static String getAppKey(Context context) {
		String umsAppkey = "";
		try {
			PackageManager pm = context.getPackageManager();

			ApplicationInfo ai = pm.getApplicationInfo(
					context.getPackageName(),
					PackageManager.GET_META_DATA);

			if (ai != null) {
				umsAppkey = ai.metaData.getString(UMS_APPKEY);
				if (umsAppkey == null)
					CobubLog.e(TAG, "Could not read UMS_APPKEY meta-data from AndroidManifest.xml.");
			}
		} catch (Exception e) {
			CobubLog.e(TAG, "Could not read UMS_APPKEY meta-data from AndroidManifest.xml.");
			CobubLog.e(TAG, e);
		}
		return umsAppkey;
	}

	static String getAppVersion(Context context) {
		String versionName = "";
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null)
				versionName = "";
		} catch (Exception e) {
			CobubLog.e(TAG, e.toString());
		}
		return versionName;
	}
	public static String getChannel(Context context) {
		String channel =null;

		SharedPreferences sharedPreferences = context.getSharedPreferences("UMSAPPCHANNEL",Context.MODE_PRIVATE);
		channel = sharedPreferences.getString("UMSAPPCHANNEL",null);
		if (channel != null) {
			return channel;
		}

		final String start_flag = "META-INF/jyall_jsjyw_channel_";
		ApplicationInfo appinfo = context.getApplicationInfo();
		String sourceDir = appinfo.sourceDir;
		ZipFile zipfile = null;
		try {
			zipfile = new ZipFile(sourceDir);
			Enumeration<?> entries = zipfile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = ((ZipEntry) entries.nextElement());
				String entryName = entry.getName();
				if (entryName.contains(start_flag)) {
					channel = entryName.replace(start_flag, "");
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (zipfile != null) {
				try {
					zipfile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (channel == null || channel.length() <= 0) {
			channel = "22";//读不到渠道号就默认官方渠道
		}
		sharedPreferences.edit().putString("UMSAPPCHANNEL" ,channel).commit();
		return channel;
	}

}
