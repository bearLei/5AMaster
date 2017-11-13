package com.puti.education.netFrame.netApi;


import com.puti.education.netFrame.response.ResponseInfo;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface StudentApi {

    @GET("student/index")
    Observable<ResponseInfo> studentIndex();

    @GET("student/eventlist")
    Observable<ResponseInfo> actionEventList(@Query("type") int type, @Query("status") String status, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    @GET("student/detail")
    Observable<ResponseInfo> getStudentDetail();

    @POST("student/info")
    Observable<ResponseInfo> modifyStudentInfo(@Body RequestBody requestBody);

    @POST("base/help")
    Observable<ResponseInfo> launchSos(@Body RequestBody requestBody);

    @POST("base/report")
    Observable<ResponseInfo> reportEvent(@Body RequestBody requestBody);

    //实践记录
    @GET("practice/pagelist")
    Observable<ResponseInfo> praticeList(@Query("type") String type, @Query("pageIndex") String pageIndex,@Query("pageSize") String pageSize);

    @POST("practice/add")
    Observable<ResponseInfo> addPracticeRecord(@Body RequestBody requestBody);


    @GET("practice/{id}")
    Observable<ResponseInfo> getPracticeDetail(@Path("id") String id);

    //学生提交自己的普通事件
    @POST("student/event")
    Observable<ResponseInfo> addNormalEvent(@Body RequestBody requestBody);

    //举报列表
    @GET("report/list")
    Observable<ResponseInfo> getReportList(@Query("pageIndex") int pageIndex,@Query("pageSize") int pageSize);


}