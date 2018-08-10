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

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class UpdateManager {
    
    private static String force;
    private static ProgressDialog progressDialog;
    private String msg = "Found new version, update?";
    private  String updateMsg = null;
    private  String apkUrl = null;
    private static Dialog noticeDialog;
    private static final String SAVEPATH = "/sdcard/";
    private  String saveFile = null;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private static int progress;
    private static Thread downLoadThread;
    private static boolean interceptFlag = false;
    
    private final String tag = "UpdateManager";
//    private final String UPDATE_URL = "/ums/update.php";
    private final String UPDATE_URL = "/ums/getApplicationUpdate";
   
    private Context context;
    
    public UpdateManager(Context context) {
        this.context = context;
    }
    
    JSONObject prepareUpdateJSON() throws JSONException {
        JSONObject jsonUpdate = new JSONObject();

        jsonUpdate.put("appkey", AppInfo.getAppKey(context));
        jsonUpdate.put("channelId", AppInfo.getChannel(context));
        jsonUpdate.put("version_code", CommonUtil.getCurVersion(context));
        return jsonUpdate;
    }
    
    public void postUpdate() {
        JSONObject updateData;
        try {
            updateData = prepareUpdateJSON();
        } catch (Exception e) {
            CobubLog.e(tag, e.toString());
            return;
        }
        
        if (CommonUtil.isNetworkAvailable(context) && CommonUtil.isNetworkTypeWifi(context)&& UmsConstants.mUpdateOnlyWifi) {
            String result = NetworkUtil.Post(UmsConstants.urlPrefix + UPDATE_URL,
                    updateData.toString());
            MyMessage message = NetworkUtil.parse(result);
            if (message == null) {
                return;
            }
            if (message.getFlag()>0) {
                try {
                    JSONObject object = new JSONObject(message.getMsg());
                    String flag = object.getString("flag");
                    if (Integer.parseInt(flag) > 0) {
                        this.apkUrl = object.getString("fileurl");
                        String msg = object.getString("msg");
                        this.force = object.getString("forceupdate");
                        String description = object.getString("description");
                        String time = object.getString("time");
                        String version = object.getString("version");
                        this.updateMsg = this.msg + "\n" + version + ":" + description;
                        this.saveFile = SAVEPATH + nametimeString;
                        
                        showNoticeDialog(context);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } 
        }
    }
    
    private  Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    progressDialog.setProgress(progress);
                    break;
                case DOWN_OVER:
                    installApk();
                    break;
                default:
                    break;
            }
        };
    };

    public String now()
    {
        Time localTime = new Time("Asia/Beijing");
        localTime.setToNow();
        return localTime.format("%Y-%m-%d");
    }

    public  String nametimeString = now();

   
    public void showNoticeDialog(final Context context) {

        Builder builder = new Builder(context);
        builder.setTitle("Update software");
        builder.setMessage(updateMsg);
        builder.setPositiveButton("OK", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDownloadDialog(context);
            }
        });
        builder.setNegativeButton("Cancel", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (UpdateManager.force.equals("true")) {
                    System.exit(0);
                } else {
                    dialog.dismiss();
                }
            }
        });
        noticeDialog = builder.create();
        noticeDialog.show();
    }

    private void showSdDialog(final Context context) {
        Builder builder = new Builder(context);
        builder.setTitle("point");
        builder.setMessage("SD card does not exist");
        builder.setNegativeButton("OK", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        noticeDialog = builder.create();
        noticeDialog.show();
    }

    private void showDownloadDialog(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Update software");

        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setButton("Cancel", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                interceptFlag = true;

            }
        });
        progressDialog.show();
        downloadApk();

    }

    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(apkUrl);

                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                //File file = new File(SAVEPATH);
                boolean sdCardExist = Environment.getExternalStorageState()
                        .equals(Environment.MEDIA_MOUNTED);
                if (!sdCardExist)
                {
                    showSdDialog(context);
                }
                String apkFile = saveFile;
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        progressDialog.dismiss();

                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);

                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    /**
     * download apk
     * 
     */

    private  void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    /**
     * install apk
     * 
c     */
    private  void installApk() {
        File apkfile = new File(saveFile);
        if (!apkfile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        context.startActivity(intent);

    }

}
