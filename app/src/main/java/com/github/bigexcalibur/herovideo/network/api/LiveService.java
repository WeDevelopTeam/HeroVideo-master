package com.github.bigexcalibur.herovideo.network.api;


import com.github.bigexcalibur.herovideo.mvp.live.model.LiveAppIndexInfo;
import com.github.bigexcalibur.herovideo.mvp.user.model.UserLiveRoomStatusInfo;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 直播相关api
 */
public interface LiveService
{


    /**
     * 首页直播
     *
     * @return
     */
    @GET("AppIndex/home?_device=android&_hwid=51e96f5f2f54d5f9&_ulv=10000&access_key=563d6046f06289cbdcb472601ce5a761&appkey=c1b107428d337928&build=410000&platform=android&scale=xxhdpi&sign=fbdcfe141853f7e2c84c4d401f6a8758")
    Observable<LiveAppIndexInfo> getLiveAppIndex();

    /**
     * 获取直播源
     *
     * @param cid
     * @return
     */
    @GET("api/playurl?player=1&quality=0")
    Observable<ResponseBody> getLiveUrl(@Query("cid") String cid,@Query("appkey")String appkey,@Query("ts") String ts,@Query("sign") String sign);

    /**
     * 获取直播状态
     *
     * @param mid
     * @return
     */
    @GET("AppRoom/getRoomInfo")
    Observable<UserLiveRoomStatusInfo> getUserLiveRoomStatus(@Query("mid") int mid);


}
