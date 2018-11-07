package com.sitong.changqin.utils;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.jyall.android.common.utils.LogUtils;
import com.sitong.changqin.base.OSSPermissionBean;
import com.sitong.changqin.ui.listerner.ProgressCallback;
import com.sitong.changqin.utils.files.DownLoadFilesUtils;
import com.sitong.changqin.utils.files.FilesUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

public class OssService {
    private OSS oss;
    private String accessKeyId;
    private String bucketName;
    private String accessKeySecret;
    private String endpoint;
    private String SecurityToken;
    private Context context;

    private ProgressCallback progressCallback;

    public OssService(Context context, String accessKeyId, String accessKeySecret, String endpoint, String bucketName, String SecurityToken) {
        this.context = context;
        this.endpoint = endpoint;
        this.bucketName = bucketName;
        this.accessKeyId = accessKeyId;
        this.SecurityToken = SecurityToken;
        this.accessKeySecret = accessKeySecret;
    }


    public void initOSSClient() {
        //OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider("<StsToken.AccessKeyId>", "<StsToken.SecretKeyId>", "<StsToken.SecurityToken>");
        //这个初始化安全性没有Sts安全，如需要很高安全性建议用OSSStsTokenCredentialProvider创建（上一行创建方式）多出的参数SecurityToken为临时授权参数
//        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(accessKeyId, accessKeySecret, SecurityToken);

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(8); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次


        // oss为全局变量，endpoint是一个OSS区域地址
        oss = new OSSClient(context, endpoint, credentialProvider, conf);

    }


    public void beginupload(Context context, String filename, final String path, final OSSPermissionBean bb) {
        //通过填写文件名形成objectname,通过这个名字指定上传和下载的文件
        String objectname = bb.getObjectkey();
        if (objectname == null || objectname.equals("")) {
            ExtraUtils.Companion.toasts("文件名不能为空");
            return;
        }
        //下面3个参数依次为bucket名，Object名，上传文件路径
        PutObjectRequest put = new PutObjectRequest(bucketName, bb.getObjectkey(), path);
        if (path == null || path.equals("")) {
            LogUtils.d("请选择图片....");
            //ToastUtils.showShort("请选择图片....");
            return;
        }
        LogUtils.d("正在上传中....");
        //ToastUtils.showShort("正在上传中....");
        // 异步上传，可以设置进度回调
        put.setCallbackParam(new HashMap<String, String>() {
            {
                put("callbackUrl", bb.getCallbackurl());
                put("callbackBodyType", bb.getCallbackBodyType());
                put("callbackBody", bb.getCallbackBody());
            }
        });
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                LogUtils.d("currentSize: " + currentSize + " totalSize: " + totalSize);
                double progress = currentSize * 1.0 / totalSize * 100.f;

                if (progressCallback != null) {
                    progressCallback.onProgressCallback(progress);
                }
            }
        });
        @SuppressWarnings("rawtypes")
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                LogUtils.d("UploadSuccess");
//                ExtraUtils.Companion.toasts("上传成功");
                progressCallback.onProgressSuccess();
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                LogUtils.d("UploadFailure");
//                ExtraUtils.toasts("UploadFailure");
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    LogUtils.e("UploadFailure：表示向OSS发送请求或解析来自OSS的响应时发生错误。\n" +
                            "  *例如，当网络不可用时，这个异常将被抛出");
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    LogUtils.e("UploadFailure：表示在OSS服务端发生错误");
                    LogUtils.e("ErrorCode", serviceException.getErrorCode());
                    LogUtils.e("RequestId", serviceException.getRequestId());
                    LogUtils.e("HostId", serviceException.getHostId());
                    LogUtils.e("RawMessage", serviceException.getRawMessage());
                }
                progressCallback.onProgressFailed();
            }
        });
        //task.cancel(); // 可以取消任务
        //task.waitUntilFinished(); // 可以等待直到任务完成
    }

    public void beginLoad(final String filename, final OSSPermissionBean bb) {
        // 构造下载文件请求
        GetObjectRequest get = new GetObjectRequest(bb.getBucket(),"music/"+ FilesUtils.Companion.getFileName(filename));
        get.setProgressListener(new OSSProgressCallback<GetObjectRequest>() {
            @Override
            public void onProgress(GetObjectRequest request, long currentSize, long totalSize) {
                LogUtils.d("currentSize: " + currentSize + " totalSize: " + totalSize);
                double progress = currentSize * 1.0 / totalSize * 100.f;

                if (progressCallback != null) {
                    progressCallback.onProgressCallback(progress);
                }
            }
        });
        OSSAsyncTask task = oss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                // 请求成功
                // Log.d("Content-Length", "" + getResult.getContentLength());

                InputStream inputStream = result.getObjectContent();
                //通过流来写
                OutputStream os = null;
                byte[] buffer = new byte[1024];
                int len;

                try {
                    os = new FileOutputStream(DownLoadFilesUtils.Companion.getInstance(context).getCurrentUri()+"/"+FilesUtils.Companion.getFileName(filename));
                    while ((len = inputStream.read(buffer)) != -1) {
                        // 处理下载的数据
                        os.write(buffer, 0, len);
                        os.flush();

                    }
                    inputStream.close();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();

                }
                progressCallback.onProgressSuccess();
            }

            @Override
            public void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
                progressCallback.onProgressFailed();
            }
        });

// task.cancel(); // 可以取消任务

// task.waitUntilFinished(); // 如果需要等待任务完成
    }


    public ProgressCallback getProgressCallback() {
        return progressCallback;
    }

    public void setProgressCallback(ProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
    }

//    public interface ProgressCallback {
//        void onProgressCallback(double progress);
//        void onProgressFailed();
//        void onProgressSuccess();
//    }
}
