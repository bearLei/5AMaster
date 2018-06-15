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

    @GET("Teacher/Classes")//登录
    Observable<BaseResponseInfo> getClass(@Query("termUID") String termUID);
    @GET("Teacher/MyStudents")//获取学生列表
    Observable<BaseResponseInfo> getStudeng(@Query("classUID") String classUID,
                                            @Query("termUID")String termUID,
                                            @Query("status") int status,
                                            @Query("pageIndex") int pageIndex,
                                            @Query("pageSize")int pageSize);
}
