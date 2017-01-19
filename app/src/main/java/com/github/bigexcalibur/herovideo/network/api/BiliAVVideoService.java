package com.github.bigexcalibur.herovideo.network.api;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Xie.Zhou on 2017/1/19.
 */

public interface BiliAVVideoService {
    /**
     * 通过cid获取html
     * @return
     */
    @GET("playurl")
    Observable<ResponseBody> getBiliAVVideoHtml(@QueryMap Map<String,String> map);
}
