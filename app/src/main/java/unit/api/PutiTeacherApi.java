package unit.api;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
import unit.base.BaseResponseInfo;

/**
 * Created by lei on 2018/6/14.
 */

public interface PutiTeacherApi {

    @GET("Teacher/Classes")//获取班级列表
    Observable<BaseResponseInfo> getClass(@Query("termUID") String termUID);
    @GET("Teacher/MyStudents")//获取学生列表
    Observable<BaseResponseInfo> getStudent(@Query("classUID") String classUID,
                                            @Query("termUID")String termUID,
                                            @Query("status") int status,
                                            @Query("pageIndex") int pageIndex,
                                            @Query("pageSize")int pageSize);
    @GET("Common/MyUID")//获取自己的uid
    Observable<BaseResponseInfo> getUid();

    @GET("Common/Teacher/Records")//教师档案：获取教师所有的任课/班主任记录
    Observable<BaseResponseInfo> getRecords(@Query("teacherUID")String teacherUID,
                                            @Query("termUID")String termUID);


    @POST("Teacher/Events/Deal")//学生事件处理
    Observable<BaseResponseInfo> eventDeal(@Body RequestBody route);

    @POST("Teacher/Events/Deals")//学生事件批量处理
    Observable<BaseResponseInfo> eventDeals(@Body RequestBody route);
}
