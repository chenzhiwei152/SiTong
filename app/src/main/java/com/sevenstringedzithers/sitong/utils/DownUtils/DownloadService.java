package com.sevenstringedzithers.sitong.utils.DownUtils;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Description:
 * Created by jia on 2017/11/30.
 * 人之所以能，是相信能
 */
public interface DownloadService {

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);

}
