package com.puti.education.netFrame.netApi;


import com.puti.education.netFrame.response.ResponseInfo;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * GET , POSET 请求传参方式不同
 * 若有参数是可选的，当参数不需要传时，设置参数值为null，则框架会自动忽略
 *
 *  @FormUrlEncoded
    @POST("warehouse/productsend.json")
    Observable<ResponseInfo> outSku(@Field("id") String id, @Field("warehouseId") int warehouseId, @Field("storeId") int storeId, @Field("items") String items);
 */
public interface PatriarchApi {

    @POST("parent/train")
    Observable<ResponseInfo> addTrain(@Body RequestBody requestBody);

    @GET("base/teacherbystudent")
    Observable<ResponseInfo> getTeacherByStudent(@Query("id") int studentId);

    @GET("parent/index")
    Observable<ResponseInfo> getParentHomeData();

    @GET("parent/trainlist")
    Observable<ResponseInfo> getTrainList(@Query("type") String type, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    @GET("parent/growthtrack")
    Observable<ResponseInfo> getGrowthTrackList(@Query("date") String date, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    @POST("child/event")
    Observable<ResponseInfo> addActionEventRecord(@Body RequestBody requestBody);

    @GET("parent/train/{uid}")
    Observable<ResponseInfo> getPracticeTrainDetail(@Path("uid") String uid);

    @GET("parent/growthtrackdetail")
    Observable<ResponseInfo> getGrowthTrackDetail(@Query("id") String id);

    @GET("parent/actioneventdetail")
    Observable<ResponseInfo> getActionEventDetail(@Query("id") int id);

    @GET("parent/detail")
    Observable<ResponseInfo> getParentInfo();

    @POST("parent/info")
    Observable<ResponseInfo> modifyParentInfo(@Body RequestBody requestBody);

    @POST("letter/new")
    Observable<ResponseInfo> writeLetter(@Body RequestBody requestBody);

    @GET("parent/childevents")
    Observable<ResponseInfo> actionEventList(@Query("childUid") String uid, @Query("eventType") int type, @Query("status") String status, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    @GET("letter/pagelist")
    Observable<ResponseInfo> getAnonymityList(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);




}