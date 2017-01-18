package com.github.bigexcalibur.herovideo.network.api;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Xie.Zhou on 2017/1/16.
 */

public interface BiliAVSearchService {
    /**
     * 通过av号获取html
     * @return
     */
    @GET("/{av}")
    Observable<String> getBiliAVSearchHtml(@Path("av") String av);
}
