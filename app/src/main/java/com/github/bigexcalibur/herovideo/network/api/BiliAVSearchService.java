package com.github.bigexcalibur.herovideo.network.api;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Xie.Zhou on 2017/1/16.
 */

public interface BiliAVSearchService {
    /**
     * 通过av号获取html
     * http://interface.bilibili.com/playurl?&cid=13022613&from=miniplay&player=1&sign=28b2be2b017493211727a28746ea6318
     * @return
     */
    @GET("video/av{av}")
    Observable<ResponseBody> getBiliAVSearchHtml(@Path("av") String av);
}
